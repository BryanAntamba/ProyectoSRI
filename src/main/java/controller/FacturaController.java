package controller;

// Importación de clases para interactuar con el servicio web del SRI
import ec.gob.sri.comprobantes.ws.recepcion.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.recepcion.RecepcionComprobantesOffline;
import ec.gob.sri.comprobantes.ws.recepcion.RecepcionComprobantesOfflineService;

import java.io.File;
import java.nio.file.Files;

public class FacturaController {

    // Método para enviar y validar la factura electrónica firmada
    public static void enviarYValidarFactura(String rutaArchivoFirmado) {
        try {
            // Crear un objeto File que apunta al archivo XML firmado
            File xmlFile = new File(rutaArchivoFirmado);
            // Leer todo el contenido del archivo XML en un arreglo de bytes
            byte[] xmlBytes = Files.readAllBytes(xmlFile.toPath());

            // Mostrar en consola la ruta del comprobante que se va a enviar
            System.out.println("Enviando comprobante firmado: " + rutaArchivoFirmado);

            // Crear instancia del servicio web para recepción de comprobantes offline
            RecepcionComprobantesOfflineService service = new RecepcionComprobantesOfflineService();
            // Obtener el puerto (endpoint) para llamar al método de validación
            RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();

            // Enviar el arreglo de bytes del XML firmado para validación y recibir la respuesta
            RespuestaSolicitud respuesta = port.validarComprobante(xmlBytes);

            // Mostrar el estado general que devuelve el SRI (APROBADO, DEVUELTA, RECHAZADO, etc.)
            System.out.println("Respuesta recibida del SRI: " + respuesta.getEstado());

            // Evaluar si la factura fue aprobada
            if ("APROBADO".equalsIgnoreCase(respuesta.getEstado())) {
                System.out.println("Comprobante aprobado correctamente por el SRI.");
            } else {
                // Si no fue aprobado, mostrar que fue rechazado o devuelto
                System.out.println("Comprobante rechazado o devuelto por el SRI.");

                // Verificar que la respuesta contenga comprobantes con mensajes de error
                if (respuesta.getComprobantes() != null) {
                    if (respuesta.getComprobantes().getComprobante() != null && !respuesta.getComprobantes().getComprobante().isEmpty()) {
                        // Recorrer cada comprobante en la respuesta (usualmente uno solo)
                        respuesta.getComprobantes().getComprobante().forEach(comp -> {
                            // Revisar si el comprobante contiene mensajes detallados
                            if (comp.getMensajes() != null && comp.getMensajes().getMensaje() != null && !comp.getMensajes().getMensaje().isEmpty()) {
                                // Mostrar cada mensaje de error o advertencia
                                comp.getMensajes().getMensaje().forEach(msg -> {
                                    System.out.println("Mensaje: " + msg.getMensaje());           // Texto del mensaje
                                    System.out.println("Tipo: " + msg.getTipo());                   // Tipo del mensaje (ERROR, INFO, etc.)
                                    System.out.println("Identificador: " + msg.getIdentificador()); // Código o identificador del mensaje
                                });
                            } else {
                                // No hay mensajes dentro del comprobante, informar
                                System.out.println("No hay mensajes en el comprobante.");
                            }
                        });
                    } else {
                        // No hay comprobantes dentro de la respuesta, informar
                        System.out.println("No hay comprobantes en la respuesta.");
                    }
                } else {
                    // No existe la sección de comprobantes en la respuesta, informar
                    System.out.println("No hay comprobantes en la respuesta.");
                }
            }

        } catch (Exception e) {
            // En caso de error en la lectura o envío del comprobante, imprimir traza y mensaje
            e.printStackTrace();
            System.err.println("Error al enviar y validar la factura.");
        }
    }

    // Método main para probar la funcionalidad desde la consola
    public static void main(String[] args) {
        try {
            // Ruta del archivo XML firmado a enviar al SRI
            String rutaFacturaFirmada = "src/main/resources/XML/Firmados/factura.xml";
            // Llamar al método que envía y valida la factura
            enviarYValidarFactura(rutaFacturaFirmada);
        } catch (Exception e) {
            // Capturar y mostrar cualquier error inesperado en el proceso completo
            e.printStackTrace();
            System.err.println("Error en el proceso completo de factura electrónica.");
        }
    }
}


