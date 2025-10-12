# WhatsOrder TWA - UML Documentation

## Project Overview

**WhatsOrder** is an Android Trusted Web Activity (TWA) application that wraps a web application (`https://whats-order-osr3.vercel.app/dashboard/`) with AdMob monetization integration.

---

## 1. Class Diagram

```plantuml
@startuml WhatsOrder_Class_Diagram

package "com.whatsorder.twa" {
    
    class LauncherActivity {
        - interstitialAd: InterstitialAd?
        - adLoadFailed: Boolean
        - timeoutHandler: Handler
        - hasNavigated: Boolean
        - timeoutRunnable: Runnable
        
        + onCreate(savedInstanceState: Bundle?)
        - loadInterstitialAd(): void
        - showInterstitialAd(): void
        - showMainActivity(): void
        + onDestroy(): void
    }
    
    class MainActivity {
        - adView: AdView
        - webView: WebView
        
        + onCreate(savedInstanceState: Bundle?)
        + onBackPressed(): void
        + onPause(): void
        + onResume(): void
        + onDestroy(): void
    }
    
    class "AppCompatActivity" as AppCompat <<Android Framework>> {
    }
    
    class "InterstitialAd" as InterstitialAd <<Google Ads SDK>> {
        + load(): void
        + show(): void
    }
    
    class "AdView" as AdView <<Google Ads SDK>> {
        + loadAd(AdRequest): void
        + pause(): void
        + resume(): void
        + destroy(): void
    }
    
    class "WebView" as WebView <<Android WebKit>> {
        + loadUrl(String): void
        + canGoBack(): Boolean
        + goBack(): void
        + onPause(): void
        + onResume(): void
        + destroy(): void
    }
    
    class "WebViewClient" as WebViewClient <<Android WebKit>> {
    }
    
    class "MobileAds" as MobileAds <<Google Ads SDK>> {
        + initialize(): void
    }
}

AppCompat <|-- LauncherActivity
AppCompat <|-- MainActivity

LauncherActivity ..> InterstitialAd : uses
LauncherActivity ..> MobileAds : initializes

MainActivity ..> AdView : manages
MainActivity ..> WebView : manages
MainActivity ..> MobileAds : initializes

WebView --> WebViewClient : uses

@enduml
```

---

## 2. Activity Flow Sequence Diagram

```plantuml
@startuml WhatsOrder_Activity_Flow

actor User
participant "LauncherActivity" as Launcher
participant "MobileAds SDK" as MobileAds
participant "InterstitialAd" as Interstitial
participant "Handler" as Handler
participant "MainActivity" as Main
participant "AdView" as Banner
participant "WebView" as WebView

User -> Launcher : Launch App
activate Launcher

Launcher -> Launcher : onCreate()
Launcher -> MobileAds : initialize()
activate MobileAds
MobileAds --> Launcher : initialized
deactivate MobileAds

Launcher -> Handler : postDelayed(timeoutRunnable, 3000ms)
activate Handler

Launcher -> Interstitial : load(adUnitId)
activate Interstitial

alt Ad Loads Successfully (< 3s)
    Interstitial --> Launcher : onAdLoaded(ad)
    Launcher -> Handler : removeCallbacks(timeoutRunnable)
    deactivate Handler
    
    Launcher -> Interstitial : show()
    Interstitial -> User : Display Full-Screen Ad
    
    User -> Interstitial : Dismiss Ad / Close Ad
    Interstitial --> Launcher : onAdDismissedFullScreenContent()
    deactivate Interstitial
    
    Launcher -> Main : startActivity(Intent)
    activate Main
    Launcher -> Launcher : finish()
    deactivate Launcher

else Ad Fails to Load
    Interstitial --> Launcher : onAdFailedToLoad()
    Launcher -> Launcher : adLoadFailed = true
    deactivate Interstitial
    
    note right of Handler : Timeout (3s) triggers
    Handler --> Launcher : timeoutRunnable executes
    deactivate Handler
    
    Launcher -> Main : startActivity(Intent)
    activate Main
    Launcher -> Launcher : finish()
    deactivate Launcher

else Timeout Reached (3s)
    Handler --> Launcher : timeoutRunnable executes
    deactivate Handler
    
    Launcher -> Main : startActivity(Intent)
    activate Main
    Launcher -> Launcher : finish()
    deactivate Launcher
end

Main -> Main : onCreate()
Main -> MobileAds : initialize()
Main -> Banner : create banner ad
Main -> Banner : loadAd()
activate Banner
Banner --> Main : ad loaded

Main -> WebView : configure settings
Main -> WebView : loadUrl("https://whats-order-osr3.vercel.app/dashboard/")
activate WebView
WebView -> User : Display Web Content
deactivate WebView

User -> Main : Interact with App

alt User Presses Back
    User -> Main : onBackPressed()
    Main -> WebView : canGoBack()?
    
    alt WebView has history
        Main -> WebView : goBack()
        WebView -> User : Navigate Back
    else No history
        Main -> Main : super.onBackPressed()
        Main -> User : Exit App
    end
end

alt App Lifecycle Changes
    User -> Main : onPause()
    Main -> Banner : pause()
    Main -> WebView : onPause()
    deactivate Banner
    
    User -> Main : onResume()
    Main -> Banner : resume()
    activate Banner
    Main -> WebView : onResume()
end

User -> Main : Exit App
Main -> Main : onDestroy()
Main -> Banner : destroy()
deactivate Banner
Main -> WebView : destroy()
deactivate Main

@enduml
```

