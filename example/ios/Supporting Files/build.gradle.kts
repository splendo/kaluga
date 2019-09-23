buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {

        val kotlinVer = (gradle as ExtensionAware).extra["kotlin_version"]
        classpath("com.android.tools.build:gradle:3.6.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
        classpath("com.google.gms:google-services:4.3.1")
    }
}
