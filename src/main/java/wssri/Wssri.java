package wssri; // Define el paquete donde se encuentra esta clase

// Importa clases necesarias para manipulación XML (SOAP) y manejo de errores
import org.w3c.dom.NodeList;
import javax.xml.soap.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wssri {

    // Método estático que autoriza un comprobante usando su clave de acceso
    public static String autorizarComprobante(String claveAcceso) {

        // Valida que la clave de acceso no sea nula ni vacía
        if (claveAcceso == null || claveAcceso.trim().isEmpty()) {
            return "Clave de acceso no válida.";
        }

        // Establece el protocolo TLS 1.2 para conexiones HTTPS seguras
        System.setProperty("https.protocols", "TLSv1.2");

        // URL del servicio web del SRI para autorización de comprobantes
        String endpoint = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl";

        // Declaración de la conexión SOAP
        SOAPConnection soapConnection = null;

        try {
            // Crea un mensaje SOAP vacío usando la fábrica de mensajes
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();

            // Obtiene las partes del mensaje SOAP (estructura del XML)
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope(); // Envoltura SOAP (raíz)
            envelope.addNamespaceDeclaration("ec", "http://ec.gob.sri.ws.autorizacion"); // Define el espacio de nombres para el SRI

            // Construye el cuerpo SOAP con la estructura esperada por el SRI
            SOAPBody soapBody = envelope.getBody();
            SOAPElement autorizacionComprobante = soapBody.addChildElement("autorizacionComprobante", "ec"); // Nodo principal
            SOAPElement clave = autorizacionComprobante.addChildElement("claveAccesoComprobante", "ec"); // Subnodo
            clave.addTextNode(claveAcceso); // Agrega la clave de acceso como contenido

            // Guarda los cambios realizados en el mensaje SOAP
            soapMessage.saveChanges();

            // Crea una conexión SOAP y la abre
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            soapConnection = soapConnectionFactory.createConnection();

            // Envía el mensaje al endpoint del SRI y recibe la respuesta
            SOAPMessage response = soapConnection.call(soapMessage, endpoint);

            // Obtiene el cuerpo de la respuesta SOAP
            SOAPBody responseBody = response.getSOAPBody();

            // Busca elementos con el nombre "autorizacion" en la respuesta
            NodeList autorizaciones = responseBody.getElementsByTagName("autorizacion");

            // Si se encontró al menos una autorización, retorna su contenido textual
            if (autorizaciones.getLength() > 0) {
                return autorizaciones.item(0).getTextContent();
            } else {
                // Si no hay autorizaciones en la respuesta
                return "No se encontró autorización en la respuesta.";
            }

        } catch (Exception ex) {
            // Manejo de errores: imprime y registra el error en el logger
            Logger.getLogger(Wssri.class.getName()).log(Level.SEVERE, null, ex);
            return "Error al conectar con el SRI: " + ex.getMessage();
        } finally {
            // Cierra la conexión SOAP si fue abierta
            if (soapConnection != null) {
                try {
                    soapConnection.close();
                } catch (SOAPException e) {
                    Logger.getLogger(Wssri.class.getName()).log(Level.WARNING, "No se pudo cerrar la conexión SOAP", e);
                }
            }
        }
    }
}
