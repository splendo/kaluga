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

import kotlinx.serialization.Serializable

@Serializable
sealed class UndefinedQuantityType {
    @Serializable
    data class Extended<WrappedPhysicalQuantity : PhysicalQuantity.PhysicalQuantityWithDimension>(val extended: WrappedPhysicalQuantity) : UndefinedQuantityType()

    @Serializable
    data class Custom<CustomQuantity>(val customQuantity: CustomQuantity) : UndefinedQuantityType()

    @Serializable
    data class Dividing<NumeratorQuantity : UndefinedQuantityType, DenominatorQuantity : UndefinedQuantityType>(
        val numerator: NumeratorQuantity,
        val denominator: DenominatorQuantity,
    ) : UndefinedQuantityType()

    @Serializable
    data class Multiplying<Left : UndefinedQuantityType, Right : UndefinedQuantityType>(val left: Left, val right: Right) : UndefinedQuantityType()

    @Serializable
    data class Reciprocal<Quantity : UndefinedQuantityType>(val reciprocal: Quantity) : UndefinedQuantityType()
}
