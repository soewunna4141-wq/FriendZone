FriendZone — Target API 36+ / Play-Ready Migration

Status

Architecture Audit: APPROVED
Migration Phase / Step / Checkpoint Plan: LOCKED
Implementation: NOT STARTED

---

1. Migration Goal

Move the existing FriendZone application foundation to a modern Android API 36+ / Play-ready platform while preserving the existing application architecture and feature boundaries.

This migration is a platform/toolchain compatibility migration, not a feature architecture refactor.

---

2. Locked Principles

- Existing feature architecture remains preserved.
- Existing Repository / ViewModel / Factory / LocalDataSource boundaries remain unchanged unless compatibility requires a minimal source fix.
- Share V1 remains untouched.
- News Paper Foundation remains FINAL LOCKED.
- Reaction Foundation remains FINAL LOCKED.
- Reels Foundation remains FINAL LOCKED.
- P2P is excluded from the current scope and will be reconsidered during the Chat phase.
- No unnecessary refactoring.
- One meaningful change per GitHub Actions checkpoint.
- A failed checkpoint is not locked.
- A successful checkpoint is locked before proceeding to the next checkpoint.

---

3. Current Toolchain Baseline

- Android Gradle Plugin: 8.5.0
- Gradle: 8.7
- JDK: 17
- Kotlin: 1.9.24
- compileSdk: 34
- targetSdk: 34
- minSdk: 24
- Compose Compiler: 1.5.14
- Compose BOM: 2024.06.00
- Lifecycle: 2.8.1
- Navigation Compose: 2.7.7
- Coil: 2.7.0
- Media3: 1.4.1
- GitHub Actions Emulator API: 34

---

4. Migration Target

- compileSdk: 36
- targetSdk: 36+
- API 36-compatible Android Gradle Plugin
- Compatible Gradle version
- JDK 17 compatibility maintained where supported
- Compatible Kotlin / Compose / AndroidX dependencies
- Compatible Media3 version
- Updated GitHub Actions build and test environment
- Real-device APK validation

---

5. Migration Dependency Graph

Phase A — Architecture Audit
        ↓
Phase B — Build Toolchain
        ↓
Phase C — Android SDK
        ↓
Phase D — Dependency Compatibility
        ↓
Phase E — Source Compatibility
        ↓
Phase F — Regression Testing
        ↓
Phase G — CI/CD
        ↓
Phase H — Real Device
        ↓
Phase I — Final Audit
        ↓
TARGET API 36+ FINAL LOCK

---

6. Phase Plan

Phase A — Architecture Audit

Status: LOCKED

Scope:

- Current toolchain audit
- Target toolchain definition
- Dependency relationship audit
- Migration dependency graph
- Feature architecture preservation rules

No code changes.

---

Phase B — Build Toolchain Migration

Scope:

- Android Gradle Plugin
- Gradle
- JDK verification
- Build compatibility

Goal:

Establish a stable build toolchain that supports the API 36 migration.

---

Phase C — Android SDK Migration

Scope:

- compileSdk 36
- targetSdk 36

Goal:

Move the application compilation and target API baseline to Android 16 / API 36.

---

Phase D — Dependency Compatibility

Scope:

- Kotlin
- Compose
- AndroidX
- Lifecycle
- Navigation
- Coil
- Media3

Goal:

Verify and update dependencies only where required for the API 36-compatible toolchain.

---

Phase E — Source Compatibility

Scope:

- News
- Reaction
- Friends
- Follow
- Reels
- Account
- Profile
- Share V1

Goal:

Resolve only source-level compatibility issues introduced by the migration.

No architectural refactor.

---

Phase F — Regression Testing

Scope:

- Unit tests
- Repository tests
- ViewModel tests
- Factory tests
- Compose UI tests
- Instrumentation tests

Goal:

Verify that existing functionality remains intact after migration.

---

Phase G — CI/CD Migration

Scope:

- GitHub Actions toolchain
- Android SDK
- Emulator API
- Build
- UI tests
- APK artifact

Goal:

Make the CI environment match the migrated project baseline.

---

