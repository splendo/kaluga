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

package com.splendo.kaluga.scientific.undefined

import UndefinedQuantityType
import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedScientificUnit
import kotlin.jvm.JvmName

interface UndefinedScientificValue<Quantity : UndefinedQuantityType, Unit : UndefinedScientificUnit<Quantity>> : ScientificValue<PhysicalQuantity.Undefined<Quantity>, Unit>

data class DefaultUndefinedScientificValue<Quantity : UndefinedQuantityType, Unit : UndefinedScientificUnit<Quantity>>(
    override val value: Double,
    override val unit: Unit
) : UndefinedScientificValue<Quantity, Unit> {
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > Number.invoke(unit: Unit) = this.toDecimal()(unit)

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    Value : UndefinedScientificValue<Quantity, Unit>,
    > Number.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = this.toDecimal()(unit, factory)

operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > Decimal.invoke(unit: Unit) = this(unit, ::DefaultUndefinedScientificValue)

@JvmName("metricAndImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.MetricAndImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.MetricAndImperial<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.MetricAndImperial<Quantity, Unit> ->
        DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.Metric<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.Metric<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric = factory(decimalValue, unit.asUndefined())

@JvmName("metricValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.Metric<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("imperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.Imperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.Imperial<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial = factory(decimalValue, unit.asUndefined())

@JvmName("imperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.Imperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("ukImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.UKImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.UKImperial<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial = factory(decimalValue, unit.asUndefined())

@JvmName("ukImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.UKImperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("usCustomaryValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.USCustomary<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.USCustomary<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("usCustomaryValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.USCustomary<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUKImperialValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.MetricAndUKImperial<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.MetricAndUKImperial<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndUKImperialValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.MetricAndUKImperial<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUSCustomaryValueAsUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    Value : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, WrappedUndefinedScientificUnit.MetricAndUSCustomary<Quantity, Unit>>,
    > ScientificValue<Quantity, Unit>.asUndefined(factory: (Decimal, WrappedUndefinedScientificUnit.MetricAndUSCustomary<Quantity, Unit>) -> Value) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary = factory(decimalValue, unit.asUndefined())

@JvmName("metricAndUSCustomaryValueAsDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > ScientificValue<Quantity, Unit>.asUndefined() where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary = asUndefined { value: Decimal, unit: WrappedUndefinedScientificUnit.MetricAndUSCustomary<Quantity, Unit> ->
    DefaultUndefinedScientificValue(value, unit)
}
