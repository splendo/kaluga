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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import kotlin.jvm.JvmName

interface UndefinedScientificValue<Quantity : UndefinedQuantityType, Unit : UndefinedScientificUnit<Quantity>> : ScientificValue<PhysicalQuantity.Undefined<Quantity>, Unit>

data class DefaultUndefinedScientificValue<Quantity : UndefinedQuantityType, Unit : UndefinedScientificUnit<Quantity>>(
    override val value: Double,
    override val unit: Unit,
) : UndefinedScientificValue<Quantity, Unit> {
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

@JvmName("metricAndImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.MetricAndImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.MetricAndImperial<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.MetricAndImperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.Metric<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.Metric<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric = factory(decimalValue, unit.asUndefined())

@JvmName("metricValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.Metric<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("imperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.Imperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.Imperial<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("imperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.Imperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("ukImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.UKImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.UKImperial<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial = factory(decimalValue, unit.asUndefined())

@JvmName("ukImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.UKImperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("usCustomaryValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.USCustomary<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.USCustomary<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("usCustomaryValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.USCustomary<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUKImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.MetricAndUKImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.MetricAndUKImperial<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndUKImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.MetricAndUKImperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUSCustomaryValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedExtendedUnit.MetricAndUSCustomary<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedExtendedUnit.MetricAndUSCustomary<Quantity, Unit>) -> Value) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndUSCustomaryValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedExtendedUnit.MetricAndUSCustomary<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}
