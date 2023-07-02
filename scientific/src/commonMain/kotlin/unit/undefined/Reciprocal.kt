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
import com.splendo.kaluga.scientific.unit.InvertedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndImperial = InvertedUndefinedScientificUnit.MetricAndImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndImperial = InvertedUndefinedScientificUnit.MetricAndImperial(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetric = InvertedUndefinedScientificUnit.Metric(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetric = InvertedUndefinedScientificUnit.Metric(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInImperial = InvertedUndefinedScientificUnit.Imperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInImperial = InvertedUndefinedScientificUnit.Imperial(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUKImperial = InvertedUndefinedScientificUnit.UKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUKImperial = InvertedUndefinedScientificUnit.UKImperial(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUSCustomary = InvertedUndefinedScientificUnit.USCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUSCustomary = InvertedUndefinedScientificUnit.USCustomary(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = InvertedUndefinedScientificUnit.MetricAndUKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = InvertedUndefinedScientificUnit.MetricAndUKImperial(asUndefined())

fun <
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = InvertedUndefinedScientificUnit.MetricAndUSCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit
    > InverseUnit.reciprocal() where
    InverseUnit : ScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = InvertedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined())
