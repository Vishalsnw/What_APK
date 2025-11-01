package com.whatsorder.twa

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
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
        
        // Configure WebView for TWA-like experience
        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        
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
    }
    
    override fun onPause() {
        adView.pause()
        webView.onPause()
        super.onPause()
    }
    
    override fun onResume() {
        super.onResume()
        adView.resume()
        webView.onResume()
    }
    
    override fun onDestroy() {
        adView.destroy()
        webView.destroy()
        super.onDestroy()
    }
}
