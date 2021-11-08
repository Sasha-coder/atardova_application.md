package tardova.online.copybook

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.wik_web.*


class WikiWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wik_web)  // говорим, что отрисовать

        //webview.settings.javaScriptEnabled = true
        webview.webViewClient = MyWebViewClient()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val url = intent?.getStringExtra("Url")
        if (url == null) {
            val message = "Поиск не удался"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else {
            webview.loadUrl(url)
        }
    }

    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }


}