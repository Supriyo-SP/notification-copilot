# Notification Copilot: Spec

## Goal
A personal Android app that captures my notifications, finds deadlines, tasks and
events hidden in them, and shows a "Today" list with reminders, for the finalcial notification like
credit or debit, it will store the information in finance tracker section
Hobby project. Single user (me). Sideloaded, no Play Store release.

## Stack
- Kotlin, Jetpack Compose (Material 3), Room (KSP), Coroutines + Flow, WorkManager (later)
- Min SDK 26. Test device: my physical phone (Android 15, brand Motorola)
- Architecture: single module, ViewModel + Repository. No Hilt, no multi-module.
- Dependencies are managed through the Gradle version catalog (libs.versions.toml).
  Do NOT add a library without asking me first.

## Principles
- Everything stays on-device. No backend, no analytics, no accounts, no network calls.
- Classifier and extractor are pure Kotlin functions, unit-testable without an emulator.
- A wrong todo is worse than a missed one: low-confidence items go to "Suggestions".
- Small vertical slices. Every slice runs on my phone before the next starts.

## Current phase
Phase 1: Listener Spike (see PROGRESS.md for the current segment)

## Phase 1 segments
0 Setup · 1 Static UI · 2 Listener skeleton · 3 Read fields · 4 Save to Room ·
5 Live data in UI · 6 Permission UX · 7 Survival tooling · 8 Overnight test

## Data model (v0, subject to change)
NotificationEntity:
- id (auto), key (String), packageName, title, text, bigText?, postTime (Long),
  conversationId?, isOngoing, isGroupSummary, capturedAt (Long)
  ListenerEventEntity (segment 7):
- id, type ("CONNECTED" | "DISCONNECTED"), timestamp

## Privacy rules
- Exclude list (stored in the DB or DataStore): banking and OTP apps are NOT stored.
- Never log full notification text in release builds.

## Non-goals (for now)
ML or LLM models, cloud sync, personalization, focus mode, location/activity,
weekly insights, Calendar/Tasks API, Play Store release, iOS.

## Later ideas
See parking-lot.md. Do not implement anything from there unless I say so.