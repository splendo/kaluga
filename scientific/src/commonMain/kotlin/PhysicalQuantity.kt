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
    object Dimensionless : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of change of velocity per unit time
     */
    @Serializable
    object Acceleration : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing momentum of particle multiplied by distance travelled
     */
    @Serializable
    object Action : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the quantity proportional to the number of particles in a sample
     */
    @Serializable
    object AmountOfSubstance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the ratio of circular arc length to radius
     */
    @Serializable
    object Angle : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing change in angular velocity per unit time
     */
    @Serializable
    object AngularAcceleration : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the angle incremented in a plane by a segment connecting an object and a reference point per unit time
     */
    @Serializable
    object AngularVelocity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the extent of a surface
     */
    @Serializable
    object Area : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the mass per unit area
     */
    @Serializable
    object AreaDensity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the amount of substance per time
     */
    @Serializable
    object CatalysticActivity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the mass per unit of volume
     */
    @Serializable
    object Density : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the resistance of an incompressible fluid to stress
     */
    @Serializable
    object DynamicViscosity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the stored charge per unit electric potential
     */
    @Serializable
    object ElectricCapacitance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the force per unit electric field strength
     */
    @Serializable
    object ElectricCharge : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing how easily current flows through a material
     */
    @Serializable
    object ElectricConductance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of flow of electrical charge per unit time
     */
    @Serializable
    object ElectricCurrent : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the magnetic flux generated per unit current through a circuit
     */
    @Serializable
    object ElectricInductance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the electric potential per unit electric current
     */
    @Serializable
    object ElectricResistance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing energy
     */
    @Serializable
    object Energy : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the transfer of momentum per unit time
     */
    @Serializable
    object Force : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the number of (periodic) occurrences per unit time
     */
    @Serializable
    object Frequency : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing energy per unit temperature change
     */
    @Serializable
    object HeatCapacity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing wavelength-weighted luminous flux per unit surface area
     */
    @Serializable
    object Illuminance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing ionizing radiation energy absorbed by biological tissue per unit mass
     */
    @Serializable
    object IonizingRadiationAbsorbedDose : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the received radiation adjusted for the effect on biological tissue
     */
    @Serializable
    object IonizingRadiationEquivalentDose : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing mass per unit of length
     */
    @Serializable
    object LinearMassDensity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing change of acceleration per unit time
     */
    @Serializable
    object Jolt : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing ratio of the dynamic viscosity over the density of the fluid
     */
    @Serializable
    object KinematicViscosity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the one-dimensional extent of an object
     */
    @Serializable
    object Length : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing luminous intensity per unit area of light
     */
    @Serializable
    object Luminance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the perceived energy of light
     */
    @Serializable
    object LuminousEnergy : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the amount of light per unit area
     */
    @Serializable
    object LuminousExposure : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the perceived power of a light source
     */
    @Serializable
    object LuminousFlux : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the wavelength-weighted power of emitted light per unit solid angle
     */
    @Serializable
    object LuminousIntensity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the mass of a substance which passes per unit of time
     */
    @Serializable
    object MassFlowRate : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the measure of magnetism, taking account of the strength and the extent of a magnetic field
     */
    @Serializable
    object MagneticFlux : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the production of an electromotive force across an electrical conductor in a changing magnetic field
     */
    @Serializable
    object MagneticInduction : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the amount of substance per unit of mass
     */
    @Serializable
    object Molality : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the amount of substance per unit of volume
     */
    @Serializable
    object Molarity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the amount of energy present in a system per unit amount of substance
     */
    @Serializable
    object MolarEnergy : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the ratio between the mass and the amount of substance
     */
    @Serializable
    object MolarMass : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the ratio of the volume occupied by a substance to the amount of substance
     */
    @Serializable
    object MolarVolume : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the product of an object's mass and velocity
     */
    @Serializable
    object Momentum : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of transfer of energy per unit time
     */
    @Serializable
    object Power : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing force per unit area
     */
    @Serializable
    object Pressure : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the number of particles decaying per unit time
     */
    @Serializable
    object Radioactivity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the ratio of area on a sphere to its radius squared
     */
    @Serializable
    object SolidAngle : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing energy density per unit mass
     */
    @Serializable
    object SpecificEnergy : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing heat capacity per unit mass
     */
    @Serializable
    object SpecificHeatCapacity : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing volume per unit mass
     */
    @Serializable
    object SpecificVolume : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the moved distance per unit time
     */
    @Serializable
    object Speed : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing energy change per unit change in surface area
     */
    @Serializable
    object SurfaceTension : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the average kinetic energy per degree of freedom of a system
     */
    @Serializable
    object Temperature : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the ease with which an object resists conduction of heat
     */
    @Serializable
    object ThermalResistance : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the duration of an event
     */
    @Serializable
    object Time : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the energy required to move a unit charge through an electric field from a reference point
     */
    @Serializable
    object Voltage : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the three dimensional extent of an object
     */
    @Serializable
    object Volume : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of change of volume with respect to time
     */
    @Serializable
    object VolumetricFlow : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of volume flow across a unit area
     */
    @Serializable
    object VolumetricFlux : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing a measure of resistance to acceleration
     */
    @Serializable
    object Weight : PhysicalQuantity()

    /**
     * A [PhysicalQuantity] representing the rate of change of force per unit time
     */
    @Serializable
    object Yank : PhysicalQuantity()
}
