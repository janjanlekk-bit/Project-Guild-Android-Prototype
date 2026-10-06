# Project Guild - Child App Prototype (Milestone 1)

**Project Guild** is a friendly, RPG-inspired productivity and learning application for children. It turns daily responsibilities and educational tasks into engaging adventurer quests, empowering kids to earn screen time and guild currency through real accomplishments.

---

## 🎯 Core Product Concept

The core behavioral loops are:

1. **LEARN → EARN SCREEN TIME → PLAY**
   - Children solve math challenges in the **Training Grounds** to earn verified screen time minutes.
   - Each correct problem awards **+5 minutes** of screen time and **+5 XP**.
   - No screen time is ever penalized for wrong answers; children receive friendly explanations and encouragement to retry.

2. **COMPLETE → PROVE → PARENT APPROVES → EARN REWARD**
   - Children accept real-world quests from the **Adventurer's Guild** (e.g., brushing teeth, making their bed, doing homework).
   - Once completed, the child submits proof (mock photo proof / parent confirmation).
   - In this prototype, parental approval or revision request is simulated locally via clearly marked developer controls.
   - Approved quests award **Guild Coins** (🪙) and **XP** (⚡) with visual fanfare!

---

## 🏗️ Architecture Summary

The prototype adheres to clean architecture principles with single observable state flow:

```
UI / Presentation Layer (Jetpack Compose & Material 3)
      │
      ▼
ViewModel Layer (GuildViewModel & MathViewModel via StateFlow)
      │
      ▼
Domain Layer (Models, MathQuestionGenerator, LevelCalculator)
      │
      ▼
Repository Layer (GuildRepository abstraction)
      │
      ▼
Data & Local Storage Layer (FileGuildStorage with kotlinx.serialization JSON)
```

### Key Architectural Decisions:
- **Ledger-Ready Design**: Although local for this prototype, all screen time additions, coin awards, and XP gains are recorded as immutable transaction models (`ScreenTimeTransaction`, `CoinTransaction`, `XpTransaction`). This makes future migration to an authoritative remote ledger (e.g., Supabase / PostgreSQL) straightforward.
- **Single Observable State**: `GuildRepository` maintains `StateFlow` streams for player stats, quests, and transactions. UI composables observe these flows rather than holding disjointed mutable state.
- **Offline & Persistence**: State is stored locally in `guild_state.json` inside the app's internal storage (`filesDir`). Progress (earned screen time, coins, XP, completed quests) survives application restarts.
- **Deterministic Math Engine**: `MathQuestionGenerator` supports customizable seeds for reproducible unit tests, difficulty levels (`EASY`, `MEDIUM`, `HARD`), guarantees non-negative subtraction results, and enforces whole-number division.

---

## 📱 Primary Screens

| Screen | Description |
| :--- | :--- |
| **🏠 Home** | Adventurer dashboard displaying child avatar, Level, XP bar (`720 / 1000 XP`), Screen Time (`45 min`), Guild Coins (`35 🪙`), daily quests, training grounds shortcut, and progress summary. |
| **🛡️ Guild** | Adventurer's Guild hub categorized into **Active**, **Available**, and **Completed** quests. Features visual difficulty tags, reward badges, and opens the mock photo verification and approval dialog. |
| **🧠 Math Training** | Training Grounds math arena with difficulty selection (`EASY`, `MEDIUM`, `HARD`), large child-friendly equation cards, multiple-choice buttons, instant celebratory feedback (`+5 MINUTES`), and 5-question session summaries. |
| **🧙 Profile** | Adventurer character card, level progression, lifetime statistics, achievements (e.g., *First Quest*, *Math Apprentice*), and recent ledger activity history. |

---

## 🛠️ Build Requirements

- **JDK**: Java 17 (e.g., Eclipse Temurin 17 or OpenJDK 17)
- **Gradle**: 9.1.0 (included via Gradle Wrapper `gradlew`)
- **Android SDK**: `compileSdk = 36`, `minSdk = 24`, `targetSdk = 36`
- **Build Tools**: Android SDK Build-Tools 36.0.0

> **Note**: Android Studio is **NOT** required. The project builds directly via Gradle CLI and GitHub Actions.

---

## 🚀 How to Build Locally

