# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Related Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) - Detailed architecture documentation
- [ROLES_SPEC.md](ROLES_SPEC.md) - User roles and specifications

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

### Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  (Screens, ViewModels, Contracts - MVI with Orbit)          │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                            │
│  (Repository Interfaces)                                     │
├─────────────────────────────────────────────────────────────┤
│                       Data Layer                             │
│  (Repository Impl, DataSources, DTOs)                       │
├─────────────────────────────────────────────────────────────┤
│                     Network Layer                            │
│  (ApiResponse, NetworkResult, Ktor HttpClient)              │
└─────────────────────────────────────────────────────────────┘
```

### Network Layer

Located in `composeApp/src/commonMain/kotlin/az/less/mobile/network/`

**Standard API Response Structure:**
```kotlin
// All API responses follow this structure
ApiResponse<T>(
    status: String,      // "success" or "error"
    message: String,
    errors: List<String>,
    data: T?,            // Generic data payload
    meta: ResponseMeta?  // requestId, path, method, timestamp
)

// Error response
ApiError(
    status: String,
    message: String,
    errors: List<String>,
    meta: ResponseMeta?
)
```

**NetworkResult - Sealed class for handling responses:**
```kotlin
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T, val message: String, val meta: ResponseMeta?)
    data class Error(val error: ApiError)
    data object Loading

    // Chainable handlers
    fun onSuccess(action: (T) -> Unit): NetworkResult<T>
    fun onError(action: (ApiError) -> Unit): NetworkResult<T>
    fun map(transform: (T) -> R): NetworkResult<R>
}
```

**safeApiCall - Extension for DataSource usage:**
```kotlin
// Wraps Ktor calls and parses ApiResponse automatically
suspend fun emailLogin(email: String): NetworkResult<EmailLoginData> {
    return safeApiCall {
        httpClient.post("v1/auth/email-login") {
            setBody(EmailLoginRequest(email = email))
        }
    }
}
```

### Data Layer

**DataSource** - Handles API calls using `safeApiCall`:
```kotlin
// Located in: data/datasource/
class AuthDataSource(private val httpClient: HttpClient) {
    suspend fun emailLogin(email: String): NetworkResult<EmailLoginData>
    suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData>
    suspend fun resendOtp(email: String): NetworkResult<ResendOtpData>
}
```

**Repository Implementation** - Delegates to DataSource:
```kotlin
// Located in: data/repository/
class AuthorizationRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthorizationRepository {
    override suspend fun emailLogin(email: String) = authDataSource.emailLogin(email)
}
```

**DTOs** - Request/Response models:
```kotlin
// Located in: data/remote/model/auth/
@Serializable data class EmailLoginRequest(val email: String)
@Serializable data class EmailLoginData(val email: String, val maskedEmail: String?, ...)
```

### Domain Layer

**Repository Interfaces** - Abstraction for data operations:
```kotlin
// Located in: domain/repository/
interface AuthorizationRepository {
    suspend fun emailLogin(email: String): NetworkResult<EmailLoginData>
    suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData>
    suspend fun resendOtp(email: String): NetworkResult<ResendOtpData>
}
```

### MVI Pattern (Orbit)

Every screen follows the MVI pattern with three components:

```kotlin
// Contract file - State, Intent, SideEffect
data class ExampleState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean get() = data.isNotEmpty()  // Computed properties for validation
}

sealed interface ExampleIntent {
    data class UpdateData(val value: String) : ExampleIntent
    data object Submit : ExampleIntent
}

sealed interface ExampleSideEffect {
    data object NavigateBack : ExampleSideEffect
    data object NavigateNext : ExampleSideEffect
    data class ShowSuccess(val message: String) : ExampleSideEffect
    data class ShowError(val message: String) : ExampleSideEffect
}

