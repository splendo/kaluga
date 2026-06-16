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
sealed class UndefinedDividedUnit<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>() {
    abstract val numerator: NumeratorUnit
    abstract val denominator: DenominatorUnit
    override val quantityType by lazy {
        UndefinedQuantityType.Dividing(numerator.quantityType, denominator.quantityType)
    }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        numerator.numeratorUnits + denominator.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        denominator.numeratorUnits + numerator.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = denominator.deltaToSIUnitDelta(numerator.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = numerator.deltaToSIUnitDelta(denominator.deltaFromSIUnitDelta(value))

    @Serializable
    data class MetricAndImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInMetric,
              NumeratorUnit : MeasurementUsage.UsedInUKImperial,
              NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInMetric,
              DenominatorUnit : MeasurementUsage.UsedInUKImperial,
              DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val imperial: Imperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { Imperial(numerator, denominator) }
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
        override val metricAndUKImperial: MetricAndUKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy {
            MetricAndUKImperial(numerator, denominator)
        }
        override val metricAndUSCustomary: MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy {
            MetricAndUSCustomary(numerator, denominator)
        }
    }

    @Serializable
    data class Metric<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInMetric,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    @Serializable
    data class Imperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInUKImperial,
              NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInUKImperial,
              DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
    }

    @Serializable
    data class UKImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInUKImperial,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    @Serializable
    data class USCustomary<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    @Serializable
    data class MetricAndUKImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInMetric,
              NumeratorUnit : MeasurementUsage.UsedInUKImperial,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInMetric,
              DenominatorUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
    }

    @Serializable
    data class MetricAndUSCustomary<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
              NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
              NumeratorUnit : MeasurementUsage.UsedInMetric,
              NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
              DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
              DenominatorUnit : MeasurementUsage.UsedInMetric,
              DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun SerializersModuleBuilder.setupForUndefinedDividedUnit() {
    polymorphic(UndefinedDividedUnit::class) {
        registerUndefinedDividedUnitClasses()
    }
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)

    polymorphicDefaultSerializer(UndefinedDividedUnit.MetricAndImperial::class) {
        UndefinedDividedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.Metric::class) {
        UndefinedDividedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Metric<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.Imperial::class) {
        UndefinedDividedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Imperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.UKImperial::class) {
        UndefinedDividedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.UKImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.USCustomary::class) {
        UndefinedDividedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.USCustomary<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.MetricAndUKImperial::class) {
        UndefinedDividedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndUKImperial<*, *, *, *>>
    }
    polymorphicDefaultSerializer(UndefinedDividedUnit.MetricAndUSCustomary::class) {
        UndefinedDividedUnit.MetricAndUSCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndUSCustomary<*, *, *, *>>
    }

    polymorphicDefaultDeserializer(UndefinedDividedUnit.MetricAndImperial::class) {
        UndefinedDividedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.Metric::class) {
        UndefinedDividedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Metric<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.Imperial::class) {
        UndefinedDividedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Imperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.UKImperial::class) {
        UndefinedDividedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.UKImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.USCustomary::class) {
        UndefinedDividedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.USCustomary<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.MetricAndUKImperial::class) {
        UndefinedDividedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndUKImperial<*, *, *, *>>
    }
    polymorphicDefaultDeserializer(UndefinedDividedUnit.MetricAndUSCustomary::class) {
        UndefinedDividedUnit.MetricAndUSCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndUSCustomary<*, *, *, *>>
    }
}

@Suppress("UNCHECKED_CAST")
internal fun PolymorphicModuleBuilder<UndefinedDividedUnit<*, *, *, *>>.registerUndefinedDividedUnitClasses() {
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)

    subclass(
        UndefinedDividedUnit.MetricAndImperial::class,
        UndefinedDividedUnit.MetricAndImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>,
    )
    subclass(
        UndefinedDividedUnit.Metric::class,
        UndefinedDividedUnit.Metric.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Metric<*, *, *, *>>,
    )
    subclass(
        UndefinedDividedUnit.Imperial::class,
        UndefinedDividedUnit.Imperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.Imperial<*, *, *, *>>,
    )
    subclass(
        UndefinedDividedUnit.UKImperial::class,
        UndefinedDividedUnit.UKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.UKImperial<*, *, *, *>>,
    )
    subclass(
        UndefinedDividedUnit.USCustomary::class,
        UndefinedDividedUnit.USCustomary.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.USCustomary<*, *, *, *>>,
    )
    subclass(
        UndefinedDividedUnit.MetricAndUKImperial::class,
        UndefinedDividedUnit.MetricAndUKImperial.serializer(
            quantitySerializer,
            undefinedSerializer,
            quantitySerializer,
            undefinedSerializer,
        ) as KSerializer<UndefinedDividedUnit.MetricAndUKImperial<*, *, *, *>>,
    )
}
