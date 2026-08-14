#!/bin/bash
# Builds the SwiftUI app into a simulator .app bundle (no Xcode project needed)
# and installs + launches it on the currently booted simulator.
set -e
cd "$(dirname "$0")"

APP="build/FinApp.app"
BUNDLE_ID="com.finapp.ios"
SDK="$(xcrun --sdk iphonesimulator --show-sdk-path)"
TARGET="arm64-apple-ios17.0-simulator"

echo "==> Compiling Swift sources"
rm -rf "$APP"
mkdir -p "$APP"
xcrun --sdk iphonesimulator swiftc \
    -sdk "$SDK" \
    -target "$TARGET" \
    -parse-as-library \
    -O \
    -o "$APP/FinApp" \
    Sources/*.swift

cp Info.plist "$APP/Info.plist"

echo "==> Installing on booted simulator"
xcrun simctl install booted "$APP"

echo "==> Launching"
xcrun simctl launch booted "$BUNDLE_ID"

echo "==> Done: $BUNDLE_ID"
