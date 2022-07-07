import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.yuk.miuiHomeR"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf("proguard-rules.pro", "proguard-log.pro"))
        }
    }

    androidResources {
        additionalParameters("--allow-reserved-package-id", "--package-id", "0x45")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
            excludes += "/*.json"
        }
        dex {
            useLegacyPackaging = true
        }
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName =
                    "MiuiHomeR-$versionName($versionCode)-$name.apk"
            }
        }
    }
}

dependencies {
    compileOnly(project(":hidden-api"))
    compileOnly("de.robv.android.xposed:api:82")

    implementation(files("libs/animation-debug.aar"))
    implementation(files("libs/appcompat-debug.aar"))
    implementation(files("libs/appcompat-resources-debug.aar"))
    implementation(files("libs/core-debug.aar"))
    implementation(files("libs/haptic-debug.aar"))
    implementation(files("libs/internal-debug.aar"))
    implementation(files("libs/annotation-debug.aar"))
    implementation(files("libs/recyclerview-debug.aar"))
    implementation(files("libs/preference-debug.aar"))
    implementation(files("libs/springback-debug.aar"))
    implementation(files("libs/utils-debug.aar"))
    implementation(files("libs/view-debug.aar"))
    implementation(files("libs/blur-debug.aar"))

    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    implementation("com.github.kyuubiran:EzXHelper:0.9.8")
    implementation("com.github.topjohnwu.libsu:core:5.0.2")

    implementation("androidx.fragment:fragment:1.5.0-alpha04")
    implementation("androidx.customview:customview:1.1.0")
    implementation("androidx.core:core:1.9.0-alpha04")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.vectordrawable:vectordrawable-animated:1.1.0")
    implementation("androidx.activity:activity:1.6.0-alpha01")
    implementation("androidx.resourceinspection:resourceinspection-annotation:1.0.1")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
}
