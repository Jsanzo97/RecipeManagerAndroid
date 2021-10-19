import org.gradle.api.JavaVersion

object Plugins {
    const val android = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val javaLibrary = "java-library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlin = "kotlin"
    const val kapt = "kotlin-kapt"
    const val gradleVersions = "com.github.ben-manes.versions"
    const val navigationKotlin = "androidx.navigation.safeargs.kotlin"
}

object Versions {
    const val kotlin = "1.3.50"
    const val coroutines = "1.3.2"
    const val androidGradlePlugin = "3.5.1"
    const val gradleVersions = "0.25.0"

    const val minSdk = 21
    const val targetSdk = 29

    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8

    const val appCompat = "1.1.0"
    const val coreKtx = "1.1.0"
    const val fragment = "1.1.0"
    const val material = "1.1.0-alpha10"
    const val constraintLayout = "2.0.0-beta3"
    const val lifecycle = "2.1.0"
    const val room = "2.1.0"
    const val navigation = "2.1.0"
    const val drawerLayout = "1.1.0-alpha03"
    const val preferences = "1.1.0"
    const val googleApiLocation = "17.0.0"
    const val googleApiUtils = "0.6.0"

    const val arrow = "0.10.0"
    const val threeTenABP = "1.2.1"
    const val timber = "4.7.1"
    const val moshi = "1.8.0"
    const val retrofit = "2.6.2"
    const val okhttp = "4.2.1"
    const val koin = "2.0.1"

    const val chucker = "3.0.1"
    const val oklog = "2.3.0"
    const val debugDb = "1.0.6"
    const val support = "28.0.0"
}

object BuildTools {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
    const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object Libs {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val drawerLayout = "androidx.drawerlayout:drawerlayout:${Versions.drawerLayout}"
    const val preferences = "androidx.preference:preference:${Versions.preferences}"
    const val threeTenABP = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenABP}"
    const val arrowCore = "io.arrow-kt:arrow-core-data:${Versions.arrow}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val koin = "org.koin:koin-core:${Versions.koin}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinAndroidScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinAndroidViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val support = "com.android.support:support-v4:${Versions.support}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val googleApiLocation = "com.google.android.gms:play-services-location:${Versions.googleApiLocation}"
    const val googleApiMaps = "com.google.android.gms:play-services-maps:${Versions.googleApiLocation}"
    const val googleApiUtils = "com.google.maps.android:android-maps-utils:${Versions.googleApiUtils}"

}

object Kapt {
    const val lifecycle = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val room = "androidx.room:room-compiler:${Versions.room}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
}

object DebugLibs {
    const val chucker = "com.github.ChuckerTeam.Chucker:library:${Versions.chucker}"
    const val oklog = "com.github.simonpercic:oklog3:${Versions.oklog}"
    const val okhttpLogginInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val debugDb = "com.amitshekhar.android:debug-db:${Versions.debugDb}"
}
