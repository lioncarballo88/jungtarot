# Deployment Checklist for Jung Tarot

Use this checklist to ensure a smooth deployment process.

## Pre-Deployment Setup (One Time)

### Development Environment
- [ ] Android Studio installed (Hedgehog 2023.1.1 or later)
- [ ] JDK 17 or later installed
- [ ] Android SDK with API level 34 installed
- [ ] Git configured with your credentials
- [ ] Repository cloned locally

### Keystore Setup (Critical - Do This First!)
- [ ] Generate signing keystore: `keytool -genkey -v -keystore release.keystore -alias jungtarot -keyalg RSA -keysize 2048 -validity 10000`
- [ ] **BACKUP YOUR KEYSTORE** - Store it securely (you cannot update your app without it!)
- [ ] Copy `keystore.properties.template` to `keystore.properties`
- [ ] Fill in keystore.properties with your actual values
- [ ] Verify keystore.properties is in .gitignore (it should be)
- [ ] Never commit keystore files to version control

### Google Play Console Setup
- [ ] Create Google Play Developer account ($25 one-time fee)
- [ ] Verify your developer account
- [ ] Create new app in Play Console
- [ ] Complete app details (name, description, category)
- [ ] Prepare store listing assets:
  - [ ] 2+ screenshots
  - [ ] Feature graphic (1024x500)
  - [ ] App icon (512x512)
  - [ ] Short description (80 chars)
  - [ ] Full description (4000 chars)
- [ ] Complete content rating questionnaire
- [ ] Set up pricing and distribution
- [ ] Prepare privacy policy (required for Play Store)

### GitHub Setup (For CI/CD)
- [ ] Set up GitHub repository secrets:
  - [ ] `KEYSTORE_BASE64` - Base64 encoded keystore file
  - [ ] `KEYSTORE_PASSWORD` - Your keystore password
  - [ ] `KEY_ALIAS` - Your key alias
  - [ ] `KEY_PASSWORD` - Your key password
- [ ] Verify GitHub Actions is enabled for your repository

## Before Each Release

### Code Preparation
- [ ] All features implemented and tested
- [ ] Code reviewed and approved
- [ ] All unit tests passing: `./gradlew test`
- [ ] Lint checks passing: `./gradlew lint`
- [ ] No critical bugs or crashes
- [ ] Performance tested on multiple devices
- [ ] Tested on minimum SDK (API 24)
- [ ] Tested on target SDK (API 34)

### Version Management
- [ ] Update `versionCode` in `app/build.gradle.kts` (increment by 1)
- [ ] Update `versionName` in `app/build.gradle.kts` (semantic version)
- [ ] Update CHANGELOG.md (if you have one)
- [ ] Prepare release notes for Play Store

### Build Configuration
- [ ] Uncomment signing configuration in `app/build.gradle.kts`
- [ ] Uncomment `signingConfig = signingConfigs.getByName("release")` in release build type
- [ ] Verify ProGuard rules are correct
- [ ] Verify all resources are properly included

## Release Build Process

### Local Build Method

#### Step 1: Clean Build
```bash
./gradlew clean
```
- [ ] Clean build completed successfully

