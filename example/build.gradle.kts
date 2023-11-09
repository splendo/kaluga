import java.io.File
import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("multiplatform").apply(false)
    id("kaluga-library-components")
}

buildscript {
    configurations.classpath {
        resolutionStrategy {
            // Temporarily override ktlint version until kotlinter is updated
            // Required because Composable methods require an override for function naming rules
            force(
                "com.pinterest.ktlint:ktlint-rule-engine:1.0.1",
                "com.pinterest.ktlint:ktlint-rule-engine-core:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-core:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-checkstyle:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-json:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-html:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-plain:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-sarif:1.0.1",
                "com.pinterest.ktlint:ktlint-ruleset-standard:1.0.1"
            )
        }
    }
}