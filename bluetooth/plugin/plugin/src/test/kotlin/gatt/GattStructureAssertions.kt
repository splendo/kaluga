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

package com.splendo.kaluga.bluetooth.plugin.gatt

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

// Helpers for asserting on the *structure* of the generated KotlinPoet output (types, properties, annotations and the
// references between them) rather than on its rendered text.

/** All top-level generated types across [this] set of files, keyed by simple name. */
internal fun List<FileSpec>.types(): Map<String, TypeSpec> = flatMap { file -> file.members.filterIsInstance<TypeSpec>() }.associateBy { checkNotNull(it.name) }

/** The single top-level type declared in a generated file. */
internal fun FileSpec.singleType(): TypeSpec = members.filterIsInstance<TypeSpec>().single()

internal fun TypeSpec.annotation(simpleName: String): AnnotationSpec? = annotations.firstOrNull { (it.typeName as? ClassName)?.simpleName == simpleName }

internal fun TypeSpec.property(name: String): PropertySpec? = propertySpecs.firstOrNull { it.name == name }

internal fun PropertySpec.annotation(simpleName: String): AnnotationSpec? = annotations.firstOrNull { (it.typeName as? ClassName)?.simpleName == simpleName }

internal fun TypeSpec.nestedType(name: String): TypeSpec? = typeSpecs.firstOrNull { it.name == name }

/** The simple names of the annotations on this property, e.g. `{Readable, Notifiable}`. */
internal val PropertySpec.annotationNames: Set<String>
    get() = annotations.mapNotNull { (it.typeName as? ClassName)?.simpleName }.toSet()

/** The simple name of a [TypeName], assuming it is a [ClassName] (the type a generated property refers to). */
internal val TypeName.simpleName: String get() = (this as ClassName).simpleName

/** The single rendered argument of an annotation, e.g. `"181A"` or `value = 1`. */
internal val AnnotationSpec.argument: String get() = members.single().toString()
