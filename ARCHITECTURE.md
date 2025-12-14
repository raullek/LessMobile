# LessMobile Architecture Documentation

## Overview

LessMobile is a **Kotlin Multiplatform Mobile (KMM)** application targeting Android and iOS platforms. The project uses **Jetpack Compose Multiplatform** for UI and follows the **MVI (Model-View-Intent)** architectural pattern with the Orbit MVI library.

---

## Project Structure

```
LessMobile/
├── composeApp/                    # Main application module
│   └── src/
│       ├── commonMain/            # Shared Kotlin code
│       ├── androidMain/           # Android-specific code
│       ├── iosMain/               # iOS-specific code
│       └── commonTest/            # Shared tests
├── design-system/                 # Reusable UI component library
│   └── src/
│       ├── commonMain/
│       ├── androidMain/
│       ├── androidDeviceTest/
│       ├── androidHostTest/
│       └── iosMain/
├── iosApp/                        # iOS app entry point
├── gradle/
│   └── libs.versions.toml         # Centralized dependency versions
├── build.gradle.kts               # Root build configuration
├── settings.gradle.kts            # Module configuration
└── gradle.properties              # Gradle properties
```

---

## Technology Stack

| Category | Technology | Version |
|----------|------------|---------|
| Language | Kotlin | 2.2.21 |
| UI Framework | Jetpack Compose Multiplatform | 1.9.3 |
| Architecture | MVI (Orbit) | 11.0.0 |
| DI | Koin | 4.1.1 |
| HTTP Client | Ktor | 3.3.2 |
| Navigation | Jetpack Navigation Compose | 2.9.1 |
| Image Loading | Coil | 3.3.0 |
| Data Persistence | AndroidX DataStore | 1.1.7 |
| Maps | Google Maps Android | 19.0.0 |
| Serialization | Kotlinx JSON | - |
| Coroutines | Kotlinx Coroutines | 1.10.2 |

### Android Configuration

- **Min SDK:** 24
- **Target SDK:** 36
- **Compile SDK:** 36
- **JVM Target:** Java 11
- **Application ID:** `az.less.mobile`

---

## Architecture Pattern

### MVI with Orbit

The project follows the **MVI (Model-View-Intent)** pattern using the Orbit MVI library. Each feature has:

- **State:** Immutable data class representing UI state
- **SideEffect:** Sealed interface for one-time events (navigation, toasts)
- **Intent:** Sealed interface for user actions

```kotlin
// Contract Example
data class AccountState(
    val name: String = "",
    val email: String = ""
)

sealed interface AccountSideEffect {
    data object NavigateBack : AccountSideEffect
}

sealed interface AccountIntent {
    data class UpdateName(val name: String) : AccountIntent
}

// ViewModel Example
class AccountViewModel : ViewModel(), ContainerHost<AccountState, AccountSideEffect> {
    override val container = viewModelScope.container(AccountState())

    fun onIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.UpdateName -> reduce { state.copy(name = intent.name) }
        }
    }
}
```

---

## Package Structure

### Root Package: `az.less.mobile`

```
az.less.mobile/
├── di/                     # Dependency Injection
│   ├── KoinConfiguration   # Koin initialization
│   ├── NetworkModule       # Ktor HttpClient setup
│   ├── CacheModule         # DataStore configuration
│   └── ViewModelModule     # ViewModel registration
├── data/                   # Data layer (repositories, data sources)
├── domain/                 # Domain layer (use cases, entities)
├── navigation/             # Navigation graphs
│   ├── NavigationGraph     # Root navigation
│   ├── MainGraph           # Home tab screens
│   ├── MoreGraph           # Account/Settings flow
│   └── ReserveGraph        # Reservation flow
└── presentation/           # UI layer
    ├── main/               # Main tab screens
    │   ├── offers/         # Offers screen
    │   ├── explore/        # Map exploration
    │   ├── orders/         # Orders history
    │   ├── saved/          # Saved items
    │   ├── search/         # Search functionality
    │   └── more/           # More menu
    ├── account/            # Account management
    ├── onboarding/         # Authentication flow
    │   ├── welcome/        # Welcome screen
    │   ├── loginemail/     # Email login
    │   └── otp/            # OTP verification
    ├── reserve/            # Reservation flow
    └── maps/               # Maps integration
```

