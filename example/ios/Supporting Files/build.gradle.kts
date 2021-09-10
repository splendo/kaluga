buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

val ext = (gradle as ExtensionAware).extra
val repo = ext["example_maven_repo"]
logger.lifecycle("Using repo: $repo for resolving dependencies")

allprojects {

    repositories {
        when(repo) {
            null, "", "local" -> mavenLocal()
            "none" -> {/* noop */}
            else ->
                maven(repo)
        }
        mavenCentral()
        google()
    }
}
