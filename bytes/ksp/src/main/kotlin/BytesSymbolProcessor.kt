/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bytes.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Origin
import com.google.devtools.ksp.symbol.Visibility
import com.google.devtools.ksp.validate
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Generate
import com.splendo.kaluga.bytes.layout.FieldLayout
import com.splendo.kaluga.bytes.layout.LayoutCompiler
import com.splendo.kaluga.bytes.layout.dump
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.serialization.SerializationException

/**
 * Generates the implementations of `@BytesLayout` interfaces. It builds the [com.splendo.kaluga.bytes.layout.TypeShape]
 * the generated DATA class will have at runtime and compiles it with [LayoutCompiler] (so an invalid layout is a compile
 * error on the interface). For DATA it emits the data class with its compile-time `LAYOUT_DUMP`; for VIEW it plans
 * the byte positions of that same layout with [ViewPlanner] and emits the view with [ViewGenerator].
 */
class BytesSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val logger = environment.logger
    private val options = Options(environment)
    private val bytesLayoutName = BytesLayout::class.qualifiedName

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotated = resolver.getSymbolsWithAnnotation(BytesLayout::class.java.name).toList()
        // A layout refers to others, so while one waits for a type a later round generates, they all wait: each
        // needs the whole index.
        if (annotated.any { !it.validate() }) return annotated
        val declarations = annotated.filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                if (declaration.classKind != ClassKind.INTERFACE) {
                    logger.error("@BytesLayout can only be applied to an interface", declaration)
                    false
                } else if (declaration.getVisibility() == Visibility.PRIVATE || declaration.getVisibility() == Visibility.LOCAL) {
                    logger.error("@BytesLayout interface ${declaration.qualifiedName?.asString()} must be public or internal so generated code can implement it", declaration)
                    false
                } else {
                    true
                }
            }
            .distinctBy { it.qualifiedName?.asString() }
        if (declarations.isEmpty()) return emptyList()

        val types = TypeExpander(resolver)
        val layouts = withoutNameCollisions(declarations.map { declaration -> Layout(declaration, declaration.bytesLayoutGenerate(), types) })
        val index = try {
            LayoutIndex(layouts, types)
        } catch (e: LayoutDeclarationException) {
            logger.error(e.message.orEmpty(), e.node)
            return emptyList()
        }
        // Every layout this pass sees is indexed, so one can refer to a layout of another source set; only those this
        // pass owns are generated and reported on.
        val owned = layouts.filter { options.includeInGeneration(it.declaration) }.toSet()

        val shapes = ShapeBuilder(index, AnnotationMaterializer())
        // Compiled once per layout, so a VIEW-only layout reports an invalid layout just like a DATA one.
        val compiled = mutableMapOf<Layout, FieldLayout>()
        layouts.forEach { layout ->
            reportingErrors(layout.declaration, report = layout in owned) {
                // A layout's serial name is its qualified name, which sealed dispatch and the dump parity rely on.
                if (layout.declaration.annotations.serialNameOverride() != null) {
                    throw LayoutDeclarationException(
                        "A @BytesLayout interface cannot have a @SerialName; its serial name is its qualified name ${layout.qualifiedName}",
                        layout.declaration,
                    )
                }
                if (Generate.DATA in layout.generate) checkDataReferences(layout, index)
                val root = LayoutCompiler.compile(shapes.layoutShape(layout))
                val violations = LayoutChecks.violations(root)
                if (layout in owned) violations.forEach { logger.error("Invalid @BytesLayout ${layout.qualifiedName}: $it", layout.declaration) }
                if (violations.isEmpty()) compiled[layout] = root
            }
        }

        val dataLayouts = layouts.filter { Generate.DATA in it.generate && it in compiled && it in owned }
        val dumps = dataLayouts.associateWith { compiled.getValue(it).dump() }
        val generator = DataGenerator(index)
        val generated = dataLayouts.filter { it.sealedParent == null }.mapNotNull { root ->
            val tree = generateSequence(listOf(root)) { level -> level.flatMap { it.sealedChildren }.takeIf { it.isNotEmpty() } }.flatten().toList()
            if (!tree.all { it in dumps }) return@mapNotNull null
            var written = false
            reportingErrors(root.declaration) {
                generator.generate(root, dumps).writeTo(environment.codeGenerator, Dependencies(false, *tree.mapNotNull { it.declaration.containingFile }.toTypedArray()))
                written = true
            }
            tree.takeIf { written }
        }.flatten()

        generated.groupBy { it.declaration.packageName.asString() }.forEach { (packageName, packageLayouts) ->
            generator.registry(packageName, packageLayouts, options.registryPrefix)
                .writeTo(environment.codeGenerator, Dependencies(true, *packageLayouts.mapNotNull { it.declaration.containingFile }.toTypedArray()))
        }

        generateViews(layouts.filter { Generate.VIEW in it.generate && it in compiled }, owned, compiled, index, generated.toSet())
        return emptyList()
    }

    // Generated names flatten nesting, so Outer.Inner and OuterInner would both generate OuterInnerData.
    private fun withoutNameCollisions(layouts: List<Layout>): List<Layout> {
        val colliding = layouts.groupBy { it.dataClassName.canonicalName }.values.filter { it.size > 1 }.flatten().toSet()
        colliding.forEach { layout ->
            val others = colliding.filter { it !== layout && it.dataClassName == layout.dataClassName }.joinToString { it.qualifiedName }
            logger.error("@BytesLayout ${layout.qualifiedName} generates ${layout.dataClassName.simpleName}, as $others does; rename one of them", layout.declaration)
        }
        return layouts.filter { it !in colliding }
    }

    private fun generateViews(viewLayouts: List<Layout>, owned: Set<Layout>, compiled: Map<Layout, FieldLayout>, index: LayoutIndex, dataGenerated: Set<Layout>) {
        val planner = ViewPlanner(index, AnnotationMaterializer())
        viewLayouts.forEach { planner.registerRoot(it, compiled.getValue(it)) }
        fun plan(layout: Layout): Result<Unit> = try {
            planner.checkViewReferences(layout)
            planner.planAny(planner.root(layout)!!)
        } catch (e: LayoutDeclarationException) {
            Result.failure(e)
        } catch (e: RuntimeException) {
            Result.failure(e)
        }
        // The pass that owns a layout generated the contexts its own layouts give it; this one cannot add another.
        val others = viewLayouts.filter { it !in owned }
        others.forEach { plan(it) }
        val generatedContexts = others.flatMap { planner.plannedContexts(it) }.toMutableSet()
        val viewed = viewLayouts.filter { it in owned }.filter { layout ->
            val result = plan(layout)
            when (val failure = result.exceptionOrNull()) {
                null -> {
                    val added = others.flatMap { planner.plannedContexts(it) }.filter { it !in generatedContexts }
                    generatedContexts += added
                    if (added.isNotEmpty()) {
                        logger.error(
                            "@BytesLayout ${layout.qualifiedName} nests ${added.map { it.layout.qualifiedName }.distinct().joinToString()} from another source set " +
                                "in a way that changes its encoding, which VIEW does not support yet",
                            layout.declaration,
                        )
                    }
                    added.isEmpty()
                }

                is UnsupportedViewFeature -> {
                    val message = "VIEW not yet supported for ${failure.feature} in @BytesLayout ${layout.qualifiedName}"
                    if (options.skipUnsupportedViews) {
                        logger.warn("$message; no view is generated", failure.node ?: layout.declaration)
                    } else {
                        logger.error(message, failure.node ?: layout.declaration)
                    }
                    false
                }

                is LayoutDeclarationException -> {
                    logger.error("Invalid @BytesLayout ${layout.qualifiedName}: ${failure.message}", failure.node ?: layout.declaration)
                    false
                }

                else -> {
                    logger.error("Invalid @BytesLayout ${layout.qualifiedName}: ${failure::class.simpleName}: ${failure.message}", layout.declaration)
                    false
                }
            }
        }
        val generating = viewed
        // A view refers to the contexts its layout's nested layouts have in it, so any layout change can affect it.
        val sources = viewed.mapNotNull { it.declaration.containingFile }.distinct().toTypedArray()
        val generator = ViewGenerator(planner)
        val written = generating.filter { layout ->
            var done = false
            reportingErrors(layout.declaration) {
                generator.generate(layout).writeTo(environment.codeGenerator, Dependencies(true, *sources))
                done = true
            }
            done
        }
        written.filter { it in dataGenerated }.groupBy { it.declaration.packageName.asString() }.forEach { (packageName, packageLayouts) ->
            generator.registry(packageName, packageLayouts, options.registryPrefix).writeTo(environment.codeGenerator, Dependencies(true, *sources))
        }
    }

    // The DATA class of a layout refers to the DATA classes of the layouts it contains.
    private fun checkDataReferences(layout: Layout, index: LayoutIndex) {
        fun check(type: KSType, node: KSNode) {
            val declaration = type.declaration as? KSClassDeclaration
            val qualifiedName = declaration?.qualifiedName?.asString()
            if (qualifiedName in ShapeBuilder.SPECIAL_NUMERICS) {
                throw LayoutDeclarationException(
                    "${layout.qualifiedName} generates DATA, which cannot hold a $qualifiedName: kotlinx.serialization has no serializer for it. Generate only VIEW, or use its underlying number",
                    node,
                )
            }
            declaration?.let { index[it] }?.let { referenced ->
                if (Generate.DATA !in referenced.generate) {
                    throw LayoutDeclarationException("${layout.qualifiedName} generates DATA, so the @BytesLayout ${referenced.qualifiedName} it uses must generate DATA too", node)
                }
            }
            if (declaration != null && index[declaration] == null && (Modifier.VALUE in declaration.modifiers || Modifier.INLINE in declaration.modifiers)) {
                val wrapped = declaration.primaryConstructor?.parameters?.singleOrNull()?.name
                declaration.getDeclaredProperties().firstOrNull { it.simpleName == wrapped }?.let { check(index.types.expand(it.asMemberOf(type.makeNotNullable())), node) }
            }
            type.arguments.forEach { argument -> argument.type?.resolve()?.let { check(it, node) } }
        }
        if (!layout.isSealed) layout.properties.forEach { check(it.type, it.declaration) }
        (layout.sealedChildren + listOfNotNull(layout.sealedParent)).forEach { related ->
            if (Generate.DATA !in related.generate) {
                throw LayoutDeclarationException("${layout.qualifiedName} and ${related.qualifiedName} share a sealed hierarchy, so both must generate DATA", layout.declaration)
            }
        }
    }

    // One layout that fails, however unexpectedly, is reported on its declaration while the others still generate.
    private fun reportingErrors(declaration: KSClassDeclaration, report: Boolean = true, block: () -> Unit) {
        val error = try {
            block()
            return
        } catch (e: LayoutDeclarationException) {
            e.message to (e.node ?: declaration)
        } catch (e: SerializationException) {
            e.message to declaration
        } catch (e: IllegalArgumentException) {
            e.message to declaration
        } catch (e: RuntimeException) {
            "${e::class.simpleName}: ${e.message}" to declaration
        }
        if (report) logger.error("Invalid @BytesLayout ${declaration.qualifiedName?.asString()}: ${error.first}", error.second)
    }

    /**
     * The `generate` of this declaration's `@BytesLayout`, or the module-wide default when the argument is not given.
     * Whether it is given is read from where the argument comes from, not from its value, so an explicit value equal
     * to the annotation's default still overrides the module-wide one.
     */
    private fun KSClassDeclaration.bytesLayoutGenerate(): Set<Generate> {
        val annotation = annotations.first { it.annotationType.resolve().declaration.qualifiedName?.asString() == bytesLayoutName }
        val argument = annotation.arguments.find { it.name?.asString() == "generate" }?.takeUnless { it.origin == Origin.SYNTHETIC }
            ?: return options.defaultGenerate
        // `getAnnotationsByType` builds a reflective proxy that does not resolve an Array<Generate> reliably.
        return (argument.value as? List<*>).orEmpty()
            .mapNotNull { (it as? KSType)?.declaration?.simpleName?.asString() ?: (it as? KSClassDeclaration)?.simpleName?.asString() }
            .map(Generate::valueOf)
            .toSet()
    }
}

class BytesSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = BytesSymbolProcessor(environment)
}
