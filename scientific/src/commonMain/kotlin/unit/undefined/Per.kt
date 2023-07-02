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
import com.splendo.kaluga.scientific.unit.DividedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial = DividedUndefinedScientificUnit.MetricAndImperial(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial = DividedUndefinedScientificUnit.MetricAndImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial = DividedUndefinedScientificUnit.MetricAndImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial = DividedUndefinedScientificUnit.MetricAndImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetric,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetric,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetric,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetric,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInImperial = DividedUndefinedScientificUnit.Imperial(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInImperial = DividedUndefinedScientificUnit.Imperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInImperial = DividedUndefinedScientificUnit.Imperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInImperial = DividedUndefinedScientificUnit.Imperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUKImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUKImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(this, denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit
    > NumeratorUnit.per(denominator: DenominatorUnit) where
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    DenominatorUnit : ScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), denominator.asUndefined())
