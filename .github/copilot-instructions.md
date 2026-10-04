# Instructions for Copilot

Project: personal Android app "Notification Copilot". Read SPEC.md for the full context.

- Language/stack: Kotlin, Jetpack Compose (Material 3), Room with KSP, Coroutines/Flow.
- Min SDK 26. Prefer current, non-deprecated Android APIs.
- Keep it simple: ViewModel + Repository, no Hilt, no extra architecture layers.
- Put business logic (classification, extraction) in `domain/` as pure Kotlin
  functions with no Android imports.
- Never add network calls, analytics, or logging of full notification content.
- Add short comments explaining non-obvious Android behavior (permissions, lifecycle).
- When unsure about an API's behavior or version support, say so instead of guessing.
- Make small, focused changes. Don't refactor unrelated code.