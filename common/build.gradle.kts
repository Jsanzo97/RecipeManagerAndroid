plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
}

configure<JavaPluginConvention> {
    sourceCompatibility = Versions.sourceCompatibility
    targetCompatibility = Versions.targetCompatibility
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.coroutines)
}
