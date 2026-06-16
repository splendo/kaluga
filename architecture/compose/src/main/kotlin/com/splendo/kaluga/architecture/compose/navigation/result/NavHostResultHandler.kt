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

package com.splendo.kaluga.architecture.compose.navigation.result

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * A Handler that will allow a [NavHostController] to handle [com.splendo.kaluga.architecture.compose.navigation.Route.Result] of a given type [Result] to be received by a given [ViewModel]
 * @param ViewModel the type of [BaseLifecycleViewModel] that will respond to the [Route.Result]
 * @param Result the type of the result to expect
 * @param viewModelClass the [KClass] of [ViewModel]
 * @param handleResult A [Composable] method for setting up a [NavHostController] to return a [Result] through a callback.
 * @param onResult Callback method called on a [ViewModel] to handle a given [Result]
 */
class NavHostResultHandler<ViewModel : BaseLifecycleViewModel, Result>(
    private val viewModelClass: KClass<ViewModel>,
    private val handleResult: @Composable NavHostController.((Result) -> Unit) -> Unit,
    private val onResult: ViewModel.(Result) -> Unit,
) {

    @Composable
    internal fun HandleResult(viewModel: BaseLifecycleViewModel, navHostController: NavHostController) {
        viewModelClass.safeCast(viewModel)?.let { vm ->
            navHostController.handleResult { vm.onResult(it) }
        }
    }
}

/**
 * Creates a [NavHostResultHandler] of [Row] for this [NavigationBundleSpec]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param Row the type of [NavigationBundleSpecRow] associated with this [NavigationBundleSpec]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, Row : NavigationBundleSpecRow<*>> NavigationBundleSpec<Row>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(NavigationBundle<Row>) -> Unit,
): NavHostResultHandler<ViewModel, NavigationBundle<Row>> {
    val spec = this
    return NavHostResultHandler(ViewModel::class, { HandleResult(spec, retain, it) }, onResult)
}

/**
 * Creates a [NavHostResultHandler] of [T] for this [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of result to expect
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, T> NavigationBundleSpecType<T>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T) -> Unit,
): NavHostResultHandler<ViewModel, T> {
    val spec = this
    return NavHostResultHandler(ViewModel::class, { HandleResult(spec, retain, it) }, onResult)
}

/**
 * Creates a [NavHostResultHandler] of [T] for this [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType] either directly or wrapped in [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of result to expect
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, T : Any> NavigationBundleSpecType<T>.NavHostResultOrNullHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T?) -> Unit,
) = NavHostResultHandler(ViewModel::class, { HandleResultOrNull(this@NavHostResultOrNullHandler, retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [T] for this [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of result to expect
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @return the [NavHostResultHandler]
 */
