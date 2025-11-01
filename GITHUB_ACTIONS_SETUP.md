# GitHub Actions Setup for Signed APK Builds

This guide explains how to configure GitHub Actions to build signed release APKs automatically.

## Quick Start

The workflow is already configured and will work out of the box with a temporary keystore. However, for production use, you should set up proper secrets.

## How It Works

### Default Behavior (No Secrets)
- GitHub Actions will generate a temporary keystore for each build
- Uses default password: `whatsorder123`
- Good for testing, but **not recommended for production**
- Each build creates a new signing key

### Production Setup (Recommended)
Configure GitHub secrets for consistent signing across builds.

## Setting Up GitHub Secrets (For Production)

### Step 1: Generate Your Keystore

On your local machine or in Replit, run:

```bash
cd VercelAdTWA/VercelAdTWA/app

keytool -genkey -v \
  -keystore release-keystore.jks \
  -alias whatsorder-twa \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

You'll be prompted to create passwords and enter your information. **Remember these values!**

### Step 2: Convert Keystore to Base64

```bash
base64 release-keystore.jks > keystore.txt
```

This creates a text file with your keystore encoded in base64.

### Step 3: Add Secrets to GitHub

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add the following secrets:

| Secret Name | Value | Description |
|-------------|-------|-------------|
| `KEYSTORE_BASE64` | Content of `keystore.txt` | Your keystore file encoded in base64 |
| `KEYSTORE_PASSWORD` | Your store password | Password you set when creating keystore |
| `KEY_ALIAS` | Your key alias | Usually `whatsorder-twa` |
| `KEY_PASSWORD` | Your key password | Password for the signing key |

### Step 4: Push and Build

Once secrets are configured:
1. Push your code to GitHub
2. GitHub Actions will automatically build signed APKs
3. Download artifacts from the Actions tab

## GitHub Secrets Summary

```
KEYSTORE_BASE64     = <base64 encoded keystore file>
KEYSTORE_PASSWORD   = whatsorder123 (or your custom password)
KEY_ALIAS          = whatsorder-twa (or your custom alias)
KEY_PASSWORD       = whatsorder123 (or your custom password)
```

## What the Workflow Does

1. **Checks out code** from your repository
2. **Sets up Java 17** and Android SDK
3. **Builds debug APK** for testing
4. **Creates/decodes keystore**:
   - If `KEYSTORE_BASE64` secret exists → uses it
   - Otherwise → generates temporary keystore
5. **Builds signed release APK** using the keystore
6. **Uploads artifacts**:
   - `app-debug.apk` (retained 30 days)
   - `app-release.apk` (retained 90 days)
7. **Creates GitHub release** (only when you push a tag)

## Downloading Built APKs

### From Actions Tab
1. Go to **Actions** tab in your repository
2. Click on the latest workflow run
3. Scroll to **Artifacts** section
4. Download `app-release-signed`

### From Releases (Tagged Commits)
1. Create a tag: `git tag v1.0.0 && git push origin v1.0.0`
2. GitHub Actions will create a release automatically
3. Go to **Releases** tab
4. Download APKs from the release

## Security Best Practices

### ✅ DO:
- Store keystore and passwords in GitHub Secrets
- Use strong, unique passwords for production
- Back up your keystore file securely
- Use different keystores for debug and release builds

### ❌ DON'T:
- Commit keystore files to your repository
- Share your keystore passwords publicly
- Use the default passwords in production
- Lose your keystore (you can't update your app without it!)

## Troubleshooting

### Build fails with "Keystore tampered" error
- Check that your `KEYSTORE_PASSWORD` secret matches the password used when creating the keystore

### APK is not signed
- Verify all four secrets are set correctly
- Check GitHub Actions logs for error messages
- Ensure keystore file was encoded correctly to base64

### Different signature on each build
- This means you're not using GitHub Secrets
- Each build generates a new temporary keystore
- Set up secrets for consistent signing

## Advanced: Using Environment Variables in build.gradle

For maximum security, you can reference environment variables in your `build.gradle`:

```gradle
signingConfigs {
    release {
        storeFile file("release-keystore.jks")
        storePassword System.getenv("KEYSTORE_PASSWORD") ?: "default-password"
        keyAlias System.getenv("KEY_ALIAS") ?: "default-alias"
        keyPassword System.getenv("KEY_PASSWORD") ?: "default-password"
    }
}
```

The workflow automatically handles this when secrets are configured.

## Next Steps

1. ✅ Set up GitHub Secrets for production signing
2. ✅ Push code and verify builds work
3. ✅ Download signed APK from Actions artifacts
4. ✅ Test APK on Android device
5. ✅ Upload to Google Play Console when ready

## Support

For more information:
- See `README_SIGNING.md` for local signing setup
- See `README.md` for general project documentation
- Check GitHub Actions logs for build errors
