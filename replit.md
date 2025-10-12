# WhatsOrder TWA - Replit Project

## Overview
This is an Android TWA (Trusted Web Activity) project that wraps a web application with AdMob integration. The project builds an Android APK that displays the WhatsOrder web app with monetization through interstitial and banner ads.

## Project Purpose
- Package the WhatsOrder web app (https://whats-order-osr3.vercel.app/dashboard/) as a native Android app
- Integrate Google AdMob for monetization
- Provide a seamless native app experience with splash screen and WebView

## Current State
- ✅ Complete Android project with Kotlin source code
- ✅ UML documentation created (UML_DIAGRAMS.md)
- ✅ GitHub Actions workflow for automated APK builds
- ✅ Comprehensive README with build instructions
- ✅ Documentation server running on port 5000

## Architecture
### Components
1. **LauncherActivity**: Splash screen with interstitial ad (3s timeout)
2. **MainActivity**: Main WebView with banner ad integration
3. **AdMob Integration**: Interstitial and banner ads
4. **WebView**: Displays the WhatsOrder web application

### Technical Stack
- **Language**: Kotlin
- **Build System**: Gradle 8.1.1
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Key Dependencies**: 
  - Google Play Services Ads 22.6.0
  - AndroidX WebKit 1.8.0
  - AndroidX AppCompat 1.6.1

## How to Use in Replit
### Documentation Server
A Python documentation server runs on port 5000 to display:
- Project overview and features
- UML diagrams and architecture
- Build instructions
- Technical specifications

Access it via the webview panel or at the Replit URL.

### Building APKs
**Note**: Building Android APKs directly in Replit is not recommended due to Android SDK size and complexity.

**Recommended approach**:
1. **Use GitHub Actions** (automated):
   - Push code to GitHub
   - GitHub Actions builds APKs automatically
   - Download from Actions artifacts

2. **Local build** (if you have Android SDK):
   ```bash
   cd VercelAdTWA/VercelAdTWA
   chmod +x gradlew
   ./gradlew assembleDebug
   ```

3. **Export and build elsewhere**:
   - Use Replit for code editing
   - Export to local Android Studio for building

## Project Structure
```
VercelAdTWA/VercelAdTWA/         # Android project root
├── app/
│   ├── src/main/
│   │   ├── java/com/whatsorder/twa/
│   │   │   ├── LauncherActivity.kt
│   │   │   └── MainActivity.kt
│   │   ├── res/                 # Resources
│   │   └── AndroidManifest.xml
│   └── build.gradle             # App-level build config
├── build.gradle                 # Project-level build config
└── settings.gradle

.github/workflows/
└── android-build.yml            # GitHub Actions CI/CD

UML_DIAGRAMS.md                  # Complete UML documentation
README.md                        # User documentation
docs_server.py                   # Documentation server
```

## Recent Changes
- 2025-10-12: Initial setup and UML documentation created
- 2025-10-12: GitHub Actions workflow configured
- 2025-10-12: Documentation server implemented

## User Preferences
- None specified yet

## Important Files
- **UML_DIAGRAMS.md**: Complete UML diagrams and architectural documentation
- **README.md**: User-facing documentation and build instructions
- **.github/workflows/android-build.yml**: Automated APK build configuration
- **docs_server.py**: Documentation web server

## Environment Notes
- This is a mobile app project (Android), not a web application
- The documentation server runs on port 5000 for viewing project info
- Actual APK builds should use GitHub Actions or local Android SDK
- Replit environment is used for documentation, code editing, and collaboration

## AdMob Configuration
- App ID: ca-app-pub-5538218540896625~1234567890
- Interstitial Unit: ca-app-pub-5538218540896625/8673958054
- Banner Unit: ca-app-pub-5538218540896625/8498698237
- **Note**: Replace with your own IDs before production use

## Next Steps (if user requests)
1. Customize AdMob IDs with user's own credentials
2. Modify web app URL to point to different application
3. Add signing configuration for release builds
4. Customize app branding (icon, name, colors)
5. Add more features to the Android wrapper