@JvmName("NavHostNullableResultOrNullHandler")
inline fun <reified ViewModel : BaseLifecycleViewModel, T> NavigationBundleSpecType<T>.NavHostResultOrNullHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T?) -> Unit,
) = NavHostResultHandler(ViewModel::class, { HandleResultOrNull(this@NavHostResultOrNullHandler, retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of Boolean
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> BooleanNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Boolean) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleBooleanResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of Boolean or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> BooleanNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Boolean?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleBooleanOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [BooleanArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [BooleanArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> BooleanArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(BooleanArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleBooleanArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [BooleanArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [BooleanArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> BooleanArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(BooleanArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleBooleanArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Byte]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Byte]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ByteNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Byte) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleByteResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Byte] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Byte] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ByteNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Byte?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleByteOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [ByteArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ByteArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ByteArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(ByteArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleByteArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [ByteArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ByteArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ByteArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(ByteArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleByteArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Char]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Char]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Char) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Char] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Char] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Char?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [CharArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(CharArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [CharArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(CharArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [CharSequence]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharSequence]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharSequenceNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(CharSequence) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharSequenceResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [CharSequence] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharSequence] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> CharSequenceNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(CharSequence?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleCharSequenceOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [KalugaDate]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [KalugaDate]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DateNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(KalugaDate) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDateResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [KalugaDate] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [KalugaDate] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DateNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(KalugaDate?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDateOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of a list of [KalugaDate]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [KalugaDate]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DateArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(List<KalugaDate>) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDateArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of a List of [KalugaDate] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [KalugaDate] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DateArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(List<KalugaDate>?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDateArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Double]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Double]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DoubleNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Double) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDoubleResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Double] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Double] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DoubleNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Double?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDoubleOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [DoubleArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [DoubleArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DoubleArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(DoubleArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDoubleArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [DoubleArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [DoubleArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> DoubleArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(DoubleArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleDoubleArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Float]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Float]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> FloatNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Float) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleFloatResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Float] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Float] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> FloatNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Float?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleFloatOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [FloatArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [FloatArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> FloatArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(FloatArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleFloatArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [FloatArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [FloatArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> FloatArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(FloatArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleFloatArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Int]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Int]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> IntNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Int) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleIntResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Int] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Int] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> IntNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Int?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleIntOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [IntArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [IntArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> IntArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(IntArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleIntArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [IntArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [IntArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> IntArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(IntArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleIntArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Long]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Long]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> LongNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Long) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleLongResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Long] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Long] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> LongNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Long?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleLongOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [LongArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [LongArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> LongArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(LongArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleLongArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [LongArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [LongArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> LongArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(LongArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleLongArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Short]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Short]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ShortNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(Short) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleShortResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [Short] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [Short] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ShortNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(Short?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleShortOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [ShortArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ShortArray]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ShortArrayNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(ShortArray) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleShortArrayResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [ShortArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ShortArray] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> ShortArrayNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(ShortArray?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleShortArrayOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [String]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [String]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> StringNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(String) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleStringResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [String] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [String] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> StringNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(String?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleStringOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of a list of [String]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [String]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> StringListNavHostResultHandler(retain: Boolean = false, noinline onResult: ViewModel.(List<String>) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleStringListResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of a list of [String] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [String] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel> StringListNavHostResultOrNullHandler(retain: Boolean = false, noinline onResult: ViewModel.(List<String>?) -> Unit) =
    NavHostResultHandler(ViewModel::class, { HandleStringListOrNullResult(retain, it) }, onResult)

/**
 * Creates a [NavHostResultHandler] of [T] for this [KSerializer]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with this Serializer
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of serialized property stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T]
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, T> KSerializer<T>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T) -> Unit,
): NavHostResultHandler<ViewModel, T> {
    val serializer = this
    return NavHostResultHandler(ViewModel::class, { HandleResultOfType(serializer, retain, it) }, onResult)
}

/**
 * Creates a [NavHostResultHandler] of [T] for this [KSerializer]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with this Serializer either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of serialized property stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T] or `null`
 * @return the [NavHostResultHandler]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, T : Any> KSerializer<T>.NavHostResultOrNullHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T?) -> Unit,
): NavHostResultHandler<ViewModel, T?> {
    val serializer = this
    return NavHostResultHandler(ViewModel::class, { HandleResultOfTypeOrNull(serializer, retain, it) }, onResult)
}

/**
 * Creates a [NavHostResultHandler] of [T] for this [KSerializer]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with this Serializer either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param ViewModel the type of [BaseLifecycleViewModel] associated with the [NavHostResultHandler]
 * @param T the type of serialized property stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T] or `null`
 * @return the [NavHostResultHandler]
 */
@JvmName("NavHostNullableResultOrNullHandler")
inline fun <reified ViewModel : BaseLifecycleViewModel, T> KSerializer<T>.NavHostResultOrNullHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(T?) -> Unit,
): NavHostResultHandler<ViewModel, T?> {
    val serializer = this
    return NavHostResultHandler(ViewModel::class, { HandleResultOfTypeOrNull(serializer, retain, it) }, onResult)
}
