import com.android.build.api.dsl.androidLibrary
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val appVersionName = "0.1.1"
val appVersionCode = 3

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "az.less.mobile.composeapp")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.core.splashscreen)

            // Google Maps for Android
            implementation(libs.google.maps.android)
            implementation(libs.google.maps.compose)
            implementation(libs.google.maps.android.compose.utils)
            implementation(libs.google.play.services.location)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Sandwich - API Response Handling
            implementation(libs.sandwich.core)
            implementation(libs.sandwich.ktor)

            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            implementation(libs.orbit.compose)
            implementation(libs.orbit.core)
            implementation(libs.orbit.viewmodel)

            implementation(libs.navigation.compose)

            // Design System Module
            implementation(projects.designSystem)

            implementation(libs.androidx.datastore)
            // The Preferences DataStore library
            implementation(libs.androidx.datastore.preferences)

            // Input Mask for Compose Multiplatform
            implementation(libs.inputmask.core)
            implementation(libs.inputmask.compose)

            // Compose Shimmer - Shimmer Effect
            implementation(libs.compose.shimmer)

            // ImagePickerKMP - Cross-platform Image Picker & Camera
            implementation(libs.imagepickerkmp)

            // Paging 3 - Pagination
            implementation(libs.paging.common)
            implementation(libs.paging.compose)

            // WebView for Compose Mult
            // iplatform
            implementation(libs.compose.webview.multiplatform)

            // Compass - Geocoding, Location, Autocomplete
            implementation(libs.compass.autocomplete)
            implementation(libs.compass.autocomplete.mobile)
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.permissions.mobile)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "az.less.mobile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "az.less.mobile"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersionName
    }
    base {
        archivesName.set("LessMobile-v$appVersionName($appVersionCode)")
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            val storeFilePath = keystoreProperties.getProperty("RELEASE_STORE_FILE")
            if (storeFilePath != null) {
                storeFile = rootProject.file(storeFilePath)
                storePassword = keystoreProperties.getProperty("RELEASE_STORE_PASSWORD")
                keyAlias = keystoreProperties.getProperty("RELEASE_KEY_ALIAS")
                keyPassword = keystoreProperties.getProperty("RELEASE_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "Axşam Bazarı Dev")
        }
        getByName("release") {
            isMinifyEnabled = false
            resValue("string", "app_name", "Axşam Bazarı")
            signingConfig = signingConfigs.getByName(
                if (keystorePropertiesFile.exists()) "release" else "debug"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

buildkonfig {
    packageName = "az.less.mobile"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "variant", "debug")
        buildConfigField(FieldSpec.Type.STRING, "apiEndPoint", "https://dev.example.com")
    }
    defaultConfigs("release") {
        buildConfigField(FieldSpec.Type.STRING, "variant", "release")
        buildConfigField(FieldSpec.Type.STRING, "apiEndPoint", "https://prod.example.com")
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

