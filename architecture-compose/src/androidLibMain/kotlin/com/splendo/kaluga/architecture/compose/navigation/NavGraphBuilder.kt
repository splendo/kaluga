/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.compose.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.splendo.kaluga.architecture.navigation.BundleConversionError
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationBundleValue
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.base.text.DateFormatter
import com.splendo.kaluga.base.text.iso8601Pattern
import kotlinx.serialization.builtins.BooleanArraySerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.CharArraySerializer
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.builtins.FloatArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.builtins.ShortArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

inline fun <SpecType : NavigationBundleSpecRow<*>, reified Action : NavigationAction<SpecType>> NavGraphBuilder.composable(
    spec: NavigationBundleSpec<SpecType>,
    noinline content: @Composable (NavigationBundle<SpecType>) -> Unit
) = composable(Action::class, spec, content)

fun <SpecType : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecType>> NavGraphBuilder.composable(
    actionClass: KClass<Action>,
    spec: NavigationBundleSpec<SpecType>,
    content: @Composable (NavigationBundle<SpecType>) -> Unit
) {
    composable(route(actionClass, spec), arguments = spec.rows.mapNotNull {
        if (it.associatedType is NavigationBundleSpecType.OptionalType<*>) {
            navArgument(it.argumentKey) { nullable = true }
        } else {
            null
        }
    }) { backStackEntry ->
        backStackEntry.arguments?.composable(spec)?.let { content(it) } ?: Spacer(modifier = Modifier.fillMaxSize())
    }
}

inline fun <Value, reified Action : SingleValueNavigationAction<Value>> NavGraphBuilder.composable(
    type: NavigationBundleSpecType<Value>,
    noinline content: @Composable (Value) -> Unit
) = composable(Action::class, type, content)

fun <Value, Action : SingleValueNavigationAction<Value>> NavGraphBuilder.composable(
    actionClass: KClass<Action>,
    type: NavigationBundleSpecType<Value>,
    content: @Composable (Value) -> Unit
) = composable(
    actionClass,
    SingleValueNavigationSpec(
        type
    )) { bundle ->
    content(bundle.get(type))
}

fun <SpecType : NavigationBundleSpecRow<*>> Bundle.composable(spec: NavigationBundleSpec<SpecType>): NavigationBundle<SpecType>? {
    return try {
        spec.toBundle { type ->
            composableValue(getString(type.argumentKey), type.associatedType)
        }
    } catch (e : BundleConversionError) {
        null
    }
}

private fun Bundle.composableValue(value: String?, specType : NavigationBundleSpecType<*>): NavigationBundleValue<*> {
    val nonNullableValue = value ?: ""
    return when (specType) {
        is NavigationBundleSpecType.UnitType -> specType.convertValue(Unit)
        is NavigationBundleSpecType.BooleanArrayType -> specType.convertValue(Json.decodeFromString(BooleanArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.BooleanType -> specType.convertValue(Json.decodeFromString(Boolean.serializer(), nonNullableValue))
        is NavigationBundleSpecType.BundleType<*> -> throw BundleConversionError() // Unsupported for now
        is NavigationBundleSpecType.ByteArrayType -> specType.convertValue(Json.decodeFromString(ByteArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.ByteType -> specType.convertValue(Json.decodeFromString(Byte.serializer(), nonNullableValue))
        is NavigationBundleSpecType.CharArrayType -> specType.convertValue(Json.decodeFromString(CharArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.CharSequenceType -> specType.convertValue(nonNullableValue)
        is NavigationBundleSpecType.CharType -> specType.convertValue(Json.decodeFromString(Char.serializer(), nonNullableValue))
        is NavigationBundleSpecType.DateArrayType -> specType.convertValue(
            Json.decodeFromString(
                ListSerializer(String.serializer()),
                nonNullableValue
            ).map { DateFormatter.Companion.iso8601Pattern().parse(it) ?: throw BundleConversionError() }
        )
        is NavigationBundleSpecType.DateType -> specType.convertValue(DateFormatter.Companion.iso8601Pattern().parse(nonNullableValue) ?: throw BundleConversionError())
        is NavigationBundleSpecType.DoubleArrayType -> specType.convertValue(Json.decodeFromString(DoubleArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.DoubleType -> specType.convertValue(Json.decodeFromString(Double.serializer(), nonNullableValue))
        is NavigationBundleSpecType.FloatArrayType -> specType.convertValue(Json.decodeFromString(FloatArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.FloatType -> specType.convertValue(Json.decodeFromString(Float.serializer(), nonNullableValue))
        is NavigationBundleSpecType.IntegerArrayType -> specType.convertValue(Json.decodeFromString(IntArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.IntegerType -> specType.convertValue(Json.decodeFromString(Int.serializer(), nonNullableValue))
        is NavigationBundleSpecType.LongArrayType -> specType.convertValue(Json.decodeFromString(LongArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.LongType -> specType.convertValue(Json.decodeFromString(Long.serializer(), nonNullableValue))
        is NavigationBundleSpecType.OptionalType<*> -> value?.let { composableValue(it, specType.type) } ?: specType.convertValue(null)
        is NavigationBundleSpecType.SerializedType<*> -> specType.generateValue(nonNullableValue) ?: throw BundleConversionError()
        is NavigationBundleSpecType.ShortArrayType -> specType.convertValue(Json.decodeFromString(ShortArraySerializer(), nonNullableValue))
        is NavigationBundleSpecType.ShortType -> specType.convertValue(Json.decodeFromString(Short.serializer(), nonNullableValue))
        is NavigationBundleSpecType.StringArrayType -> specType.convertValue(Json.decodeFromString(ListSerializer(String.serializer()), nonNullableValue))
        is NavigationBundleSpecType.StringType -> specType.convertValue(nonNullableValue)
    }
}