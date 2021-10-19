buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(BuildTools.navigationSafeArgs)
    }
}

apply(plugin = Plugins.navigationKotlin)

plugins {
    id(Plugins.android)
    id(Plugins.kotlinAndroid)
    id(Plugins.kapt)
}

val VERSION_MAJOR: String by project
val VERSION_MINOR: String by project
val VERSION_PATCH: String by project
val versionMajor = VERSION_MAJOR.toInt()
val versionMinor = VERSION_MINOR.toInt()
val versionPatch = VERSION_PATCH.toInt()

val appVersionCode = versionMajor * 1_000_000 + versionMinor * 1_000 + versionPatch
val appVersionName = "$versionMajor.$versionMinor.$versionPatch"

android {
    compileSdkVersion(Versions.targetSdk)
    defaultConfig {
        applicationId = "com.jsanzo97.recipemanager"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = appVersionCode
        versionName = appVersionName
        resValue("string", "app_name", "@string/application_name")
        resConfigs("en", "es")
        multiDexEnabled = true
        buildConfigField("String", "SERVER_ENDPOINT", "\"http://ec2-52-28-187-11.eu-central-1.compute.amazonaws.com/\"")
    }
    signingConfigs {
        val KEYSTORE_PASSWORD: String by project
        val KEY_PASSWORD: String by project
        getByName("debug") {
            try {
                storeFile = file("${project.rootDir}/keystore.jks")
                storePassword = KEYSTORE_PASSWORD
                keyAlias = "debug"
                keyPassword = KEY_PASSWORD
            } catch (e: Exception) {
                throw InvalidUserDataException("You should define KEYSTORE_PASSWORD and KEY_PASSWORD in gradle.properties.")
            }
        }
        register("release") {
            try {
                storeFile = file("${project.rootDir}/keystore.jks")
                storePassword = KEYSTORE_PASSWORD
                keyAlias = "release"
                keyPassword = KEY_PASSWORD
            } catch (e: Exception) {
                throw InvalidUserDataException("You should define KEYSTORE_PASSWORD and KEY_PASSWORD in gradle.properties.")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    sourceSets {
        forEach {
            it.java.srcDir("src/${it.name}/kotlin")
        }
        getByName("main") {
            val addResources: (Array<File>) -> Unit = { files: Array<File> ->
                files.filter { it.exists() }
                    .mapNotNull { it.listFiles { file: File -> file.isDirectory } }
                    .forEach { folders -> res.srcDirs(*folders) }
            }
            val resScreens = file("src/main/res-screens")

            addResources(arrayOf(resScreens))
        }
    }
    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":remote"))
    implementation(project(":local"))
    implementation(Libs.kotlinStdlib)
    implementation(Libs.coroutines)
    implementation(Libs.coroutinesAndroid)
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.lifecycle)
    implementation(Libs.liveDataKtx)
    implementation(Libs.viewModelKtx)
    implementation(Libs.navigationFragmentKtx)
    implementation(Libs.navigationUiKtx)
    implementation(Libs.drawerLayout)
    implementation(Libs.koin)
    implementation(Libs.koinAndroid)
    implementation(Libs.koinAndroidScope)
    implementation(Libs.koinAndroidViewModel)
    implementation(Libs.arrowCore)
    implementation(Libs.okhttp)
    implementation(Libs.roomKtx)
    implementation(Libs.retrofit)
    implementation(Libs.retrofitMoshi)
    implementation(Libs.threeTenABP)
    implementation(Libs.support)
    implementation(Libs.timber)
    implementation(Libs.preferences)
    implementation(Libs.googleApiLocation)
    implementation(Libs.googleApiMaps)
    implementation(Libs.googleApiUtils)

    debugImplementation(DebugLibs.okhttpLogginInterceptor)
    debugImplementation(DebugLibs.chucker)
    debugImplementation(DebugLibs.oklog)
    debugImplementation(DebugLibs.debugDb)

    kapt(Kapt.lifecycle)
}
