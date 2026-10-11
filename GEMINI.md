# Project Guild - Assistant Memory & Guidelines

## 🚀 Build & Deployment Workflow (Option B: Local-First)
- **Default Build Mode**: Always build the debug APK directly on the local machine using Gradle Wrapper (`cmd.exe /c "set JAVA_HOME=C:\Users\Jayem Masiddo\.jdks\jdk-17.0.20.1+1&& gradlew.bat assembleDebug"`) for fast 15-second iteration.
- **Local Artifact Path**: The resulting APK is located at:
  `app/build/outputs/apk/debug/app-debug.apk`
- **Device Installation**:
  - Provide the user with the direct path to `app/build/outputs/apk/debug/app-debug.apk`.
  - When the user's Android device is connected via USB/WiFi ADB, use `adb install -r "app/build/outputs/apk/debug/app-debug.apk"` for instant 1-command deployment.
- **GitHub Actions Usage**: Only push to GitHub (`git push origin main`) when completing major milestones, when the user is completely satisfied with the changes, or upon explicit user request.
- **Keystore & Signatures**: Maintain consistent debug signing via `app/debug.keystore` and monotonic versioning so the app always installs cleanly as an update.
