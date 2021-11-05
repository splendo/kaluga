## Scientific
This Scientific Library for Kaluga contains methods for converting values to and from different scientific units.

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
    implementation("com.splendo.kaluga:scientific:$kalugaVersion")
}
```

## Usage

### Scientific Unit

This library contains a `ScientificUnit` sealed class that describes units according to a `PhysicalQuantity` and `MeasurementSystem`. Units can convert Number or Decimal values to a value of another unit with the same PhysicalQuantity:

```kotlin
val meterInFoot = Meter.convert(10, Foot)
```

There are 59 `PhysicalQuantities` supported by this library. Each has a equaly named sealed class that contains units that measure the given quantity. Depending on the quantity, this may be further split into sealed classes that contain only units used in a `MeasurementSystem` (e.g. `Metric`, `USCustomary`).

See [PhysicalQuantity.kt](src/commonMain/kotlin/PhysicalQuantity.kt) for a list of all supported quantities. This library supports all seven [base quantities](https://en.wikipedia.org/wiki/Physical_quantity#Base_quantities) as well as a large set of [derived quantities](https://en.wikipedia.org/wiki/SI_derived_unit).

Not all units are named. Some can be derived from using other units using either the `x` or `per` operator, depending on whether the unit is derived by multiplying or dividing other units (and has no named alternatives):

```kotlin
val speed = Meter per Second
val momentum = Kilogram x speed
```

### Scientific Value

The ``ScientificValue` interface can be used to mark a `Number` as a value of a `ScientificUnit`. A `DefaultScientificUnit` can be easily created using its invoke operator: `10(Meter)`. Alternatively, create a custom ScientificValue implementation and pass a construction function in the invoke method.

Convert a ScientificValue to another value of the same `PhysicalQuantity` using `convert`: `5(Meter).convert(Foot)`

#### Calculations

`ScientificValue` objects can be modified with numbers or other `ScientificValue` objects. All values have a `plus`, `minus`, `times`, and `div` function. By default this results in a new `DefaultScientificUnit` but this behaviour can be overloaded using custom parameters.

In addition, some units may be multiplied or divided by other units depending on how they are defined. For instance, you can create a Force value by multiplying a `Weight` and `Acceleration` unit. The unit of the returned ScientificValue will be determined based on the input. Calculating with values in the Imperial system generally returns an imperial unit, while using CGS-units will usually return a CGS unit. Alternatively the unit can be explicitly defined by using the creation method (usually named after the `PhysicalQuantity`).

```kotlin
val weight = 10(Kilogram)
val acceleration = 3(Meter per Second per Second)
val force = weight * acceleration // Returns in Newton
val dyneForce = weight.convert(Gram) * acceleration.convert(Centimeter per Second per Second) // Returns in Dyne
val poundForce = PoundForce.force(weight, acceleration) // Returns in PoundForce even though constructing units are in metric
```

### Scientific Array

The `ScientificArray` allows to mark a list of Numbers as values of a `ScientificUnit`. A `DefaultScientificArray` can be easily created using `listOf(1, 2, 3)(Meter)`. Alternatively, create a custom ScientificArray implementation and pass a construction function in the invoke method. Since `ScientificArray` expects all values to be of the same number type, convenience interfaces are created: `DoubleScientificArray`, `IntScientificArray`, `FloatScientificArray`, `LongScientificArray`, `ShortScientificArray`, and `ByteScientificArray`.

A list of `ScientificValue` of the same `PhysicalQuantity` can also be merged using the `toScientificArray()` method.

#### Map, Combine, Concat, and Split

ScientificArray can be modied using some methods:

- `map` takes all values, converts them to a `ScientificUnit` and transforms these using a transformation method, returning a ScientificArray of all resulting units.
- `combine` takes two ScientificArrays of equal size and combines each value into a new ScientificArray
- `concat` creates a new ScientificArray out of two ScientificArray of the same `PhysiscalQuantity`.
- `split` splits a ScientificArray into `ScientificUnit` for each value.