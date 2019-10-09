plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.multiplatform'
}

repositories {
    mavenCentral()
    google()
    jcenter()
    maven { url "https://dl.bintray.com/pocketbyte/hydra/" }
}

apply from: "../../gradle/component.gradle"

kor
