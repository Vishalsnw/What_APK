#!/bin/bash

# WhatsOrder TWA - Signed Release Build Script
# This script builds signed release APK and AAB

set -e

echo "🚀 WhatsOrder TWA - Signed Release Build"
echo "=============================================="
echo ""

# Check if we're in the right directory
if [ ! -d "VercelAdTWA/VercelAdTWA" ]; then
    echo "❌ Error: VercelAdTWA/VercelAdTWA directory not found"
    echo "   Please run this script from the project root"
    exit 1
fi

# Navigate to project directory
cd VercelAdTWA/VercelAdTWA/app

# Check if keystore exists, if not create it
if [ ! -f "release-keystore.jks" ]; then
    echo "🔑 Creating release keystore..."
    echo ""
    
    # Generate keystore with predefined password
    keytool -genkey -v \
        -keystore release-keystore.jks \
        -alias whatsorder-twa \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass whatsorder123 \
        -keypass whatsorder123 \
        -dname "CN=WhatsOrder, OU=Development, O=WhatsOrder, L=Unknown, ST=Unknown, C=US"
    
    echo ""
    echo "✅ Keystore created successfully!"
    echo ""
else
    echo "✓ Keystore already exists"
    echo ""
fi

# Go back to project root
cd ..

# Make gradlew executable
chmod +x gradlew

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

echo ""
echo "📋 Build Options:"
echo "1. Build APK only (for direct install/sideloading)"
echo "2. Build AAB only (for Play Store)"
echo "3. Build Both APK and AAB (recommended)"
echo ""
read -p "Choose an option (1-3, or press Enter for option 3): " choice

# Default to option 3 if no choice
choice=${choice:-3}

case $choice in
    1)
        echo ""
        echo "🔨 Building signed release APK..."
        ./gradlew assembleRelease --stacktrace
        echo ""
        echo "✅ Signed Release APK built successfully!"
        echo "📦 Location: app/build/outputs/apk/release/app-release.apk"
        ;;
    2)
        echo ""
        echo "🔨 Building signed release AAB..."
        ./gradlew bundleRelease --stacktrace
        echo ""
        echo "✅ Signed Release AAB built successfully!"
        echo "📦 Location: app/build/outputs/bundle/release/app-release.aab"
        ;;
    3)
        echo ""
        echo "🔨 Building signed release APK..."
        ./gradlew assembleRelease --stacktrace
        echo ""
        echo "🔨 Building signed release AAB..."
        ./gradlew bundleRelease --stacktrace
        echo ""
        echo "✅ Both builds completed successfully!"
        echo ""
        echo "📦 APK Location: app/build/outputs/apk/release/app-release.apk"
        echo "📦 AAB Location: app/build/outputs/bundle/release/app-release.aab"
        ;;
    *)
        echo "❌ Invalid option"
        exit 1
        ;;
esac

echo ""
echo "ℹ️  Keystore Details:"
echo "   Location: app/release-keystore.jks"
echo "   Alias: whatsorder-twa"
echo "   Password: whatsorder123"
echo "   (Keep these credentials safe for future updates!)"
echo ""
echo "ℹ️  What to use where:"
echo "   - APK: For direct installation, testing, sideloading"
echo "   - AAB: For uploading to Google Play Store (preferred)"
echo ""
echo "=============================================="
