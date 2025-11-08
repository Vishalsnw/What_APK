package com.vishal.whatsorder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class LauncherActivity : AppCompatActivity() {
    
    private var interstitialAd: InterstitialAd? = null
    private var adLoadFailed = false
    private val timeoutHandler = Handler(Looper.getMainLooper())
    private var hasNavigated = false
    
    private val timeoutRunnable = Runnable {
        if (!hasNavigated && (adLoadFailed || interstitialAd == null)) {
            showMainActivity()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_launcher)
            
            // Initialize Mobile Ads SDK with production configuration
            MobileAds.initialize(this) {}
            
            // Configure for production ads (no test devices)
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(emptyList())
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            
            // Load the interstitial ad
            loadInterstitialAd()
            
            // Set a timeout to show main activity even if ad fails to load
            timeoutHandler.postDelayed(timeoutRunnable, 3000) // 3 second timeout
        } catch (e: Exception) {
            Log.e("LauncherActivity", "Error in onCreate", e)
            showMainActivity()
        }
    }
    
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            this,
            "ca-app-pub-5538218540896625/8673958054",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    adLoadFailed = true
                    // Don't call showMainActivity here, let timeout handle it
                }
                
                override fun onAdLoaded(ad: InterstitialAd) {
                    // Cancel timeout since ad loaded successfully
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    interstitialAd = ad
                    showInterstitialAd()
                }
            }
        )
    }
    
    private fun showInterstitialAd() {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    showMainActivity()
                }
                
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    showMainActivity()
                }
                
                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                }
            }
            ad.show(this)
        } ?: run {
            showMainActivity()
        }
    }
    
    private fun showMainActivity() {
        if (!hasNavigated) {
            hasNavigated = true
            timeoutHandler.removeCallbacks(timeoutRunnable)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    
    override fun onDestroy() {
        timeoutHandler.removeCallbacks(timeoutRunnable)
        super.onDestroy()
    }
}
