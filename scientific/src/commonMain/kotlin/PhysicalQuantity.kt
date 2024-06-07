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

package com.splendo.kaluga.scientific

import kotlinx.serialization.Serializable

/**
 * A physical property of a material or system that can be quantified by measurement
 */
@Serializable
sealed class PhysicalQuantity : com.splendo.kaluga.base.utils.Serializable {
    /**
     * A [PhysicalQuantity] that has no specific dimension
     */
    @Serializable
    data object Dimensionless : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] that has a specific dimension
     */
    @Serializable
    sealed class PhysicalQuantityWithDimension : PhysicalQuantity()

    /**
     * A [PhysicalQuantityWithDimension] that has been defined by this library
     */
    @Serializable
    sealed class DefinedPhysicalQuantityWithDimension : PhysicalQuantityWithDimension() {
        val undefined get() = Undefined(UndefinedQuantityType.Extended(this))
    }

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of velocity per unit time
     */
    @Serializable
    data object Acceleration : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing momentum of particle multiplied by distance travelled
     */
    @Serializable
    data object Action : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the quantity proportional to the number of particles in a sample
     */
    @Serializable
    data object AmountOfSubstance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of circular arc length to radius
     */
    @Serializable
    data object Angle : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change in angular velocity per unit time
     */
    @Serializable
    data object AngularAcceleration : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the angle incremented in a plane by a segment connecting an object and a reference point per unit time
     */
    @Serializable
    data object AngularVelocity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the extent of a surface
     */
    @Serializable
    data object Area : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit area
     */
    @Serializable
    data object AreaDensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per time
     */
    @Serializable
    data object CatalysticActivity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit of volume
     */
    @Serializable
    data object Density : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the resistance of an incompressible fluid to stress
     */
    @Serializable
    data object DynamicViscosity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the stored charge per unit electric potential
     */
    @Serializable
    data object ElectricCapacitance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the force per unit electric field strength
     */
    @Serializable
    data object ElectricCharge : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing how easily current flows through a material
     */
    @Serializable
    data object ElectricConductance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of flow of electrical charge per unit time
     */
    @Serializable
    data object ElectricCurrent : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the magnetic flux generated per unit current through a circuit
     */
    @Serializable
    data object ElectricInductance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the electric potential per unit electric current
     */
    @Serializable
    data object ElectricResistance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy
     */
    @Serializable
    data object Energy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the transfer of momentum per unit time
     */
    @Serializable
    data object Force : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of (periodic) occurrences per unit time
     */
    @Serializable
    data object Frequency : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy per unit temperature change
     */
    @Serializable
    data object HeatCapacity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing wavelength-weighted luminous flux per unit surface area
     */
    @Serializable
    data object Illuminance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ionizing radiation energy absorbed by biological tissue per unit mass
     */
    @Serializable
    data object IonizingRadiationAbsorbedDose : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the received radiation adjusted for the effect on biological tissue
     */
    @Serializable
    data object IonizingRadiationEquivalentDose : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing mass per unit of length
     */
    @Serializable
    data object LinearMassDensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change of acceleration per unit time
     */
    @Serializable
    data object Jolt : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ratio of the dynamic viscosity over the density of the fluid
     */
    @Serializable
    data object KinematicViscosity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the one-dimensional extent of an object
     */
    @Serializable
    data object Length : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing luminous intensity per unit area of light
     */
    @Serializable
    data object Luminance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived energy of light
     */
    @Serializable
    data object LuminousEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of light per unit area
     */
    @Serializable
    data object LuminousExposure : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived power of a light source
     */
    @Serializable
    data object LuminousFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the wavelength-weighted power of emitted light per unit solid angle
     */
    @Serializable
    data object LuminousIntensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass of a substance which passes per unit of time
     */
    @Serializable
    data object MassFlowRate : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the measure of magnetism, taking account of the strength and the extent of a magnetic field
     */
    @Serializable
    data object MagneticFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the production of an electromotive force across an electrical conductor in a changing magnetic field
     */
    @Serializable
    data object MagneticInduction : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of mass
     */
    @Serializable
    data object Molality : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of volume
     */
    @Serializable
    data object Molarity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of energy present in a system per unit amount of substance
     */
    @Serializable
    data object MolarEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio between the mass and the amount of substance
     */
    @Serializable
    data object MolarMass : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of the volume occupied by a substance to the amount of substance
     */
    @Serializable
    data object MolarVolume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the product of an object's mass and velocity
     */
    @Serializable
    data object Momentum : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of transfer of energy per unit time
     */
    @Serializable
    data object Power : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing force per unit area
     */
    @Serializable
    data object Pressure : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of particles decaying per unit time
     */
    @Serializable
    data object Radioactivity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of area on a sphere to its radius squared
     */
    @Serializable
    data object SolidAngle : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy density per unit mass
     */
    @Serializable
    data object SpecificEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing heat capacity per unit mass
     */
    @Serializable
    data object SpecificHeatCapacity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing volume per unit mass
     */
    @Serializable
    data object SpecificVolume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the moved distance per unit time
     */
    @Serializable
    data object Speed : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy change per unit change in surface area
     */
    @Serializable
    data object SurfaceTension : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the average kinetic energy per degree of freedom of a system
     */
    @Serializable
    data object Temperature : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ease with which an object resists conduction of heat
     */
    @Serializable
    data object ThermalResistance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the duration of an event
     */
    @Serializable
    data object Time : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the energy required to move a unit charge through an electric field from a reference point
     */
    @Serializable
    data object Voltage : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the three dimensional extent of an object
     */
    @Serializable
    data object Volume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of volume with respect to time
     */
    @Serializable
    data object VolumetricFlow : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of volume flow across a unit area
     */
    @Serializable
    data object VolumetricFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing a measure of resistance to acceleration
     */
    @Serializable
    data object Weight : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of force per unit time
     */
    @Serializable
    data object Yank : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] that can be used to create custom scientific units
     */
    @Serializable
    data class Undefined<CustomQuantity : UndefinedQuantityType>(val customQuantity: CustomQuantity) : PhysicalQuantityWithDimension()
}
