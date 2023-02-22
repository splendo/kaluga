# Links

Module used to decode an object from either an App Link, Universal Link or Deep Link's query.

## Installing
This library is available on Maven Central. You can import Kaluga Links as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.links:$kalugaVersion")
}
```

### Usage
This library can be used to process an incoming URL into an object. This is useful when handling an App Link, Universal Link or Deep Link

```kotlin
val linksManager = DefaultLinksManager.Builder().create()
val someClassOrNull = linksManager.validateLink(url)?.let {
    linksManager.handleIncomingLink(it, SomeClass.serializer)
}
```

The library parses the query parameters of a URL into a list of objects and decodes them into an object using a serializer.
This means query parameters should be in the order at which the Serializer specifies them.
When the object contains nested properties, they should be preceded by a numeric indicator of the amount of elements to expect:

```kotlin
@Serializable 
data class Aliment(val name: String)

@Serializable 
data class Recipe(val name: String, val ingredients: List<Aliment>)

// Somewhere in the code
val ur = "https://kaluga.splendo.com/?name=Carbonara&size=3&ingredients=Spaghetti&ingredients=Bacon&ingredients=Egg"

```

The names of the parameters are ignored when decoding.
