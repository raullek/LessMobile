# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Android (Windows)
.\gradlew.bat :composeApp:assembleDebug          # Build APK
.\gradlew.bat :composeApp:installDebug           # Install on device
.\gradlew.bat :composeApp:testDebugUnitTest      # Run unit tests

# Android (macOS/Linux)
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug
./gradlew :composeApp:testDebugUnitTest

# iOS (requires macOS)
./gradlew :composeApp:linkDebugFrameworkIosArm64
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
# Then open iosApp/ in Xcode and run

# Design System
.\gradlew.bat :design-system:testDebugUnitTest   # Unit tests
.\gradlew.bat :design-system:androidDeviceTest   # Instrumentation tests
```

## Architecture Overview

**Kotlin Multiplatform** app targeting Android and iOS using **Jetpack Compose Multiplatform** for shared UI.

### Modules
- `composeApp/` - Main application (shared + platform-specific code)
- `design-system/` - Reusable UI component library
- `iosApp/` - iOS Xcode project entry point

### MVI Pattern (Orbit)

Every screen follows the MVI pattern with three components:

```kotlin
// State - Immutable UI state
data class ExampleState(val data: String = "")

// SideEffect - One-time events (navigation, toasts)
sealed interface ExampleSideEffect {
    data object NavigateBack : ExampleSideEffect
}

// Intent - User actions
sealed interface ExampleIntent {
    data class UpdateData(val value: String) : ExampleIntent
}

// ViewModel implementation
class ExampleViewModel : ViewModel(), ContainerHost<ExampleState, ExampleSideEffect> {
    override val container = viewModelScope.container(ExampleState())

    fun onIntent(intent: ExampleIntent) {
        when (intent) {
            is ExampleIntent.UpdateData -> intent { reduce { state.copy(data = intent.value) } }
        }
    }
}
```

### Navigation Structure

Type-safe navigation using sealed classes. The app has dual navigation roots:

```
AppRootNavigation
├── ROOT_CLIENT → ClientGraph (customer-facing app)
│   ├── MainGraph (tabs: Offers, Explore, Orders, Saved, More, Search)
│   └── MoreGraph (Account, PaymentMethods, AddNewCard, Login flows)
└── ROOT_MERCHANT → MerchantGraph (merchant-facing app)
    └── MerchantScreens (Orders, Add, History, More)
```

### Dependency Injection (Koin)

Modules in `composeApp/src/commonMain/kotlin/az/less/mobile/di/`:
- `NetworkModule` - Ktor HttpClient (base URL: `http://127.0.0.1:8080/api/v1`)
- `CacheModule` - DataStore preferences
- `ViewModelModule` - All ViewModel registrations

Koin initializes in `LessApplication.onCreate()` (Android) and wraps with `KoinContext { }` in `App.kt`.

### Design System Usage

```kotlin
LessTheme {
    val colors = LessTheme.colors
    val typography = LessTheme.typography
    val spacing = LessTheme.spacing
    val radius = LessTheme.radius
}
```

Components: Button, TextField, CheckBox, RadioButton, Cell, TopBar, SelectionField, SegmentedButton, various BottomSheets.

## Key Paths

| Purpose | Path |
|---------|------|
| Shared code | `composeApp/src/commonMain/kotlin/az/less/mobile/` |
| Android code | `composeApp/src/androidMain/kotlin/az/less/mobile/` |
| iOS code | `composeApp/src/iosMain/kotlin/az/less/mobile/` |
| Navigation graphs | `composeApp/src/commonMain/kotlin/az/less/mobile/navigation/` |
| DI modules | `composeApp/src/commonMain/kotlin/az/less/mobile/di/` |
| Design system | `design-system/src/commonMain/kotlin/az/less/designsystem/` |
| Dependencies | `gradle/libs.versions.toml` |

## Development Notes

- **Application ID:** `az.less.mobile`
- **Min SDK:** 24, **Target SDK:** 36
- **JVM Target:** Java 11
- **Active branch:** `develop`
- **Data/Domain layers:** Structure exists but implementation pending (using mock data)
- Maps integration uses Google Maps on Android; iOS implementation is placeholder
