# FriendZone (Fz) — Multi-Platform Share Skeleton

A minimal Android (Kotlin + Jetpack Compose) project skeleton that lets a
user write one caption, optionally attach a photo/video, tick which of
Facebook / TikTok / Telegram to send it to, and tap **Post**.

## How it works

- `ShareDispatcher.kt` builds a standard Android `ACTION_SEND` intent and
  targets it at each selected platform's package name in turn. This opens
  each app's **own** share/compose screen — the user makes the final tap
  in each app themselves. No OAuth, no API keys, no app-review process
  needed for any platform.
- If a platform app isn't installed, it falls back to the system share
  chooser.
- `ComposerScreen.kt` is the single Compose screen: caption field, media
  picker, per-platform checkboxes, Post button.

## Known platform limits (by design of the share-sheet approach)

- **TikTok** — its share target generally only accepts image/video, not
  text-only posts. The app blocks sending text-only content to TikTok and
  asks the user to attach media first.
- **Facebook** — the installed Facebook app's share target usually opens
  its own composer pre-filled with your text/media; the user still taps
  Facebook's own "Post" button.
- **Telegram** — opens Telegram's forward/share screen where the user
  picks a chat, channel, or "Saved Messages" to send to.

## Next steps to make this buildable

1. Open in Android Studio (Jellyfish or newer) as an existing project.
2. Let Gradle sync — it will pull the versions pinned in
   `app/build.gradle.kts`.
3. Add real app icons under `res/mipmap-*` (a placeholder icon is not
   included in this skeleton).
4. Run on a device/emulator that has Facebook, TikTok, and Telegram
   installed to test each share target for real — emulators without
   those apps will just fall back to the generic chooser.

## Possible future upgrades

- Sequential share sheets with a short delay/confirmation between each,
  so the user isn't hit with three share sheets at once.
- Per-platform caption editing (e.g. auto-add hashtags only for TikTok).
- Persisting draft posts locally (Room database) so a post survives if
  the user backs out of a share sheet.