Phase H — Real Device Validation

Scope:

- Install migrated APK
- Application launch
- Basic navigation
- Existing feature smoke tests
- Share V1 smoke test
- Reels foundation smoke test

Goal:

Validate the migrated application on a physical Android device.

---

Phase I — Final Audit

Scope:

- Toolchain verification
- API 36 verification
- Dependency verification
- Source compatibility
- Regression tests
- CI/CD
- Real-device validation
- Architecture boundary verification

Result:

TARGET API 36+ / PLAY-READY MIGRATION FINAL LOCK

---

7. Checkpoint Workflow

Every implementation checkpoint follows this order:

Audit / Architecture
        ↓
Decision
        ↓
User Grant
        ↓
One meaningful change
        ↓
Commit
        ↓
GitHub Actions
        ↓
Green
        ↓
Lock checkpoint
        ↓
Next checkpoint

A failed build is investigated and fixed in a separate checkpoint.

---

8. One-Change Rule

Each GitHub Actions checkpoint should contain one meaningful migration change whenever practical.

Avoid combining unrelated:

- Toolchain changes
- SDK changes
- Dependency upgrades
- Source refactors
- CI changes

This keeps failures easy to identify and preserves a clear migration history.

---

9. Current Special Case — Media3

The Reels Full-screen Video phase selected Media3 / ExoPlayer as the playback engine.

The initial Media3 1.11.1 dependency was incompatible with the current compileSdk 34 baseline.

Checkpoint #376 failed because the dependency requires a newer compile SDK.

The temporary Media3 downgrade checkpoint was intentionally placed on hold while the project evaluates the API 36 migration.

Media3 compatibility will therefore be handled during Phase D after the required platform/toolchain migration.

---

10. Preserved Feature Architecture

The following existing foundations remain preserved during migration:

- Account + Profile Foundation
- News Paper Foundation
- Reaction Foundation
- Friend / Follow Foundation
- Reels Foundation
- Share V1

Migration must not change their architectural boundaries unless a concrete compatibility issue requires a minimal source-level adjustment.

---

11. Reels Foundation Boundary

Reels Foundation remains FINAL LOCKED.

The current foundation includes:

- Reel model
- Reels repository
- Local data source
- In-memory data source
- Reels action
- Reels UI state
- Reels ViewModel
- Reels ViewModel factory
- Reels screen
- Foundation tests
- Localization

Full-screen video playback remains the next Reels feature phase after the platform migration.

---

12. Explicitly Out of Scope

The following are not part of this migration:

- P2P
- Chat implementation
- Backend/API implementation
- Video upload
- Cloud storage
- Recommendation algorithms
- Feed ranking
- Notifications
- AI Agent implementation
- Unrelated architecture refactoring
- Cross-feature repository redesign

---

13. Migration Completion Criteria

The migration can be FINAL LOCKED only after:

- compileSdk 36 is verified
- targetSdk 36+ is verified
- compatible build toolchain is verified
- dependencies are compatible
- source compiles successfully
- unit tests pass
- repository tests pass
- ViewModel tests pass
- factory tests pass
- Compose/UI tests pass
- GitHub Actions build passes
- GitHub Actions tests pass
- APK artifact is produced
- physical-device smoke test passes
- existing feature boundaries remain intact
- Share V1 remains functional
- final architecture audit passes

---

14. Migration Status

Architecture Audit              🟢 LOCKED
Migration Plan                  🟢 LOCKED
Documentation                  ⏳ IN PROGRESS

Phase B — Build Toolchain       ⏳ NEXT
Phase C — Android SDK           ⏸
Phase D — Dependencies          ⏸
Phase E — Source Compatibility  ⏸
Phase F — Regression Tests      ⏸
Phase G — CI/CD                 ⏸
Phase H — Real Device           ⏸
Phase I — Final Audit           ⏸

---

15. Final Migration Lock

When all migration phases and validations are complete:

🎯 FriendZone — Target API 36+
        ↓
PLAY-READY MIGRATION
        ↓
🟢 FINAL AUDIT PASS
        ↓
🔒 FINAL LOCK
