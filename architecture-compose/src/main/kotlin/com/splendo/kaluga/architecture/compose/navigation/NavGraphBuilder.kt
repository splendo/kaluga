/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import androidx.navigation.navArgument
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
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.text.iso8601Pattern
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer
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

/**
 * Adds a [Composable] for an [Action] to the [NavGraphBuilder]
 * @param spec The [NavigationBundleSpec] to use for mapping route arguments to a [NavigationBundle]
 * @param content Creates the content by providing the [NavigationBundle] generated from the route arguments
 */
inline fun <SpecType : NavigationBundleSpecRow<*>, reified Action : NavigationAction<SpecType>> NavGraphBuilder.composable(
    spec: NavigationBundleSpec<SpecType>,
    noinline content: @Composable (NavigationBundle<SpecType>) -> Unit,
) = composable(Action::class, spec, content)

/**
 * Adds a [Composable] for an [Action] to the [NavGraphBuilder]
 * @param actionClass The [KClass] of the [Action] for which the [Composable] should be created
 * @param spec The [NavigationBundleSpec] to use for mapping route arguments to a [NavigationBundle]
 * @param content Creates the content by providing the [NavigationBundle] generated from the route arguments
 */
fun <SpecType : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecType>> NavGraphBuilder.composable(
    actionClass: KClass<Action>,
    spec: NavigationBundleSpec<SpecType>,
    content: @Composable (NavigationBundle<SpecType>) -> Unit,
) {
    composable(
        route(actionClass, spec),
        arguments = spec.rows.mapNotNull {
            if (!it.associatedType.isRequired) {
                navArgument(it.argumentKey) { nullable = true }
            } else {
                null
            }
        },
    ) { backStackEntry ->
        backStackEntry.arguments?.composable(spec)?.let { content(it) }
            ?: Spacer(modifier = Modifier.fillMaxSize())
    }
}

/**
 * Adds a [Composable] for an [Action] to the [NavGraphBuilder]
 * @param type The [NavigationBundleSpecType] to use for extracting the [Value] from the route arguments
 * @param content Creates the content by providing the [Value] generated from the route arguments
 */
inline fun <Value, reified Action : SingleValueNavigationAction<Value>> NavGraphBuilder.composable(
    type: NavigationBundleSpecType<Value>,
    noinline content: @Composable (Value) -> Unit,
) = composable(Action::class, type, content)

@JvmName("singleValueUnitComposable")
inline fun <reified Action : SingleValueNavigationAction<Unit>> NavGraphBuilder.composable(noinline content: @Composable () -> Unit) =
    composable(Action::class, NavigationBundleSpecType.UnitType) { content() }

@JvmName("singleValueBooleanComposable")
inline fun <reified Action : SingleValueNavigationAction<Boolean>> NavGraphBuilder.composable(noinline content: @Composable (Boolean) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.BooleanType, content)

