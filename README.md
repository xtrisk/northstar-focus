# Northstar Focus

Northstar Focus is a native Android project built with Gradle and Jetpack Compose. It was designed to satisfy the assignment requirements while feeling more personal and supportive than a default template app.

## What it includes

- Multiple views through a bottom navigation bar: `Overview`, `Planner`, and `Reflect`
- Interactive components such as buttons, switches, filter chips, and text input
- An information list using `LazyColumn`, which is the modern Compose replacement for `ListView`
- A warm custom visual style with a calmer tone and more intentional layout choices

## Requirements

- Android Studio
- Android SDK
- An Android emulator or a physical Android device

## Run in Android Studio

1. Open Android Studio.
2. Choose `Open` and select the project folder you cloned or downloaded, such as `northstar-focus`.
3. Wait for Gradle Sync to finish.
4. Open `Device Manager` and start an emulator, or connect an Android phone with USB debugging enabled.
5. Make sure the `app` run configuration is selected in the top toolbar.
6. Click the green `Run` button.

## Run from the command line

1. Open PowerShell in the project folder.
2. Build the app:

```powershell
.\gradlew.bat assembleDebug
```

3. Install the app on a running emulator or connected device:

```powershell
.\gradlew.bat installDebug
```

## GitHub-friendly quick commands

```powershell
git clone <your-repo-url>
cd northstar-focus
.\gradlew.bat assembleDebug
```

## Project Structure

- `app/` contains the Android application code
- `app/src/main/java/com/ink/northstarfocus/NorthstarApp.kt` contains the main Compose screens and app logic
- `app/src/main/java/com/ink/northstarfocus/MainActivity.kt` launches the app
- `build.gradle.kts` and `app/build.gradle.kts` define the Gradle build setup

## Notes

- The project uses Jetpack Compose for the UI.
- `LazyColumn` is used as the list-based information display component.
- If the emulator opens to a black screen, wake it with the emulator power button or do a cold boot from Android Studio Device Manager.

  ## Video

  https://lut-my.sharepoint.com/:v:/g/personal/sufian_sunjed_student_lut_fi/IQCofsIcL6YkR7M9XB-Cvz5JAdt7Ej_1qO1OK4W6Jzd3RxY?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=hEUpqw
