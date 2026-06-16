/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed class UndefinedMultipliedUnit<
    LeftQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
    RightQuantity : UndefinedQuantityType,
    RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>>() {
    abstract val left: LeftUnit
    abstract val right: RightUnit

    override val quantityType by lazy { UndefinedQuantityType.Multiplying(left.quantityType, right.quantityType) }
    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        left.numeratorUnits + right.numeratorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        left.denominatorUnits + right.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = left.deltaFromSIUnitDelta(right.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = left.deltaToSIUnitDelta(right.deltaToSIUnitDelta(value))

    @Serializable
    data class MetricAndImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInMetric,
              LeftUnit : MeasurementUsage.UsedInUKImperial,
              LeftUnit : MeasurementUsage.UsedInUSCustomary,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInMetric,
              RightUnit : MeasurementUsage.UsedInUKImperial,
              RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val imperial: Imperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Imperial(left, right) }
        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
        override val metricAndUKImperial: MetricAndUKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { MetricAndUKImperial(left, right) }
        override val metricAndUSCustomary: MetricAndUSCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { MetricAndUSCustomary(left, right) }
    }

    @Serializable
    data class Metric<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInMetric,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    @Serializable
    data class Imperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInUKImperial,
              LeftUnit : MeasurementUsage.UsedInUSCustomary,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInUKImperial,
              RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial

        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
    }

    @Serializable
    data class UKImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInUKImperial,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    @Serializable
    data class USCustomary<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInUSCustomary,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    @Serializable
    data class MetricAndUKImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInMetric,
              LeftUnit : MeasurementUsage.UsedInUKImperial,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInMetric,
              RightUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
    }

    @Serializable
    data class MetricAndUSCustomary<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
              LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
              LeftUnit : MeasurementUsage.UsedInMetric,
              LeftUnit : MeasurementUsage.UsedInUSCustomary,
              RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
              RightUnit : MeasurementUsage.UsedInMetric,
              RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun SerializersModuleBuilder.setupForUndefinedMultipliedUnit() {
    polymorphic(UndefinedMultipliedUnit::class) {
        registerUndefinedMultipliedUnitClasses()
    }
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)

    polymorphicDefaultSerializer(UndefinedMultipliedUnit.MetricAndImperial::class) {
        UndefinedMultipliedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.Metric::class) {
        UndefinedMultipliedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Metric<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.Imperial::class) {
        UndefinedMultipliedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Imperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.UKImperial::class) {
        UndefinedMultipliedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.UKImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.USCustomary::class) {
        UndefinedMultipliedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.USCustomary<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.MetricAndUKImperial::class) {
        UndefinedMultipliedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndUKImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedMultipliedUnit.MetricAndUSCustomary::class) {
        UndefinedMultipliedUnit.MetricAndUSCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndUSCustomary<*, *, *, *>>
    }

    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.MetricAndImperial::class) {
        UndefinedMultipliedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.Metric::class) {
        UndefinedMultipliedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Metric<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.Imperial::class) {
        UndefinedMultipliedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Imperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.UKImperial::class) {
        UndefinedMultipliedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.UKImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.USCustomary::class) {
        UndefinedMultipliedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.USCustomary<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.MetricAndUKImperial::class) {
        UndefinedMultipliedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndUKImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedMultipliedUnit.MetricAndUSCustomary::class) {
        UndefinedMultipliedUnit.MetricAndUSCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndUSCustomary<*, *, *, *>>
    }
}

@Suppress("UNCHECKED_CAST")
internal fun PolymorphicModuleBuilder<UndefinedMultipliedUnit<*, *, *, *>>.registerUndefinedMultipliedUnitClasses() {
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)
    subclass(
        UndefinedMultipliedUnit.MetricAndImperial::class,
        UndefinedMultipliedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>,
    )
    subclass(
        UndefinedMultipliedUnit.Metric::class,
        UndefinedMultipliedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Metric<*, *, *, *>>,
    )
    subclass(
        UndefinedMultipliedUnit.Imperial::class,
        UndefinedMultipliedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.Imperial<*, *, *, *>>,
    )
    subclass(
        UndefinedMultipliedUnit.UKImperial::class,
        UndefinedMultipliedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.UKImperial<*, *, *, *>>,
    )
    subclass(
        UndefinedMultipliedUnit.USCustomary::class,
        UndefinedMultipliedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.USCustomary<*, *, *, *>>,
    )
    subclass(
        UndefinedMultipliedUnit.MetricAndUKImperial::class,
        UndefinedMultipliedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedMultipliedUnit.MetricAndUKImperial<*, *, *, *>>,
    )
}