// ViewModel - Uses repository with NetworkResult
class ExampleViewModel(
    private val repository: ExampleRepository
) : ViewModel(), ContainerHost<ExampleState, ExampleSideEffect> {

    override val container = viewModelScope.container(ExampleState())

    fun onIntent(intent: ExampleIntent) {
        when (intent) {
            is ExampleIntent.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        repository.submit(state.data)
            .onSuccess { data ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(ExampleSideEffect.NavigateNext)
            }
            .onError { error ->
                reduce { state.copy(isLoading = false, error = error.message) }
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

```kotlin
// NetworkModule - Ktor HttpClient configuration
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) { json(...) }
            install(Logging) { level = LogLevel.ALL }
            defaultRequest {
                url("https://axshambazari.com/api/")
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Accept, "application/json")
            }
        }
    }
}

// DataModule - DataSources and Repositories
val dataModule = module {
    single { AuthDataSource(get()) }
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get()) }
}

// CacheModule - DataStore and local repositories
val cacheModule = module {
    single { createPlatformDataStore() }
    single { UserRepository(get()) }
}

// ViewModelModule - All ViewModel registrations
val viewModelModule = module {
    viewModelOf(::LoginEmailViewModel)
    viewModelOf(::LoginCodeViewModel)
    // ... other ViewModels
}
```

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

**Components:** DsButton (with loading state), DsTextField, CheckBox, RadioButton, Cell, DsToolBar, SelectionField, SegmentedButton, LessToast/AnimatedToast, various BottomSheets.

**DsButton with loading:**
```kotlin
DsButton(
    text = "Submit",
    onClick = { },
    enabled = state.isValid,      // Controls gray disabled state
    isLoading = state.isLoading,  // Shows spinner, prevents clicks, keeps color
    variant = ButtonVariant.Primary
)
```

### Image Picker (imagepickerkmp)

Used for selecting images from gallery in merchant flows:
```kotlin
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher

// In Screen composable - controlled by state boolean (e.g. state.showImagePicker)
Box {
    if (state.showImagePicker) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                photos.firstOrNull()?.let { photo ->
                    coroutineScope.launch {
                        val bytes = photo.loadBytes()
                        if (bytes != null) {
                            onIntent(MyIntent.OnImageSelected(bytes))
                        } else {
                            onIntent(MyIntent.OnImagePickerDismiss)
                        }
                    }
                } ?: onIntent(MyIntent.OnImagePickerDismiss)
            },
            onError = { onIntent(MyIntent.OnImagePickerDismiss) },
            onDismiss = { onIntent(MyIntent.OnImagePickerDismiss) },
            allowMultiple = false
        )
    }
}
```

### Multipart Form Data Upload

For endpoints requiring file uploads (e.g. venue creation with images):
```kotlin
// In DataSource - use submitFormWithBinaryData
suspend fun createVenue(name: String, image: ByteArray?): NetworkResult<VenueDto> {
    return safeApiCall {
        httpClient.submitFormWithBinaryData(
            url = "v1/venues",
            formData = formData {
                append("name", name)
                image?.let {
                    append("image", it, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    })
                }
            }
        ) {
            method = HttpMethod.Post
        }
    }
}
```

### JSON Request Body (Preferred for non-file endpoints)

For POST/PUT endpoints without file uploads, use `@Serializable` request DTOs with `setBody()`:
```kotlin
// Create request DTO
@Serializable data class CreateBoxRequest(
    val title: String,
    val boxType: String,
    val quantity: Int
)

// In DataSource - simple and clean
suspend fun createBox(request: CreateBoxRequest): NetworkResult<CreateBoxResponseDto> {
    return safeApiCall {
        httpClient.post("v1/boxes") { setBody(request) }
    }
}
```

### DTO Naming Conventions

- **Request body:** `XxxRequest` (e.g. `CreateBoxRequest`, `EmailLoginRequest`)
- **Response data:** `XxxDto` or `XxxData` (e.g. `CreateBoxResponseDto`, `EmailLoginData`)
- **Avoid duplicate class names** across DTO files — Kotlin sees all classes in the same package. Check existing DTOs before naming.
- **Don't duplicate fields handled by the network layer** — `safeApiCall` already parses `ApiResponse<T>` wrapping (`status`, `message`, `errors`, `meta`). DTOs should only model the `data` payload.

### Localization

String resources in 3 languages — always add to all:
- `composeApp/src/commonMain/composeResources/values/strings.xml` (English)
- `composeApp/src/commonMain/composeResources/values-az/strings.xml` (Azerbaijani)
- `composeApp/src/commonMain/composeResources/values-ru/strings.xml` (Russian)

## Key Paths

| Purpose | Path |
|---------|------|
| Shared code | `composeApp/src/commonMain/kotlin/az/less/mobile/` |
| Android code | `composeApp/src/androidMain/kotlin/az/less/mobile/` |
| iOS code | `composeApp/src/iosMain/kotlin/az/less/mobile/` |
| Network layer | `composeApp/src/commonMain/kotlin/az/less/mobile/network/` |
| Data layer | `composeApp/src/commonMain/kotlin/az/less/mobile/data/` |
| Domain layer | `composeApp/src/commonMain/kotlin/az/less/mobile/domain/` |
| DataSources | `composeApp/src/commonMain/kotlin/az/less/mobile/data/datasource/` |
| Repositories | `composeApp/src/commonMain/kotlin/az/less/mobile/data/repository/` |
| DTOs | `composeApp/src/commonMain/kotlin/az/less/mobile/data/remote/model/` |
| Navigation graphs | `composeApp/src/commonMain/kotlin/az/less/mobile/navigation/` |
| DI modules | `composeApp/src/commonMain/kotlin/az/less/mobile/di/` |
| Design system | `design-system/src/commonMain/kotlin/az/less/designsystem/` |
| Dependencies | `gradle/libs.versions.toml` |

## Development Notes

- **Application ID:** `az.less.mobile`
- **Min SDK:** 24, **Target SDK:** 36
- **JVM Target:** Java 11
- **API Base URL:** `https://axshambazari.com/api/`
- Maps integration uses Google Maps on Android; iOS implementation is placeholder

## Adding New API Endpoints

1. **Create DTOs** in `data/remote/model/`:
```kotlin
@Serializable data class MyRequest(val field: String)
@Serializable data class MyData(val result: String)
```

2. **Add to DataSource** in `data/datasource/`:
```kotlin
suspend fun myEndpoint(param: String): NetworkResult<MyData> {
    return safeApiCall {
        httpClient.post("v1/my-endpoint") {
            setBody(MyRequest(field = param))
        }
    }
}
```

3. **Add to Repository Interface** in `domain/repository/`:
```kotlin
suspend fun myEndpoint(param: String): NetworkResult<MyData>
```

4. **Implement in Repository** in `data/repository/`:
```kotlin
override suspend fun myEndpoint(param: String) = dataSource.myEndpoint(param)
```

5. **Use in ViewModel**:
```kotlin
repository.myEndpoint(param)
    .onSuccess { data -> /* handle success */ }
    .onError { error -> /* handle error */ }
```
