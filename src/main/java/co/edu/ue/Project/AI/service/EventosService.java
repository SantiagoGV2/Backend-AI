package co.edu.ue.Project.AI.service;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosGuardado;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.repository.IEventos;
import co.edu.ue.Project.AI.repository.IEventosCompartidoDao;
import co.edu.ue.Project.AI.repository.IEventosGuardadoDao;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventosService implements IEventosService {

    private static final Logger logger = LoggerFactory.getLogger(EventosService.class);

    @Autowired
    IEventos dao;
    
    @Autowired
    IUsuariosService usuarioService;
    
    @Autowired
    IEventosGuardadoDao eventosGuardadoDao;
    
    @Autowired
    IEventosCompartidoDao eventosCompartidosDao;

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;
    
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=";
    
    @Override
    public Evento agregarEventos(Evento eventos) {
        logger.info("Agregando evento: {}", eventos);
        return dao.addEventos(eventos);
    }

    @Override
    public Evento actualizarEventos(Evento eventos) {
        logger.info("Actualizando evento: {}", eventos);
        return dao.uppEventos(eventos);
    }

    @Override
    public List<Evento> todasEventos() {
        logger.info("Obteniendo todos los eventos");
        return dao.getAllEventos();
    }

    @Override
    public Evento buscarIdEventos(int eve_id) {
        logger.info("Buscando evento con ID: {}", eve_id);
        return dao.getIdEventos(eve_id);
    }

    @Override
    public boolean bajaEventos(int eve_id) {
        logger.info("Eliminando evento con ID: {}", eve_id);
        return dao.deleteEventos(eve_id);
    }
    
    @Transactional
    public void borrarHistorialDeBusqueda(int usuarioId) {
        logger.info("Iniciando borrado de historial para el usuario ID: {}", usuarioId);

        // 1. Obtener los IDs de eventos que se deben PRESERVAR (los guardados)
        List<EventosGuardado> registrosGuardados = eventosGuardadoDao.findByUsuarioUsuId(usuarioId);
        Set<Integer> idsDeEventosGuardados = registrosGuardados.stream()
            .filter(eg -> eg.getEvento() != null)
            .map(eg -> eg.getEvento().getEveId())
            .collect(Collectors.toSet());
        
        // 2. Obtener la lista de eventos del historial que NO están guardados
        List<Evento> historialCompleto = dao.findByUsuario_UsuId(usuarioId);
        List<Evento> eventosParaBorrar = historialCompleto.stream()
            .filter(eventoDelHistorial -> !idsDeEventosGuardados.contains(eventoDelHistorial.getEveId()))
            .collect(Collectors.toList());

        logger.info("Se borrarán {} eventos del historial.", eventosParaBorrar.size());

        // 3. BORRAR LOS "PADRES". JPA se encargará de los hijos automáticamente.
        if (!eventosParaBorrar.isEmpty()) {
            dao.deleteAllEventos(eventosParaBorrar);
            logger.info("Borrado en cascada ejecutado con éxito.");
        }
    }
 // CAMBIO: Lógica de búsqueda simplificada y más robusta
    @Override
    public List<Evento> buscarEventosDesdeWeb(String consulta, int usuId) {
        logger.info("Buscando eventos desde web con consulta: '{}' para usuario ID: {}", consulta, usuId);
        Usuario usuario = usuarioService.buscarIdUsuarios(usuId);
        if (usuario == null) {
            logger.error("No se encontró un usuario con ID: {}", usuId);
            return Collections.emptyList();
        }

        List<Evento> eventosEncontrados = obtenerEventosDesdeAPI(consulta);
        
        if (eventosEncontrados.isEmpty()) {
            logger.warn("La API de Gemini no retornó eventos para la consulta: '{}'", consulta);
            return Collections.emptyList();
        }
        // se manejará al momento de guardar y determinar el estado.
        List<Evento> eventosValidos = eventosEncontrados.stream()
            .filter(evento -> {
                boolean esFechaValida = evento.getEveFechaFin() == null || evento.getEveFechaFin().toLocalDateTime().isAfter(LocalDateTime.now());
                if (!esFechaValida) {
                    logger.warn("Evento filtrado por fecha vencida: {}", evento.getEveTitulo());
                }
                return esFechaValida;
            })
            .collect(Collectors.toList());

        return filtrarYGuardarEventos(eventosValidos, usuario);
    }

    /**
     * Filtra los eventos duplicados y los guarda en la base de datos.
     */
    private List<Evento> filtrarYGuardarEventos(List<Evento> eventosEncontrados, Usuario usuario) {
        validarCategorias(eventosEncontrados);

        List<Evento> eventosNuevos = new ArrayList<>();
        for (Evento evento : eventosEncontrados) {
            if (!dao.existsByTituloYDescripcion(evento.getEveTitulo(), evento.getEveDescripcion())) {
                evento.setUsuario(usuario); // Asignar el objeto Usuario completo
                evento.setEveFechaCreacion(Timestamp.valueOf(LocalDateTime.now()));
                evento.setEveFechaModificacion(Timestamp.valueOf(LocalDateTime.now()));
                evento.setEveEstado(determinarEstadoEvento(evento));
                
                dao.addEventos(evento);
                eventosNuevos.add(evento);
                logger.info("Guardando nuevo evento para el usuario {}: {}", usuario.getUsuId(), evento.getEveTitulo());
            } else {
                logger.warn("Evento duplicado omitido: '{}'", evento.getEveTitulo());
            }
        }
        return eventosNuevos;
    }

 // CAMBIO: Método principal para llamar a la API
    private List<Evento> obtenerEventosDesdeAPI(String consulta) {
        String apiUrl = API_URL + GEMINI_API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        
        // Obtenemos la fecha actual para dársela a la IA como referencia
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));

        // CAMBIO: Prompt mucho más específico y riguroso
        String prompt = String.format(
            "Actúa como un asistente experto en encontrar eventos. Necesito una lista de eventos futuros sobre '%s', " +
            "que ocurran a partir de la fecha de hoy, %s. " +
            "Responde EXCLUSIVAMENTE con un array de objetos JSON. No incluyas texto fuera del array. " +
            "Para cada evento, asegúrate de que el enlace sea una URL oficial y funcional. Si no puedes encontrar un enlace real y verificable, " +
            "OBLIGATORIAMENTE asigna el valor 'No disponible' al campo 'enlace' y no inventes una URL. " +
            "La estructura debe ser: " +
            "{\"titulo\": \"...\", \"descripcion\": \"...\", \"fecha_inicio\": \"YYYY-MM-DD HH:mm:ss\", " +
            "\"fecha_fin\": \"YYYY-MM-DD HH:mm:ss\", \"ubicacion\": \"...\", \"enlace\": \"URL verificada\", " +
            "\"categoria\": \"...\"}",
            consulta, fechaActual
        );

        String jsonBody = String.format(
            "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}], " +
            "\"generationConfig\": {\"response_mime_type\": \"application/json\"}}", 
            prompt.replace("\"", "\\\"")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            logger.info("Enviando solicitud a la API de Gemini 1.5 Pro.");
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            logger.info("Respuesta de la API recibida.");
            return extraerEventosDesdeRespuestaJSON(response.getBody());
        } catch (Exception e) {
            logger.error("Error al llamar a la API de Gemini: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
 // CAMBIO: Nuevo método para parsear la respuesta JSON directamente.
    private List<Evento> extraerEventosDesdeRespuestaJSON(String responseBody) {
        List<Evento> eventos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            
            // Navegamos por la estructura de la respuesta de Gemini
            String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            
            // Si el texto está vacío o no es un array JSON, retornamos lista vacía
            if (jsonText.trim().isEmpty() || !jsonText.trim().startsWith("[")) {
                logger.warn("La respuesta de Gemini no es un array JSON válido. Respuesta: {}", jsonText);
                return eventos;
            }

            // DTO (Data Transfer Object) para mapeo intermedio
            List<EventoDTO> eventosDTO = objectMapper.readValue(jsonText, new TypeReference<List<EventoDTO>>() {});

            // Convertimos de DTO a nuestra entidad Evento
            for (EventoDTO dto : eventosDTO) {
                Evento evento = new Evento();
                evento.setEveTitulo(dto.getTitulo());
                evento.setEveDescripcion(dto.getDescripcion());
                evento.setEveFechaInicio(convertirStringATimestamp(dto.getFecha_inicio()));
                evento.setEveFechaFin(convertirStringATimestamp(dto.getFecha_fin()));
                evento.setEveUbicacion(dto.getUbicacion());
                evento.setEveEnlace(dto.getEnlace());
                evento.setEveCategoria(dto.getCategoria());
                eventos.add(evento);
            }

        } catch (Exception e) {
            logger.error("Error al procesar la respuesta JSON de la API: {}", e.getMessage(), e);
            logger.error("Cuerpo de la respuesta que causó el error: {}", responseBody);
        }
        return eventos;
    }
    
    public void validarCategorias(List<Evento> eventos) {
        Set<String> categoriasValidas = Set.of(
            "tecnológico", "financiero", "deportivo", "cultural", "educativo", "general"
        );
        eventos.forEach(evento -> {
            if (evento.getEveCategoria() == null) {
                evento.setEveCategoria("General");
                return;
            }
            String categoriaNormalizada = evento.getEveCategoria().trim().toLowerCase();
            if (!categoriasValidas.contains(categoriaNormalizada)) {
                logger.warn("Categoría inválida '{}' en evento '{}'. Se asignará 'General'.", 
                            evento.getEveCategoria(), evento.getEveTitulo());
                evento.setEveCategoria("General");
            } else {
                // Capitalizar la primera letra para consistencia
                evento.setEveCategoria(categoriaNormalizada.substring(0, 1).toUpperCase() + categoriaNormalizada.substring(1));
            }
        });
    }
    private String determinarEstadoEvento(Evento evento) {
        // La fecha de fin es el criterio principal para la inactividad
        Timestamp fechaFin = evento.getEveFechaFin();
        if (fechaFin != null && fechaFin.toLocalDateTime().isBefore(LocalDateTime.now())) {
            return "Inactivo"; 
        }

        // Si el enlace no es válido, también lo consideramos "Inactivo" en el sentido
        // de que no se puede interactuar con él, pero AÚN ASÍ se mostrará al usuario.
        if (!esEnlaceValido(evento.getEveEnlace())) {
             logger.warn("El enlace para el evento '{}' es inválido. Se marcará como Inactivo.", evento.getEveTitulo());
            return "Inactivo";
        }

        // Solo si la fecha es futura Y el enlace es válido, está verdaderamente Activo.
        return "Activo";
    }

    private boolean esEnlaceValido(String url) {
        return url != null && !url.trim().isEmpty() && !url.equalsIgnoreCase("No disponible") && url.matches("^https?://.*");
    }
    
    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME, // Acepta YYYY-MM-DD'T'HH:mm:ss
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
        };

    
    public static Timestamp convertirStringATimestamp(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty() || fechaStr.equalsIgnoreCase("No disponible")) {
            return null;
        }
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return Timestamp.valueOf(LocalDateTime.parse(fechaStr, formatter));
            } catch (DateTimeParseException e) {
                // No es un error, solo un intento fallido
            }
        }
        logger.error("Error al convertir la fecha: '{}' - Formato no soportado.", fechaStr);
        return null;
    }
    
    private static class EventoDTO {
        private String titulo;
        private String descripcion;
        private String fecha_inicio;
        private String fecha_fin;
        private String ubicacion;
        private String enlace;
        private String categoria;

        // Getters y Setters
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getFecha_inicio() { return fecha_inicio; }
        public void setFecha_inicio(String fecha_inicio) { this.fecha_inicio = fecha_inicio; }
        public String getFecha_fin() { return fecha_fin; }
        public void setFecha_fin(String fecha_fin) { this.fecha_fin = fecha_fin; }
        public String getUbicacion() { return ubicacion; }
        public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
        public String getEnlace() { return enlace; }
        public void setEnlace(String enlace) { this.enlace = enlace; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
    }

    public boolean existeEvento(Evento evento) {
        return dao.existsByTituloYDescripcion(evento.getEveTitulo(), evento.getEveDescripcion());
    }
    
    public List<Evento> buscarEventos(List<String> filtros, Integer cantidad) {
        List<Evento> eventos = dao.findEventosByFiltros(filtros);

        if (cantidad != null && cantidad > 0) {
            return eventos.stream().limit(cantidad).collect(Collectors.toList());
        }
        return eventos;
    }

    public List<Evento> obtenerEventosPorUsuario(int usuarioId) {
        return dao.findByUsuario_UsuId(usuarioId);
    }

}