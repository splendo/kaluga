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

import androidx.activity.result.ActivityResultLauncher
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationBundleValue
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.base.text.KalugaDateFormatter
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

/**
 * Key to be used to indicate an argument in a route that is associated with a NavigationBundleSpecRow
 */
val NavigationBundleSpecRow<*>.argumentKey: String get() = key ?: javaClass.simpleName

/**
 * Creates a route string associated with a [NavigationAction] class given its [NavigationBundleSpec].
 * The route created by this method will create placeholders for all properties of the [spec]
 * @param spec The [NavigationBundleSpec] describing the associated [NavigationAction]
 */
inline fun <SpecRow : NavigationBundleSpecRow<*>, reified Action : NavigationAction<SpecRow>> route(
    spec: NavigationBundleSpec<SpecRow>
): String = route(Action::class, spec)

/**
 * Creates a route string associated with a [NavigationAction] class given its [NavigationBundleSpec].
 * The route created by this method will create placeholders for all properties of the [spec]
 * @param actionClass The [KClass] of the [NavigationAction] to create the route for
 * @param spec The [NavigationBundleSpec] describing the associated [NavigationAction]
 */
fun <SpecRow : NavigationBundleSpecRow<*>, T : NavigationAction<SpecRow>> route(
    actionClass: KClass<T>,
    spec: NavigationBundleSpec<SpecRow>
): String {
    val arguments = spec.rows.mapNotNull { row ->
        val key = row.argumentKey
        when (row.associatedType) {
            is NavigationBundleSpecType.UnitType -> null
            is NavigationBundleSpecType.OptionalType<*> -> RouteArgument(key, "{$key}", false)
            else -> RouteArgument(key, "{$key}", true)
        }
    }
    return route(actionClass, *arguments.toTypedArray())
}

/**
 * Creates a route string for a [NavigationAction].
 * The route created by this method will will in all arguments of the action into the route
 */
fun <SpecRow : NavigationBundleSpecRow<*>> NavigationAction<SpecRow>.route(): String {
    val arguments = bundle?.values?.mapNotNull { (row, value) ->
        value.routeArgument?.let {
            RouteArgument(
                row.argumentKey,
                it,
                value !is NavigationBundleValue.OptionalValue
            )
        }
    } ?: emptyList()
    return route(this::class, *arguments.toTypedArray())
}

private data class RouteArgument(val key: String, val value: String, val isOptional: Boolean)

private fun route(
    navigationActionClass: KClass<out NavigationAction<*>>,
    vararg arguments: RouteArgument
): String {
    val allArguments = arguments.toList()
        .groupBy(keySelector = { it.isOptional }, valueTransform = { it.key to it.value })
    val requiredArguments = allArguments[true] ?: emptyList()
    val optionalArguments = allArguments[false] ?: emptyList()
    val baseRoute = navigationActionClass.simpleName!!
    val routeWithRequiredArguments = requiredArguments.takeIf(List<*>::isNotEmpty)
        ?.joinToString("/", prefix = "$baseRoute/") { it.second } ?: baseRoute
    return optionalArguments.takeIf(List<*>::isNotEmpty)
        ?.joinToString(
            "&",
            prefix = "$routeWithRequiredArguments?"
        ) { "${it.first}={${it.second}}" }
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
            value.map { KalugaDateFormatter.Companion.iso8601Pattern().format(it) }
        )
        is NavigationBundleValue.DateValue -> KalugaDateFormatter.Companion.iso8601Pattern().format(value)
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

/**
 * Route for navigating within a [RouteController].
 */
sealed class Route {

    companion object {
        val Back = Back()
        val PopToRoot = PopToRoot()
    }

    sealed class Result {
        companion object {
            const val KEY = "com.splendo.kaluga.architecture.compose.navigation.Route.Result.KEY"
        }
        object Empty: Result()
        data class Data<SpecRow : NavigationBundleSpecRow<*>, Bundle : NavigationBundle<SpecRow>>(val bundle: Bundle) : Result()
    }

    /**
     * Route that navigates to a new screen using a [NavigationAction]
     */
    sealed class Navigate<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> :
        Route() {
        protected abstract val routeAction: Action

        /**
         * The string value of the route to navigate to
         */
        val route: String get() = routeAction.route()
    }

    /**
     * Route that navigates from the current screen to the screen associated with a [NavigationAction]
     * @param routeAction The [Action] associated with the screen to navigate to
     */
    data class NextRoute<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    /**
     * Route that navigates from a given route to the screen associated with a [NavigationAction]
     * @param routeAction The [Action] associated with the screen to navigate to
     * @param from The route to navigate from
     */
    data class FromRoute<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action,
        val from: String
    ) : Navigate<SpecRow, Action>()

    /**
     * Route that navigates back to the screen associated with a [NavigationAction]
     * @param routeAction The [Action] associated with the screen to navigate back to.
     * @param result The [Result] to be provided to the screen to navigate back to.
     */
    data class PopTo<
        SpecRow : NavigationBundleSpecRow<*>,
        Action : NavigationAction<SpecRow>,
        >(
        override val routeAction: Action,
        val result: Result = Result.Empty
    ) : Navigate<SpecRow, Action>()

    /**
     * Route that navigates back from the screen associated with a [NavigationAction]
     * @param routeAction The [Action] associated with the screen to navigate back from
     */
    data class PopToIncluding<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    /**
     * Route that replaces the current screen to the screen associated with a [NavigationAction]
     * @param routeAction The [Action] associated with the screen to replace the current screen with
     */
    data class Replace<SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>>(
        override val routeAction: Action
    ) : Navigate<SpecRow, Action>()

    /**
     * Navigates back one step in the hierarchy
     * @param result The [Result] to provide to the previous view.
     */
    data class Back(val result: Result = Result.Empty) : Route()

    /**
     * Navigates to the Root of a navigation stack
     * @param result The [Result] to provide to the root view.
     */
    data class PopToRoot(val result: Result = Result.Empty) : Route()

    /**
     * Closes all screens in the navigation stack
     */
    object Close : Route()

    /**
     * Navigates using an [ActivityResultLauncher] and a valid [input].
     * @param activityResultLauncher The launcher to launch with. This should have been created using [androidx.activity.compose.rememberLauncherForActivityResult]
     * @param input The input to be provided to [activityResultLauncher]
     */
    data class Launcher<I>(val activityResultLauncher: ActivityResultLauncher<I>, val input: I) : Route() {

        /**
         * Launches the [activityResultLauncher] with [input]
         */
        fun launch() = activityResultLauncher.launch(input)
    }
}

/**
 * Creates a [Route.NextRoute] from [Action]
 */
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.next
    get() = Route.NextRoute(
        this
    )

/**
 * Creates a [Route.FromRoute] from [Action]
 * @param from The string route to navigate from
 */
fun <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.from(route: String) =
    Route.FromRoute(this, route)

/**
 * Creates a [Route.FromRoute] that navigates to the Root view from [Action]
 */
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.fromRoot
    get() = Route.FromRoute(
        this,
        ROOT_VIEW
    )

/**
 * Creates a [Route.PopTo] from [Action]
 */
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.popTo
    get() = Route.PopTo(
        this
    )

/**
 * Creates a [Route.PopToIncluding] from [Action]
 */
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.popToIncluding
    get() = Route.PopToIncluding(
        this
    )

/**
 * Creates a [Route.Replace] from [Action]
 */
val <SpecRow : NavigationBundleSpecRow<*>, Action : NavigationAction<SpecRow>> Action.replace
    get() = Route.Replace(
        this
    )