#### Step 2: Build Release AAB (for Play Store)
```bash
./gradlew bundleRelease
```
- [ ] Build completed successfully
- [ ] AAB file generated at: `app/build/outputs/bundle/release/app-release.aab`
- [ ] File size is reasonable (check it's not too large)

#### Step 3: Build Release APK (for direct distribution)
```bash
./gradlew assembleRelease
```
- [ ] Build completed successfully
- [ ] APK file generated at: `app/build/outputs/apk/release/app-release.apk`

#### Step 4: Test Release Build
- [ ] Install APK on a test device: `adb install app/build/outputs/apk/release/app-release.apk`
- [ ] Test all major features work correctly
- [ ] Verify app launches without crashes
- [ ] Check app signing is correct: `keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk`

### Automated Build Method (GitHub Actions)

#### Create Release Tag
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```
- [ ] Tag created and pushed
- [ ] GitHub Actions workflow triggered
- [ ] Build job completed successfully
- [ ] Artifacts uploaded successfully
- [ ] GitHub Release created (if on main branch)

## Play Store Submission

### Upload Build
- [ ] Log in to Google Play Console
- [ ] Navigate to your app
- [ ] Go to Release > Production (or Testing)
- [ ] Create new release
- [ ] Upload `app-release.aab` file
- [ ] Wait for upload to complete (may take a few minutes)

### Release Information
- [ ] Add release notes (what's new)
- [ ] Set release name (optional)
- [ ] Review release

### Roll Out
- [ ] Choose rollout percentage (start with 20% for safety)
- [ ] Click "Review Release"
- [ ] Verify all information is correct
- [ ] Click "Start Rollout"
- [ ] Monitor for crashes and issues

## Post-Release

### Immediate (First 24 Hours)
- [ ] Monitor crash reports in Play Console
- [ ] Check user reviews and ratings
- [ ] Verify app appears in Play Store search
- [ ] Test installation from Play Store
- [ ] Monitor analytics (if configured)
- [ ] Respond to critical issues immediately

### Short Term (First Week)
- [ ] Increase rollout percentage gradually (if started at partial rollout)
- [ ] Continue monitoring crash reports
- [ ] Collect user feedback
- [ ] Plan hotfix if critical issues found
- [ ] Update rollout to 100% if stable

### Long Term
- [ ] Track key metrics (downloads, active users, retention)
- [ ] Respond to user reviews
- [ ] Plan next release based on feedback
- [ ] Schedule regular updates (monthly/quarterly)

## Alternative Distribution Checklist

### GitHub Releases
- [ ] Create release tag: `git tag -a v1.0.0 -m "Release 1.0.0"`
- [ ] Push tag: `git push origin v1.0.0`
- [ ] GitHub Actions creates release automatically
- [ ] Verify release appears in GitHub Releases section
- [ ] Add release notes manually if needed
- [ ] Download and test the APK from releases

### Firebase App Distribution
- [ ] Install Firebase CLI: `npm install -g firebase-tools`
- [ ] Login: `firebase login`
- [ ] Initialize: `firebase init appdistribution`
- [ ] Distribute: `firebase appdistribution:distribute app/build/outputs/apk/release/app-release.apk --app <APP_ID> --groups testers`
- [ ] Verify testers receive notification
- [ ] Collect feedback from testers

### Direct APK Distribution
- [ ] Build release APK
- [ ] Test APK thoroughly
- [ ] Host APK on your website or file hosting
- [ ] Create download page with installation instructions
- [ ] Warn users about "Install from Unknown Sources"
- [ ] Consider using QR code for easy download
- [ ] Monitor download counts

## Rollback Plan

### If Critical Issues Found After Release

#### Play Store
- [ ] Create hotfix branch
- [ ] Fix critical issue
- [ ] Increment versionCode
- [ ] Build new release
- [ ] Submit as emergency update
- [ ] Or: Halt rollout and revert to previous version in Play Console

#### GitHub/Direct Distribution
- [ ] Remove problematic release from GitHub Releases
- [ ] Re-promote previous stable version
- [ ] Update download links
- [ ] Notify users of the issue

## Version History Tracking

### Current Release
- Version Code: _____
- Version Name: _____
- Release Date: _____
- Rollout Status: _____

### Previous Releases
| Version | Release Date | Notes |
|---------|--------------|-------|
| 1.0.0   | YYYY-MM-DD   | Initial release |

## Emergency Contacts

- Google Play Support: https://support.google.com/googleplay/android-developer
- GitHub Support: https://support.github.com
- Development Team Lead: [Add contact]
- Project Manager: [Add contact]

## Common Issues and Solutions

### Build fails with "SDK not found"
```bash
# Create local.properties
echo "sdk.dir=/path/to/Android/sdk" > local.properties
```

### "Could not resolve dependencies"
```bash
# Clear Gradle cache
./gradlew clean --refresh-dependencies
```

### "Signing failed"
- Verify keystore password is correct
- Check keystore.properties file exists and is correct
- Ensure signing config is uncommented in build.gradle.kts

### "Upload failed" in Play Console
- Ensure versionCode is higher than previous release
- Verify AAB is signed correctly
- Check file size limits (150MB for AAB)
- Ensure you're not trying to upload the same versionCode twice

## Notes

- Always keep a backup of your keystore file in a secure location
- Document any changes to build configuration
- Keep release notes clear and user-friendly
- Test on real devices, not just emulators
- Consider staged rollouts to minimize impact of issues
- Plan for version compatibility if you have a backend API

---

**Remember**: The signing keystore is critical. Losing it means you cannot update your app on the Play Store!

Last Updated: [Current Date]
