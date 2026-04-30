plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.enova.graphenefixer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.enova.graphenefixer"
        minSdk = 26
        targetSdk = 35
        versionCode = 100010
        versionName = "1.0.1"
    }

    signingConfigs {
        create("release") {
            storeFile = File(projectDir, "release-keystore.jks")
            storePassword = System.getenv("storePassword")
            keyAlias = System.getenv("keyAlias")
            keyPassword = System.getenv("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources.excludes.add("**/kotlin/**")
        resources.excludes.add("kotlin-tooling-metadata.json")
    }
}

dependencies {
    compileOnly(libs.xposed.api)
    compileOnly(libs.xposed.api.sources)
}