---

## Dependency Injection

### Koin Setup

The project uses **Koin** for dependency injection. Configuration is in the `di` package.

#### Modules

1. **NetworkModule** - HTTP client configuration
   ```kotlin
   // Provides Ktor HttpClient with JSON serialization
   // Base URL: http://127.0.0.1:8080/api/v1
   ```

2. **CacheModule** - Local data persistence
   ```kotlin
   // Provides DataStore<Preferences> for persistent storage
   ```

3. **ViewModelModule** - ViewModel registration
   ```kotlin
   // Registers all ViewModels as singletons
   ```

#### Initialization

Koin is initialized in `LessApplication.onCreate()` and wrapped in Compose via `KoinContext { }`.

---

## Navigation

### Type-Safe Navigation with Sealed Classes

The project uses Jetpack Navigation Compose with type-safe routes defined as sealed classes.

#### Navigation Graphs

| Graph | Purpose | Screens |
|-------|---------|---------|
| MainGraph | Home tabs | Offers, Explore, Orders, Saved, More, Search, CategoryOffers |
| MoreGraph | Account & Settings | Account, PaymentMethods, AddNewCard, Welcome, LoginEmail, LoginCode |
| ReserveGraph | Reservations | Reservation-related screens |

#### Route Definitions

```kotlin
// HomeScreens for main tabs
sealed class HomeScreens {
    object Offers : HomeScreens()
    object Explore : HomeScreens()
    object Orders : HomeScreens()
    object Saved : HomeScreens()
    object More : HomeScreens()
}

// MoreScreens for account flow
sealed class MoreScreens {
    object Account : MoreScreens()
    object PaymentMethods : MoreScreens()
    object AddNewCard : MoreScreens()
}
```

---

## Design System

### Module: `design-system`

A separate module containing reusable UI components and theming.

#### Package: `az.less.designsystem`

```
az.less.designsystem/
├── base/
│   ├── Color.kt          # Color definitions
│   ├── Elevation.kt      # Shadow/elevation values
│   ├── Radius.kt         # Corner radius values
│   ├── Size.kt           # Size constants
│   ├── Spacing.kt        # Spacing values
│   ├── Typography.kt     # Text styles
│   └── Theme.kt          # LessTheme composition locals
└── components/
    ├── Button.kt         # Custom buttons
    ├── TextField.kt      # Input fields
    ├── CheckBox.kt       # Checkbox component
    ├── RadioButton.kt    # Radio button component
    ├── Cell.kt           # List cell component
    ├── TopBar.kt         # App bar component
    ├── SelectionField.kt # Selection input
    ├── SegmentedButton.kt# Segmented control
    ├── Icons.kt          # Icon definitions
    └── *BottomSheet.kt   # Various bottom sheet components
```

#### Usage

```kotlin
LessTheme {
    // Access theme values via composition locals
    val colors = LessTheme.colors
    val typography = LessTheme.typography
    val spacing = LessTheme.spacing
}
```

---

## Data Layer

### Current Status

The data layer structure is prepared but not yet implemented:

- `data/` - Empty, ready for repositories and data sources
- `domain/` - Empty, ready for use cases and entities

### Network Configuration

```kotlin
// Ktor HttpClient configured in NetworkModule
HttpClient {
    install(ContentNegotiation) { json() }
    install(Logging) { level = LogLevel.ALL }
    defaultRequest {
        url("http://127.0.0.1:8080/api/v1")
    }
}
```

### Local Persistence

```kotlin
// DataStore configured in CacheModule
DataStore<Preferences> // File: dice.preferences_pb
```

---

## Maps Integration

### Components