@JvmName("singleValueBooleanArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<BooleanArray>> NavGraphBuilder.composable(noinline content: @Composable (BooleanArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.BooleanArrayType, content)

@JvmName("singleValueByteComposable")
inline fun <reified Action : SingleValueNavigationAction<Byte>> NavGraphBuilder.composable(noinline content: @Composable (Byte) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.ByteType, content)

@JvmName("singleValueByteArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<ByteArray>> NavGraphBuilder.composable(noinline content: @Composable (ByteArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.ByteArrayType, content)

@JvmName("singleValueCharComposable")
inline fun <reified Action : SingleValueNavigationAction<Char>> NavGraphBuilder.composable(noinline content: @Composable (Char) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.CharType, content)

@JvmName("singleValueCharArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<CharArray>> NavGraphBuilder.composable(noinline content: @Composable (CharArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.CharArrayType, content)

@JvmName("singleValueCharSequenceComposable")
inline fun <reified Action : SingleValueNavigationAction<CharSequence>> NavGraphBuilder.composable(noinline content: @Composable (CharSequence) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.CharSequenceType, content)

@JvmName("singleValueDoubleComposable")
inline fun <reified Action : SingleValueNavigationAction<Double>> NavGraphBuilder.composable(noinline content: @Composable (Double) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.DoubleType, content)

@JvmName("singleValueDoubleArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<DoubleArray>> NavGraphBuilder.composable(noinline content: @Composable (DoubleArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.DoubleArrayType, content)

@JvmName("singleValueFloatComposable")
inline fun <reified Action : SingleValueNavigationAction<Float>> NavGraphBuilder.composable(noinline content: @Composable (Float) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.FloatType, content)

@JvmName("singleValueFloatArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<FloatArray>> NavGraphBuilder.composable(noinline content: @Composable (FloatArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.FloatArrayType, content)

@JvmName("singleValueIntComposable")
inline fun <reified Action : SingleValueNavigationAction<Int>> NavGraphBuilder.composable(noinline content: @Composable (Int) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.IntegerType, content)

@JvmName("singleValueIntArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<IntArray>> NavGraphBuilder.composable(noinline content: @Composable (IntArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.IntegerArrayType, content)

@JvmName("singleValueLongComposable")
inline fun <reified Action : SingleValueNavigationAction<Long>> NavGraphBuilder.composable(noinline content: @Composable (Long) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.LongType, content)

@JvmName("singleValueLongArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<LongArray>> NavGraphBuilder.composable(noinline content: @Composable (LongArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.LongArrayType, content)

@JvmName("singleValueShortComposable")
inline fun <reified Action : SingleValueNavigationAction<Short>> NavGraphBuilder.composable(noinline content: @Composable (Short) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.ShortType, content)

@JvmName("singleValueShortArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<ShortArray>> NavGraphBuilder.composable(noinline content: @Composable (ShortArray) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.ShortArrayType, content)

@JvmName("singleValueStringComposable")
inline fun <reified Action : SingleValueNavigationAction<String>> NavGraphBuilder.composable(noinline content: @Composable (String) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.StringType, content)

@JvmName("singleValueStringArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<List<String>>> NavGraphBuilder.composable(noinline content: @Composable (List<String>) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.StringArrayType, content)

@JvmName("singleValueDateComposable")
inline fun <reified Action : SingleValueNavigationAction<KalugaDate>> NavGraphBuilder.composable(noinline content: @Composable (KalugaDate) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.DateType, content)

@JvmName("singleValueDateArrayComposable")
inline fun <reified Action : SingleValueNavigationAction<List<KalugaDate>>> NavGraphBuilder.composable(noinline content: @Composable (List<KalugaDate>) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.DateArrayType, content)

inline fun <Value, reified Action : SingleValueNavigationAction<Value>> NavGraphBuilder.composable(serializer: KSerializer<Value>, noinline content: @Composable (Value) -> Unit) =
    composable(Action::class, NavigationBundleSpecType.SerializedType(serializer), content)

/**
 * Adds a [Composable] for an [Action] to the [NavGraphBuilder]
 * @param actionClass The [KClass] of the [Action] for which the [Composable] should be created
 * @param type The [NavigationBundleSpecType] to use for extracting the [Value] from the route arguments
 * @param content Creates the content by providing the [Value] generated from the route arguments
 */
fun <Value, Action : SingleValueNavigationAction<Value>> NavGraphBuilder.composable(
    actionClass: KClass<Action>,
    type: NavigationBundleSpecType<Value>,
    content: @Composable (Value) -> Unit,
) = composable(
    actionClass,
    SingleValueNavigationSpec(
        type,
    ),
) { bundle ->
    content(bundle.get(type))
}

private fun <SpecType : NavigationBundleSpecRow<*>> Bundle.composable(spec: NavigationBundleSpec<SpecType>): NavigationBundle<SpecType>? = try {
    spec.toBundle { type ->
        composableValue(getString(type.argumentKey), type.associatedType)
    }
} catch (e: BundleConversionError) {
    null
}

private fun Bundle.composableValue(value: String?, specType: NavigationBundleSpecType<*>): NavigationBundleValue<*> {
    val nonNullableValue = value ?: ""
    return when (specType) {
        is NavigationBundleSpecType.UnitType -> specType.convertValue(Unit)
        is NavigationBundleSpecType.BooleanArrayType -> specType.convertValue(
            Json.decodeFromString(
                BooleanArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.BooleanType -> specType.convertValue(
            Json.decodeFromString(
                Boolean.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.BundleType<*> -> throw BundleConversionError() // Unsupported for now
        is NavigationBundleSpecType.ByteArrayType -> specType.convertValue(
            Json.decodeFromString(
                ByteArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.ByteType -> specType.convertValue(
            Json.decodeFromString(
                Byte.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.CharArrayType -> specType.convertValue(
            Json.decodeFromString(
                CharArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.CharSequenceType -> specType.convertValue(nonNullableValue)
        is NavigationBundleSpecType.CharType -> specType.convertValue(
            Json.decodeFromString(
                Char.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.DateArrayType -> specType.convertValue(
            Json.decodeFromString(
                ListSerializer(String.serializer()),
                nonNullableValue,
            ).map {
                KalugaDateFormatter.Companion.iso8601Pattern().parse(it) ?: throw BundleConversionError()
            },
        )
        is NavigationBundleSpecType.DateType -> specType.convertValue(
            KalugaDateFormatter.Companion.iso8601Pattern().parse(nonNullableValue)
                ?: throw BundleConversionError(),
        )
        is NavigationBundleSpecType.DoubleArrayType -> specType.convertValue(
            Json.decodeFromString(
                DoubleArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.DoubleType -> specType.convertValue(
            Json.decodeFromString(
                Double.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.FloatArrayType -> specType.convertValue(
            Json.decodeFromString(
                FloatArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.FloatType -> specType.convertValue(
            Json.decodeFromString(
                Float.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.IntegerArrayType -> specType.convertValue(
            Json.decodeFromString(
                IntArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.IntegerType -> specType.convertValue(
            Json.decodeFromString(
                Int.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.LongArrayType -> specType.convertValue(
            Json.decodeFromString(
                LongArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.LongType -> specType.convertValue(
            Json.decodeFromString(
                Long.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.OptionalType<*> -> value?.let {
            composableValue(
                it,
                specType.type,
            )
        } ?: specType.convertValue(null)
        is NavigationBundleSpecType.SerializedType<*> -> specType.generateValue(nonNullableValue)
            ?: throw BundleConversionError()
        is NavigationBundleSpecType.ShortArrayType -> specType.convertValue(
            Json.decodeFromString(
                ShortArraySerializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.ShortType -> specType.convertValue(
            Json.decodeFromString(
                Short.serializer(),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.StringArrayType -> specType.convertValue(
            Json.decodeFromString(
                ListSerializer(String.serializer()),
                nonNullableValue,
            ),
        )
        is NavigationBundleSpecType.StringType -> specType.convertValue(nonNullableValue)
    }
}
