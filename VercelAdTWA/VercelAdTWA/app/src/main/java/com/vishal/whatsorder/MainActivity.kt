package com.vishal.whatsorder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class MainActivity : AppCompatActivity() {
    
    private lateinit var adView: AdView
    private lateinit var webView: WebView
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private lateinit var fileChooserLauncher: ActivityResultLauncher<Intent>
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize file chooser launcher
        fileChooserLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val uris = if (data?.clipData != null) {
                    // Multiple files selected
                    val clipData = data.clipData!!
                    Array(clipData.itemCount) { i ->
                        clipData.getItemAt(i).uri
                    }
                } else if (data?.data != null) {
                    // Single file selected
                    arrayOf(data.data!!)
                } else {
                    null
                }
                fileUploadCallback?.onReceiveValue(uris)
            } else {
                fileUploadCallback?.onReceiveValue(null)
            }
            fileUploadCallback = null
        }
        
        try {
            setContentView(R.layout.activity_main)
            
            // Initialize Mobile Ads SDK with production configuration
            MobileAds.initialize(this) {}
            
            // Configure for production ads (no test devices)
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(emptyList())
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            
            // Create and configure the banner ad
            adView = AdView(this)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = "ca-app-pub-5538218540896625/8498698237"
            
            // Add the banner ad to the layout
            val adContainer = findViewById<LinearLayout>(R.id.ad_container)
            adContainer.addView(adView)
            
            // Load the ad
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing ads", e)
        }
        
        try {
            // Configure WebView for TWA-like experience
            webView = findViewById(R.id.webview)
            
            // Add WebChromeClient to handle file chooser
            webView.webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    fileUploadCallback?.onReceiveValue(null)
                    fileUploadCallback = filePathCallback
                    
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "image/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    }
                    
                    val chooserIntent = Intent.createChooser(intent, "Select Image")
                    fileChooserLauncher.launch(chooserIntent)
                    return true
                }
            }
            
            // Custom WebViewClient to handle external URL schemes
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url?.toString() ?: return false
                    
                    return when {
                        // Handle WhatsApp URLs
                        url.startsWith("whatsapp://") -> {
                            openExternalApp(url, "com.whatsapp")
                            true
                        }
                        // Handle tel: URLs
                        url.startsWith("tel:") -> {
                            openExternalApp(url, null)
                            true
                        }
                        // Handle mailto: URLs
                        url.startsWith("mailto:") -> {
                            openExternalApp(url, null)
                            true
                        }
                        // Handle other app schemes
                        url.startsWith("intent://") || 
                        url.startsWith("market://") ||
                        url.startsWith("sms:") -> {
                            openExternalApp(url, null)
                            true
                        }
                        // Load http/https URLs in WebView
                        url.startsWith("http://") || url.startsWith("https://") -> {
                            view?.loadUrl(url)
                            false
                        }
                        else -> false
                    }
                }
            }
            
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.databaseEnabled = true
            webSettings.setSupportZoom(true)
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            
            // Enable cookies and ensure they persist
            val cookieManager = android.webkit.CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            
            // Set cache mode for optimal data persistence
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
            
            // Load the web app
            webView.loadUrl("https://whats-order-osr3.vercel.app/dashboard/")
            
            // Handle back button press
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            })
        } catch (e: Exception) {
            Log.e("MainActivity", "Error configuring WebView", e)
        }
    }
    
    private fun openExternalApp(url: String, packageName: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            
            // If package name is provided, try to open specific app
            if (packageName != null) {
                intent.setPackage(packageName)
            }
            
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // If the specific app is not installed, try without package restriction
            try {
                if (packageName != null) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    throw e
                }
            } catch (ex: Exception) {
                Log.e("MainActivity", "Cannot open URL: $url", ex)
                Toast.makeText(
                    this,
                    "Required app is not installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error opening external app", e)
            Toast.makeText(
                this,
                "Cannot open link",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun onPause() {
        try {
            if (::adView.isInitialized) {
                adView.pause()
            }
            if (::webView.isInitialized) {
                webView.onPause()
            }
            // Flush cookies to ensure session data is saved
            android.webkit.CookieManager.getInstance().flush()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onPause", e)
        }
        super.onPause()
    }
    
    override fun onResume() {
        super.onResume()
        try {
            if (::adView.isInitialized) {
                adView.resume()
            }
            if (::webView.isInitialized) {
                webView.onResume()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onResume", e)
        }
    }
    
    override fun onDestroy() {
        try {
            // Flush cookies before destroying to ensure all data is saved
            android.webkit.CookieManager.getInstance().flush()
            
            if (::adView.isInitialized) {
                adView.destroy()
            }
            if (::webView.isInitialized) {
                webView.destroy()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onDestroy", e)
        }
        super.onDestroy()
    }
}
