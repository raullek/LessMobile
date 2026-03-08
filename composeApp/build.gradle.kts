import com.android.build.api.dsl.androidLibrary
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.regex.Pattern

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebase.crashlytics)
}

kotlin {
    project.extra.set("buildkonfig.flavor", currentBuildVariant())
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
            implementation(project.dependencies.platform(libs.android.firebase.bom))
            implementation(libs.android.firebase.analytics)
            implementation(libs.android.firebase.crashlytics)
            implementation(libs.android.firebase.config)
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

            // Compass - Geocoding, Location, Autocomplete
            implementation(libs.compass.autocomplete)
            implementation(libs.compass.autocomplete.mobile)
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.permissions.mobile)

            implementation(libs.firebase.analytics)
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.crashlytics)
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
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    flavorDimensions.add("variant")
    productFlavors {
        create("dev") {
            dimension = "variant"
            isDefault = true
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "Config Sample Dev")
        }

        create("prod") {
            dimension = "variant"
        }
    }
}

buildkonfig {
    packageName = "az.less.mobile"
    defaultConfigs {}
    defaultConfigs("dev") {
        buildConfigField(FieldSpec.Type.STRING, "variant", "dev")
        buildConfigField(FieldSpec.Type.STRING, "apiEndPoint", "https://dev.example.com")

    }

    defaultConfigs("prod") {
        buildConfigField(FieldSpec.Type.STRING, "variant", "dev")
        buildConfigField(FieldSpec.Type.STRING, "apiEndPoint", "https://prod.example.com")
    }
}

fun Project.getAndroidBuildVariantOrNull(): String? {
    val variants = setOf("dev", "prod")
    val taskRequestsStr = gradle.startParameter.taskRequests.toString()
    val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    } else {
        Pattern.compile("bundle(\\w+)(Release|Debug)")
    }

    val matcher = pattern.matcher(taskRequestsStr)
    val variant = if (matcher.find()) matcher.group(1).lowercase() else null
    return if (variant in variants) {
        variant
    } else {
        null
    }
}

private fun Project.currentBuildVariant(): String {
    val variants = setOf("dev", "prod")
    return getAndroidBuildVariantOrNull()
        ?: System.getenv()["VARIANT"]
            .toString()
            .takeIf { it in variants } ?: "dev"
}

dependencies {
    debugImplementation(compose.uiTooling)
}

