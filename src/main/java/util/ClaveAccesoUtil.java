package util;

public class ClaveAccesoUtil {

    public static String generarClaveAcceso(
            String fechaEmision, String tipoComprobante, String ruc,
            String ambiente, String establecimiento, String puntoEmision,
            String secuencial, String codigoNumerico, String tipoEmision
    ) {
        String clave = fechaEmision + tipoComprobante + ruc + ambiente
                + establecimiento + puntoEmision + secuencial
                + codigoNumerico + tipoEmision;

        return clave + calcularDigitoVerificador(clave);
    }

    public static String generarCodigoNumerico() {
        int numero = (int) (Math.random() * 90000000) + 10000000;
        return String.format("%08d",numero);
    }

    private static int calcularDigitoVerificador(String clave) {
        int[] pesos = {2, 3, 4, 5, 6, 7};
        int suma = 0;
        int pesoIndex = 0;

        for (int i = clave.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(clave.charAt(i));
            suma += digito * pesos[pesoIndex];
            pesoIndex = (pesoIndex + 1) % pesos.length;
        }

        int mod = 11 - (suma % 11);
        if (mod == 11) return 0;
        if (mod == 10) return 1;
        return mod;
    }
}