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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Objects;

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
    
    // El valor de la propiedad se lee correctamente desde application.properties
    @Value("${google.search.api.key}")
    private String googleSearchApiKey; // Este ya estaba bien, pero lo dejamos para confirmar

    private static final String GoogleSearch_CX = "903c92c91c9cf4090"; // <-- CORREGIDO: Nombre de constante válido

    @Value("${gemini.api.key}")
    private String geminiApiKey; // <-- CORREGIDO: Nombre en camelCase

    private final String API_URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=";
    
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
    
    
    private String buscarResultadosEnGoogle(String consulta) {
    	logger.info("Llamando a Google Custom Search API para: '{}'", consulta);
        
        String apiUrl = "https://www.googleapis.com/customsearch/v1";
        RestTemplate restTemplate = new RestTemplate();

        // Construimos la URL usando las variables con nombres corregidos
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("key", googleSearchApiKey) 
                .queryParam("cx", GoogleSearch_CX)      // <-- CORREGIDO
                .queryParam("q", consulta);
        

        try {
            ResponseEntity<GoogleSearchResponse> response = restTemplate.getForEntity(builder.toUriString(), GoogleSearchResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getItems() != null) {
                StringBuilder snippets = new StringBuilder();
                for (SearchItem item : response.getBody().getItems()) {
                    snippets.append(item.getTitle()).append(": ").append(item.getSnippet())
                           .append(" URL: ").append(item.getLink()).append(". ");
                }
                logger.info("Snippets obtenidos de Google: {}", snippets.toString());
                return snippets.toString();
            }
        } catch (Exception e) {
            logger.error("Error al llamar a Google Custom Search API: {}", e.getMessage());
        }
        
        return "";
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
    	logger.info("Buscando eventos desde web con consulta: '{}', usuario ID: {}", consulta, usuId);
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
        final LocalDateTime ahora = LocalDateTime.now();
        
        List<Evento> eventosFuturos = eventosEncontrados.stream()
            .filter(evento -> {
                // Si el evento no tiene fecha de inicio, lo dejamos pasar por si acaso
                if (evento.getEveFechaInicio() == null) {
                    return true; 
                }
                // Comparamos la fecha del evento con la fecha actual
                // El evento pasa el filtro si su fecha NO es anterior a ahora.
                return !evento.getEveFechaInicio().toLocalDateTime().isBefore(ahora);
            })
            .collect(Collectors.toList());

        if (eventosFuturos.isEmpty()) {
            logger.warn("Todos los eventos encontrados para '{}' ya han pasado. No se mostrarán.", consulta);
            return Collections.emptyList();
        }

        return filtrarYGuardarEventos(eventosFuturos, usuario);
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
        // 1. Buscar en una fuente de datos real
        String resultadosDeBusqueda = buscarResultadosEnGoogle(consulta);

        if (resultadosDeBusqueda == null || resultadosDeBusqueda.isEmpty()) {
            logger.warn("No se obtuvieron resultados de la búsqueda web para: {}", consulta);
            return Collections.emptyList();
        }
        
        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String categoriasValidas = "'tecnológico', 'financiero', 'deportivo', 'cultural', 'educativo', 'general'";
        String promptParaGemini = String.format(
        		"Tu única tarea es extraer eventos CUYA FECHA DE INICIO SEA FUTURA. La fecha y hora actual es %s. " +
        		        "CUALQUIER evento con una fecha de inicio anterior a la fecha actual debe ser IGNORADO COMPLETAMENTE. No lo incluyas en la respuesta bajo ninguna circunstancia. " +
        		        "Responde EXCLUSIVAMENTE con un array de objetos JSON sin texto introductorio. Si no encuentras eventos futuros, devuelve un array vacío []. " +
        		        "La estructura JSON requerida es la siguiente: " +
        		        "{" +
        		        "  \"titulo\": \"(String) El nombre del evento.\"," +
        		        "  \"descripcion\": \"(String) Una descripción breve.\"," +
        		        "  \"fecha_inicio\": \"(String) La fecha y hora de inicio en formato 'YYYY-MM-DD HH:mm:ss'. Si la hora no está disponible, usa '09:00:00'. Si es indeterminable, usa un valor JSON null.\"," +
        		        "  \"fecha_fin\": \"(String) La fecha de fin en formato 'YYYY-MM-DD HH:mm:ss'. Si no se menciona, usa un valor JSON null.\"," +
        		        "  \"ubicacion\": \"(String) El lugar del evento. Si no se menciona, usa 'No disponible'.\"," +
        		        "  \"enlace\": \"(String) La URL. Si no hay, usa un valor JSON null.\"," +
        		        "  \"categoria\": \"(String) Elige la categoría MÁS RELEVANTE de la lista: [%s]. Si ninguna encaja, usa 'general'.\"" +
        		        "}" +
        		        "Texto a analizar: \"%s\"",
        		        fechaActual,
        		        categoriasValidas,
        		        resultadosDeBusqueda
        );
        
        // 3. Llamar a Gemini
        String apiUrl = API_URL_GEMINI + geminiApiKey; // <-- CORREGIDO
        RestTemplate restTemplate = new RestTemplate();
        String jsonBody = String.format(
            "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}], \"generationConfig\": {\"response_mime_type\":\"application/json\"}}",
            promptParaGemini.replace("\"", "\\\"")
        );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            logger.info("Enviando solicitud a la API de Gemini para estructurar datos.");
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            logger.info("Respuesta de Gemini recibida.");
            return extraerEventosDesdeRespuestaJSON(response.getBody());
        } catch (Exception e) {
            logger.error("Error al llamar a la API de Gemini: {}", e.getMessage());
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
        eventos.forEach(evento -> {
            String categoria = evento.getEveCategoria();
            if (categoria == null || categoria.trim().isEmpty()) {
                evento.setEveCategoria("General");
            } else {
                // Capitalizar la primera letra para consistencia visual
                String categoriaNormalizada = categoria.trim().toLowerCase();
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
    	if (fechaStr == null || fechaStr.trim().isEmpty() || fechaStr.equalsIgnoreCase("No disponible") || fechaStr.equalsIgnoreCase("null")) {
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
@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleSearchResponse {
    private List<SearchItem> items;
    public List<SearchItem> getItems() { return items; }
    public void setItems(List<SearchItem> items) { this.items = items; }
}
@JsonIgnoreProperties(ignoreUnknown = true)
class SearchItem {
    private String title;
    private String link;
    private String snippet;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }
}