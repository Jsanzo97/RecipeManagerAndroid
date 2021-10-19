plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kapt)
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                        mapOf(
                                "room.schemaLocation" to "$projectDir/schemas",
                                "room.incremental" to "true"
                        )
                )
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        forEach {
            it.java.srcDir("src/${it.name}/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(Libs.kotlinStdlib)
    implementation(Libs.coroutines)
    implementation(Libs.arrowCore)
    implementation(Libs.roomKtx)

    kapt(Kapt.room)
}
