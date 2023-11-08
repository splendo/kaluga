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
     * A [PhysicalQuantityWithDimension] representing the rate of change of velocity per unit time
     */
    @Serializable
    data object Acceleration : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing momentum of particle multiplied by distance travelled
     */
    @Serializable
    data object Action : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the quantity proportional to the number of particles in a sample
     */
    @Serializable
    data object AmountOfSubstance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of circular arc length to radius
     */
    @Serializable
    data object Angle : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change in angular velocity per unit time
     */
    @Serializable
    data object AngularAcceleration : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the angle incremented in a plane by a segment connecting an object and a reference point per unit time
     */
    @Serializable
    data object AngularVelocity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the extent of a surface
     */
    @Serializable
    data object Area : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit area
     */
    @Serializable
    data object AreaDensity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per time
     */
    @Serializable
    data object CatalysticActivity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass per unit of volume
     */
    @Serializable
    data object Density : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the resistance of an incompressible fluid to stress
     */
    @Serializable
    data object DynamicViscosity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the stored charge per unit electric potential
     */
    @Serializable
    data object ElectricCapacitance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the force per unit electric field strength
     */
    @Serializable
    data object ElectricCharge : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing how easily current flows through a material
     */
    @Serializable
    data object ElectricConductance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of flow of electrical charge per unit time
     */
    @Serializable
    data object ElectricCurrent : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the magnetic flux generated per unit current through a circuit
     */
    @Serializable
    data object ElectricInductance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the electric potential per unit electric current
     */
    @Serializable
    data object ElectricResistance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy
     */
    @Serializable
    data object Energy : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the transfer of momentum per unit time
     */
    @Serializable
    data object Force : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of (periodic) occurrences per unit time
     */
    @Serializable
    data object Frequency : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy per unit temperature change
     */
    @Serializable
    data object HeatCapacity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing wavelength-weighted luminous flux per unit surface area
     */
    @Serializable
    data object Illuminance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ionizing radiation energy absorbed by biological tissue per unit mass
     */
    @Serializable
    data object IonizingRadiationAbsorbedDose : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the received radiation adjusted for the effect on biological tissue
     */
    @Serializable
    data object IonizingRadiationEquivalentDose : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing mass per unit of length
     */
    @Serializable
    data object LinearMassDensity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing change of acceleration per unit time
     */
    @Serializable
    data object Jolt : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing ratio of the dynamic viscosity over the density of the fluid
     */
    @Serializable
    data object KinematicViscosity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the one-dimensional extent of an object
     */
    @Serializable
    data object Length : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing luminous intensity per unit area of light
     */
    @Serializable
    data object Luminance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived energy of light
     */
    @Serializable
    data object LuminousEnergy : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of light per unit area
     */
    @Serializable
    data object LuminousExposure : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the perceived power of a light source
     */
    @Serializable
    data object LuminousFlux : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the wavelength-weighted power of emitted light per unit solid angle
     */
    @Serializable
    data object LuminousIntensity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the mass of a substance which passes per unit of time
     */
    @Serializable
    data object MassFlowRate : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the measure of magnetism, taking account of the strength and the extent of a magnetic field
     */
    @Serializable
    data object MagneticFlux : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the production of an electromotive force across an electrical conductor in a changing magnetic field
     */
    @Serializable
    data object MagneticInduction : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of mass
     */
    @Serializable
    data object Molality : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of substance per unit of volume
     */
    @Serializable
    data object Molarity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the amount of energy present in a system per unit amount of substance
     */
    @Serializable
    data object MolarEnergy : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio between the mass and the amount of substance
     */
    @Serializable
    data object MolarMass : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of the volume occupied by a substance to the amount of substance
     */
    @Serializable
    data object MolarVolume : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the product of an object's mass and velocity
     */
    @Serializable
    data object Momentum : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of transfer of energy per unit time
     */
    @Serializable
    data object Power : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing force per unit area
     */
    @Serializable
    data object Pressure : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the number of particles decaying per unit time
     */
    @Serializable
    data object Radioactivity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ratio of area on a sphere to its radius squared
     */
    @Serializable
    data object SolidAngle : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy density per unit mass
     */
    @Serializable
    data object SpecificEnergy : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing heat capacity per unit mass
     */
    @Serializable
    data object SpecificHeatCapacity : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing volume per unit mass
     */
    @Serializable
    data object SpecificVolume : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the moved distance per unit time
     */
    @Serializable
    data object Speed : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing energy change per unit change in surface area
     */
    @Serializable
    data object SurfaceTension : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the average kinetic energy per degree of freedom of a system
     */
    @Serializable
    data object Temperature : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the ease with which an object resists conduction of heat
     */
    @Serializable
    data object ThermalResistance : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the duration of an event
     */
    @Serializable
    data object Time : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the energy required to move a unit charge through an electric field from a reference point
     */
    @Serializable
    data object Voltage : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the three dimensional extent of an object
     */
    @Serializable
    data object Volume : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of volume with respect to time
     */
    @Serializable
    data object VolumetricFlow : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of volume flow across a unit area
     */
    @Serializable
    data object VolumetricFlux : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing a measure of resistance to acceleration
     */
    @Serializable
    data object Weight : PhysicalQuantityWithDimension()

    /**
     * A [PhysicalQuantityWithDimension] representing the rate of change of force per unit time
     */
    @Serializable
    data object Yank : PhysicalQuantityWithDimension()
}
