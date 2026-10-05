# Notification Copilot

An Android app that captures your phone notifications, stores them **locally on your device**, and (in future versions) finds the deadlines, tasks and events hidden inside them.

> **Status:** early prototype (v0.1). Capture and display work. Smart classification and reminders are not built yet.

## Why this exists

Important messages (college deadlines, bills, meetings) get buried under promos and chat noise. The goal is one place that tells you what needs action today.

## Features (current)

- Captures notifications from your apps using Android's notification listener
- Saves them in a local database on the phone
- Shows a live list, newest first
- Shows listener connection history (so you can see if Android stopped the service)
- Excludes noisy system notifications and supports an exclude list of apps
- Shortcut to battery settings

## Roadmap

- [ ] Keyword-based categories (promo, social, important, informational)
- [ ] Deadline and task extraction
- [ ] "Today" screen with reminders
- [ ] Correction buttons and mute-by-sender
- [ ] Proper database migrations

## Privacy

- All data stays on your phone. The app has **no internet permission**, no accounts, no analytics and no backend.
- Because the app can read all your notifications, only install it if you trust the source, and ideally build it yourself from this code.
- To erase everything: uninstall the app, or clear its storage in Android Settings → Apps → Notification Copilot → Storage.
- Banking and OTP apps should be added to the exclude list so their notifications are never stored.

## Install (use the app)

Requires **Android 8.0 (API 26) or newer**.

1. Open the [Releases page](https://github.com/Supriyo-SP/notification-copilot/releases) on your phone and download the latest `.apk`.
2. Open the file. If Android asks, allow installing from your browser or file manager.
3. Open **Notification Copilot** and tap **Grant access**.
4. On the system screen, turn on **Notification Copilot**.
    - If the switch is greyed out (Android 13+): go to Settings → Apps → Notification Copilot → ⋮ (top right) → **Allow restricted settings**, then try again.
5. Go to Settings → Apps → Notification Copilot → Battery and choose **Unrestricted**, so Android does not stop the app in the background.
6. Check that the app now shows "Connected" and lists new notifications.

**Updating:** download the newer APK and install it over the old one.
> Note: while the app is in early development, an update may reset the saved notification list.

## Build from source

You need [Android Studio](https://developer.android.com/studio) (current stable version) and a phone with USB debugging enabled, or an emulator.

1. Clone the repo:
```
   git clone https://github.com/YOURUSERNAME/notification-copilot.git
```
2. Open the folder in Android Studio and wait for Gradle sync to finish.
3. Connect your phone (Settings → Developer options → USB debugging) and press **Run**.
4. Or build from the terminal:
```
   ./gradlew assembleDebug
```
The APK is created in `app/build/outputs/apk/debug/`.

## Tech stack

Kotlin · Jetpack Compose (Material 3) · Room (KSP) · Coroutines and Flow · `NotificationListenerService`

## Project structure

```
app/src/main/java/.../
├── service/   notification listener
├── data/      Room database (entities, DAOs)
├── domain/    classification and extraction logic (planned)
├── ui/        screens
└── util/      permission helpers
```

## Known limitations

- Android only. iOS does not allow reading other apps' notifications.
- The app sees only what a notification shows: text can be truncated, and sensitive content (such as OTPs) may be hidden by Android.
- Some phone brands (Xiaomi, Oppo, Vivo, Realme, Samsung) aggressively stop background apps. See [dontkillmyapp.com](https://dontkillmyapp.com) for per-brand steps.
- This is a hobby project with no warranty.

## Contributing

Issues and suggestions are welcome. For code changes: fork the repo, create a branch (`feature/...` or `fix/...`), and open a pull request.

## Versioning

[Semantic Versioning](https://semver.org). See [CHANGELOG.md](CHANGELOG.md) and the Releases page.

## License

MIT (see `LICENSE`).