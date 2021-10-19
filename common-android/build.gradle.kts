plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
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
    implementation(Libs.kotlinStdlib)
    implementation(Libs.coreKtx)
    implementation(Libs.fragmentKtx)
    implementation(Libs.liveDataKtx)
    implementation(Libs.navigationFragmentKtx)
    implementation(Libs.arrowCore)
    implementation(Libs.koinAndroidViewModel)
    implementation(Libs.timber)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)

}
