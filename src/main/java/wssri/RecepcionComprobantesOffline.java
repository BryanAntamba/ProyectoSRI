package wssri; // Define el paquete donde se encuentra esta clase

// Importa clases necesarias del servicio web del SRI para enviar comprobantes
import ec.gob.sri.comprobantes.ws.recepcion.RecepcionComprobantesOfflineService;
import ec.gob.sri.comprobantes.ws.recepcion.RespuestaSolicitud;

// Importa clases estándar para manejo de archivos
import java.io.File;
import java.nio.file.Files;

public class RecepcionComprobantesOffline {

    // URL del servicio WSDL en ambiente de pruebas del SRI
    private static final String URL_PRUEBAS = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl";

    // Método estático que recibe la ruta de un XML firmado y lo envía al SRI
    public static String enviarComprobante(String rutaArchivoFirmado) {
        // StringBuilder para construir la respuesta del proceso
        StringBuilder resultado = new StringBuilder();

        try {
            // Crea un objeto File con la ruta del archivo firmado
            File xmlFile = new File(rutaArchivoFirmado);
            // Lee el contenido del archivo como arreglo de bytes
            byte[] archivoBytes = Files.readAllBytes(xmlFile.toPath());

            // Crea una instancia del servicio web del SRI
            RecepcionComprobantesOfflineService service = new RecepcionComprobantesOfflineService();
            // Obtiene el puerto del servicio para llamar al método de validación
            ec.gob.sri.comprobantes.ws.recepcion.RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();

            // Llama al servicio del SRI para validar el comprobante y guarda la respuesta
            RespuestaSolicitud respuesta = port.validarComprobante(archivoBytes);

            // Recorre los comprobantes devueltos en la respuesta
            respuesta.getComprobantes().getComprobante().forEach(comp -> {
                // Agrega la clave de acceso del comprobante al resultado
                resultado.append("Clave de Acceso: ").append(comp.getClaveAcceso()).append("\n");

                // Verifica si existen mensajes asociados al comprobante
                if (comp.getMensajes() != null && comp.getMensajes().getMensaje() != null) {
                    // Recorre los mensajes y los agrega al resultado
                    comp.getMensajes().getMensaje().forEach(mensaje -> {
                        resultado.append("Mensaje: ").append(mensaje.getMensaje()).append("\n");
                        resultado.append("Información adicional: ").append(mensaje.getInformacionAdicional()).append("\n");
                    });
                } else {
                    // Si no hay mensajes, indica que no se encontraron
                    resultado.append("No hay mensajes para este comprobante.\n");
                }
            });

        } catch (Exception e) {
            // En caso de error, imprime el stack trace y agrega el mensaje al resultado
            e.printStackTrace();
            resultado.append("Error al enviar comprobante al SRI: ").append(e.getMessage());
        }

        // Devuelve el resultado como string
        return resultado.toString();
    }
}
