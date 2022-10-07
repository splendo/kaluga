afterEvaluate {
    // publishing {
    //     publications {
    //         val component_type: String by extra
    //         val library_version: String by extra
    //         logger.lifecycle("This project module will be published as: $component_type")
    //         if (type.contains("compose")) {
    //             release(MavenPublication) {
    //                 from(components.release)
    //
    //                 artifactId = project.name
    //                 groupId = "com.splendo.kaluga"
    //                 version = library_version
    //             }
    //             // Creates a Maven publication called “debug”.
    //             debug(MavenPublication) {
    //                 from(components.debug)
    //
    //                 artifactId = project.name
    //                 groupId = "com.splendo.kaluga"
    //                 version = library_version
    //             }
    //         } else {
    //             kotlinMultiplatform { publication ->
    //                 artifactId = project.name
    //                 groupId = "com.splendo.kaluga"
    //                 version = library_version
    //             }
    //         }
    //     }
    // }
}
