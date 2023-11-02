import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import java.security.SecureRandom

object SSLUtil {
    fun trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }), SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
