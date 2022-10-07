repositories {
    google()
    mavenCentral()
}

// apply("$path_prefix/gradle/android_common.gradle.kts")
//
// android {
//
//     testOptions {
//         unitTests.returnDefaultValues = true
//     }
//
//     packagingOptions {
//         resources.excludes.add("META-INF/kotlinx-coroutines-core.kotlin_module")
//         resources.excludes.add("META-INF/shared_debug.kotlin_module")
//         resources.excludes.add("META-INF/kotlinx-serialization-runtime.kotlin_module")
//         resources.excludes.add("META-INF/AL2.0")
//         resources.excludes.add("META-INF/LGPL2.1")
//         // bytebuddy 🤡
//         resources.excludes.add("win32-x86-64/attach_hotspot_windows.dll")
//         resources.excludes.add("win32-x86/attach_hotspot_windows.dll")
//         //
//         resources.excludes.add("META-INF/licenses/ASM")
//     }
// }
//
// task printConfigurations {
//     doLast {
//         configurations.each { println(it) }
//     }
// }
//
// afterEvaluate {
//     val ios_targets: List<String> by extra
//     ios_targets.each { target ->
//         if (tasks.getNames().contains("linkDebugTest${target.capitalize()}"))
//             // creating copy task for the target
//             tasks.create(name = "copy${target.capitalize()}TestResources", type = Copy) {
//                 from(file('src/iosTest/resources/.'))
//                 into(file("$buildDir/bin/$target/debugTest"))
//             }
//
//             // apply copy task to the target
//             tasks.named("linkDebugTest${target.capitalize()}").configure {
//                 dependsOn("copy${target.capitalize()}TestResources")
//             }
//     }
// }
//
// ktlint {
//     disabledRules = listOf("no-wildcard-imports","filename","import-ordering")
// }

// apply(from = "$path_prefix/gradle/componentskt.gradle.kts")
