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

import UndefinedQuantityType
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
    object Dimensionless : PhysicalQuantity()

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
    object Acceleration : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing momentum of particle multiplied by distance travelled
     */
    @Serializable
    object Action : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the quantity proportional to the number of particles in a sample
     */
    @Serializable
    object AmountOfSubstance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of circular arc length to radius
     */
    @Serializable
    object Angle : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change in angular velocity per unit time
     */
    @Serializable
    object AngularAcceleration : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the angle incremented in a plane by a segment connecting an object and a reference point per unit time
     */
    @Serializable
    object AngularVelocity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the extent of a surface
     */
    @Serializable
    object Area : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit area
     */
    @Serializable
    object AreaDensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per time
     */
    @Serializable
    object CatalysticActivity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit of volume
     */
    @Serializable
    object Density : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the resistance of an incompressible fluid to stress
     */
    @Serializable
    object DynamicViscosity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the stored charge per unit electric potential
     */
    @Serializable
    object ElectricCapacitance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the force per unit electric field strength
     */
    @Serializable
    object ElectricCharge : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing how easily current flows through a material
     */
    @Serializable
    object ElectricConductance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of flow of electrical charge per unit time
     */
    @Serializable
    object ElectricCurrent : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the magnetic flux generated per unit current through a circuit
     */
    @Serializable
    object ElectricInductance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the electric potential per unit electric current
     */
    @Serializable
    object ElectricResistance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy
     */
    @Serializable
    object Energy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the transfer of momentum per unit time
     */
    @Serializable
    object Force : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of (periodic) occurrences per unit time
     */
    @Serializable
    object Frequency : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy per unit temperature change
     */
    @Serializable
    object HeatCapacity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing wavelength-weighted luminous flux per unit surface area
     */
    @Serializable
    object Illuminance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ionizing radiation energy absorbed by biological tissue per unit mass
     */
    @Serializable
    object IonizingRadiationAbsorbedDose : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the received radiation adjusted for the effect on biological tissue
     */
    @Serializable
    object IonizingRadiationEquivalentDose : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing mass per unit of length
     */
    @Serializable
    object LinearMassDensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change of acceleration per unit time
     */
    @Serializable
    object Jolt : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ratio of the dynamic viscosity over the density of the fluid
     */
    @Serializable
    object KinematicViscosity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the one-dimensional extent of an object
     */
    @Serializable
    object Length : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing luminous intensity per unit area of light
     */
    @Serializable
    object Luminance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived energy of light
     */
    @Serializable
    object LuminousEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of light per unit area
     */
    @Serializable
    object LuminousExposure : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived power of a light source
     */
    @Serializable
    object LuminousFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the wavelength-weighted power of emitted light per unit solid angle
     */
    @Serializable
    object LuminousIntensity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass of a substance which passes per unit of time
     */
    @Serializable
    object MassFlowRate : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the measure of magnetism, taking account of the strength and the extent of a magnetic field
     */
    @Serializable
    object MagneticFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the production of an electromotive force across an electrical conductor in a changing magnetic field
     */
    @Serializable
    object MagneticInduction : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of mass
     */
    @Serializable
    object Molality : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of volume
     */
    @Serializable
    object Molarity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of energy present in a system per unit amount of substance
     */
    @Serializable
    object MolarEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio between the mass and the amount of substance
     */
    @Serializable
    object MolarMass : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of the volume occupied by a substance to the amount of substance
     */
    @Serializable
    object MolarVolume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the product of an object's mass and velocity
     */
    @Serializable
    object Momentum : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of transfer of energy per unit time
     */
    @Serializable
    object Power : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing force per unit area
     */
    @Serializable
    object Pressure : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of particles decaying per unit time
     */
    @Serializable
    object Radioactivity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of area on a sphere to its radius squared
     */
    @Serializable
    object SolidAngle : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy density per unit mass
     */
    @Serializable
    object SpecificEnergy : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing heat capacity per unit mass
     */
    @Serializable
    object SpecificHeatCapacity : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing volume per unit mass
     */
    @Serializable
    object SpecificVolume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the moved distance per unit time
     */
    @Serializable
    object Speed : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy change per unit change in surface area
     */
    @Serializable
    object SurfaceTension : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the average kinetic energy per degree of freedom of a system
     */
    @Serializable
    object Temperature : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ease with which an object resists conduction of heat
     */
    @Serializable
    object ThermalResistance : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the duration of an event
     */
    @Serializable
    object Time : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the energy required to move a unit charge through an electric field from a reference point
     */
    @Serializable
    object Voltage : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the three dimensional extent of an object
     */
    @Serializable
    object Volume : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of volume with respect to time
     */
    @Serializable
    object VolumetricFlow : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of volume flow across a unit area
     */
    @Serializable
    object VolumetricFlux : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing a measure of resistance to acceleration
     */
    @Serializable
    object Weight : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of force per unit time
     */
    @Serializable
    object Yank : DefinedPhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] that can be used to create custom scientific units
     */
    @Serializable
    data class Undefined<CustomQuantity : UndefinedQuantityType>(val customQuantity: CustomQuantity) : PhysicalQuantityWithDimension()
}
