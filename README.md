# AI Agent Automation — Android Operator

An Android-native voice-operated companion for the AI Agent Automation platform.

## Included
- Kotlin + Jetpack Compose starter app
- Speech recognition and text-to-speech interaction
- Runtime permission handling for microphone and notifications
- Optional AccessibilityService skeleton for user-approved UI automation
- Notification listener skeleton (user must enable it in Android settings)
- Secure backend connection settings
- Explicit confirmation screen before sending an action request
- Action log screen and device capability status

## Important limitations
This is a development starter, not a Play Store-ready or fully autonomous phone controller.
Android restricts background microphone access, call control, SMS, WhatsApp/Instagram automation,
and cross-app UI control. Capabilities depend on Android version, OEM, app policies, granted
permissions, and official APIs. Accessibility is not a bypass for app security or user consent.
The app does not silently answer calls, send messages, publish reels, make payments, or access
private data. Those workflows require supported integrations and explicit user approval.

## Build
Requirements: Android Studio, JDK 17 or newer, Android SDK 35.

1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Run on a physical Android device.
4. Set your backend URL in Settings.
5. Grant only permissions you understand.

The backend URL defaults to `http://10.0.2.2:8000` for the Android emulator.
For a physical phone, use your PC/server LAN address or HTTPS server URL.

## Security
- Do not hardcode API keys in the app.
- Use HTTPS outside local development.
- Keep approval enabled for messages, posts, calls, file changes and other external actions.
