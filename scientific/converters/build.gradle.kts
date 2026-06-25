plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "scientific.converter"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                implementation(project(":base:core"))
                api(project(":base:decimal"))
                implementation(project(":scientific:scientific"))
            }
            test {
                implementation(project(":base:test"))
            }
        }
    }
}

// Regenerates the per-quantity "operator web" Mermaid diagrams in README.md straight from the `convertTo*` sources, so the
// docs stay in sync with the converters. Run `./gradlew :scientific:converters:generateConversionDiagrams`.
tasks.register("generateConversionDiagrams") {
    group = "documentation"
    description = "Regenerates the per-quantity converter Mermaid diagrams in README.md from the convertTo* sources."

    val sourceRoot = layout.projectDirectory.dir("src/commonMain/kotlin")
    val readmeFile = layout.projectDirectory.file("README.md")
    inputs.dir(sourceRoot)
    outputs.file(readmeFile)

    doLast {
        val beginMarker = "<!-- BEGIN GENERATED CONVERSION DIAGRAMS -->"
        val endMarker = "<!-- END GENERATED CONVERSION DIAGRAMS -->"
        // First `PhysicalQuantity` after a `.times(`/`.div(` is the other operand; the result quantity is the file name.
        val binaryOperator = Regex("""\.(times|div)\(\s*\w+:\s*ScientificValue<\s*PhysicalQuantity\.(\w+)""")
        // A nullary extension on a `ScientificValue<PhysicalQuantity.X, Unit>` receiver: a reinterpret (`asY`) or reciprocal bridge.
        val unaryBridge = Regex("""ScientificValue<\s*PhysicalQuantity\.(\w+)\s*,\s*\w+\s*>\.(\w+)\(\s*\)""")

        data class Edge(val target: String, val label: String, val dotted: Boolean)

        val diagrams = sortedMapOf<String, MutableSet<Edge>>()
        sourceRoot.asFile.listFiles { file -> file.isDirectory }?.forEach { dir ->
            val source = dir.name.replaceFirstChar { it.uppercase() }
            dir.listFiles { file -> file.isFile && file.name.startsWith("convertTo") && file.name.endsWith(".kt") }?.forEach { file ->
                val target = file.name.removePrefix("convertTo").removeSuffix(".kt")
                val text = file.readText()
                val edges = diagrams.getOrPut(source) { sortedSetOf(compareBy({ it.target }, { it.label })) }
                val binaryMatches = binaryOperator.findAll(text).toList()
                binaryMatches.forEach { match ->
                    val operator = if (match.groupValues[1] == "times") "×" else "÷"
                    edges.add(Edge(target, "$operator ${match.groupValues[2]}", dotted = false))
                }
                if (binaryMatches.isEmpty()) {
                    unaryBridge.findAll(text).forEach { match ->
                        edges.add(Edge(target, match.groupValues[2], dotted = true))
                    }
                }
            }
        }

        val sections = diagrams.filterValues { it.isNotEmpty() }.map { (quantity, edges) ->
            val body = edges.joinToString("\n") { edge ->
                "  $quantity ${if (edge.dotted) "-.->" else "-->"}|\"${edge.label}\"| ${edge.target}"
            }
            "<details>\n<summary><code>$quantity</code></summary>\n\n```mermaid\ngraph LR\n$body\n```\n\n</details>"
        }

        val generated = buildString {
            appendLine(beginMarker)
            appendLine()
            appendLine("## Conversion diagrams")
            appendLine()
            appendLine(
                "For each `PhysicalQuantity`, the diagram shows which quantities it converts to by multiplying (`×`) or " +
                    "dividing (`÷`) by another quantity (shown on the edge), plus reinterpret/reciprocal bridges (dotted). " +
                    "Generated from the `convertTo*` sources by `./gradlew :scientific:converters:generateConversionDiagrams`.",
            )
            appendLine()
            sections.forEach {
                appendLine(it)
                appendLine()
            }
            append(endMarker)
        }

        val readme = readmeFile.asFile
        val current = readme.readText()
        val region = Regex(Regex.escape(beginMarker) + ".*?" + Regex.escape(endMarker), RegexOption.DOT_MATCHES_ALL)
        val updated = if (region.containsMatchIn(current)) {
            region.replace(current) { generated }
        } else {
            current.trimEnd() + "\n\n" + generated + "\n"
        }
        readme.writeText(updated)
        logger.lifecycle("Wrote ${sections.size} conversion diagrams to ${readme.path}")
    }
}
