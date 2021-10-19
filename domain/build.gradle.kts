plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
}

configure<JavaPluginConvention> {
    sourceCompatibility = Versions.sourceCompatibility
    targetCompatibility = Versions.targetCompatibility
}

dependencies {
    implementation(project(":common"))
    implementation(Libs.kotlinStdlib)
    implementation(Libs.coroutines)
    implementation(Libs.arrowCore)
}
