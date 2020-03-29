package bawas.mahkamahagung.bawasattendanceforwfh

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
    private val webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Timer().schedule(3000){
            val intent = Intent(applicationContext,WebViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