---

## 3. Component Architecture Diagram

```plantuml
@startuml WhatsOrder_Architecture

package "Android Application" {
    
    [LauncherActivity] as Launcher
    [MainActivity] as Main
    
    package "AdMob Integration" {
        [InterstitialAd] as Interstitial
        [Banner Ad (AdView)] as Banner
        [MobileAds SDK] as SDK
    }
    
    package "Web Integration" {
        [WebView] as Web
        [WebViewClient] as WebClient
    }
    
    package "UI Components" {
        [Splash Screen] as Splash
        [Main Layout] as Layout
        [Ad Container] as AdContainer
    }
}

cloud "External Services" {
    [AdMob Platform] as AdMob
    [WhatsOrder Web App\nhttps://whats-order-osr3.vercel.app] as WebApp
}

Launcher --> SDK : initializes
Launcher --> Interstitial : loads & shows
Launcher --> Splash : displays
Launcher ..> Main : navigates to

Main --> SDK : initializes
Main --> Banner : creates & loads
Main --> Web : configures & loads
Main --> Layout : sets content view
Main --> AdContainer : adds banner

Interstitial --> AdMob : requests ad
Banner --> AdMob : requests ad

Web --> WebClient : uses
Web --> WebApp : loads URL

@enduml
```

---

## 4. State Diagram - Launcher Activity

```plantuml
@startuml LauncherActivity_State

[*] --> Initializing : onCreate()

Initializing --> LoadingAd : MobileAds.initialize()
LoadingAd : Start 3s timeout
LoadingAd : Load interstitial ad

LoadingAd --> AdLoaded : Ad loads successfully\n(< 3s)
LoadingAd --> AdFailedOrTimeout : Ad fails to load
LoadingAd --> AdFailedOrTimeout : 3s timeout reached

AdLoaded : Cancel timeout
AdLoaded --> ShowingAd : show()

ShowingAd --> NavigateToMain : User dismisses ad
ShowingAd --> NavigateToMain : Ad fails to show

AdFailedOrTimeout --> NavigateToMain : Timeout triggers

NavigateToMain : Start MainActivity
NavigateToMain : finish()

NavigateToMain --> [*]

@enduml
```

---

## 5. State Diagram - MainActivity

```plantuml
@startuml MainActivity_State

[*] --> Initializing : onCreate()

Initializing --> LoadingContent : Initialize MobileAds\nLoad banner ad\nConfigure WebView

LoadingContent --> Active : WebView loads URL\nBanner ad displays

Active --> Active : User interacts\nNavigates web app

Active : Banner ad visible
Active : WebView active
Active : Back navigation enabled

Active --> Paused : onPause()
Paused : AdView.pause()
Paused : WebView.onPause()

Paused --> Active : onResume()

Active --> Destroyed : onDestroy()\nor Back press (no history)

Destroyed : AdView.destroy()
Destroyed : WebView.destroy()

Destroyed --> [*]

@enduml
```

