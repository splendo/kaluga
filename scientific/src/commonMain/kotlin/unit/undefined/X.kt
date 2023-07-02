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

package com.splendo.kaluga.scientific.unit.undefined

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.MultipliedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndImperial = MultipliedUndefinedScientificUnit.MetricAndImperial(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndImperial = MultipliedUndefinedScientificUnit.MetricAndImperial(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndImperial = MultipliedUndefinedScientificUnit.MetricAndImperial(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndImperial = MultipliedUndefinedScientificUnit.MetricAndImperial(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = MultipliedUndefinedScientificUnit.Metric(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = MultipliedUndefinedScientificUnit.Metric(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = MultipliedUndefinedScientificUnit.Metric(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = MultipliedUndefinedScientificUnit.Metric(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInImperial = MultipliedUndefinedScientificUnit.Imperial(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInImperial = MultipliedUndefinedScientificUnit.Imperial(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInImperial = MultipliedUndefinedScientificUnit.Imperial(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInImperial = MultipliedUndefinedScientificUnit.Imperial(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = MultipliedUndefinedScientificUnit.UKImperial(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = MultipliedUndefinedScientificUnit.UKImperial(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = MultipliedUndefinedScientificUnit.UKImperial(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = MultipliedUndefinedScientificUnit.UKImperial(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = MultipliedUndefinedScientificUnit.USCustomary(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = MultipliedUndefinedScientificUnit.USCustomary(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = MultipliedUndefinedScientificUnit.USCustomary(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = MultipliedUndefinedScientificUnit.USCustomary(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUKImperial = MultipliedUndefinedScientificUnit.MetricAndUKImperial(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUKImperial = MultipliedUndefinedScientificUnit.MetricAndUKImperial(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUKImperial = MultipliedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUKImperial = MultipliedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUSCustomary = MultipliedUndefinedScientificUnit.MetricAndUSCustomary(this, right)

infix fun <
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUSCustomary = MultipliedUndefinedScientificUnit.MetricAndUSCustomary(this, right.asUndefined())

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUSCustomary = MultipliedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), right)

infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit
    > LeftUnit.x(right: RightUnit) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUSCustomary = MultipliedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), right.asUndefined())
