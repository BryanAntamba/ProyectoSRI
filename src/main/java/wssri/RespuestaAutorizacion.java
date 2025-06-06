package wssri; // Define el paquete donde se encuentra esta clase

// Importa la clase List para manejar listas de objetos
import java.util.List;

public class RespuestaAutorizacion {

    // Declara una lista de Strings para almacenar las autorizaciones recibidas
    private List<String> autorizaciones;

    // Método getter para obtener la lista de autorizaciones
    public List<String> getAutorizaciones() {
        return autorizaciones;
    }

    // Método setter para establecer la lista de autorizaciones
    public void setAutorizaciones(List<String> autorizaciones) {
        this.autorizaciones = autorizaciones;
    }

    // Sobrescribe el método toString para representar el objeto como texto
    @Override
    public String toString() {
        return "RespuestaAutorizacion{" +
                "autorizaciones=" + autorizaciones +
                '}';
    }
}