---

## 6. Build System Architecture

```
┌─────────────────────────────────────────┐
│         Project Root                     │
│  VercelAdTWA/VercelAdTWA/               │
│                                          │
│  ├── build.gradle (Project)             │
│  │   └── Android Gradle Plugin 8.1.1    │
│  │   └── Kotlin Plugin 1.9.0            │
│  │                                       │
│  ├── settings.gradle                     │
│  │   └── Project: "WhatsOrder"          │
│  │                                       │
│  └── app/                                │
│      ├── build.gradle (Module)          │
│      │   └── compileSdk: 34             │
│      │   └── minSdk: 21                 │
│      │   └── targetSdk: 34              │
│      │   └── Dependencies:              │
│      │       ├── AndroidX Core KTX      │
│      │       ├── AndroidX AppCompat     │
│      │       ├── Google Ads 22.6.0      │
│      │       ├── WebKit 1.8.0           │
│      │       └── ConstraintLayout       │
│      │                                   │
│      ├── src/main/                      │
│      │   ├── AndroidManifest.xml        │
│      │   ├── java/com/whatsorder/twa/   │
│      │   │   ├── LauncherActivity.kt    │
│      │   │   └── MainActivity.kt        │
│      │   └── res/                       │
│      │       ├── layout/                │
│      │       ├── values/                │
│      │       └── drawable/              │
│      │                                   │
│      └── proguard-rules.pro             │
│                                          │
└──────────────────────────────────────────┘
```

---

## 7. Key Technical Specifications

### Application Configuration
- **Package Name**: `com.whatsorder.twa`
- **Min SDK**: 21 (Android 5.0 Lollipop)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### AdMob Integration
- **App ID**: `ca-app-pub-5538218540896625~1234567890`
- **Interstitial Ad Unit**: `ca-app-pub-5538218540896625/8673958054`
- **Banner Ad Unit**: `ca-app-pub-5538218540896625/8498698237`

### Web Application
- **URL**: `https://whats-order-osr3.vercel.app/dashboard/`
- **WebView Features**:
  - JavaScript enabled
  - DOM storage enabled
  - Database enabled
  - Zoom controls (built-in, display hidden)

### Build Variants
- **Debug**: Standard debug build with debug signing
- **Release**: Minified with ProGuard optimization

---

## 8. Ad Flow Logic

### Launcher Activity (Splash with Interstitial)
1. **Initialize** MobileAds SDK
2. **Start** 3-second timeout timer
3. **Load** interstitial ad asynchronously
4. **Wait** for one of three outcomes:
   - ✅ Ad loads successfully → Show ad → Navigate to MainActivity
   - ❌ Ad fails to load → Set flag → Wait for timeout
   - ⏱️ Timeout (3s) reached → Navigate to MainActivity
5. **Navigate** to MainActivity and finish LauncherActivity

### MainActivity (Banner + WebView)
1. **Initialize** MobileAds SDK
2. **Create** banner AdView programmatically
3. **Add** banner to ad_container LinearLayout
4. **Load** banner ad
5. **Configure** WebView settings
6. **Load** web application URL
7. **Manage** lifecycle: pause/resume/destroy ads and WebView

---

## 9. Security & Permissions

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### ProGuard Rules
- Applied in release builds
- Uses default Android optimization rules
- Custom rules in `proguard-rules.pro`

---

## 10. Dependencies Summary

| Library | Version | Purpose |
|---------|---------|---------|
| AndroidX Core KTX | 1.12.0 | Kotlin extensions |
| AndroidX AppCompat | 1.6.1 | Backward compatibility |
| Google Play Services Ads | 22.6.0 | AdMob monetization |
| AndroidX WebKit | 1.8.0 | WebView functionality |
| AndroidX ConstraintLayout | 2.1.4 | Layout management |
| Kotlin Gradle Plugin | 1.9.0 | Kotlin compilation |
| Android Gradle Plugin | 8.1.1 | Build automation |

---

## Build Commands Reference

```bash
# Clean build artifacts
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK on connected device
./gradlew installDebug

# Run all checks and build
./gradlew build

# List all available tasks
./gradlew tasks
```

**Output Locations:**
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk`
