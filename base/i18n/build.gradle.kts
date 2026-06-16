import groovy.json.JsonSlurper
import java.net.URI
import java.time.Year

plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "base.i18n"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                api(project(":base:core", ""))
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
            appendLine("// Refresh: ./gradlew :base:i18n:$refreshTask")
            appendLine()
            appendLine("package $packageName")
            appendLine()
            append(declarations)
        },
    )
    return count
}

// Regenerates AvailableLocales.kt from CLDR. The web has no API to enumerate supported locales, so the
// full CLDR locale set backs KalugaLocale.availableLocales. Run on demand; output is checked in.
tasks.register("generateAvailableLocales") {
    val cldrVersion = libs.versions.cldr.get()
    group = "codegen"
    description = "Regenerates AvailableLocales.kt from CLDR $cldrVersion availableLocales.json."

    val outputFile = layout.projectDirectory.file("src/webMain/kotlin/utils/AvailableLocales.kt").asFile
    outputs.file(outputFile)

    doLast {
        // CLDR omits "default content" locales — those whose data is identical to their parent, e.g.
        // `en-US` (en's default content) — from `availableLocales`, so they must be unioned back in or
        // common locales like en-US/de-DE/es-ES would be missing from KalugaLocale.availableLocales.
        @Suppress("UNCHECKED_CAST")
        val defaultContent = run {
            val dcUrl = "https://raw.githubusercontent.com/unicode-org/cldr-json/$cldrVersion/cldr-json/cldr-core/defaultContent.json"
            val dcJson = URI(dcUrl).toURL().openStream().bufferedReader().use { it.readText() }
            (JsonSlurper().parseText(dcJson) as Map<String, Any>)["defaultContent"] as List<String>
        }
        val count = generateFromCldr(cldrVersion, "cldr-core/availableLocales.json", "generateAvailableLocales", outputFile, "com.splendo.kaluga.base.utils") { root ->
            @Suppress("UNCHECKED_CAST")
            // "full" is the exhaustive CLDR locale set (the "modern" subset is empty as of recent CLDR).
            val full = (root["availableLocales"] as Map<String, Any>)["full"] as List<String>
            val tags = (full + defaultContent).filterNot { it == "root" }.toSortedSet().toList()
            buildString {
                appendLine("internal val availableLocaleTags: List<String> by lazy {")
                appendLine("    listOf(")
                for (tag in tags) appendLine("        \"$tag\",")
                appendLine("    )")
                appendLine("}")
            } to tags.size
        }
        logger.lifecycle("Wrote $count locale tags to ${outputFile.relativeTo(rootDir)}")
    }
}
