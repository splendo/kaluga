/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Dimensionless]
 *
 * Dimensionless Quantity
 * is a quantity to which no physical dimension is assigned, also known as a bare, pure, or scalar quantity
 * or a quantity of dimension one,
 * with a corresponding constant of measurement in the SI of the constant one (or 1), which is not explicitly shown.
 *
 * "
 * 2.3.3 Dimensions of quantities
 * ...
 * There are quantities Q for which the defining equation is such that all of the dimensional
 * exponents in the equation for the dimension of Q are zero.
 * This is true in particular for any quantity that is defined as the ratio of two quantities of the same kind.
 * For example, the refractive index is the ratio of two speeds and the relative permittivity is
 * the ratio of the permittivity of a dielectric medium to that of free space.
 * Such quantities are simply numbers. The associated constant is the constant one, symbol 1,
 * although this is rarely explicitly written (see 5.4.7).
 * ...
 * The constant one is the neutral element of any system of units – necessary and present automatically.
 * There is no requirement to introduce it formally by decision.
 * Therefore, a formal traceability to the SI can be established through appropriate,
 * validated measurement procedures.
 *
 * 5.4.7 Stating quantity values being pure numbers
 *
 * As discussed in Section 2.3.3, values of quantities with constant one, are expressed simply as numbers.
 * The constant symbol 1 or constant name “one” are not explicitly shown.
 * SI prefix symbols can neither be attached to the symbol 1 nor to the name “one”,
 * therefore powers of 10 are used to express particularly large or small values.
 *
 * Quantities that are ratios of quantities of the same kind (for example length ratios and amount fractions)
 * have the option of being expressed with units (m/m, mol/mol) to aid the understanding of
 * the quantity being expressed and also allow the use of SI prefixes, if this is desirable (μm/m, nmol/mol).
 * Quantities relating to counting do not have this option, they are just numbers.
 *
 * The internationally recognized symbol % (percent) may be used with the SI.
 * When it is used, a space separates the number and the symbol %.
 * The symbol % should be used rather than the name “percent”.
 * In written text, however, the symbol % generally takes the meaning of “parts per hundred”.
 * Phrases such as “percentage by mass”, “percentage by volume”, or “percentage by amount of substance” shall not be used;
 * the extra information on the quantity should instead be conveyed in the description and symbol for the quantity.
 * "
 * Source:  "SI Brochure: The International System of Units, 9th Edition".
 */
@Serializable
sealed class Dimensionless :
    AbstractScientificUnit<PhysicalQuantity.Dimensionless>(),
    MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Dimensionless>

/**
 * Set of all [Dimensionless]
 */
val DimensionlessUnits: Set<Dimensionless> get() = setOf(
    One,
    Percent,
    Permill,
)

@Serializable
data object One : Dimensionless() {
    const val UNIT_VALUE = 1.0
    override val symbol: String = ""
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Dimensionless
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

val One.constant get() = UNIT_VALUE.invoke(One)

@Serializable
data object Percent : Dimensionless() {
    const val PARTS_PER_HUNDRED = 100.0
    override val symbol: String = "%"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Dimensionless
    override fun fromSIUnit(value: Decimal): Decimal = value * PARTS_PER_HUNDRED.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / PARTS_PER_HUNDRED.toDecimal()
}

@Serializable
data object Permill : Dimensionless() {
    const val PARTS_PER_THOUSAND = 1000.0
    override val symbol: String = "‰"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Dimensionless
    override fun fromSIUnit(value: Decimal): Decimal = value * PARTS_PER_THOUSAND.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / PARTS_PER_THOUSAND.toDecimal()
}
