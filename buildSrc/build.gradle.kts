plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
    google()
}

dependencies {
    // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("com.android.tools.build:gradle:7.3.0")
}
