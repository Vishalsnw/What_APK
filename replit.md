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
- ✅ Signed release APK build configuration
- ✅ Java JDK and Android tools installed
- ✅ Automated build script for signed releases
- ✅ **LATEST (Nov 2, 2025):** Package name changed to com.vishal.whatsorder
- ✅ **LATEST (Nov 2, 2025):** Version code 8 for Google Play
- ✅ **LATEST (Nov 2, 2025):** Image upload from gallery support
- ✅ **LATEST (Nov 2, 2025):** App icon display issue fixed

## Architecture
### Components
1. **LauncherActivity**: Splash screen with interstitial ad (3s timeout)
2. **MainActivity**: Main WebView with banner ad integration and file upload support
3. **AdMob Integration**: Interstitial and banner ads
4. **WebView**: Displays the WhatsOrder web application with full image upload capability

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

### Building APKs & AABs
**You can now build signed release APKs and AABs directly in Replit!**

**Quick Build (Recommended)**:
```bash
bash build_signed_apk.sh
```

This will:
- Create a signing keystore automatically
- Build signed release APK and/or AAB
- Give you options to build APK, AAB, or both
- Output the file locations

**What's the difference?**
- **APK**: For direct installation, testing, sideloading
- **AAB**: For uploading to Google Play Store (required by Play Store)

**Alternative build methods**:
1. **Manual build**:
   ```bash
   cd VercelAdTWA/VercelAdTWA
   chmod +x gradlew
   ./gradlew assembleRelease  # Build APK
   ./gradlew bundleRelease    # Build AAB
   ```

2. **GitHub Actions** (automated):
   - Push code to GitHub
   - GitHub Actions builds APK + AAB automatically
   - Download from Actions artifacts

3. **Debug build**:
   ```bash
   bash build_apk.sh
   ```

**Documentation**:
- Signing: `README_SIGNING.md`
- GitHub Actions: `GITHUB_ACTIONS_SETUP.md`
- AdMob Setup: `ADMOB_SETUP.md`
- Quick Start: `QUICK_START.md`

## Project Structure
```
VercelAdTWA/VercelAdTWA/         # Android project root
├── app/
│   ├── src/main/
│   │   ├── java/com/vishal/whatsorder/
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
- 2025-11-01: **Major Updates - Signed Release, WhatsApp Sharing, AAB Support**
  - ✅ Fixed WhatsApp sharing from WebView (handles whatsapp://, tel:, mailto: URLs)
  - ✅ Added AAB (Android App Bundle) build support for Play Store
  - ✅ Configured app signing in build.gradle
  - ✅ Created automated build script supporting both APK and AAB
  - ✅ Added comprehensive documentation:
    - README_SIGNING.md (signing guide)
    - GITHUB_ACTIONS_SETUP.md (CI/CD setup)
    - ADMOB_SETUP.md (AdMob configuration)
    - QUICK_START.md (quick start guide)
  - ✅ Installed Java JDK and Android tools in Replit
  - ✅ Updated GitHub Actions workflow for APK + AAB builds
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
3. ✅ ~~Add signing configuration for release builds~~ (COMPLETED)
4. Customize app branding (icon, name, colors)
5. Add more features to the Android wrapper
6. Upload signed APK to Google Play Console
