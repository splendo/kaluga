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

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.navigation.*
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer

/**
 * Handles a [Route.Result.Data] matching a given [NavigationBundleSpec]
 * @param Row the type of [NavigationBundleSpecRow] to.
 * @param spec The [NavigationBundleSpec] used to create the [Route.Result]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @throws [BundleConversionError] if the [Bundle] does not contain the correct keys or values associated with the [NavigationBundleSpec]
 */
@Composable
fun <Row : NavigationBundleSpecRow<*>> NavHostController.HandleResult(spec: NavigationBundleSpec<Row>, retain: Boolean = false, onResult: NavigationBundle<Row>.() -> Unit) =
    HandleResult(retain) { toNavigationBundle(spec).onResult() }

/**
 * Handles a [Route.Result.Data] matching a given [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param T the type of the result.
 * @param type The [NavigationBundleSpecType] stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec]
 */
@Composable
fun <T> NavHostController.HandleResult(type: NavigationBundleSpecType<T>, retain: Boolean = false, onResult: T.() -> Unit) =
    HandleResult(retain) { toTypedProperty(type).onResult() }

/**
 * Handles a [Route.Result.Data] matching a given [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType] either directly or wrapped in [NavigationBundleSpecType.OptionalType]
 * @param T the type of the result.
 * @param type The [NavigationBundleSpecType] stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
fun <T : Any> NavHostController.HandleResultOrNull(type: NavigationBundleSpecType<T>, retain: Boolean = false, onResult: T?.() -> Unit) =
    HandleResult(retain) { toTypedPropertyOrNull(type).onResult() }

/**
 * Handles a [Route.Result.Data] matching a given [NavigationBundleSpecType]
 * Requires that [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param T the type of the result.
 * @param type The [NavigationBundleSpecType] stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
@JvmName("HandleNullableResultOrNull")
fun <T> NavHostController.HandleResultOrNull(type: NavigationBundleSpecType<T>, retain: Boolean = false, onResult: T?.() -> Unit) =
    HandleResult(retain) { toTypedPropertyOrNull(type).onResult() }

/**
 * Handles a [Route.Result.Data] matching a Boolean
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType].
 */
