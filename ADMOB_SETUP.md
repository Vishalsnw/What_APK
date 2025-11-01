# AdMob Configuration Guide

## Why You See "Test Ad" Text

If you see "Test Ad" text on your ads, it means:

1. **Using Test Mode** - AdMob is showing test ads (safe for development)
2. **New App** - Your app isn't approved yet in AdMob console
3. **Development Build** - Debug builds often show test ads

## Current AdMob Configuration

Your app is configured with these AdMob IDs:

```
App ID:         ca-app-pub-5538218540896625~5131951256
Banner Unit:    ca-app-pub-5538218540896625/8498698237
Interstitial:   ca-app-pub-5538218540896625/8673958054
```

## How to Show Real Ads

### Option 1: Verify App in AdMob Console

1. Go to [AdMob Console](https://apps.admob.com/)
2. Add your app
3. Create ad units
4. Wait for approval (can take 24-48 hours)
5. Real ads will start showing automatically

### Option 2: Update Ad Unit IDs

If you want to use your own AdMob account:

1. **Create AdMob Account** at https://admob.google.com/
2. **Add Your App**:
   - App name: WhatsOrder
   - Platform: Android
   - Package name: `com.whatsorder.twa`

3. **Create Ad Units**:
   - Banner ad unit
   - Interstitial ad unit

4. **Update the Code**:

Edit `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_APP_ID_HERE"/>
```

Edit `MainActivity.kt` (Banner):
```kotlin
adView.adUnitId = "YOUR_BANNER_AD_UNIT_ID"
```

Edit `LauncherActivity.kt` (Interstitial):
```kotlin
InterstitialAd.load(
    this,
    "YOUR_INTERSTITIAL_AD_UNIT_ID",
    ...
)
```

## Test Ads vs Real Ads

### Test Ads (Development)
- Show "Test Ad" text
- Safe to click repeatedly
- Don't generate revenue
- Use during development

### Real Ads (Production)
- Show actual advertisements
- Generate revenue when clicked
- Require AdMob approval
- Use in published apps

## Important Notes

⚠️ **Never click your own real ads** - This violates AdMob policies and can get you banned!

✅ **Always use test ads during development**

## Troubleshooting

### Ads not showing at all?
- Check internet connection
- Verify AdMob IDs are correct
- Check app is registered in AdMob console
- Wait 24-48 hours for new apps

### Still showing "Test Ad"?
- This is normal for:
  - Debug builds
  - New apps (< 48 hours old)
  - Apps not verified in AdMob console

### Ads showing blank space?
- AdMob might not have ads for your region
- Try again later
- Check AdMob console for issues

## Next Steps

1. ✅ Keep current setup for testing
2. ✅ Register app in AdMob console
3. ✅ Create your own ad units (optional)
4. ✅ Update IDs in code if using your account
5. ✅ Build and test
6. ✅ Publish to Play Store
7. ✅ Wait for AdMob approval
8. ✅ Real ads will start showing automatically

For more information, visit:
- [AdMob Help Center](https://support.google.com/admob)
- [AdMob Android Guide](https://developers.google.com/admob/android)
