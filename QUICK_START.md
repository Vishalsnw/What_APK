# Quick Start Guide - Signed Release APK

This guide gets you started building signed release APKs immediately.

## 🚀 Fastest Way: Build in Replit

```bash
bash build_signed_apk.sh
```

**That's it!** The script will:
1. Create a keystore if needed
2. Build a signed release APK
3. Show you where the APK is located

### Download Your APK

After the build completes:
```
APK Location: VercelAdTWA/VercelAdTWA/app/build/outputs/apk/release/app-release.apk
```

Download this file and install it on your Android device.

## 📱 Install on Android Device

1. Download the APK to your device
2. Open the APK file
3. Allow "Install from unknown sources" if prompted
4. Install and enjoy!

## 🔄 GitHub Actions (Automated)

For automatic builds when you push code:

1. Push this code to GitHub
2. Go to **Actions** tab
3. Wait for build to complete
4. Download `app-release-signed` artifact

### Production Signing (Optional)

To use your own keystore in GitHub Actions:
- See [GITHUB_ACTIONS_SETUP.md](./GITHUB_ACTIONS_SETUP.md)

## 📚 More Information

- **Local signing details**: [README_SIGNING.md](./README_SIGNING.md)
- **GitHub Actions setup**: [GITHUB_ACTIONS_SETUP.md](./GITHUB_ACTIONS_SETUP.md)
- **Full documentation**: [README.md](./README.md)
- **Architecture**: [UML_DIAGRAMS.md](./UML_DIAGRAMS.md)

## 🔑 Default Keystore Info

The auto-generated keystore uses:
- **Password**: `whatsorder123`
- **Alias**: `whatsorder-twa`
- **Location**: `VercelAdTWA/VercelAdTWA/app/release-keystore.jks`

⚠️ **Important**: Back up this keystore! You'll need it to update your app later.

## ❓ Troubleshooting

### Build fails?
- Ensure Java is installed: `java -version`
- Check you're in the project root directory

### APK won't install?
- Enable "Install from unknown sources" in Android settings
- Make sure you're using the release APK, not debug

### Need help?
- Check the logs during build
- See README.md for detailed instructions