| Component | Purpose |
|-----------|---------|
| Google Maps Android | Native maps SDK |
| Maps Compose | Compose integration |
| Location Services | GPS and location |

### Implementation

- **Platform-specific:** `GoogleMaps.android.kt` in androidMain
- **Permission handling:** `LocationPermissionHandler.kt`
- **Models:** `LatLong`, `Marker` classes

### Permissions Required

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

---

## Testing

### Framework

- Kotlin Test (JUnit compatible)
- AndroidX Test for instrumentation tests

### Test Locations

| Location | Type |
|----------|------|
| `composeApp/src/commonTest/` | Shared unit tests |
| `design-system/src/androidHostTest/` | Host-based tests |
| `design-system/src/androidDeviceTest/` | Instrumentation tests |

### Dependencies

- `kotlin-test`
- `kotlin-test-junit`
- `junit:4.13.2`
- `androidx.test.ext:junit`
- `androidx.test.espresso:espresso-core`

---

## Key Files Reference

| Component | Path |
|-----------|------|
| Root Build | `build.gradle.kts` |
| App Build | `composeApp/build.gradle.kts` |
| Design System Build | `design-system/build.gradle.kts` |
| Dependencies | `gradle/libs.versions.toml` |
| Manifest | `composeApp/src/androidMain/AndroidManifest.xml` |
| App Entry | `composeApp/src/commonMain/kotlin/az/less/mobile/App.kt` |
| MainActivity | `composeApp/src/androidMain/kotlin/az/less/mobile/MainActivity.kt` |
| Application | `composeApp/src/androidMain/kotlin/az/less/mobile/LessApplication.kt` |
| Koin Config | `composeApp/src/commonMain/kotlin/az/less/mobile/di/KoinConfiguration.kt` |
| Navigation | `composeApp/src/commonMain/kotlin/az/less/mobile/navigation/NavigationGraph.kt` |
| Theme | `design-system/src/commonMain/kotlin/az/less/designsystem/base/Theme.kt` |

---

## ViewModels

| ViewModel | Screen | Purpose |
|-----------|--------|---------|
| OffersViewModel | OffersScreen | Promotional offers |
| ExploreViewModel | ExploreScreen | Map exploration with filters |
| OrdersViewModel | OrdersScreen | Order history |
| SavedViewModel | SavedScreen | Saved items |
| MoreViewModel | MoreScreen | More menu options |
| SearchViewModel | SearchScreen | Search functionality |
| AccountViewModel | AccountScreen | Profile management |
| PaymentMethodsViewModel | PaymentMethodsScreen | Payment methods |
| AddNewCardViewModel | AddNewCardScreen | Add payment card |
| LoginEmailViewModel | LoginEmailScreen | Email login |
| LoginCodeViewModel | LoginCodeScreen | OTP verification |
| ReserveViewModel | Reserve screens | Reservation flow |
| CategoryOffersViewModel | CategoryOffersScreen | Category-based offers |

---

## Build Commands

```bash
# Build Android app
./gradlew :composeApp:assembleDebug

# Run Android app
./gradlew :composeApp:installDebug

# Run tests
./gradlew :composeApp:testDebugUnitTest
./gradlew :design-system:testDebugUnitTest

# Build iOS (requires macOS)
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

---

## Development Notes

### Current Status

- Active development on `develop` branch
- Recent focus on authentication/onboarding flows
- Splash screen and navigation recently implemented
- Maps integration completed

### Areas for Development

1. **Data Layer:** Repository pattern implementation pending
2. **Domain Layer:** Use cases to be added
3. **API Integration:** Backend API not yet connected (using mocks)
4. **Testing:** Minimal test coverage

### Git Workflow

- Feature branches merged via Pull Requests
- Main development on `develop` branch

---

## Architecture Strengths

- Clean MVI separation with Orbit
- Kotlin Multiplatform for code sharing
- Modular design system
- Type-safe navigation
- Modern tech stack (Compose, Ktor, Coroutines)
- Dependency injection with Koin

---

*Last Updated: December 2025*
