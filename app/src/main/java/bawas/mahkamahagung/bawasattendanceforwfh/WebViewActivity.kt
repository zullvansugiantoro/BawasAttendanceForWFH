package bawas.mahkamahagung.bawasattendanceforwfh

import android.animation.ValueAnimator
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_web_view.*
import android.Manifest
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient


class WebViewActivity : AppCompatActivity()  {
    private val url = "https://bawasmari.mahkamahagung.go.id/attendent"
    private var isAlreadyCreated = false

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        startLoaderAnimate()

        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 0
        )

        webView.getSettings().setJavaScriptEnabled(true)


        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }
        })

        webView.webViewClient = object :  WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                endLoaderAnimate()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                endLoaderAnimate()
                showErrorDialog("Error","No Internet Connection. Please check your connection",this@WebViewActivity)
            }
        }

        webView.loadUrl(url)
    }

    override fun onResume(){
        super.onResume()

        if(isAlreadyCreated && !isNetworkAvailable()){
            isAlreadyCreated = false
            showErrorDialog("Error","No internet connection. Please check your connection.",
            this@WebViewActivity)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            this@WebViewActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
      if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
          webView.goBack()
          return true
      }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel", null);
        dialog.setNeutralButton("Settings", null)
        dialog.setPositiveButton("Retry",null)
        dialog.create().show()
    }

    private fun endLoaderAnimate(){
        loaderImage.clearAnimation()
        loaderImage.visibility = View.GONE
    }

    private fun startLoaderAnimate(){
        val objectAnimator = object : Animation(){
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val startHeight = 170
                val newHeight = (startHeight * (startHeight * 40) * interpolatedTime).toInt()
                loaderImage.layoutParams.height = newHeight
                loaderImage.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        objectAnimator.repeatCount = -1
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.duration = 1000
        loaderImage.startAnimation(objectAnimator)
    }
}