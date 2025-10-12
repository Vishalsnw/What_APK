#!/bin/bash

# WhatsOrder TWA - Local APK Build Script
# This script helps build the APK locally (requires Android SDK)

set -e

echo "🚀 WhatsOrder TWA - APK Build Script"
echo "======================================"
echo ""

# Check if we're in the right directory
if [ ! -d "VercelAdTWA/VercelAdTWA" ]; then
    echo "❌ Error: VercelAdTWA/VercelAdTWA directory not found"
    echo "   Please run this script from the project root"
    exit 1
fi

# Navigate to project directory
cd VercelAdTWA/VercelAdTWA

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found"
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "📋 Build Options:"
echo "1. Build Debug APK"
echo "2. Build Release APK (unsigned)"
echo "3. Build Both"
echo "4. Clean Build"
echo ""
read -p "Choose an option (1-4): " choice

case $choice in
    1)
        echo ""
        echo "🔨 Building Debug APK..."
        ./gradlew assembleDebug --stacktrace
        echo ""
        echo "✅ Debug APK built successfully!"
        echo "📦 Location: app/build/outputs/apk/debug/app-debug.apk"
        ;;
    2)
        echo ""
        echo "🔨 Building Release APK..."
        ./gradlew assembleRelease --stacktrace
        echo ""
        echo "✅ Release APK built successfully!"
        echo "📦 Location: app/build/outputs/apk/release/app-release-unsigned.apk"
        ;;
    3)
        echo ""
        echo "🔨 Building Debug APK..."
        ./gradlew assembleDebug --stacktrace
        echo ""
        echo "🔨 Building Release APK..."
        ./gradlew assembleRelease --stacktrace
        echo ""
        echo "✅ Both APKs built successfully!"
        echo "📦 Debug: app/build/outputs/apk/debug/app-debug.apk"
        echo "📦 Release: app/build/outputs/apk/release/app-release-unsigned.apk"
        ;;
    4)
        echo ""
        echo "🧹 Cleaning build artifacts..."
        ./gradlew clean
        echo ""
        echo "✅ Clean completed!"
        ;;
    *)
        echo "❌ Invalid option"
        exit 1
        ;;
esac

echo ""
echo "======================================"
echo "✨ Build process completed!"
