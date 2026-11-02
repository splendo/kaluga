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

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Turns [KSAnnotation]s into real [Annotation] instances of the `@SerialInfo` annotation classes on the processor classpath, so
 * [com.splendo.kaluga.bytes.layout.LayoutCompiler] reads the same annotation objects at compile time as it does
 * from a `SerialDescriptor` at runtime.
 *
 * KSP's own `getAnnotationsByType` proxies do not resolve every default (an `Array<Enum>` default fails), and they
 * cannot unwrap the container KSP reports for a repeated annotation read from a compiled dependency. This builds its
 * own proxies: explicit arguments are converted to the JVM return type of the annotation method, and anything absent
 * falls back to the method's compiled default value.
 */
internal class AnnotationMaterializer(private val classLoader: ClassLoader = AnnotationMaterializer::class.java.classLoader) {

    /** The `@SerialInfo` annotations among [annotations], in declaration order, with repeatable containers unwrapped. */
    fun serialInfo(annotations: Sequence<KSAnnotation>): List<Annotation> = annotations.flatMap { materialize(it) }.toList()

    private fun materialize(annotation: KSAnnotation): List<Annotation> {
        val declaration = annotation.annotationType.resolve().declaration as? KSClassDeclaration ?: return emptyList()
        // `@SerialInfo` has binary retention, so it is only visible on the symbols, not on the loaded classes.
        if (declaration.isSerialInfo()) {
            val annotationClass = declaration.jvmClass()?.takeIf { it.isAnnotation } ?: return emptyList()
            return listOf(instantiate(annotationClass, annotation))
        }
        // A repeated annotation read from a compiled dependency arrives as its synthetic `Container`.
        val repeated = declaration.parentDeclaration as? KSClassDeclaration
        if (repeated != null && declaration.simpleName.asString() == "Container" && repeated.isSerialInfo()) {
            return (annotation.argument("value") as? List<*>).orEmpty().filterIsInstance<KSAnnotation>().flatMap { materialize(it) }
        }
        return emptyList()
    }

    private fun KSClassDeclaration.jvmClass(): Class<*>? {
        val packageName = packageName.asString()
        val qualified = qualifiedName?.asString() ?: return null
        val relative = qualified.removePrefix("$packageName.").replace('.', '$')
        val binaryName = if (packageName.isEmpty()) relative else "$packageName.$relative"
        return try {
            Class.forName(binaryName, false, classLoader)
        } catch (_: ClassNotFoundException) {
            null
        } catch (_: LinkageError) {
            null
        }
    }

    private fun instantiate(annotationClass: Class<*>, annotation: KSAnnotation): Annotation {
        val values = annotationClass.declaredMethods
            .filter { it.parameterCount == 0 }
            .associate { method -> method.name to (annotation.argument(method.name)?.let { convert(it, method.returnType) } ?: method.defaultValue) }
        return Proxy.newProxyInstance(annotationClass.classLoader, arrayOf(annotationClass), AnnotationHandler(annotationClass, values)) as Annotation
    }

    private fun convert(value: Any, type: Class<*>): Any? = when {
        type == java.lang.Boolean.TYPE -> value as? Boolean

        type == java.lang.Byte.TYPE -> value.number()?.toByte()

        type == java.lang.Short.TYPE -> value.number()?.toShort()

        type == Integer.TYPE -> value.number()?.toInt()

        type == java.lang.Long.TYPE -> value.number()?.toLong()

        type == java.lang.Float.TYPE -> value.number()?.toFloat()

        type == java.lang.Double.TYPE -> value.number()?.toDouble()

        type == String::class.java -> value as? String

        type.isEnum -> value.enumEntryName()?.let { name -> type.enumConstants.firstOrNull { (it as Enum<*>).name == name } }

        type.isAnnotation -> (value as? KSAnnotation)?.let { instantiate(type, it) }

        type.isArray -> {
            val items = when (value) {
                is List<*> -> value
                is Array<*> -> value.toList()
                else -> listOf(value)
            }
            val componentType = type.componentType
            val array = java.lang.reflect.Array.newInstance(componentType, items.size)
            items.forEachIndexed { index, item -> java.lang.reflect.Array.set(array, index, item?.let { convert(it, componentType) }) }
            array
        }

        else -> null
    }

    // Unsigned arguments compile to their signed JVM counterparts, but KSP may report them boxed as unsigned values.
    private fun Any.number(): Number? = when (this) {
        is Number -> this
        is UByte -> toLong()
        is UShort -> toLong()
        is UInt -> toLong()
        is ULong -> toLong()
        is Char -> code
        else -> null
    }

    private fun Any.enumEntryName(): String? = when (this) {
        is KSType -> declaration.simpleName.asString()
        is KSClassDeclaration -> if (classKind == ClassKind.ENUM_ENTRY) simpleName.asString() else null
        else -> null
    }

    private class AnnotationHandler(private val annotationClass: Class<*>, private val values: Map<String, Any?>) : InvocationHandler {
        override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? = when (method.name) {
            "annotationType" -> annotationClass

            "hashCode" -> values.entries.sumOf { (name, value) -> (127 * name.hashCode()) xor valueHashCode(value) }

            "equals" -> args?.firstOrNull()?.let { other ->
                annotationClass.isInstance(other) &&
                    annotationClass.declaredMethods.filter { it.parameterCount == 0 }.all { valueEquals(values[it.name], it.invoke(other)) }
            }
                ?: false

            "toString" -> "@${annotationClass.name}(${values.entries.joinToString { (name, value) -> "$name=${valueString(value)}" }})"

            else -> values[method.name]
        }

        private fun valueHashCode(value: Any?): Int = when (value) {
            null -> 0
            is ByteArray -> value.contentHashCode()
            is IntArray -> value.contentHashCode()
            is Array<*> -> value.contentHashCode()
            else -> value.hashCode()
        }

        private fun valueEquals(a: Any?, b: Any?): Boolean = when {
            a is ByteArray && b is ByteArray -> a.contentEquals(b)
            a is IntArray && b is IntArray -> a.contentEquals(b)
            a is Array<*> && b is Array<*> -> a.contentEquals(b)
            else -> a == b
        }

        private fun valueString(value: Any?): String = when (value) {
            is ByteArray -> value.contentToString()
            is IntArray -> value.contentToString()
            is Array<*> -> value.contentToString()
            else -> value.toString()
        }
    }
}

internal fun KSAnnotation.argument(name: String): Any? = arguments.firstOrNull { it.name?.asString() == name }?.value
