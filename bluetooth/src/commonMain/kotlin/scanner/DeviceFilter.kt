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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.scanner.DeviceFilter.ServiceMatcher.Constrained
import com.splendo.kaluga.bluetooth.scanner.DeviceFilter.ServiceMatcher.Exactly
import kotlin.jvm.JvmInline
import kotlin.time.Duration

/**
 * Filter used for filtering a [com.splendo.kaluga.bluetooth.device.ConnectableDevice] based on [DeviceInfo]
 * @property nameMatcher the [NameMatcher] used to filter [DeviceInfo.name]
 * @property serviceMatcher the [ServiceMatcher] used to filter [BaseAdvertisementData]
 * @property updateMatcher the [UpdateMatcher] used to filter [DeviceInfo.updatedAt]
 * @property manufacturer the set of [ManufacturerIdentifier] used to filter [BaseAdvertisementData.manufacturerId]
 * @property exclude the set of [Identifier] to exclude
 */
data class DeviceFilter(
    val nameMatcher: NameMatcher = NameMatcher.All,
    val serviceMatcher: ServiceMatcher = ServiceMatcher.Any,
    val updateMatcher: UpdateMatcher = UpdateMatcher.All,
    val manufacturer: Set<ManufacturerIdentifier> = emptySet(),
    val exclude: Set<Identifier> = emptySet(),
) {

    /**
     * Checks whether a [DeviceInfo.name] matches a filter
     */
    sealed interface NameMatcher {

        /**
         * A [NameMatcher] that accepts any name
         */
        data object All : NameMatcher {
            override fun matches(deviceName: String?) = true
            override fun plus(matcher: NameMatcher): NameMatcher = matcher
            override fun plus(pattern: NamePattern): NameMatcher = Some(setOf(pattern))
        }

        /**
         * A [NameMatcher] that accepts a name that matches one of the given [NamePattern]s
         * @property namePatterns the [NamePattern] allowed
         */
        data class Some(val namePatterns: Set<NamePattern>) : NameMatcher {
            override fun matches(deviceName: String?) = deviceName != null && namePatterns.any { it.value.containsMatchIn(deviceName.lowercase()) }
            override fun plus(matcher: NameMatcher): NameMatcher = namePatterns.fold(matcher) { acc, pattern -> acc + pattern }
            override fun plus(pattern: NamePattern): NameMatcher = copy(namePatterns = namePatterns + pattern)
        }

        /**
         * Checks whether a [DeviceInfo.name] matches the [NameMatcher]
         * @param deviceInfo the [DeviceInfo] to check
         * @return `true` if the [DeviceInfo.name] matches the [NameMatcher]
         */
        fun matches(deviceInfo: DeviceInfo) = matches(deviceInfo.name)

        /**
         * Checks whether a [deviceName] matches the [NameMatcher]
         * @param deviceName the [String] to check
         * @return `true` if the [String] matches the [NameMatcher]
         */
        fun matches(deviceName: String?): Boolean

        /**
         * Combines this [NameMatcher] with another [NameMatcher]
         * @param matcher the [NameMatcher] to combine with
         * @return the combined [NameMatcher]
         */
        infix operator fun plus(matcher: NameMatcher): NameMatcher

        /**
         * Combines this [NameMatcher] with a [NamePattern]
         * @param pattern the [NamePattern] to combine with
         * @return the combined [NameMatcher]
         */
        infix operator fun plus(pattern: NamePattern): NameMatcher
    }

    /**
     * Checks whether [BaseAdvertisementData.serviceUUIDs] match a filter]
     */
    sealed interface ServiceMatcher {

        /**
         * A [ServiceMatcher] that accepts any services
         */
        data object Any : ServiceMatcher {
            override fun matches(uuids: List<UUID>) = true
            override fun or(matcher: Constrained): Constrained = Some(setOf(matcher))
            override fun and(matcher: Constrained): Constrained = All(setOf(matcher))
        }

        /**
         * A [ServiceMatcher] that is somehow constrained to a set of [UUID]
         */
        sealed interface Constrained : ServiceMatcher

        /**
         * A [Constrained] that accepts a single [UUID]
         * @property uuid the [UUID] to accept
         */
        @JvmInline
        value class Exactly(val uuid: UUID) : Constrained {
            override fun matches(uuids: List<UUID>): Boolean = uuid in uuids
            override fun or(matcher: Constrained): Constrained = Some(setOf(this, matcher))
            override fun and(matcher: Constrained): Constrained = All(setOf(this, matcher))
        }

        /**
         * A [Constrained] that matches when the set of [UUID] matches one of a set of [Constrained]
         * @property constraints the set of [Constrained] to accept. If one of them matches the [Constrained] is accepted
         */
        data class Some(val constraints: Set<Constrained>) : Constrained {

            /**
             * Creates a [Some] that accepts one of a set of [UUID]
             * @param uuids the set of [UUID] to accept
             */
            constructor(vararg uuids: UUID) : this(uuids.map { Exactly(it) }.toSet())

            override fun matches(uuids: List<UUID>): Boolean = constraints.any { it.matches(uuids) }
            override fun or(matcher: Constrained): Constrained = copy(constraints = constraints + matcher)
            override fun and(matcher: Constrained): Constrained = All(setOf(this, matcher))
        }

        /**
         * A [Constrained] that matches when the set of [UUID] matches all sets of [Constrained]
         * @property constraints the set of [Constrained] to accept. If all of them match the [Constrained] is accepted
         */
        data class All(val constraints: Set<Constrained>) : Constrained {

            /**
             * Creates an [All] that requires all of a set of [UUID]
             * @param uuids the set of [UUID] to accept
             */
            constructor(vararg uuids: UUID) : this(uuids.map { Exactly(it) }.toSet())

            override fun matches(uuids: List<UUID>): Boolean = constraints.all { it.matches(uuids) }
            override fun or(matcher: Constrained): Constrained = Some(setOf(this, matcher))
            override fun and(matcher: Constrained): Constrained = copy(constraints = constraints + matcher)
        }

        /**
         * Checks whether [BaseAdvertisementData.serviceUUIDs] match the [ServiceMatcher]
         * @param deviceInfo the [DeviceInfo] to check
         * @return `true` if the [BaseAdvertisementData.serviceUUIDs] match the [ServiceMatcher]
         */
        fun matches(deviceInfo: DeviceInfo) = matches(deviceInfo.advertisementData.serviceUUIDs)

        /**
         * Checks whether a list of [UUID] match the [ServiceMatcher]
         * @param uuids the list of [UUID] to check
         * @return `true` if the [uuids] match the [ServiceMatcher]
         */
        fun matches(uuids: List<UUID>): Boolean

        /**
         * Combines this [ServiceMatcher] with another [ServiceMatcher] so that one of them must match
         * @param matcher the [ServiceMatcher] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun or(matcher: ServiceMatcher) = when (matcher) {
            is Constrained -> or(matcher)
            is Any -> Any
        }

        /**
         * Combines this [ServiceMatcher] with a [Constrained] so that one of them must match
         * @param matcher the [Constrained] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun or(matcher: Constrained): Constrained

        /**
         * Combines this [ServiceMatcher] with a [UUID] so that one of them must match
         * @param uuid the [UUID] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun or(uuid: UUID): Constrained = or(Exactly(uuid))

        /**
         * Combines this [ServiceMatcher] with another [ServiceMatcher] so that both must match\
         * @param matcher the [ServiceMatcher] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun and(matcher: ServiceMatcher) = when (matcher) {
            is Constrained -> and(matcher)
            is Any -> this
        }

        /**
         * Combines this [ServiceMatcher] with a [Constrained] so that both must match
         * @param matcher the [Constrained] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun and(matcher: Constrained): Constrained

        /**
         * Combines this [ServiceMatcher] with a [UUID] so that both must match
         * @param uuid the [UUID] to combine with
         * @return the combined [ServiceMatcher]
         */
        infix fun and(uuid: UUID): Constrained = and(Exactly(uuid))
    }

    /**
     * Checks whether [DeviceInfo.updatedAt] matches a filter
     */
    sealed interface UpdateMatcher {

        /**
         * A [UpdateMatcher] that accepts any update
         */
        data object All : UpdateMatcher {
            override fun matches(lastUpdate: KalugaDate): Boolean = true
            override fun plus(matcher: UpdateMatcher): UpdateMatcher = matcher
            override fun plus(duration: Duration) = Age(duration)
        }

        /**
         * A [UpdateMatcher] that accepts an update that happened after a given [KalugaDate]
         * @property date the [KalugaDate] to accept
         */
        data class UpdatedAfter(val date: KalugaDate) : UpdateMatcher {
            override fun matches(lastUpdate: KalugaDate): Boolean = date <= lastUpdate
            override fun plus(matcher: UpdateMatcher): UpdateMatcher = when (matcher) {
                is All -> this
                is UpdatedAfter -> copy(date = maxOf(date, matcher.date))
                is Age -> minus(matcher.age)
            }
            override fun plus(duration: Duration): UpdateMatcher = copy(date = date + duration)
        }

        /**
         * A [UpdateMatcher] that accepts an update that happened at least a given [Duration] ago
         * @property age the [Duration] to accept
         * @throws IllegalArgumentException if [age] is negative
         */
        data class Age(val age: Duration) : UpdateMatcher {

            init {
                require(age >= Duration.ZERO) { "Age must be non-negative" }
            }

            override fun matches(lastUpdate: KalugaDate): Boolean = lastUpdate >= DefaultKalugaDate.now() - age
            override fun plus(matcher: UpdateMatcher): UpdateMatcher = when (matcher) {
                is All -> this
                is UpdatedAfter -> UpdatedAfter(matcher.date - age)
                is Age -> plus(matcher.age)
            }
            override fun plus(duration: Duration): UpdateMatcher = copy(age = age + duration)
        }

        /**
         * Checks whether [DeviceInfo.updatedAt] matches the [UpdateMatcher]
         * @param deviceInfo the [DeviceInfo] to check
         * @return `true` if the [DeviceInfo.updatedAt] matches the [UpdateMatcher]
         */
        fun matches(deviceInfo: DeviceInfo) = matches(deviceInfo.updatedAt)

        /**
         * Checks whether a [KalugaDate] matches the [UpdateMatcher]
         * @param lastUpdate the [KalugaDate] to check
         * @return `true` if the [KalugaDate] matches the [UpdateMatcher]
         */
        fun matches(lastUpdate: KalugaDate): Boolean

        /**
         * Adds the time of an [UpdateMatcher] with another [UpdateMatcher]
         * @param matcher the [UpdateMatcher] to combine with
         * @return the combined [UpdateMatcher]
         */
        infix operator fun plus(matcher: UpdateMatcher): UpdateMatcher

        /**
         * Adds the time of an [UpdateMatcher] with a [Duration]
         * @param duration the [Duration] to combine with
         * @return the combined [UpdateMatcher]
         */
        infix operator fun plus(duration: Duration): UpdateMatcher

        /**
         * Adds the time of an [UpdateMatcher] with a [Duration]
         * @param duration the [Duration] to combine with
         * @return the combined [UpdateMatcher]
         */
        infix operator fun minus(duration: Duration): UpdateMatcher = plus(-duration)
    }

    /**
     * A [ManufacturerIdentifier] used to filter [BaseAdvertisementData.manufacturerId]
     * @property manufacturerId the [Int] representing the manufacturer
     */
    @JvmInline
    value class ManufacturerIdentifier(val manufacturerId: Int)

    /**
     * Combines this [DeviceFilter] with another [DeviceFilter]
     * @param other the [DeviceFilter] to combine with
     * @return the combined [DeviceFilter]
     */
    infix fun union(other: DeviceFilter) = copy(
        nameMatcher = nameMatcher + other.nameMatcher,
        serviceMatcher = serviceMatcher and other.serviceMatcher,
        updateMatcher = updateMatcher + other.updateMatcher,
        manufacturer = manufacturer + other.manufacturer,
        exclude = exclude + other.exclude,
    )

    /**
     * Combines this [DeviceFilter] with a [NameMatcher] so that [matcher] is added to [DeviceFilter.nameMatcher] using [DeviceFilter.NameMatcher.plus]
     * @param matcher the [NameMatcher] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun plus(matcher: NameMatcher) = copy(nameMatcher = nameMatcher + matcher)

    /**
     * Combines this [DeviceFilter] with a [NamePattern] so that [pattern] is added to [DeviceFilter.nameMatcher] using [DeviceFilter.NameMatcher.plus]
     * @param pattern the [NamePattern] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun plus(pattern: NamePattern) = copy(nameMatcher = nameMatcher + pattern)

    /**
     * Combines this [DeviceFilter] with a [ServiceMatcher] so that [matcher] is added to [DeviceFilter.serviceMatcher] using [DeviceFilter.ServiceMatcher.or]
     * @param matcher the [ServiceMatcher] to combine with
     * @return the combined [DeviceFilter]
     */
    infix fun or(matcher: ServiceMatcher) = copy(serviceMatcher = serviceMatcher or matcher)

    /**
     * Combines this [DeviceFilter] with a [UUID] so that [uuid] is added to [DeviceFilter.serviceMatcher] using [DeviceFilter.ServiceMatcher.or]
     * @param uuid the [UUID] to combine with
     * @return the combined [DeviceFilter]
     */
    infix fun or(uuid: UUID) = or(Exactly(uuid))

    /**
     * Combines this [DeviceFilter] with a [ServiceMatcher] so that [matcher] is added to [DeviceFilter.serviceMatcher] using [DeviceFilter.ServiceMatcher.and]
     * @param matcher the [ServiceMatcher] to combine with
     * @return the combined [DeviceFilter]
     */
    infix fun and(matcher: ServiceMatcher) = copy(serviceMatcher = serviceMatcher and matcher)

    /**
     * Combines this [DeviceFilter] with a [UUID] so that [uuid] is added to [DeviceFilter.serviceMatcher] using [DeviceFilter.ServiceMatcher.and]
     * @param uuid the [UUID] to combine with
     * @return the combined [DeviceFilter]
     */
    infix fun and(uuid: UUID) = and(Exactly(uuid))

    /**
     * Combines this [DeviceFilter] with a [UpdateMatcher] so that [updateMatcher] is added to [DeviceFilter.updateMatcher] using [DeviceFilter.UpdateMatcher.plus]
     * @param updateMatcher the [UpdateMatcher] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun plus(updateMatcher: UpdateMatcher): DeviceFilter = copy(updateMatcher = updateMatcher + updateMatcher)

    /**
     * Combines this [DeviceFilter] with a [Duration] so that [duration] is added to [DeviceFilter.updateMatcher] using [DeviceFilter.UpdateMatcher.plus]
     * @param duration the [Duration] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun plus(duration: Duration): DeviceFilter = copy(updateMatcher = updateMatcher + duration)

    /**
     * Combines this [DeviceFilter] with a [Duration] so that [duration] is added to [DeviceFilter.updateMatcher] using [DeviceFilter.UpdateMatcher.minus]
     * @param duration the [Duration] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun minus(duration: Duration): DeviceFilter = copy(updateMatcher = updateMatcher - duration)

    /**
     * Combines this [DeviceFilter] with a [ManufacturerIdentifier] so that [manufacturer] is added to [DeviceFilter.manufacturer]
     * @param manufacturer the [ManufacturerIdentifier] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun plus(manufacturer: ManufacturerIdentifier) = copy(manufacturer = this.manufacturer + manufacturer)

    /**
     * Combines this [DeviceFilter] with a [Identifier] so that [exclude] is added to [DeviceFilter.exclude]
     * @param exclude the [Identifier] to combine with
     * @return the combined [DeviceFilter]
     */
    infix operator fun minus(exclude: Identifier) = copy(exclude = this.exclude + exclude)

    /**
     * Checks whether a [DeviceInfo] matches the [DeviceFilter]
     */
    fun matches(info: DeviceInfo) = nameMatcher.matches(info) &&
        serviceMatcher.matches(info) &&
        updateMatcher.matches(info) &&
        (manufacturer.isEmpty() || info.advertisementData.manufacturerId?.let { ManufacturerIdentifier(it) } in manufacturer) &&
        info.identifier !in exclude
}

/**
 * A pattern for matching a name in a [DeviceFilter.NameMatcher]
 * @property value the [Regex] used to match a name
 */
@JvmInline
value class NamePattern(val value: Regex) {
    constructor(value: String) : this(value.toRegex())
}

/**
 * Creates a [NamePattern] that matches a name that starts with [value]
 * @param value the [String] to match
 */
@Suppress("FunctionName")
fun NamePrefix(value: String) = NamePattern("^${value.lowercase()}")

/**
 * Creates a [NamePattern] that matches a name that ends with [value]
 * @param value the [String] to match
 */
@Suppress("FunctionName")
fun NamePostfix(value: String) = NamePattern($$"$${value.lowercase()}$")
