package co.edu.ue.Project.AI.service;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.repository.IEventos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EventosService implements IEventosService {

    private static final Logger logger = LoggerFactory.getLogger(EventosService.class);

    @Autowired
    IEventos dao;
    
    @Autowired
    IUsuariosService usuarioService;

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;
    
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    
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

    @Override
    public List<Evento> buscarEventosDesdeWeb(String consulta, int usuId) {
        logger.info("Buscando eventos desde web con consulta: {}", consulta);
        Usuario usuario = usuarioService.buscarIdUsuarios(usuId);
        if (usuario == null) {
            logger.error("No se encontró un usuario con ID: {}", usuId);
            return Collections.emptyList();
        }

        // Intento 1: Buscar eventos
        List<Evento> eventosEncontrados = obtenerEventosDesdeAPI(consulta, usuId);
        List<Evento> eventosNoDuplicados = filtrarYGuardarEventos(eventosEncontrados, usuario);

        if (!eventosNoDuplicados.isEmpty()) {
            return eventosNoDuplicados; // Retorna los eventos si los encuentra en el primer intento
        }

        logger.warn("No se encontraron eventos en el primer intento. Intentando nuevamente...");

        // Intento 2: Buscar eventos otra vez
        eventosEncontrados = obtenerEventosDesdeAPI(consulta, usuId);
        eventosNoDuplicados = filtrarYGuardarEventos(eventosEncontrados, usuario);

        if (!eventosNoDuplicados.isEmpty()) {
            return eventosNoDuplicados;
        }

        // Si aún no encuentra nada, retornar un mensaje de "No se encontraron eventos"
        logger.warn("No se encontraron eventos después de dos intentos.");
        return Collections.emptyList();
    }

    /**
     * Filtra los eventos duplicados y los guarda en la base de datos.
     */
    private List<Evento> filtrarYGuardarEventos(List<Evento> eventosEncontrados, Usuario usuario) {
        List<Evento> eventosNoDuplicados = eventosEncontrados.stream()
            .filter(evento -> !dao.existsByTituloYDescripcion(evento.getEveTitulo(), evento.getEveDescripcion()))
            .collect(Collectors.toList());
        
        validarCategorias(eventosNoDuplicados);

        eventosNoDuplicados.forEach(evento -> {
            evento.setUsuario(usuario);
            dao.addEventos(evento);
            logger.info("Guardando nuevo evento para el usuario {}: {}", usuario.getUsuId(), evento.getEveTitulo());
        });

        return eventosNoDuplicados;
    }

    
    private List<Evento> obtenerEventosDesdeAPI(String consulta, int usuid) {
        String apiUrl = API_URL + GEMINI_API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        
        String prompt = "Genera una lista de eventos relevantes con el siguiente formato: " +
        	    "\nTítulo: [Nombre del evento]" +
        	    "\nDescripción: [Descripción breve]" +
        	    "\nFecha de inicio: [YYYY-MM-DD HH:MM:SS]" +
        	    "\nFecha de fin: [YYYY-MM-DD HH:MM:SS]" +
        	    "\nUbicación: [Ciudad, país]" +
        	    "\nEnlace: [URL del evento si aplica]" +
        	    "\nCategoría: [Tecnológico, Financiero, Deportivo, Cultural, etc.]" +  // Aquí refuerzo que debe incluir una categoría
        	    "\nAsegúrate de que todos los eventos tengan una categoría bien definida." +
        	    "\nConsulta: " + consulta;

        
        String jsonBody = "{ \"model\": \"gemini-2.0-flash\", \"contents\": [{ \"role\": \"user\", \"parts\": [{ \"text\": \"" + prompt + "\" }]}]}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            logger.info("Enviando solicitud a la API de Gemini");
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            logger.info("Respuesta de la API recibida");
            return extraerEventosDesdeRespuesta(response.getBody(), usuid);
        } catch (Exception e) {
            logger.error("Error al llamar a la API de Gemini", e);
            return new ArrayList<>();
        }
    }
    
    private List<Evento> extraerEventosDesdeRespuesta(String responseBody, int usuid) {
        List<Evento> eventos = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            logger.info("Respuesta JSON de Gemini: {}", root.toPrettyString());
            
            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray()) return eventos;
            
            for (JsonNode candidate : candidates) {
                JsonNode content = candidate.path("content");
                if (content.has("parts")) {
                    for (JsonNode part : content.path("parts")) {
                        if (part.has("text")) {
                            String text = part.get("text").asText();
                            eventos.addAll(parsearEventos(text, usuid));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al procesar la respuesta de la API", e);
        }
        return eventos;
    }
    
    private List<Evento> parsearEventos(String respuesta, int usuid) {
        List<Evento> eventos = new ArrayList<>();
        String[] bloques = respuesta.split("\\n\\n"); // Divide por doble salto de línea
        
        for (String bloque : bloques) {
            Evento evento = new Evento();
            
            Matcher tituloMatcher = Pattern.compile("\\\\*Título:\\\\* (.+)").matcher(bloque);
            Matcher descripcionMatcher = Pattern.compile("\\\\*Descripción:\\\\* (.+)").matcher(bloque);
            Matcher inicioMatcher = Pattern.compile("\\\\*Fecha de inicio:\\\\* (.+)").matcher(bloque);
            Matcher finMatcher = Pattern.compile("\\\\*Fecha de fin:\\\\* (.+)").matcher(bloque);
            Matcher ubicacionMatcher = Pattern.compile("\\\\*Ubicación:\\\\* (.+)").matcher(bloque);
            Matcher enlaceMatcher = Pattern.compile("\\\\*Enlace:\\\\* \\[(.+)\\]").matcher(bloque);
            Matcher categoriaMatcher = Pattern.compile("\\\\*Categoria:\\\\* (.+)").matcher(bloque);
            
            if (tituloMatcher.find()) evento.setEveTitulo(tituloMatcher.group(1));
            if (descripcionMatcher.find()) evento.setEveDescripcion(descripcionMatcher.group(1));
            if (inicioMatcher.find()) {
                String fechaInicioStr = inicioMatcher.group(1);
                evento.setEveFechaInicio(convertirStringATimestamp(fechaInicioStr));
            }
            if (finMatcher.find()) {
                String fechaFinStr = finMatcher.group(1);
                evento.setEveFechaFin(convertirStringATimestamp(fechaFinStr));
            }
            if (ubicacionMatcher.find()) evento.setEveUbicacion(ubicacionMatcher.group(1));
            if (enlaceMatcher.find()) evento.setEveEnlace(enlaceMatcher.group(1));
            if (categoriaMatcher.find()) {
                evento.setEveCategoria(categoriaMatcher.group(1));
            } else {
                evento.setEveCategoria("General"); // Categoría por defecto si no se detecta ninguna
            }
            

            // Asignar valores a los campos faltantes
            evento.setEveFechaCreacion(Timestamp.valueOf(LocalDateTime.now())); 
            evento.setEveFechaModificacion(Timestamp.valueOf(LocalDateTime.now()));

            // Determinar estado del evento
            evento.setEveEstado(determinarEstadoEvento(evento));

            // Asignar el ID del usuario
            evento.setUsuid(usuid);
            
            if (evento.getEveTitulo() != null) { // Solo agregar si tiene título
                eventos.add(evento);
            }
        }
        
        return eventos;
    }
    
    public void validarCategorias(List<Evento> eventos) {
        Set<String> categoriasValidas = Set.of(
            "tecnológico", "financiero", "deportivo", "cultural", "educativo", "general"
        );

        eventos.forEach(evento -> {
            String categoriaNormalizada = evento.getEveCategoria().trim().toLowerCase();
            
            if (!categoriasValidas.contains(categoriaNormalizada)) {
                logger.warn("Categoría inválida '{}' en evento '{}'. Se asignará 'General'.", 
                            evento.getEveCategoria(), evento.getEveTitulo());
                evento.setEveCategoria("General");
            }
        });
    }



    
    private String determinarEstadoEvento(Evento evento) {
        Timestamp fechaFin = evento.getEveFechaFin();
        LocalDateTime ahora = LocalDateTime.now();

        // Si el evento tiene una fecha de fin y esta ya pasó, el evento está inactivo
        if (fechaFin != null && fechaFin.toLocalDateTime().isBefore(ahora)) {
            return "inactivo";
        }

        // Si el evento no tiene una fecha de fin válida, verificamos el enlace
        if (!esEnlaceValido(evento.getEveEnlace())) {
            return "inactivo";
        }

        // Si pasa todas las verificaciones, el evento se considera activo
        return "activo";
    }

    private boolean esEnlaceValido(String url) {
        return url != null && url.startsWith("http") && !url.toLowerCase().contains("no disponible");
    }
    
    private static final DateTimeFormatter[] FORMATTERS = {
    		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
    	    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
    	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
    	    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    	};

    
    public static Timestamp convertirStringATimestamp(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            logger.error("La fecha proporcionada es nula o vacía.");
            return null;
        }

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);
                return Timestamp.valueOf(fecha);
            } catch (DateTimeParseException e) {
                logger.warn("Intento fallido con formato: " + formatter.toString());
            }
        }

        logger.error("Error al convertir la fecha: {} - Formato incorrecto", fechaStr);
        return null;
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