# Phase 1-B Account + Profile Foundation Lock

## Status

LOCKED

Phase 1-B Account + Profile Foundation has completed its architecture audit and stabilization review.

## Scope

Phase 1-B covers:

- Account Foundation
- Profile Foundation
- Account / User / Profile domain boundaries
- Repository and LocalDataSource boundaries
- ViewModel and Factory wiring
- Navigation integration
- Profile unit tests
- Profile Compose UI tests
- Android UI test execution

## Domain Model Boundaries

### Account

Account represents the system-level identity of a FriendZone user.

Account is intentionally separate from User and Profile.

### User

User represents a FriendZone domain person or identity reference.

### Profile

Profile represents the social identity of a FriendZone user.

Profile is intentionally separate from Account and User.

The following boundaries are locked:

- Account != User
- Account != Profile
- User != Profile

## Account Architecture

AccountScreen
→ AccountViewModel
→ AccountRepository
→ AccountRepositoryImpl
→ AccountLocalDataSource

## Profile Architecture

ProfileScreen
→ ProfileAction
→ ProfileViewModel
→ ProfileRepository
→ ProfileRepositoryImpl
→ ProfileLocalDataSource

## Dependency Rules

- UI does not access DataSource directly.
- UI does not access database or API directly.
- ViewModels depend on repository abstractions.
- Repositories depend on data-source abstractions.
- Navigation contains route-to-screen mapping only.
- Navigation does not contain business logic.
- Core does not depend on Feature implementations.
- Existing working V1 Share functionality is preserved.

## Testing Status

### Profile

- Repository unit tests: PASS
- ViewModel unit tests: PASS
- Compose UI tests: PASS
- Android UI test execution: PASS

Android UI test execution was verified at GitHub checkpoint #143.

### Account

Account-specific test coverage is not currently present.

This is recorded as a future test-coverage improvement and does not change the current architecture boundary.

## Local Data Strategy

The current Account and Profile local implementations use in-memory data sources.

Persistent storage such as Room or DataStore is intentionally deferred until it is required by a later feature.

## Lock Rule

Do not perform unnecessary refactoring of the Account or Profile foundation.

Future changes should preserve the locked domain boundaries and dependency direction unless there is a clear architectural or functional reason to change them.

## Principle

ရှိပြီးသားအရာကို မဖျက်ဘဲ၊ လိုအပ်တဲ့နေရာမှာပဲ တစ်လွှာချင်းတိုးမယ်။

## Next Phase

Future feature work must build on this foundation incrementally.

Phase 1-B Account + Profile Foundation is locked.
