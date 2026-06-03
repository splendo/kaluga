import groovy.json.JsonSlurper
import java.net.URI
import java.time.Year

plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "base"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    appleInterop {
        main {
            create("objectObserver").apply {
                definitionFile.set(project.file("src/nativeInterop/cinterop/objectObserver.def"))
                packageName("com.splendo.kaluga.base.kvo")
                compilerOpts("-I/src/nativeInterop/cinterop")
                includeDirs {
                    allHeaders("src/nativeInterop/cinterop")
                }
            }
        }
    }
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
        js {
            main {
                api(libs.kotlinx.atomicfu)
                implementation(npm("luxon", "3.5.0"))
            }
        }
        wasmJs {
            main {
                implementation(npm("luxon", "3.5.0"))
            }
        }
    }
}

// Regenerates DefaultCurrencyForCountry.kt from CLDR. Run on demand; output is checked in.
tasks.register("generateDefaultCurrencyMap") {
    val cldrVersion = libs.versions.cldr.get()
    group = "codegen"
    description = "Regenerates DefaultCurrencyForCountry.kt from CLDR $cldrVersion currencyData.json."

    val outputFile = layout.projectDirectory.file(
        "src/webMain/kotlin/text/DefaultCurrencyForCountry.kt",
    ).asFile
    outputs.file(outputFile)

    doLast {
        val url = "https://raw.githubusercontent.com/unicode-org/cldr-json/" +
            "$cldrVersion/cldr-json/cldr-core/supplemental/currencyData.json"
        val json = URI(url).toURL().openStream().bufferedReader().use { it.readText() }

        @Suppress("UNCHECKED_CAST")
        val supplemental = (JsonSlurper().parseText(json) as Map<String, Any>)["supplemental"] as Map<String, Any>
        @Suppress("UNCHECKED_CAST")
        val regions = (supplemental["currencyData"] as Map<String, Any>)["region"] as Map<String, Any>

        val mapping = sortedMapOf<String, String>()
        for ((regionCode, entries) in regions) {
            // ISO 3166 alpha-2 only — skip numeric region codes (`001`, `419`) that Kaluga never queries.
            if (regionCode.length != 2 || !regionCode.all { it.isLetter() }) continue
            @Suppress("UNCHECKED_CAST")
            val entryList = entries as List<Map<String, Map<String, String>>>
            for (entry in entryList) {
                val (code, attrs) = entry.entries.first()
                if (attrs["_to"] != null) continue       // historical currency
                if (attrs["_tender"] == "false") continue // non-tender clearing/accounting unit
                mapping[regionCode] = code
                break
            }
        }

        val out = buildString {
            appendLine("/*")
            appendLine(" Copyright ${Year.now().value} Splendo Consulting B.V. The Netherlands")
            appendLine()
            appendLine("    Licensed under the Apache License, Version 2.0 (the \"License\");")
            appendLine("    you may not use this file except in compliance with the License.")
            appendLine("    You may obtain a copy of the License at")
            appendLine()
            appendLine("      http://www.apache.org/licenses/LICENSE-2.0")
            appendLine()
            appendLine("    Unless required by applicable law or agreed to in writing, software")
            appendLine("    distributed under the License is distributed on an \"AS IS\" BASIS,")
            appendLine("    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.")
            appendLine("    See the License for the specific language governing permissions and")
            appendLine("    limitations under the License.")
            appendLine()
            appendLine(" */")
            appendLine()
            appendLine("// Generated from CLDR $cldrVersion supplemental/currencyData.json — do not edit by hand.")
            appendLine("// Refresh: ./gradlew :base:generateDefaultCurrencyMap")
            appendLine()
            appendLine("package com.splendo.kaluga.base.text")
            appendLine()
            appendLine("internal val defaultCurrencyForCountry: Map<String, String> = mapOf(")
            for ((regionCode, currency) in mapping) {
                appendLine("    \"$regionCode\" to \"$currency\",")
            }
            appendLine(")")
        }
        outputFile.writeText(out)
        logger.lifecycle("Wrote ${mapping.size} entries to ${outputFile.relativeTo(rootDir)}")
    }
}
