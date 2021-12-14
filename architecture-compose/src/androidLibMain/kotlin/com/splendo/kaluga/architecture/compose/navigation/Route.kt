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

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationBundleValue
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

/** Reserved route name for the back button navigation. */
internal const val ROOT_VIEW = "com.splendo.kaluga.architecture.compose.navigation.root_view"

val NavigationBundleSpecRow<*>.argumentKey: String get() = key ?: javaClass.simpleName

/** @return a route represented by the [NavigationAction]. */
inline fun <SpecRow : NavigationBundleSpecRow<*>, reified Action : NavigationAction<SpecRow>> route(spec: NavigationBundleSpec<SpecRow>): String = route(Action::class, spec)

fun <SpecRow : NavigationBundleSpecRow<*>, T : NavigationAction<SpecRow>> route(
    actionClass: KClass<T>,
    spec: NavigationBundleSpec<SpecRow>): String {
    val arguments = spec.rows.mapNotNull { row ->
        val key = row.argumentKey
        when (row.associatedType) {
            is NavigationBundleSpecType.UnitType -> null
            is NavigationBundleSpecType.OptionalType<*> -> Triple(key, "{$key}", false)
            else -> Triple(key, "{$key}", true)
        }
    }
    return route(actionClass, *arguments.toTypedArray())
}

fun <SpecRow : NavigationBundleSpecRow<*>> NavigationAction<SpecRow>.route(): String {
    val arguments = bundle?.values?.mapNotNull { (row, value) ->
        value.routeArgument?.let {
            Triple(
                row.argumentKey,
                it,
                value !is NavigationBundleValue.OptionalValue
            )
        }
    } ?: emptyList()
    return route(this::class, *arguments.toTypedArray())
}

/** @return a route represented by [navigationActionClass] and [arguments]. */
fun route(
    navigationActionClass: KClass<out NavigationAction<*>>,
    vararg arguments: Triple<String, String, Boolean>
): String {
    val allArguments = arguments.toList()
        .groupBy(keySelector = { it.third }, valueTransform = { it.first to it.second })
    val requiredArguments = allArguments[true] ?: emptyList()
    val optionalArguments = allArguments[false] ?: emptyList()
    val baseRoute = navigationActionClass.simpleName!!
    val routeWithRequiredArguments = requiredArguments.takeIf(List<*>::isNotEmpty)
        ?.joinToString("/", prefix = "${baseRoute}/") { it.second } ?: baseRoute
    return optionalArguments.takeIf(List<*>::isNotEmpty)
        ?.joinToString("&", prefix = "$routeWithRequiredArguments?") { "${it.first}={${it.second}}" }
        ?: routeWithRequiredArguments
}

private val NavigationBundleValue<*>.routeArgument: String?
    get() = when (this) {
        is NavigationBundleValue.UnitValue -> null
        is NavigationBundleValue.BooleanArrayValue -> Json.encodeToString(
            BooleanArraySerializer(),
            value
        )
        is NavigationBundleValue.BooleanValue -> Json.encodeToString(Boolean.serializer(), value)
        is NavigationBundleValue.BundleValue<*> -> null // Unsupported for now
        is NavigationBundleValue.ByteArrayValue -> Json.encodeToString(ByteArraySerializer(), value)
        is NavigationBundleValue.ByteValue -> Json.encodeToString(Byte.serializer(), value)
        is NavigationBundleValue.CharArrayValue -> Json.encodeToString(CharArraySerializer(), value)
        is NavigationBundleValue.CharSequenceValue -> value.toString()
        is NavigationBundleValue.CharValue -> Json.encodeToString(Char.serializer(), value)
        is NavigationBundleValue.DateArrayValue -> Json.encodeToString(
            ListSerializer(String.serializer()),
            value.map { DateFormatter.Companion.iso8601Pattern().format(it) })
        is NavigationBundleValue.DateValue -> DateFormatter.Companion.iso8601Pattern().format(value)
        is NavigationBundleValue.DoubleArrayValue -> Json.encodeToString(
            DoubleArraySerializer(),
            value
        )
        is NavigationBundleValue.DoubleValue -> Json.encodeToString(Double.serializer(), value)
        is NavigationBundleValue.FloatArrayValue -> Json.encodeToString(
            FloatArraySerializer(),
            value
        )
        is NavigationBundleValue.FloatValue -> Json.encodeToString(Float.serializer(), value)
        is NavigationBundleValue.IntegerArrayValue -> Json.encodeToString(
            IntArraySerializer(),
            value
        )
        is NavigationBundleValue.IntegerValue -> Json.encodeToString(Int.serializer(), value)
        is NavigationBundleValue.LongArrayValue -> Json.encodeToString(LongArraySerializer(), value)
        is NavigationBundleValue.LongValue -> Json.encodeToString(Long.serializer(), value)
        is NavigationBundleValue.OptionalValue<*> -> optionalValue?.routeArgument
        is NavigationBundleValue.SerializedValue<*> -> valueString
        is NavigationBundleValue.ShortArrayValue -> Json.encodeToString(
            ShortArraySerializer(),
            value
        )
        is NavigationBundleValue.ShortValue -> Json.encodeToString(Short.serializer(), value)
        is NavigationBundleValue.StringArrayValue -> Json.encodeToString(
            ListSerializer(String.serializer()),
            value
        )
        is NavigationBundleValue.StringValue -> value
    }

sealed class Route {
    sealed class Navigate<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> :
        Route() {
        protected abstract val routeAction: Action
        val route: String get() = routeAction.route()
    }

    data class NextRoute<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    data class FromRoute<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action,
        val from: String
    ) : Navigate<SpecRow, Action>()

    data class PopTo<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    data class PopToIncluding<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    data class Replace<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    object Back : Route()
    object PopToRoot : Route()
    object Close : Route()
}

val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.next get() = Route.NextRoute(this)
fun <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.from(route: String) = Route.FromRoute(this, route)
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.fromRoot get() = Route.FromRoute(this, ROOT_VIEW)
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.popTo get() = Route.PopTo(this)
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.popToIncluding get() = Route.PopToIncluding(this)
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.replace get() = Route.Replace(this)
