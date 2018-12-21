package testRequestAuthentication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

public class testRequestAuthentication {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // TODO Auto-generated method stub
        // exampleCall();
        // testIt();
        String url2 = "https://16.200.0.250/rest/seismic/token?email=unamas@hpe.com";
        // sendGet(url, null, null);

        String userAndPassword = "tokenLogin:t0k3nL0gin";

        String userPassEncoded = encodeBase64(userAndPassword.getBytes());

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        String token = "";
        URL url = new URL(url2);
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        URLConnection con = url.openConnection();
        con.setRequestProperty("Authorization", userPassEncoded);
        Reader reader = new InputStreamReader(con.getInputStream());
        while (true) {
            int ch = reader.read();
            if (ch == -1) {
                break;
            }

            token += (char) ch;
        }
        System.out.print(token);
    }

    /**
     * Encode to Base64.
     * 
     * @param bytes
     *            bytes to encode
     * @return string base 64
     */
    private static String encodeBase64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

}
