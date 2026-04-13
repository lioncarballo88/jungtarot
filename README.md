# Jung Tarot App

An Android application that provides Jungian Tarot readings based on the Tarot of Marseilles, as interpreted by Sallie Nichols in "Jung and Tarot". The app treats tarot cards not as fortune-telling tools, but as instruments for self-discovery, synchronicity, and the process of Individuation.

## Overview

This app allows users to:
- Perform one-card, two-card, or three-card tarot readings
- Ask questions and receive psychologically-oriented interpretations
- Explore the archetypal meanings of the Major Arcana
- Work with tarot as a "weather map" of the psyche

## Features

- **Multiple Spread Types**: Choose from 1, 2, or 3 card spreads
- **Jungian Interpretation**: All readings are interpreted through a Jungian psychological lens
- **Beautiful UI**: Built with Jetpack Compose and Material 3 design
- **No Fortune Telling**: Focus on self-discovery and psychological growth

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle 8.2
- **Android Gradle Plugin**: 8.1.4

## Prerequisites

Before you begin, ensure you have the following installed:
- [Android Studio](https://developer.android.com/studio) (Hedgehog 2023.1.1 or later)
- JDK 17 or later
- Android SDK with API level 34
- Git

## Project Structure

```
jungtarot/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/tarot/
│   │       │   ├── MainActivity.kt
│   │       │   ├── data/
│   │       │   │   ├── TarotCard.kt
│   │       │   │   └── TarotRepository.kt
│   │       │   ├── domain/
│   │       │   │   ├── ReadingEngine.kt
│   │       │   │   ├── ReadingModels.kt
│   │       │   │   └── interpretation/
│   │       │   │       └── ReadingTemplate.kt
│   │       │   └── ui/
│   │       │       ├── components/
│   │       │       ├── home/
│   │       │       ├── result/
│   │       │       └── theme/
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/lioncarballo88/jungtarot.git
cd jungtarot
```

### 2. Configure API Secrets

This project uses the [Secrets Gradle Plugin](https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin) for secure secret management.

#### Setup Steps:

1. **Copy template file**
   ```bash
   cp local.properties.template local.properties
   ```

2. **Get your Gemini API Key**
   - Visit [Google AI Studio](https://ai.google.dev/)
   - Create a new API key for Android

3. **Add the key to local.properties**
   ```properties
   # local.properties
   gemini.api.key=YOUR_ACTUAL_API_KEY_HERE
   ```

**Important**: `local.properties` is in `.gitignore` and should **never** be committed to version control.

### 3. Open in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository
4. Wait for Gradle sync to complete

### 4. Build the Project

#### Using Android Studio:
1. Click "Build" > "Make Project" (or press Ctrl+F9 / Cmd+F9)
2. Wait for the build to complete

#### Using Command Line:
```bash
# On Linux/Mac:
./gradlew build

# On Windows:
gradlew.bat build
```

### 5. Run the App

#### Using Android Studio:
1. Connect an Android device or start an emulator
2. Click the "Run" button (or press Shift+F10)

#### Using Command Line:
```bash
# Install on connected device
./gradlew installDebug

# Run on device
adb shell am start -n com.example.tarot/.MainActivity
```

## Building for Release

### 1. Generate a Signing Key

If you don't have a signing key, create one:

```bash
keytool -genkey -v -keystore jungtarot-release.keystore -alias jungtarot -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Configure Signing (Optional but Recommended)

Create a `keystore.properties` file in the project root:

```properties
storeFile=/path/to/jungtarot-release.keystore
storePassword=your_store_password
keyAlias=jungtarot
keyPassword=your_key_password
```

**Important**: Do NOT commit this file to version control!

### 3. Build Release APK

```bash
./gradlew assembleRelease
```

The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

### 4. Build Release AAB (for Play Store)

```bash
./gradlew bundleRelease
```

The AAB will be generated at: `app/build/outputs/bundle/release/app-release.aab`

## Deployment Options

### Option 1: Google Play Store

1. Create a [Google Play Developer account](https://play.google.com/console)
2. Create a new app in the Play Console
3. Upload the release AAB (`.aab` file)
4. Fill in store listing information
5. Complete content rating and pricing setup
6. Submit for review

### Option 2: Direct APK Distribution

1. Build a release APK (see above)
2. Distribute the APK file through:
   - Your website
   - Firebase App Distribution
   - GitHub Releases
   - Third-party app stores (Amazon Appstore, Samsung Galaxy Store, etc.)

**Note**: Users will need to enable "Install from Unknown Sources" for APK installations.

### Option 3: Internal Testing

Use Firebase App Distribution or Google Play Internal Testing:

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login and initialize
firebase login
firebase init

# Distribute to testers
firebase appdistribution:distribute app/build/outputs/apk/release/app-release.apk \
  --app YOUR_FIREBASE_APP_ID \
  --groups testers
```

## CI/CD with GitHub Actions

This repository includes a GitHub Actions workflow for automated builds. The workflow:
- Runs on every push and pull request
- Builds both debug and release variants
- Runs unit tests
- Archives build artifacts

See `.github/workflows/android-build.yml` for details.

## Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumentation Tests

```bash
./gradlew connectedAndroidTest
```

## Troubleshooting

### Build Issues

**Problem**: Gradle sync failed
- **Solution**: Ensure you have JDK 17 installed and set as your project JDK

**Problem**: SDK not found
- **Solution**: Set `ANDROID_HOME` environment variable or create `local.properties`:
  ```properties
  sdk.dir=/path/to/Android/sdk
  ```

**Problem**: Out of memory during build
- **Solution**: Increase heap size in `gradle.properties`:
  ```properties
  org.gradle.jvmargs=-Xmx4096m
  ```

### Runtime Issues

**Problem**: App crashes on startup
- **Solution**: Check logcat output in Android Studio for stack traces

**Problem**: Cards not displaying
- **Solution**: Verify that all card data is properly loaded in TarotRepository

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Based on Jungian psychology and Carl Jung's work on archetypes
- Inspired by Sallie Nichols' "Jung and Tarot: An Archetypal Journey"
- Built with modern Android development best practices

## Support

For issues and questions:
- Open an issue on GitHub
- Check existing documentation
- Review the code comments

## Roadmap

- [ ] Add more detailed card interpretations
- [ ] Implement card position meanings in spreads
- [ ] Add daily card feature
- [ ] Implement reading history
- [ ] Add custom spread builder
- [ ] Localization support
- [ ] Dark theme improvements

---

Made with ❤️ for psychological growth and self-discovery
