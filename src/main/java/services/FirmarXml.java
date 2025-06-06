package services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.Collections;


import org.w3c.dom.Document;

public class FirmarXml {

    public static void firmarXML(String xmlInputPath, String xmlOutputPath, String keystorePath, String keystorePassword, String alias, String keyPassword) throws Exception {
        // Cargar XML
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new java.io.File(xmlInputPath));

        // Cargar KeyStore y obtener llave privada y certificado
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        // Crear la fábrica de firmas digitales
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        // Crear referencia al documento completo
        Reference ref = fac.newReference(
                "",
                fac.newDigestMethod(DigestMethod.SHA256, null),
                java.util.Collections.singletonList(
                        fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)
                ),
                null,
                null
        );

        // Crear SignedInfo
        SignedInfo signedInfo = fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref) // Aquí está la lista correcta
        );

        // Crear KeyInfo que incluye el certificado público
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Data x509Data = kif.newX509Data(java.util.Collections.singletonList(cert));
        KeyInfo keyInfo = kif.newKeyInfo(java.util.Collections.singletonList(x509Data));

        // Crear contexto para firmar el documento
        DOMSignContext dsc = new DOMSignContext(privateKey, doc.getDocumentElement());

        // Crear firma XML
        XMLSignature signature = fac.newXMLSignature(signedInfo, keyInfo);

        // Firmar el documento
        signature.sign(dsc);

        // Guardar documento firmado en archivo
        try (FileOutputStream fos = new FileOutputStream(xmlOutputPath)) {
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer trans = tf.newTransformer();
            trans.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            trans.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(fos));
        }

        System.out.println("Archivo XML firmado generado en: " + xmlOutputPath);
    }

    public static void main(String[] args) {
        try {
            String xmlSinFirmar = "src/main/resources/XML/noFirmado.xml"; // tu XML generado
            String xmlFirmado = "src/main/resources/XML/firmado.xml";    // salida firmada
            String keystore = "src/main/resources/certs/tu_certificado.p12";  // tu certificado
            String keystorePass = "tuContraseñaKeystore"; // contraseña del archivo p12
            String alias = "tuAliasCertificado"; // alias de la llave en el keystore
            String keyPass = "tuContraseñaLlave"; // contraseña de la llave privada (usualmente igual al keystore)

            firmarXML(xmlSinFirmar, xmlFirmado, keystore, keystorePass, alias, keyPass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}