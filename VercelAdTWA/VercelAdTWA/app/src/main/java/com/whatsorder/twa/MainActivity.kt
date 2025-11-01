package com.whatsorder.twa

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    
    private lateinit var adView: AdView
    private lateinit var webView: WebView
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_main)
            
            // Initialize Mobile Ads SDK
            MobileAds.initialize(this) {}
            
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
