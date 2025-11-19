# Deployment Guide for Jung Tarot App

This guide provides detailed instructions for deploying the Jung Tarot Android application to various platforms.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Building the App](#building-the-app)
3. [Google Play Store Deployment](#google-play-store-deployment)
4. [Alternative Distribution Methods](#alternative-distribution-methods)
5. [Continuous Deployment](#continuous-deployment)
6. [Version Management](#version-management)
7. [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Tools

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Version 17 or later
- **Android SDK**: API Level 34
- **Gradle**: 8.2 (included via wrapper)

### Required Accounts (for store deployment)

- Google Play Developer Account ($25 one-time fee)
- Or alternative app store accounts (Samsung, Amazon, etc.)

## Building the App

### 1. Debug Build (for testing)

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### 2. Release Build (for production)

#### Step 1: Create Signing Key

```bash
keytool -genkey -v -keystore jungtarot-release.keystore \
  -alias jungtarot \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**Important**: Store this keystore file securely! You'll need it for all future updates.

#### Step 2: Configure Signing in build.gradle.kts

Add to `app/build.gradle.kts`:

```kotlin
android {
    // ... existing config
    
    signingConfigs {
        create("release") {
            storeFile = file("../jungtarot-release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            keyAlias = System.getenv("KEY_ALIAS") ?: "jungtarot"
            keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

#### Step 3: Build Release APK

```bash
export KEYSTORE_PASSWORD="your_password"
export KEY_ALIAS="jungtarot"
export KEY_PASSWORD="your_key_password"
./gradlew assembleRelease
```

#### Step 4: Build Release AAB (for Play Store)

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## Google Play Store Deployment

### Initial Setup

1. **Create Developer Account**
   - Visit [Google Play Console](https://play.google.com/console)
   - Pay the $25 registration fee
   - Complete account verification

2. **Create App**
   - Click "Create app"
   - Fill in app details:
     - Name: "Jung Tarot"
     - Default language: English (US)
     - App/Game: App
     - Free/Paid: Free

3. **Set Up App Details**
   - Short description (80 chars)
   - Full description (4000 chars)
   - Screenshots (minimum 2)
   - Feature graphic (1024x500)
   - App icon (512x512)
   - Category: Lifestyle
   - Content rating

### Uploading Your First Release

1. **Production Track**
   - Navigate to "Production" in Release Management
   - Click "Create new release"
   - Upload `app-release.aab`
   - Add release notes
   - Review and rollout

2. **Internal Testing (Recommended First)**
   - Navigate to "Internal testing"
   - Create a release
   - Upload AAB
   - Add testers via email
   - Test thoroughly before production

### Version Updates

1. **Update Version Numbers**
   
   In `app/build.gradle.kts`:
   ```kotlin
   defaultConfig {
       versionCode = 2  // Increment by 1
       versionName = "1.1"  // Update as appropriate
   }
   ```

2. **Build New Release**
   ```bash
   ./gradlew bundleRelease
   ```

3. **Upload to Play Console**
   - Create new release
   - Upload new AAB
   - Add changelog
   - Submit for review

## Alternative Distribution Methods

### 1. GitHub Releases

Automate with GitHub Actions (already configured):

```yaml
# Tag your release
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# GitHub Actions will automatically create a release
```

Users can download APK from: `https://github.com/lioncarballo88/jungtarot/releases`

### 2. Firebase App Distribution

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Initialize
firebase init appdistribution

# Distribute
firebase appdistribution:distribute \
  app/build/outputs/apk/release/app-release.apk \
  --app YOUR_FIREBASE_APP_ID \
  --groups "testers" \
  --release-notes "What's new in this version"
```

### 3. Amazon Appstore

1. Register at [Amazon Developer Console](https://developer.amazon.com/)
2. Create a new app
3. Upload APK (they accept APK, not AAB)
4. Fill in app details
5. Submit for review

### 4. Samsung Galaxy Store

1. Register at [Samsung Seller Portal](https://seller.samsungapps.com/)
2. Create application
3. Upload APK
4. Configure store listing
5. Submit

### 5. Direct APK Distribution

```bash
# Build APK
./gradlew assembleRelease

# Host on your website
# Users must enable "Install from Unknown Sources"
```

## Continuous Deployment

### GitHub Actions Setup

The repository includes a CI/CD workflow (`.github/workflows/android-build.yml`).

#### Required Secrets

Set these in GitHub Repository Settings > Secrets and Variables > Actions:

```
KEYSTORE_BASE64       # Base64 encoded keystore file
KEYSTORE_PASSWORD     # Keystore password
KEY_ALIAS            # Key alias
KEY_PASSWORD         # Key password
```

#### Encode Keystore

```bash
base64 -i jungtarot-release.keystore | pbcopy  # macOS
base64 -i jungtarot-release.keystore           # Linux
```

### Automatic Deployment on Tag

```bash
# Create and push a version tag
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0

# GitHub Actions will:
# 1. Build the APK
# 2. Sign it
# 3. Create a GitHub Release
# 4. Upload the APK as an asset
```

## Version Management

### Semantic Versioning

Follow [SemVer](https://semver.org/):
- MAJOR.MINOR.PATCH (e.g., 1.2.3)
- MAJOR: Breaking changes
- MINOR: New features (backwards compatible)
- PATCH: Bug fixes

### Version Code Strategy

- Increment by 1 for each release
- Must be greater than previous version
- Never reuse a version code

Example:
```
v1.0.0 = versionCode 1
v1.0.1 = versionCode 2
v1.1.0 = versionCode 3
v2.0.0 = versionCode 4
```

## Troubleshooting

### Common Build Issues

**Error: SDK location not found**
```bash
# Create local.properties
echo "sdk.dir=/path/to/Android/sdk" > local.properties
```

**Error: Keystore not found**
- Verify keystore path in build.gradle.kts
- Check environment variables are set

**Error: Out of memory**
```bash
# Increase Gradle heap size
echo "org.gradle.jvmargs=-Xmx4096m" >> gradle.properties
```

### Play Console Issues

**Rejection: Duplicate package name**
- Ensure `applicationId` in build.gradle.kts is unique

**Rejection: Missing privacy policy**
- Add privacy policy URL in Play Console
- Host policy on your website

**Rejection: Target API level too low**
- Update `targetSdk` to latest stable version
- Test thoroughly after update

### Signing Issues

**Error: Failed to sign APK**
- Verify keystore password is correct
- Check key alias exists in keystore

**Warning: Different signing key**
- You must use the same key for all updates
- If key is lost, you cannot update the app

## Pre-deployment Checklist

- [ ] Update version code and version name
- [ ] Test on multiple devices/API levels
- [ ] Run all unit and integration tests
- [ ] Perform UI/UX testing
- [ ] Test on different screen sizes
- [ ] Verify ProGuard/R8 rules (for release builds)
- [ ] Check for memory leaks
- [ ] Review app permissions
- [ ] Update release notes
- [ ] Test app signing
- [ ] Verify deep links (if any)
- [ ] Check analytics integration
- [ ] Review crash reporting setup
- [ ] Update screenshots and store listing
- [ ] Verify privacy policy compliance
- [ ] Check third-party library licenses

## Post-deployment

1. **Monitor Crashes**
   - Use Firebase Crashlytics or Play Console
   - Address critical crashes immediately

2. **Track Metrics**
   - Active users
   - Retention rate
   - App performance
   - User ratings and reviews

3. **Gather Feedback**
   - Monitor Play Store reviews
   - Set up in-app feedback
   - Engage with user community

4. **Plan Updates**
   - Regular bug fixes
   - New features based on feedback
   - Performance improvements
   - Security updates

## Security Considerations

1. **Never commit sensitive data**
   - Keystore files
   - Passwords
   - API keys
   - Private keys

2. **Use environment variables**
   - For CI/CD secrets
   - For API keys
   - For signing credentials

3. **Enable ProGuard/R8**
   - Obfuscates code
   - Reduces APK size
   - Makes reverse engineering harder

4. **Keep dependencies updated**
   - Regular security patches
   - Check for vulnerabilities
   - Use dependency scanning tools

## Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [Gradle User Guide](https://docs.gradle.org/)
- [Android App Bundles](https://developer.android.com/guide/app-bundle)
- [ProGuard/R8](https://developer.android.com/studio/build/shrink-code)

## Support

For deployment issues:
1. Check this documentation
2. Review Android Developer docs
3. Check Play Console help center
4. Open an issue on GitHub

---

Good luck with your deployment! 🚀
