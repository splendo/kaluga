## Build instructions

This project uses Android Studio. You might need a canary version at times. Check [`kaluga.androidGradlePluginVersion` in gradle.properties](gradle.properties) to see which Android gradle plugin is used, as this determines compatability with Android Studio versions. You can check the [Android Gradle plugin release notes](https://developer.android.com/studio/releases/gradle-plugin), the [Android Studio release notes](https://developer.android.com/studio/releases) and the [Android Studio Preview Release Updates](https://androidstudio.googleblog.com/) pages to see which version to use.

You can also check the Kotlin version, and if needed align this with the Kotlin plugin version for Android Studio under `Android Studio` -> `Preferences` -> `Languages & Frameworks` -> `Kotlin`.

Install the Kotlin Mobile Multiplatform plugin (KMM) for additional functionality, such as running the iOS app straight from Android Studio.

For advanced iOS development XCode and/or AppCode can be used. AppCode can also use the KMM plugin. 

## Adding new modules

There is a template and step list for adding new modules, see [adding-a-new-module](adding-a-new-module/).

## Tests

The main tests are in `commonTest`. These test should be able to run in iOS, Android, JS and JVM scopes. However the latter two are not yet properly supported, and tests might be broken as a result.

If a purely common test does not work, you can make the test's class `open` or `abstract` so that specific platform tests can extend them to test their own implementations.

By extending `BaseTest` you can avoid a lot of boilerplate to make tests behave cross-platform (e.g. enableing the coroutines debugger).

There are specialized test classes in the [`test-utils`](test-utils/) modules, e.g. for testings `Flows`, `ViewModel`s etc. 

### Android tests

Android has two test targets, unit and integration. Both should be able to run the tests defined in the common set.

To run the location tests, the `Kaluga Tests` app needs to be set as the mock location app in developer settings (on lower platform levels just allowing mock locations might suffice)

### JVM tests

Consider using a Gradle task configuration to run JVM tests (you can use the `--tests` to filter) if the regular JUnit runner does not work. The test runner UI and debugging will still work.

### iOS tests

iOS tests can be run using the `iosTest` task in gradle which uses XCTest.
Make sure you have the Simulator setup with a working target device. For now you can change the target device inside the gradle build file.

The `ioTest` task supports the `--tests` flag like other Gradle tasks to filter which tests to run.

Note that for Kaluga iOS tests run on a background thread, in order to have a properly working Main dispatchers (and align better with Android). How this works is described in [`test-utils-base`](test-utils-base/)

## Architecture

Most of the components within this project use Kotlin coroutines and `Flow`, so for new components try to use these APIs where it makes sense.

For modules that rely on state, try using `StateRepo` based classes. These automatically expose a `Flow` for state changes, and using `ColdStateRepo` cold flow behaviour can be used.

## Branching

Kaluga uses [Gitflow](https://nvie.com/posts/a-successful-git-branching-model/).

We use with the following branches:
- `master`
- `develop`
- `release/*` (example: `release/0.1.2`)
- `feature/[<issue_id>-]*` (example: `feature/345-bluetooth-scanning`)
- `hotfix/[<issue_id>-]*` (example: `hotfix/678-navigation-crash`)

When making a pull request fill in the template.

## API compatibility

The [binary-compatibility-validator](https://github.com/Kotlin/binary-compatibility-validator) gradle plugin is used to keep track of our public API.

This means if you change the public API you need to run the `apiDump` task, and then commit and changes to the `.api` files or CI will fail.

Please study these changes carefully both when committing and reviewing pull requests to ensure there are:
- no needless breaking changes to the public API
- no classes or methods added that could be `private` or `internal`

Currently this tool only supports JVM based targets, so iOS/js only API changes are not tracked yet.

## Publishing

### Publishing process

#### Version

The version is retrieved from `libs.versions.toml`'s `kaluga` property. 

In case the version does not already end with `-SNAPSHOT`, it is post-fixed with the current git branch (unless that branch is `master`, `main` or `develop`) and `-SNAPSHOT` (unless the `MAVEN_CENTRAL_RELEASE` environment variable is set to `true`). The exact implementation of this can be found in [kaluga-library-components/src/main/kotlin/GitBranch.kt](kaluga-library-components/src/main/kotlin/GitBranch.kt).

For example if the base version is `1.1` and `feature/123_fix_bug` is the current branch the resulting version will be `1.1-feature-123_fix_bug-SNAPSHOT`.

To simulate release from a branch such as `develop` or `main` you can set the `GITHUB_REF_NAME` environment variable to these values.

#### Local Testing

Before doing any publishing, make sure that changes are working with the one available in [Nexus Repository Manager](`s01.oss.sonatype.org`).
Just adding the following code inside the `local.properties` file you can test both Android and iOS example app in `kaluga/example/`.
```
kaluga.exampleEmbeddingMethod=repo
kaluga.exampleMavenRepo=https://s01.oss.sonatype.org/service/local/repositories/comsplendo-REPO_NUMBER/
kaluga.libraryVersion=LIBRARY_VERSION
```
Where 
`REPO_NUMBER` is the id of the staging repository created to do the release (normally done automatically by GitHub Actions upon a `master` commit, the number can be found on the https://s01.oss.sonatype.org console under "staging repositories")
`LIBRARY_VERSION` is the version of the library that we are publish

Don't forget to remove these when you are done.

#### Publishing locally

1. Publish to local maven:

```sh
./gradlew publishToMavenLocal
```

2. Upload and publish on Maven Central:

```sh
./gradlew publishAllPublicationsToSonatypeRepository -PsigningKeyId=SIGNING_KEY_ID -PsigningPassword=SIGNING_KEY_PASSWORD -PsigningSecretKeyRingFile=SIGNING_KEY_FILE -PossrhUsername=OSSRH_USERNAME -PossrhPassword=OSSRH_PASSWORD
```

Where `SIGNING_KEY_ID` is the key id associated with the signing key,
`SIGNING_KEY_PASSWORD` is the password for the signing key,
`SIGNING_KEY_PASSWORD` is the gpg file used for signing,
`OSSRH_USERNAME` is the Sonatype user name to upload the repository to,
and `OSSRH_PASSWORD` is the password for the Sonatype account to upload the repository to.

If these values are present as environment variables they will also be picked up.

#### Publishing via CI

GitHub Actions automatically publishes every branch (except `master`) to the Sonatype snapshot repository (`https://s01.oss.sonatype.org/content/repositories/snapshots/`). Commits on `master` are send to the Sonatype staging repository as the first step of a Maven Central release.

#### Releasing to Maven Central

Projects publishing to Sonatype's staging repository need to be manually closed and released (promoted) before they will appear on Maven Central. This can only be done by people with access to https://s01.oss.sonatype.org.

#### Increase version after publishing

In case this has not been done yet, bump the base version at [kaluga-library-components/src/main/kotlin/Library](kaluga-library-components/src/main/kotlin/Library]) in the `develop` branch to start the next development iteration.

```kotlin
private val baseVersion = "X.X.X"
```

Also update the version numbers in the README to the just released version and the next version. 

## Code conventions

The project uses regular Kotlin code conventions. This includes not creating `com/splendo/kaluga` directories, since they are common to all other folders.

### Code style verification

This project uses [ktlint](https://github.com/pinterest/ktlint) Kotlin linter with standard rules.
Each component should setup kotlinter gradle [plugin](https://github.com/jeremymailen/kotlinter-gradle) in `build.gradle.kts` file:

```kotlin
plugins {
    id("org.jmailen.kotlinter")
}
```

You can run `Lint Kotlin` configuration from IDE before commit changes to git.

#### Formatting

You can run `Format Kotlin` configuration to reformat source code if needed.

See kotlinter and gradle plugin documentation for more details.

## Frequent issues

```xcrun: error: SDK "iphonesimulator" cannot be located
xcrun: error: unable to lookup item 'Path' in SDK 'iphonesimulator'
e: org.jetbrains.kotlin.konan.KonanExternalToolFailure: The /usr/bin/xcrun command returned non-zero exit code: 1.
```
Go to `XCode` -> `Preferences` -> `Locations tab` -> `Command Line Tools` dropdown.

It should show none selected, so select any item.
