# Scientific
This Extension of the Scientific Library for Kaluga contains methods for combining Scientific values into new values.

## Installing
This library is available on Maven Central. You can import Kaluga Scientific as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:scientific-converters:$kalugaVersion")
}
```

## Usage

Units may be multiplied or divided by other units depending on how they are defined. For instance, you can create a Force value by multiplying a `Weight` and `Acceleration` unit. The unit of the returned ScientificValue will be determined based on the input. Calculating with values in the Imperial system generally returns an imperial unit, while using CGS-units will usually return a CGS unit. Alternatively the unit can be explicitly defined by using the creation method (usually named after the `PhysicalQuantity`).

```kotlin
val weight = 10(Kilogram)
val acceleration = 3(Meter per Second per Second)
val force = weight * acceleration // Returns in Newton
val dyneForce = weight.convert(Gram) * acceleration.convert(Centimeter per Second per Second) // Returns in Dyne
val poundForce = PoundForce.force(weight, acceleration) // Returns in PoundForce even though constructing units are in metric
```

For custom Scientific units, currently no operators exist due to compiler limitations. 
These units can be converted through the usages of one of the `multipliedBy`/`dividedBy` methods. 
To get properly simplified results, ensure that the right method is called. E.g. a custom multiplicationUnit multiplied by a dividing unit containing its left value as a numerator would be called using:

`com.splendo.kaluga.scientific.converter.undefined.multiplying.multipliedByDividingUnitWithLeftAsNumerator`
  