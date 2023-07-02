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
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedScientificUnit

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndImperial = WrappedUndefinedScientificUnit.MetricAndImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetric = WrappedUndefinedScientificUnit.Metric(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInImperial = WrappedUndefinedScientificUnit.Imperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInUKImperial = WrappedUndefinedScientificUnit.UKImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedScientificUnit.USCustomary(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndUKImperial = WrappedUndefinedScientificUnit.MetricAndUKImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndUSCustomary = WrappedUndefinedScientificUnit.MetricAndUSCustomary(this)
