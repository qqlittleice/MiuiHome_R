import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.rikka.tools.autoresconfig")
}

android {
    compileSdk = 33
    namespace = "com.yuk.miuiHomeR"
    defaultConfig {
        applicationId = namespace
        minSdk = 30
        targetSdk = 33
        versionCode = 16
        versionName = "1.0.6" + (getGitHeadRefsSuffix(rootProject))
    }
    val properties = Properties()
    runCatching { properties.load(project.rootProject.file("local.properties").inputStream()) }
    val keystorePath = properties.getProperty("KEYSTORE_PATH") ?: System.getenv("KEYSTORE_PATH")
    val keystorePwd = properties.getProperty("KEYSTORE_PASS") ?: System.getenv("KEYSTORE_PASS")
    val alias = properties.getProperty("KEY_ALIAS") ?: System.getenv("KEY_ALIAS")
    val pwd = properties.getProperty("KEY_PASSWORD") ?: System.getenv("KEY_PASSWORD")
    if (keystorePath != null) {
        signingConfigs {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = keystorePwd
                keyAlias = alias
                keyPassword = pwd
                enableV3Signing = true
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf("proguard-rules.pro", "proguard-log.pro"))
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
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
        jvmTarget = JavaVersion.VERSION_11.majorVersion
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
                (this as BaseVariantOutputImpl).outputFileName = "MiuiHomeR-$versionName($versionCode)-$name.apk"
            }
        }
    }
}
fun getGitHeadRefsSuffix(project: Project): String {
    // .git/HEAD描述当前目录所指向的分支信息，内容示例："ref: refs/heads/master\n"
    val headFile = File(project.rootProject.projectDir, ".git" + File.separator + "HEAD")
    if (headFile.exists()) {
        val string: String = headFile.readText(Charsets.UTF_8)
        val string1 = string.replace(Regex("""ref:|\s"""), "")
        val result = if (string1.isNotBlank() && string1.contains('/')) {
            val refFilePath = ".git" + File.separator + string1
            // 根据HEAD读取当前指向的hash值，路径示例为：".git/refs/heads/master"
            val refFile = File(project.rootProject.projectDir, refFilePath)
            // 索引文件内容为hash值+"\n"，
            // 示例："90312cd9157587d11779ed7be776e3220050b308\n"
            refFile.readText(Charsets.UTF_8).replace(Regex("""\s"""), "").subSequence(0, 7)
        } else {
            string.substring(0, 7)
        }
        println("commit_id: $result")
        return ".$result"
    } else {
        println("WARN: .git/HEAD does NOT exist")
        return ""
    }
}
autoResConfig {
    generateClass.set(true)
    generatedClassFullName.set("com.yuk.miuiHomeR.utils.Locales")
    generateRes.set(true)
    generatedArrayFirstItem.set("SYSTEM")
    generateLocaleConfig.set(true)
}
dependencies {
    compileOnly(project(":hidden-api"))
    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha01")
    implementation("androidx.collection:collection:1.2.0")
    implementation("androidx.core:core:1.9.0")
    implementation("androidx.fragment:fragment:1.5.5")
    implementation("androidx.lifecycle:lifecycle-common:2.5.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.vectordrawable:vectordrawable-animated:1.1.0")
    implementation("com.github.kyuubiran:EzXHelper:1.0.3")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    implementation(files("libs/animation-alpha.aar"))
    implementation(files("libs/appcompat-alpha.aar"))
    implementation(files("libs/annotation-alpha.aar"))
    implementation(files("libs/appcompat-resources-alpha.aar"))
    implementation(files("libs/core-alpha.aar"))
    implementation(files("libs/haptic-alpha.aar"))
    implementation(files("libs/internal-alpha.aar"))
    implementation(files("libs/annotation-alpha.aar"))
    implementation(files("libs/preference-alpha.aar"))
    implementation(files("libs/springback-alpha.aar"))
    implementation(files("libs/blur-alpha.aar"))
    implementation(files("libs/common-alpha.aar"))
    implementation(files("libs/Smooth-alpha.aar"))
}
