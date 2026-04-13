# Secrets Configuration Guide

This document explains how to securely configure API keys and sensitive data for the Jung Tarot App.

## Overview

The project uses the **[Secrets Gradle Plugin](https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin)** by Google for secure secret management. This plugin ensures that:

- ✅ Secrets are never hardcoded in source files
- ✅ Secrets are never committed to version control
- ✅ BuildConfig fields are generated automatically
- ✅ Works seamlessly in local and CI/CD environments

## Local Development Setup

### 1. Create local.properties

```bash
cp local.properties.template local.properties
```

### 2. Add Your Gemini API Key

Edit `local.properties`:

```properties
# Get your key from https://ai.google.dev/
gemini.api.key=AIzaSyBYtx5hcp5NfyEIz3__GGspzU9gi4HWOCU

# Optional: Android SDK path (usually auto-detected)
# sdk.dir=/path/to/Android/Sdk
```

### 3. Verify Setup

```bash
./gradlew build
```

If the build succeeds, you're all set!

## How It Works

### BuildConfig Generation

The Secrets Gradle Plugin automatically:

1. Reads properties from `local.properties`
2. Generates `BuildConfig.GEMINI_API_KEY` field
3. Ensures values are stripped from APK debug strings
4. Provides compile-time type safety

### In GeminiService.kt

```kotlin
// The plugin automatically exposes this at compile time
val apiKey = BuildConfig.GEMINI_API_KEY

// With safety checks
if (BuildConfig.GEMINI_API_KEY.isBlank()) {
    return "Error: La clave de API de Gemini no está configurada."
}
```

## CI/CD Integration

### GitHub Actions

Set secrets in GitHub repository settings:

1. Go to **Settings** > **Secrets and variables** > **Actions**
2. Create new secret: `SECRETS_GEMINI_API_KEY`
3. Paste your API key value

In `.github/workflows/android-build.yml`:

```yaml
env:
  SECRETS_GEMINI_API_KEY: ${{ secrets.SECRETS_GEMINI_API_KEY }}

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Create local.properties
        run: |
          echo "gemini.api.key=$SECRETS_GEMINI_API_KEY" > local.properties
      
      - name: Build
        run: ./gradlew build
```

### Firebase App Distribution

```bash
# Set environment variable before building
export SECRETS_GEMINI_API_KEY="your-api-key"

# Create local.properties
echo "gemini.api.key=$SECRETS_GEMINI_API_KEY" > local.properties

# Build and distribute
firebase appdistribution:distribute app/build/outputs/apk/release/app-release.apk
```

### Google Play Store

1. Create signed release build locally
2. Never commit `keystore.properties` to version control
3. Use GitHub Secrets for keystore credentials in CI/CD:

```yaml
- name: Download keystore
  env:
    KEYSTORE_BASE64: ${{ secrets.KEYSTORE_BASE64 }}
    KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
    KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
    KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
  run: |
    echo $KEYSTORE_BASE64 | base64 --decode > release.keystore
    echo "gemini.api.key=${{ secrets.SECRETS_GEMINI_API_KEY }}" > local.properties
```

## File Structure

```
jungtarot/
├── local.properties          # ⚠️ DO NOT COMMIT - Contains real secrets
├── local.properties.template # ✅ Safe to commit - Template with placeholders
├── local.defaults.properties # ✅ Safe to commit - CI/CD defaults (empty)
└── .gitignore               # ✅ Contains local.properties
```

## Security Best Practices

### Do ✅

- ✅ Use `.gitignore` to prevent accidental commits
- ✅ Use environment variables in CI/CD
- ✅ Rotate API keys periodically
- ✅ Use different keys for development, staging, and production
- ✅ Restrict API key scope to minimum required permissions
- ✅ Enable API usage monitoring and alerts

### Don't ❌

- ❌ Commit `local.properties` to version control
- ❌ Hardcode secrets in source files
- ❌ Share secrets in chat, email, or unencrypted channels
- ❌ Use production keys for development
- ❌ Commit keystore files or credentials
- ❌ Log sensitive API keys

## Gradle Plugin Configuration

The plugin is configured in `app/build.gradle.kts`:

```gradle
secrets {
    // Read secrets from local.properties
    propertiesFileName = "local.properties"
    
    // Fallback for CI/CD when local.properties doesn't exist
    defaultPropertiesFileName = "local.defaults.properties"
    
    // Don't fail build if file doesn't exist
    ignoreFileNotFound = true
}
```

## Troubleshooting

### Build fails: "API key is blank"

**Problem**: `BuildConfig.GEMINI_API_KEY` is empty

**Solutions**:
1. Verify `local.properties` exists: `ls -la local.properties`
2. Check key is not empty: `grep gemini.api.key local.properties`
3. Rebuild: `./gradlew clean build`
4. Invalidate cache: In Android Studio: File > Invalidate Caches > Invalidate and Restart

### Gradle sync fails

**Problem**: "Cannot resolve symbol BuildConfig.GEMINI_API_KEY"

**Solutions**:
1. Ensure plugin is applied in `app/build.gradle.kts`
2. Run `./gradlew clean build`
3. File > Sync Now in Android Studio
4. Check no syntax errors in `local.properties`

### API key doesn't work at runtime

**Problem**: "Error: La clave de API de Gemini no está configurada"

**Solutions**:
1. Verify BuildConfig generation: `./gradlew tasks | grep secrets`
2. Generate new API key from [Google AI Studio](https://ai.google.dev/)
3. Check API is enabled in Google Cloud Console
4. Verify API quotas haven't been exceeded

## Environment Variables Reference

For CI/CD systems, these environment variables are commonly used:

| Variable | Purpose | Example |
|----------|---------|---------|
| `SECRETS_GEMINI_API_KEY` | Gemini API Key | `AIzaSy...` |
| `KEYSTORE_FILE` | Path to signing keystore | Release signing |
| `KEYSTORE_PASSWORD` | Keystore password | APK signing |
| `KEY_ALIAS` | Signing key alias | APK signing |
| `KEY_PASSWORD` | Signing key password | APK signing |

## Additional Resources

- [Secrets Gradle Plugin Docs](https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin)
- [Google AI Studio](https://ai.google.dev/)
- [Android Security Best Practices](https://developer.android.com/privacy-and-security)
- [OWASP Mobile Security](https://owasp.org/www-project-mobile-security/)

## Support

For issues with secrets configuration:

1. Check this guide
2. Review `.github/workflows/` for examples
3. Open a GitHub issue with error details
4. Never share your actual API keys in issues
