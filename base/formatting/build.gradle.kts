import groovy.json.JsonSlurper
import java.net.URI
import java.time.Year

plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "base.formatting"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                implementation(project(":base:core", ""))
                implementation(project(":base:bytes", ""))
                api(project(":base:i18n", ""))
                implementation(project(":date-time:date-time", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}

// Downloads the pinned-version (`libs.versions.cldr`) CLDR JSON at [cldrPath] and writes [outputFile] as
// a generated Kotlin file: the standard license header, a "do not edit / refresh" banner and the
// [packageName], followed by the declarations [build] produces from the parsed JSON root. Returns the
// number of entries.
fun generateFromCldr(cldrVersion: String, cldrPath: String, refreshTask: String, outputFile: File, packageName: String, build: (root: Map<String, Any>) -> Pair<String, Int>): Int {
    val url = "https://raw.githubusercontent.com/unicode-org/cldr-json/$cldrVersion/cldr-json/$cldrPath"
    val json = URI(url).toURL().openStream().bufferedReader().use { it.readText() }

    @Suppress("UNCHECKED_CAST")
    val root = JsonSlurper().parseText(json) as Map<String, Any>
    val (declarations, count) = build(root)

    outputFile.writeText(
        buildString {
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
            appendLine("// Generated from CLDR $cldrVersion $cldrPath — do not edit by hand.")
            appendLine("// Refresh: ./gradlew :base:formatting:$refreshTask")
            appendLine()
            appendLine("package $packageName")
            appendLine()
            append(declarations)
        },
    )
    return count
}

// Regenerates DefaultCurrencyForCountry.kt from CLDR. Run on demand; output is checked in.
tasks.register("generateDefaultCurrencyMap") {
    val cldrVersion = libs.versions.cldr.get()
    group = "codegen"
    description = "Regenerates DefaultCurrencyForCountry.kt from CLDR $cldrVersion currencyData.json."

    val outputFile = layout.projectDirectory.file("src/webMain/kotlin/formatting/DefaultCurrencyForCountry.kt").asFile
    outputs.file(outputFile)

    doLast {
        val count = generateFromCldr(cldrVersion, "cldr-core/supplemental/currencyData.json", "generateDefaultCurrencyMap", outputFile, "com.splendo.kaluga.base.formatting") { root ->
            @Suppress("UNCHECKED_CAST")
            val regions = ((root["supplemental"] as Map<String, Any>)["currencyData"] as Map<String, Any>)["region"] as Map<String, Any>
            val mapping = sortedMapOf<String, String>()
            for ((regionCode, entries) in regions) {
                // ISO 3166 alpha-2 only — skip numeric region codes (`001`, `419`) that Kaluga never queries.
                if (regionCode.length != 2 || !regionCode.all { it.isLetter() }) continue
                @Suppress("UNCHECKED_CAST")
                val entryList = entries as List<Map<String, Map<String, String>>>
                for (entry in entryList) {
                    val (code, attrs) = entry.entries.first()
                    if (attrs["_to"] != null) continue // historical currency
                    if (attrs["_tender"] == "false") continue // non-tender clearing/accounting unit
                    mapping[regionCode] = code
                    break
                }
            }
            buildString {
                appendLine("internal val defaultCurrencyForCountry: Map<String, String> by lazy {")
                appendLine("    mapOf(")
                for ((regionCode, currency) in mapping) appendLine("        \"$regionCode\" to \"$currency\",")
                appendLine("    )")
                appendLine("}")
            } to mapping.size
        }
        logger.lifecycle("Wrote $count entries to ${outputFile.relativeTo(rootDir)}")
    }
}
