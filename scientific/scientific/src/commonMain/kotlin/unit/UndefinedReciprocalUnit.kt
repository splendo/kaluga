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
sealed class UndefinedReciprocalUnit<
    InverseQuantity : UndefinedQuantityType,
    InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
    > :
    AbstractUndefinedScientificUnit<UndefinedQuantityType.Reciprocal<InverseQuantity>>() {

    abstract val inverse: InverseUnit

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        inverse.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        inverse.numeratorUnits
    }

    override val quantityType: UndefinedQuantityType.Reciprocal<InverseQuantity> by lazy {
        UndefinedQuantityType.Reciprocal(inverse.quantityType)
    }

    override fun fromSIUnit(value: Decimal): Decimal = inverse.deltaToSIUnitDelta(value)
    override fun toSIUnit(value: Decimal): Decimal = inverse.deltaFromSIUnitDelta(value)

    @Serializable
    data class MetricAndImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInMetric,
              InverseUnit : MeasurementUsage.UsedInUKImperial,
              InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val imperial: Imperial<InverseQuantity, InverseUnit> by lazy { Imperial(inverse) }
        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
        override val metricAndUKImperial: MetricAndUKImperial<InverseQuantity, InverseUnit> by lazy { MetricAndUKImperial(inverse) }
        override val metricAndUSCustomary: MetricAndUSCustomary<InverseQuantity, InverseUnit> by lazy { MetricAndUSCustomary(inverse) }
    }

    @Serializable
    data class Metric<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    @Serializable
    data class Imperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInUKImperial,
              InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial

        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
    }

    @Serializable
    data class UKImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    @Serializable
    data class USCustomary<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    @Serializable
    data class MetricAndUKImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInMetric,
              InverseUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
    }

    @Serializable
    data class MetricAndUSCustomary<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
              InverseUnit : AbstractUndefinedScientificUnit<InverseQuantity>,
              InverseUnit : MeasurementUsage.UsedInMetric,
              InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun SerializersModuleBuilder.setupForUndefinedReciprocalUnit() {
    polymorphic(UndefinedReciprocalUnit::class) {
        registerUndefinedReciprocalUnitClasses()
    }
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)

    polymorphicDefaultSerializer(UndefinedReciprocalUnit.MetricAndImperial::class) {
        UndefinedReciprocalUnit.MetricAndImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndImperial<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.Metric::class) {
        UndefinedReciprocalUnit.Metric.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Metric<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.Imperial::class) {
        UndefinedReciprocalUnit.Imperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Imperial<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.UKImperial::class) {
        UndefinedReciprocalUnit.UKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.UKImperial<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.USCustomary::class) {
        UndefinedReciprocalUnit.USCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.USCustomary<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.MetricAndUKImperial::class) {
        UndefinedReciprocalUnit.MetricAndUKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUKImperial<*, *>>
    }
    polymorphicDefaultSerializer(UndefinedReciprocalUnit.MetricAndUSCustomary::class) {
        UndefinedReciprocalUnit.MetricAndUSCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUSCustomary<*, *>>
    }

    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.MetricAndImperial::class) {
        UndefinedReciprocalUnit.MetricAndImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndImperial<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.Metric::class) {
        UndefinedReciprocalUnit.Metric.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Metric<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.Imperial::class) {
        UndefinedReciprocalUnit.Imperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Imperial<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.UKImperial::class) {
        UndefinedReciprocalUnit.UKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.UKImperial<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.USCustomary::class) {
        UndefinedReciprocalUnit.USCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.USCustomary<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.MetricAndUKImperial::class) {
        UndefinedReciprocalUnit.MetricAndUKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUKImperial<*, *>>
    }
    polymorphicDefaultDeserializer(UndefinedReciprocalUnit.MetricAndUSCustomary::class) {
        UndefinedReciprocalUnit.MetricAndUSCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUSCustomary<*, *>>
    }
}

@Suppress("UNCHECKED_CAST")
internal fun PolymorphicModuleBuilder<UndefinedReciprocalUnit<*, *>>.registerUndefinedReciprocalUnitClasses() {
    val quantitySerializer = PolymorphicSerializer(UndefinedQuantityType::class)
    val undefinedSerializer = AbstractUndefinedScientificUnit.serializer(quantitySerializer)
    subclass(
        UndefinedReciprocalUnit.MetricAndImperial::class,
        UndefinedReciprocalUnit.MetricAndImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndImperial<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.Metric::class,
        UndefinedReciprocalUnit.Metric.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Metric<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.Imperial::class,
        UndefinedReciprocalUnit.Imperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.Imperial<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.UKImperial::class,
        UndefinedReciprocalUnit.UKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.UKImperial<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.USCustomary::class,
        UndefinedReciprocalUnit.USCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.USCustomary<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.MetricAndUKImperial::class,
        UndefinedReciprocalUnit.MetricAndUKImperial.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUKImperial<*, *>>,
    )
    subclass(
        UndefinedReciprocalUnit.MetricAndUSCustomary::class,
        UndefinedReciprocalUnit.MetricAndUSCustomary.serializer(quantitySerializer, undefinedSerializer) as KSerializer<UndefinedReciprocalUnit.MetricAndUSCustomary<*, *>>,
    )
}
