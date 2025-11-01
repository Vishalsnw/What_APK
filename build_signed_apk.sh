#!/bin/bash

# WhatsOrder TWA - Signed Release APK Build Script
# This script builds a signed release APK

set -e

echo "🚀 WhatsOrder TWA - Signed Release APK Build"
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
echo "🔨 Building signed release APK..."
./gradlew assembleRelease --stacktrace

echo ""
echo "=============================================="
echo "✅ Signed Release APK built successfully!"
echo ""
echo "📦 APK Location: app/build/outputs/apk/release/app-release.apk"
echo ""
echo "ℹ️  Keystore Details:"
echo "   Location: app/release-keystore.jks"
echo "   Alias: whatsorder-twa"
echo "   Password: whatsorder123"
echo "   (Keep these credentials safe for future updates!)"
echo ""
echo "=============================================="
