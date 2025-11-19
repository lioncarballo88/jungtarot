# Quick Start Guide

Get the Jung Tarot app up and running in 5 minutes!

## For Developers (Testing Locally)

### 1. Prerequisites
- Install [Android Studio](https://developer.android.com/studio)
- Install JDK 17+

### 2. Clone & Open
```bash
git clone https://github.com/lioncarballo88/jungtarot.git
cd jungtarot
```

Open the project in Android Studio.

### 3. Build & Run
- Click the green "Run" button, or
- Press `Shift + F10` (Windows/Linux) or `Control + R` (Mac)

That's it! The app will launch on your connected device or emulator.

## For Users (Installing APK)

### Option 1: Download from GitHub Releases
1. Go to [Releases](https://github.com/lioncarballo88/jungtarot/releases)
2. Download the latest `app-release.apk`
3. Open the APK on your Android device
4. Allow "Install from Unknown Sources" if prompted
5. Install and enjoy!

### Option 2: Build from Source
```bash
git clone https://github.com/lioncarballo88/jungtarot.git
cd jungtarot
./gradlew assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## For Deployers (Publishing to Play Store)

### Quick Deploy Steps

1. **Create Keystore**
   ```bash
   keytool -genkey -v -keystore release.keystore \
     -alias jungtarot -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Set Environment Variables**
   ```bash
   export KEYSTORE_PASSWORD="your_password"
   export KEY_ALIAS="jungtarot"
   export KEY_PASSWORD="your_key_password"
   ```

3. **Build Release AAB**
   ```bash
   ./gradlew bundleRelease
   ```

4. **Upload to Play Console**
   - Upload `app/build/outputs/bundle/release/app-release.aab`
   - Fill in store listing
   - Submit for review

**Need more details?** See [DEPLOYMENT.md](DEPLOYMENT.md) for the complete guide.

## Troubleshooting

### "SDK not found"
Create `local.properties`:
```properties
sdk.dir=/path/to/Android/sdk
```

### "Build failed"
```bash
./gradlew clean
./gradlew build
```

### "Out of memory"
Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
```

## Next Steps

- 📖 Read the full [README.md](README.md)
- 🚀 Check [DEPLOYMENT.md](DEPLOYMENT.md) for deployment options
- 🐛 Found a bug? [Open an issue](https://github.com/lioncarballo88/jungtarot/issues)
- 💡 Want to contribute? Fork and submit a PR!

## Quick Commands Reference

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build release AAB (for Play Store)
./gradlew bundleRelease

# Run tests
./gradlew test

# Run lint
./gradlew lint

# Clean build
./gradlew clean

# Install on device
./gradlew installDebug
```

---

Happy coding! 🎴✨
