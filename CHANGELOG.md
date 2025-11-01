# Changelog

## [1.1.0] - 2025-11-01

### ✨ Major Features Added

#### WhatsApp Sharing Fixed
- ✅ Added URL scheme handler in MainActivity
- ✅ Now supports `whatsapp://`, `tel:`, `mailto:`, and other external URLs
- ✅ Automatically opens WhatsApp when sharing from web app
- ✅ Handles app not installed scenarios gracefully

#### Android App Bundle (AAB) Support
- ✅ Added AAB build configuration
- ✅ Updated GitHub Actions to build both APK and AAB
- ✅ Updated local build script with AAB option
- ✅ Ready for Google Play Store submission

#### Signed Release Builds
- ✅ Configured automatic keystore generation
- ✅ Added signing configuration in build.gradle
- ✅ Created automated build scripts
- ✅ GitHub Actions builds signed APK + AAB

#### Comprehensive Documentation
- ✅ ADMOB_SETUP.md - AdMob configuration and test ads explanation
- ✅ README_SIGNING.md - Keystore and signing guide
- ✅ GITHUB_ACTIONS_SETUP.md - CI/CD setup with secrets
- ✅ QUICK_START.md - Quick start guide for beginners
- ✅ This CHANGELOG.md

### 🔧 Technical Improvements

#### Code Changes
- Enhanced WebViewClient with URL scheme interceptor
- Added Intent handlers for external apps
- Improved error handling and logging
- Added toast notifications for user feedback

#### Build System
- Updated GitHub Actions workflow
- Added bundleRelease task
- Automatic keystore management
- Support for GitHub Secrets

#### Development Tools
- Installed Java JDK 19 (GraalVM)
- Installed Android build tools
- Configured Python environment for docs

### 📚 Documentation
- Updated README.md with new features
- Updated replit.md with build instructions
- Created comprehensive guides for all features
- Added troubleshooting sections

### 🐛 Bug Fixes
- Fixed WhatsApp sharing ERR_UNKNOWN_URL_SCHEME error
- Resolved WebView URL navigation issues
- Fixed build configuration for signed releases

---

## [1.0.0] - 2025-10-12

### Initial Release
- Basic TWA wrapper for WhatsOrder web app
- AdMob integration (banner + interstitial ads)
- Splash screen with launcher activity
- WebView configuration
- GitHub Actions workflow
- UML documentation