### On Windows (PowerShell):
```powershell
# Set JAVA_HOME if not already configured in your environment
$env:JAVA_HOME = "path/to/jdk-17"

# Run all unit tests
.\gradlew.bat testDebugUnitTest

# Assemble the debug APK
.\gradlew.bat assembleDebug
```

The compiled APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### On macOS / Linux:
```bash
export JAVA_HOME="/path/to/jdk-17"
chmod +x gradlew
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

---

## 🤖 GitHub Actions Workflow

A robust CI/CD workflow is provided at [`.github/workflows/android-build.yml`](.github/workflows/android-build.yml).

### Workflow Steps:
1. Triggered automatically on `push` and `pull_request` to `main`/`master` (or manual dispatch).
2. Checks out the repository.
3. Sets up JDK 17 with Gradle dependency caching.
4. Sets up the Android SDK automatically on Ubuntu.
5. Runs all unit tests (`./gradlew testDebugUnitTest`).
6. Builds the debug APK (`./gradlew assembleDebug`).
7. Uploads the debug APK as a downloadable artifact named `project-guild-debug-apk`.

---

## 📦 How to Download and Install the APK

1. Push your code to GitHub:
   ```bash
   git push origin main
   ```
2. Open your repository on GitHub and navigate to the **Actions** tab.
3. Select the latest **Android CI / Build APK** workflow run.
4. Scroll down to the **Artifacts** section at the bottom of the summary page.
5. Click **project-guild-debug-apk** to download the zip file.
6. Extract `app-debug.apk` from the zip archive.
7. Transfer the APK to your Android device via USB, Google Drive, or `adb`:
   ```bash
   adb install -r app-debug.apk
   ```
8. On your Android phone, enable *Install unknown apps* if prompted, and launch **Project Guild**!

---

## 🧪 Testing Summary

All unit tests run via `./gradlew testDebugUnitTest` and pass with 0 errors:

1. **`MathQuestionGeneratorTest`**:
   - Validates that questions contain 4 unique choices including the correct answer.
   - Ensures subtraction operations across 200 iterations never produce negative values ($num1 \ge num2$).
   - Confirms division produces exact integer quotients with zero remainder ($divisor > 0$).
   - Verifies difficulty boundaries (easy single-digit vs medium/hard).
   - Validates deterministic reproducibility using seeded random instances.
2. **`LevelCalculatorTest`**:
   - Confirms XP additions below 1000 maintain current level.
   - Verifies level-up triggering when XP crosses threshold.
   - Verifies rollover XP carried over accurately to subsequent levels.
   - Tests multi-level gains from large XP rewards.
3. **`GuildRepositoryTest`**:
   - Verifies correct math answer rewards +5 minutes screen time and +5 XP, appending ledger transactions.
   - Confirms incorrect math answer never deducts screen time and awards 0 minutes/XP.
   - Verifies mission acceptance, mock proof submission, and approval award coins and XP.
   - Confirms mission rejection does not award coins/XP and preserves status with rejection reason.
   - Verifies retry resets quest status back to `ACTIVE`.
   - Confirms local persistence survives mock app restarts by re-instantiating repository with persistent storage.

---

## ⚠️ Current Limitations (Milestone 1 Prototype)

- **Offline-Only**: Runs without internet; no backend server or cloud synchronization yet.
- **Simulated Camera / Proof**: Photo proof is simulated locally via mock preview and approval buttons; no real device camera permissions are requested.
- **No Device-Owner App Locking**: Actual system-level application suspension and screen time locking are deliberately postponed to Phase 9 to evaluate legitimate Android device-management APIs (DevicePolicyManager/Profile Owner).
- **Single Child Profile**: Pre-configured with sample adventurer "Alex" (Level 4).

---

## 🗺️ Roadmap & Next Milestones

- **Phase 2**: Visual polish, custom vector badges, sound effects, and enhanced haptics.
- **Phase 3**: Comprehensive local SQLite/Room screen-time ledger with time deduction countdown.
- **Phase 4**: Dedicated Parent Companion Application.
- **Phase 5**: Supabase backend integration (Authentication, PostgreSQL Database, Storage).
- **Phase 6**: Real camera capture and photo/video proof upload pipeline.
- **Phase 7**: Realtime parent approval notifications and sync via WebSockets / Push Notifications.
- **Phase 8**: Child reward store and coin redemption economy.
- **Phase 9**: Legitimate Android enterprise/device-management research for safe, compliant app suspension.