@Composable
fun NavHostController.HandleBooleanResult(retain: Boolean = false, onResult: Boolean.() -> Unit) = HandleResult(retain) { asBoolean().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Boolean or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean
 */
@Composable
fun NavHostController.HandleBooleanOrNullResult(retain: Boolean = false, onResult: Boolean?.() -> Unit) = HandleResult(retain) { asBooleanOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [BooleanArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType].
 */
@Composable
fun NavHostController.HandleBooleanArrayResult(retain: Boolean = false, onResult: BooleanArray.() -> Unit) = HandleResult(retain) { asBooleanArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [BooleanArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.BooleanArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Boolean
 */
@Composable
fun NavHostController.HandleBooleanArrayOrNullResult(retain: Boolean = false, onResult: BooleanArray?.() -> Unit) = HandleResult(retain) { asBooleanArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Byte
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Byte
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType].
 */
@Composable
fun NavHostController.HandleByteResult(retain: Boolean = false, onResult: Byte.() -> Unit) = HandleResult(retain) { asByte().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Byte or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Byte
 */
@Composable
fun NavHostController.HandleByteOrNullResult(retain: Boolean = false, onResult: Byte?.() -> Unit) = HandleResult(retain) { asByteOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [ByteArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ByteArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType].
 */
@Composable
fun NavHostController.HandleByteArrayResult(retain: Boolean = false, onResult: ByteArray.() -> Unit) = HandleResult(retain) { asByteArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [ByteArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ByteArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ByteArray] or`null`
 */
@Composable
fun NavHostController.HandleByteArrayOrNullResult(retain: Boolean = false, onResult: ByteArray?.() -> Unit) = HandleResult(retain) { asByteArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Char
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Char
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType].
 */
@Composable
fun NavHostController.HandleCharResult(retain: Boolean = false, onResult: Char.() -> Unit) = HandleResult(retain) { asChar().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Char or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Char or`null`
 */
@Composable
fun NavHostController.HandleCharOrNullResult(retain: Boolean = false, onResult: Char?.() -> Unit) = HandleResult(retain) { asCharOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [CharArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType].
 */
@Composable
fun NavHostController.HandleCharArrayResult(retain: Boolean = false, onResult: CharArray.() -> Unit) = HandleResult(retain) { asCharArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [CharArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharArray] or`null`
 */
@Composable
fun NavHostController.HandleCharArrayOrNullResult(retain: Boolean = false, onResult: CharArray?.() -> Unit) = HandleResult(retain) { asCharArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [CharSequence]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharSequence]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType].
 */
@Composable
fun NavHostController.HandleCharSequenceResult(retain: Boolean = false, onResult: CharSequence.() -> Unit) = HandleResult(retain) { asCharSequence().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [CharSequence] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.CharSequenceType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [CharSequence] or`null`
 */
@Composable
fun NavHostController.HandleCharSequenceOrNullResult(retain: Boolean = false, onResult: CharSequence?.() -> Unit) = HandleResult(retain) { asCharSequenceOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [KalugaDate]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [KalugaDate]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType].
 */
@Composable
fun NavHostController.HandleDateResult(retain: Boolean = false, onResult: KalugaDate.() -> Unit) = HandleResult(retain) { asDate().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [KalugaDate] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [KalugaDate] or`null`
 */
@Composable
fun NavHostController.HandleDateOrNullResult(retain: Boolean = false, onResult: KalugaDate?.() -> Unit) = HandleResult(retain) { asDateOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a list of [KalugaDate]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [KalugaDate]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType].
 */
@Composable
fun NavHostController.HandleDateArrayResult(retain: Boolean = false, onResult: List<KalugaDate>.() -> Unit) = HandleResult(retain) { asDateArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a list of [KalugaDate] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DateArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [KalugaDate] or`null`
 */
@Composable
fun NavHostController.HandleDateArrayOrNullResult(retain: Boolean = false, onResult: List<KalugaDate>?.() -> Unit) = HandleResult(retain) { asDateArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Double
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Double
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType].
 */
@Composable
fun NavHostController.HandleDoubleResult(retain: Boolean = false, onResult: Double.() -> Unit) = HandleResult(retain) { asDouble().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Double or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Double or`null`
 */
@Composable
fun NavHostController.HandleDoubleOrNullResult(retain: Boolean = false, onResult: Double?.() -> Unit) = HandleResult(retain) { asDoubleOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [DoubleArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [DoubleArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType].
 */
@Composable
fun NavHostController.HandleDoubleArrayResult(retain: Boolean = false, onResult: DoubleArray.() -> Unit) = HandleResult(retain) { asDoubleArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [DoubleArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.DoubleArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [DoubleArray] or`null`
 */
@Composable
fun NavHostController.HandleDoubleArrayOrNullResult(retain: Boolean = false, onResult: DoubleArray?.() -> Unit) = HandleResult(retain) { asDoubleArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Float
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Float
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType].
 */
@Composable
fun NavHostController.HandleFloatResult(retain: Boolean = false, onResult: Float.() -> Unit) = HandleResult(retain) { asFloat().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Float or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Float or`null`
 */
@Composable
fun NavHostController.HandleFloatOrNullResult(retain: Boolean = false, onResult: Float?.() -> Unit) = HandleResult(retain) { asFloatOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [FloatArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [FloatArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType].
 */
@Composable
fun NavHostController.HandleFloatArrayResult(retain: Boolean = false, onResult: FloatArray.() -> Unit) = HandleResult(retain) { asFloatArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [FloatArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.FloatArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [FloatArray] or`null`
 */
@Composable
fun NavHostController.HandleFloatArrayOrNullResult(retain: Boolean = false, onResult: FloatArray?.() -> Unit) = HandleResult(retain) { asFloatArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching an Int
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Int
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType].
 */
@Composable
fun NavHostController.HandleIntResult(retain: Boolean = false, onResult: Int.() -> Unit) = HandleResult(retain) { asInt().onResult() }

/**
 * Handles a [Route.Result.Data] matching an Int or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Int or`null`
 */
@Composable
fun NavHostController.HandleIntOrNullResult(retain: Boolean = false, onResult: Int?.() -> Unit) = HandleResult(retain) { asIntOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching an [IntArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [IntArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType].
 */
@Composable
fun NavHostController.HandleIntArrayResult(retain: Boolean = false, onResult: IntArray.() -> Unit) = HandleResult(retain) { asIntArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching an [IntArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.IntegerArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [IntArray] or`null`
 */
@Composable
fun NavHostController.HandleIntArrayOrNullResult(retain: Boolean = false, onResult: IntArray?.() -> Unit) = HandleResult(retain) { asIntArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Long
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Long
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType].
 */
@Composable
fun NavHostController.HandleLongResult(retain: Boolean = false, onResult: Long.() -> Unit) = HandleResult(retain) { asLong().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Long or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Long or`null`
 */
@Composable
fun NavHostController.HandleLongOrNullResult(retain: Boolean = false, onResult: Long?.() -> Unit) = HandleResult(retain) { asLongOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [LongArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [LongArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType].
 */
@Composable
fun NavHostController.HandleLongArrayResult(retain: Boolean = false, onResult: LongArray.() -> Unit) = HandleResult(retain) { asLongArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [LongArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.LongArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [LongArray] or`null`
 */
@Composable
fun NavHostController.HandleLongArrayOrNullResult(retain: Boolean = false, onResult: LongArray?.() -> Unit) = HandleResult(retain) { asLongArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Short
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Short
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType].
 */
@Composable
fun NavHostController.HandleShortResult(retain: Boolean = false, onResult: Short.() -> Unit) = HandleResult(retain) { asShort().onResult() }

/**
 * Handles a [Route.Result.Data] matching a Short or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received Short or`null`
 */
@Composable
fun NavHostController.HandleShortOrNullResult(retain: Boolean = false, onResult: Short?.() -> Unit) = HandleResult(retain) { asShortOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [ShortArray]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ShortArray]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType].
 */
@Composable
fun NavHostController.HandleShortArrayResult(retain: Boolean = false, onResult: ShortArray.() -> Unit) = HandleResult(retain) { asShortArray().onResult() }

/**
 * Handles a [Route.Result.Data] matching a [ShortArray] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.ShortArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [ShortArray] or`null`
 */
@Composable
fun NavHostController.HandleShortArrayOrNullResult(retain: Boolean = false, onResult: ShortArray?.() -> Unit) = HandleResult(retain) { asShortArrayOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a String
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received String
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType].
 */
@Composable
fun NavHostController.HandleStringResult(retain: Boolean = false, onResult: String.() -> Unit) = HandleResult(retain) { asString().onResult() }

/**
 * Handles a [Route.Result.Data] matching a String or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received String or `null`
 */
@Composable
fun NavHostController.HandleStringOrNullResult(retain: Boolean = false, onResult: String?.() -> Unit) = HandleResult(retain) { asStringOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a list of [String]
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [String]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType].
 */
@Composable
fun NavHostController.HandleStringListResult(retain: Boolean = false, onResult: List<String>.() -> Unit) = HandleResult(retain) { asStringList().onResult() }

/**
 * Handles a [Route.Result.Data] matching a list of [String] or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.StringArrayType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received list of [String] or `null`
 */
@Composable
fun NavHostController.HandleStringListOrNullResult(retain: Boolean = false, onResult: List<String>?.() -> Unit) = HandleResult(retain) { asStringListOrNull().onResult() }

/**
 * Handles a [Route.Result.Data] matching a given type [T].
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with [serializer]
 * @param T the type of serialized property stored in the result
 * @param serializer The [KSerializer] to deserialize [T] from the result.
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T]
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] with [serializer].
 */
@Composable
fun <T> NavHostController.HandleResultOfType(serializer: KSerializer<T>, retain: Boolean = false, onResult: T.() -> Unit) = HandleResult(retain) { asTypeOf(serializer).onResult() }

/**
 * Handles a [Route.Result.Data] matching a given type [T].
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param T the type of serialized property stored in the result
 * @param serializer The [KSerializer] to deserialize [T] from the result.
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T] or `null`
 */
@Composable
fun <T : Any> NavHostController.HandleResultOfTypeOrNull(serializer: KSerializer<T>, retain: Boolean = false, onResult: T?.() -> Unit) =
    HandleResult(retain) { asTypeOfOrNull(serializer).onResult() }

/**
 * Handles a [Route.Result.Data] matching a given type [T].
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.SerializedType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param T the type of serialized property stored in the result
 * @param serializer The [KSerializer] to deserialize [T] from the result.
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received [T] or `null`
 */
@Composable
@JvmName("HandleResultOfNullableTypeOrNull")
fun <T> NavHostController.HandleResultOfTypeOrNull(serializer: KSerializer<T>, retain: Boolean = false, onResult: T?.() -> Unit) =
    HandleResult(retain) { asTypeOfOrNull(serializer).onResult() }

/**
 * Handles a [Route.Result.Data] matching a Unit
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.UnitType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the result
 * @throws [BundleConversionError] if the [Bundle] is not associated with a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.UnitType].
 */
@Composable
fun NavHostController.HandleUnitResult(retain: Boolean = false, onResult: () -> Unit) = HandleResult(retain) {
    asUnit()
    onResult()
}

/**
 * Handles a [Route.Result.Data] matching a Unit or `null`
 * Requires that the [Route.Result.Data.bundle] is described by a [SingleValueNavigationSpec] matching [NavigationBundleSpecType.UnitType] either directly or wrapped by [NavigationBundleSpecType.OptionalType]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the result
 */
@Composable
fun NavHostController.HandleUnitOrNullResult(retain: Boolean = false, onResult: () -> Unit) = HandleResult(retain) {
    asUnitNull()
    onResult()
}

@Composable
internal fun NavHostController.HandleResult(retain: Boolean = false, onResult: Bundle.() -> Unit) {
    // Check if we have a result in the current BackStack.
    val result = currentBackStackEntry?.savedStateHandle?.getStateFlow<Bundle?>(Route.Result.KEY, null)?.collectAsState()
    result?.value?.let {
        try {
            onResult(it)
            // If retain is set we keep the result, otherwise clean up.
            if (!retain) {
                currentBackStackEntry?.savedStateHandle?.remove<Bundle>(Route.Result.KEY)
            }
        } catch (e: BundleConversionError) {}
    }
}
