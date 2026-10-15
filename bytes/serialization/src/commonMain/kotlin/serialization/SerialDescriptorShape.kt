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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.layout.TypeKind
import com.splendo.kaluga.bytes.layout.TypeShape
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.getPolymorphicDescriptors
import kotlinx.serialization.modules.SerializersModule

/**
 * A [TypeShape] backed by a [SerialDescriptor]; open polymorphic subtypes are resolved through [module].
 */
internal class SerialDescriptorShape(private val descriptor: SerialDescriptor, private val module: SerializersModule) : TypeShape {
    override val serialName: String get() = descriptor.serialName
    override val kind: TypeKind get() = descriptor.kind.toTypeKind()
    override val isNullable: Boolean get() = descriptor.isNullable
    override val isInline: Boolean get() = descriptor.isInline
    override val annotations: List<Annotation> get() = descriptor.annotations
    override val elementsCount: Int get() = descriptor.elementsCount

    override fun elementName(index: Int): String = descriptor.getElementName(index)
    override fun elementAnnotations(index: Int): List<Annotation> = descriptor.getElementAnnotations(index)
    override fun element(index: Int): TypeShape = SerialDescriptorShape(descriptor.getElementDescriptor(index), module)
    override fun openSubtypes(): List<TypeShape> = module.getPolymorphicDescriptors(descriptor).map { SerialDescriptorShape(it, module) }
}

private fun SerialKind.toTypeKind(): TypeKind = when (this) {
    PrimitiveKind.BOOLEAN -> TypeKind.BOOLEAN
    PrimitiveKind.BYTE -> TypeKind.BYTE
    PrimitiveKind.SHORT -> TypeKind.SHORT
    PrimitiveKind.INT -> TypeKind.INT
    PrimitiveKind.LONG -> TypeKind.LONG
    PrimitiveKind.FLOAT -> TypeKind.FLOAT
    PrimitiveKind.DOUBLE -> TypeKind.DOUBLE
    PrimitiveKind.CHAR -> TypeKind.CHAR
    PrimitiveKind.STRING -> TypeKind.STRING
    SerialKind.ENUM -> TypeKind.ENUM
    SerialKind.CONTEXTUAL -> TypeKind.CONTEXTUAL
    StructureKind.CLASS, StructureKind.OBJECT -> TypeKind.STRUCTURE
    StructureKind.LIST -> TypeKind.LIST
    StructureKind.MAP -> TypeKind.MAP
    PolymorphicKind.SEALED -> TypeKind.SEALED
    PolymorphicKind.OPEN -> TypeKind.OPEN
}
