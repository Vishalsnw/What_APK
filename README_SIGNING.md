# Android App Signing Guide

## Building a Signed Release APK

This project is configured to build signed release APKs for distribution.

### Quick Start

Run the signing script from the project root:

```bash
bash build_signed_apk.sh
```

This script will:
1. Create a release keystore (if it doesn't exist)
2. Build a signed release APK
3. Output the APK location

### Keystore Information

**IMPORTANT:** Keep these credentials safe for future app updates!

- **Keystore File:** `VercelAdTWA/VercelAdTWA/app/release-keystore.jks`
- **Keystore Password:** `whatsorder123`
- **Key Alias:** `whatsorder-twa`
- **Key Password:** `whatsorder123`

### Manual Build

If you prefer to build manually:

```bash
cd VercelAdTWA/VercelAdTWA
chmod +x gradlew
./gradlew assembleRelease
```

The signed APK will be located at:
```
app/build/outputs/apk/release/app-release.apk
```

### Creating Your Own Keystore

If you want to create a custom keystore with your own details:

```bash
keytool -genkey -v \
    -keystore my-release-key.jks \
    -alias my-alias \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000
```

Then update `app/build.gradle` with your keystore details in the `signingConfigs` section.

### Important Notes

1. **Never commit your keystore to version control** - Add `*.jks` to `.gitignore`
2. **Back up your keystore** - You cannot update your app on Play Store without it
3. **Remember your passwords** - Store them securely (password manager recommended)
4. **Production Use:** For production apps, use stronger passwords and store credentials in environment variables

### Verify APK Signature

To verify your APK is properly signed:

```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

Or using apksigner:

```bash
apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
```

### Upload to Play Store

The signed release APK (`app-release.apk`) can be uploaded to:
- Google Play Console (for Play Store distribution)
- Direct distribution websites
- Internal testing platforms

### Troubleshooting

**Error: keytool not found**
- Install Java JDK and ensure it's in your PATH

**Error: Keystore was tampered with**
- Verify you're using the correct password

**Error: Android SDK not found**
- Set ANDROID_HOME environment variable to your Android SDK location
