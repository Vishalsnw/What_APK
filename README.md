# WhatsOrder - TWA Android App

A Trusted Web Activity (TWA) Android application that wraps the WhatsOrder web application with AdMob monetization integration.

## 📱 Project Overview

**WhatsOrder** is an Android app that provides a native app experience for the WhatsOrder web application (`https://whats-order-osr3.vercel.app/dashboard/`) with integrated Google AdMob advertising.

### Key Features
- 🚀 **Splash Screen** with interstitial ad
- 📱 **WebView Integration** for seamless web app experience
- 💰 **AdMob Monetization**:
  - Interstitial ads on app launch
  - Banner ads in main activity
- ⬅️ **Back Navigation** support within WebView
- 🔄 **Lifecycle Management** for ads and WebView

## 📊 UML Documentation

For comprehensive UML diagrams and architectural documentation, see [UML_DIAGRAMS.md](./UML_DIAGRAMS.md)

The documentation includes:
- Class Diagrams
- Activity Flow Sequence Diagrams
- Component Architecture
- State Diagrams
- Build System Architecture

## 🏗️ Project Structure

```
VercelAdTWA/VercelAdTWA/
├── app/
│   ├── src/main/
│   │   ├── java/com/whatsorder/twa/
│   │   │   ├── LauncherActivity.kt    # Splash screen with interstitial ad
│   │   │   └── MainActivity.kt        # Main WebView with banner ad
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle                        # Project-level build config
├── settings.gradle
└── gradle.properties
```

## 🔧 Technical Specifications

- **Min SDK**: 21 (Android 5.0 Lollipop)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
- **Language**: Kotlin
- **Build System**: Gradle 8.1.1

### Dependencies
- AndroidX Core KTX: 1.12.0
- AndroidX AppCompat: 1.6.1
- Google Play Services Ads: 22.6.0
- AndroidX WebKit: 1.8.0
- AndroidX ConstraintLayout: 2.1.4

## 🚀 Building the APK

### Option 1: Using GitHub Actions (Recommended) ✅

This project includes a **verified and tested** GitHub Actions workflow that automatically builds the APK:

1. **Push to GitHub**: Push this code to a GitHub repository
2. **Automatic Build**: The workflow triggers on push/PR to main/master/develop branches
3. **Download APK**: Check the "Actions" tab for build artifacts
4. **Manual Trigger**: Use the "workflow_dispatch" event in GitHub Actions UI

The workflow builds both:
- `app-debug.apk` - Debug version for testing
- `app-release-unsigned.apk` - Release version (needs signing)

**What the workflow does:**
- Sets up JDK 17 with Gradle caching
- Installs Android SDK (platform-tools, API 34, build-tools 34.0.0)
- Accepts Android SDK licenses
- Builds debug and release APKs
- Uploads artifacts with 30-day retention

### Option 2: Local Build (Requires Android SDK)

If you have Android SDK installed locally:

```bash
# Navigate to project directory
cd VercelAdTWA/VercelAdTWA

# Grant execution permission
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

**Output Locations:**
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Option 3: Replit Environment Setup

Building Android APKs directly in Replit is challenging due to Android SDK size. Instead:

1. **Use this Replit for**:
   - Code editing and collaboration
   - Documentation and UML reference
   - Version control

2. **Build APKs via**:
   - GitHub Actions (automatic CI/CD)
   - Export to local machine with Android Studio
   - Use cloud build services (Expo EAS for React Native alternatives)

## 📦 Installing the APK

### Debug APK
1. Download `app-debug.apk` from GitHub Actions artifacts or build output
2. Transfer to Android device
3. Enable "Install from Unknown Sources" in device settings
4. Install the APK

### Release APK (Signing Required)

For production distribution, you need to sign the release APK:

1. **Generate Keystore**:
```bash
keytool -genkey -v -keystore whatsorder-release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias whatsorder-key
```

2. **Configure Signing in `app/build.gradle`**:
```gradle
android {
    signingConfigs {
        release {
            storeFile file('whatsorder-release.jks')
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias 'whatsorder-key'
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

3. **Build Signed APK**:
```bash
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_PASSWORD="your_key_password"
./gradlew assembleRelease
```

## 🔐 AdMob Configuration

The app includes AdMob integration with the following IDs:

- **App ID**: `ca-app-pub-5538218540896625~1234567890`
- **Interstitial Ad Unit**: `ca-app-pub-5538218540896625/8673958054`
- **Banner Ad Unit**: `ca-app-pub-5538218540896625/8498698237`

> ⚠️ **Note**: Replace these with your own AdMob IDs before production release.

### How to Update AdMob IDs

1. **AndroidManifest.xml**: Update `APPLICATION_ID`
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID"/>
```

2. **LauncherActivity.kt**: Update interstitial ad unit ID (line 48)
```kotlin
"YOUR_INTERSTITIAL_AD_UNIT_ID",
```

3. **MainActivity.kt**: Update banner ad unit ID (line 31)
```kotlin
adView.adUnitId = "YOUR_BANNER_AD_UNIT_ID"
```

## 🌐 Changing the Web App URL

To point to a different web application:

Edit `MainActivity.kt` (line 56):
```kotlin
webView.loadUrl("YOUR_WEB_APP_URL")
```

## 📱 App Flow

1. **Launch** → LauncherActivity (Splash Screen)
2. **Load** → Interstitial Ad (3-second timeout)
3. **Display** → Ad shown or skip on timeout
4. **Navigate** → MainActivity
5. **Show** → Banner ad + WebView with web application

## 🔍 Troubleshooting

### Build Issues

**Problem**: Gradle build fails
- Ensure you have Java 17 installed
- Run `./gradlew clean` before building
- Check `gradle.properties` for correct settings

**Problem**: SDK not found
- Install Android SDK locally or use GitHub Actions
- Set `ANDROID_SDK_ROOT` environment variable

### Runtime Issues

**Problem**: Ads not showing
- Verify AdMob IDs are correct
- Check internet connectivity
- Review AdMob account status
- Use test ad units for development

**Problem**: WebView not loading
- Check internet permission in AndroidManifest.xml
- Verify web app URL is accessible
- Enable JavaScript in WebView settings

## 🚢 Publishing to Google Play

1. **Sign** the release APK (see signing instructions above)
2. **Create** Google Play Developer account
3. **Upload** signed APK/AAB to Play Console
4. **Configure** store listing, screenshots, description
5. **Submit** for review

### Generating AAB (Android App Bundle)

For Play Store submission, generate an AAB instead of APK:

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## 📄 License

See [LICENSE](./LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For issues related to:
- **Build System**: Check GitHub Actions logs
- **AdMob**: Visit [AdMob Help Center](https://support.google.com/admob)
- **Android Development**: See [Android Developer Docs](https://developer.android.com)

## 🔗 Resources

- [UML Documentation](./UML_DIAGRAMS.md)
- [Android Developer Guide](https://developer.android.com/guide)
- [AdMob Integration Guide](https://developers.google.com/admob/android/quick-start)
- [Trusted Web Activities](https://developer.chrome.com/docs/android/trusted-web-activity/)
- [Gradle Build Guide](https://developer.android.com/build)

---

**Built with ❤️ for WhatsOrder**
