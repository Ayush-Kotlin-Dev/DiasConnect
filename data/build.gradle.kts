plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.9.22"
    kotlin("kapt")
    id("com.apollographql.apollo") version "4.0.0"
}
apollo {
    service("service") {
        packageName.set("com.ayush.data")
        introspection {
            endpointUrl.set("https://diasconnect-buyer-backend.onrender.com/graphql")
//            headers.put("api-key", "1234567890abcdef")
            schemaFile.set(file("src/main/graphql/com/ayush/data/schema.graphqls"))
        }
    }
}
android {
    namespace = "com.ayush.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {
    hilt()
    serialization()
    preferenceDataStore()
    apollo()
    domain()
}