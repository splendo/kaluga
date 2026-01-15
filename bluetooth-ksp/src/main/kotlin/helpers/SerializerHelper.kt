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

package com.splendo.kaluga.bluetooth.ksp.helpers

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BOOLEAN_ARRAY
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.CHAR_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.DOUBLE_ARRAY
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FLOAT_ARRAY
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.INT_ARRAY
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.LONG_ARRAY
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MAP_ENTRY
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.SET
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.SHORT_ARRAY
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.U_BYTE
import com.squareup.kotlinpoet.U_BYTE_ARRAY
import com.squareup.kotlinpoet.U_INT
import com.squareup.kotlinpoet.U_INT_ARRAY
import com.squareup.kotlinpoet.U_LONG
import com.squareup.kotlinpoet.U_LONG_ARRAY
import com.squareup.kotlinpoet.U_SHORT
import com.squareup.kotlinpoet.U_SHORT_ARRAY
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.joinToCode

fun TypeName.serializer(logger: KSPLogger): CodeBlock = if (isNullable) {
    CodeBlock.of("%L.%M", copy(nullable = false).serializer(logger), References.KotlinX.Serialization.nullable)
} else {
    when (this) {
        BYTE -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        BYTE_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.byteArraySerializer)
        CHAR -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        CHAR_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.charArraySerializer)
        U_BYTE -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        U_BYTE_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.uByteArraySerializer)
        SHORT -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        SHORT_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.shortArraySerializer)
        U_SHORT -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        U_SHORT_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.uShortArraySerializer)
        INT -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        INT_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.intArraySerializer)
        U_INT -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        U_INT_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.uIntArraySerializer)
        LONG -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        LONG_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.longArraySerializer)
        U_LONG -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        U_LONG_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.uLongArraySerializer)
        FLOAT -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        FLOAT_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.floatArraySerializer)
        DOUBLE -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        DOUBLE_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.doubleArraySerializer)
        BOOLEAN -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        BOOLEAN_ARRAY -> CodeBlock.of("%M()", References.KotlinX.Serialization.booleanArraySerializer)
        STRING -> CodeBlock.of("%T.%M()", this, References.KotlinX.Serialization.serializer)
        is ClassName -> CodeBlock.of("$FORMAT.serializer<%T>()", this)
        is ParameterizedTypeName -> {
            val parameters = typeArguments.joinToCode { it.serializer(logger) }
            when (rawType) {
                LIST -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.listSerializer, parameters)
                References.Kotlin.pair -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.pairSerializer, parameters)
                MAP_ENTRY -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.mapEntrySerializer, parameters)
                References.Kotlin.triple -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.tripleSerializer, parameters)
                ARRAY -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.arraySerializer, parameters)
                SET -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.setSerializer, parameters)
                MAP -> CodeBlock.of("%M(%L)", References.KotlinX.Serialization.mapSerializer, parameters)
                else -> CodeBlock.of("%T.%M(%L)", rawType, References.KotlinX.Serialization.serializer, parameters)
            }
        }
        else -> {
            logger.error("Invalid type: $this")
            throw IllegalArgumentException("Invalid type: $this")
        }
    }
}