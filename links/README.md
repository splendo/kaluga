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

## Usage
This library can be used to process an incoming URL into an object. This is useful when handling an App Link, Universal Link or Deep Link

```kotlin
val linksManager = DefaultLinksManager.Builder().create()
val someClassOrNull = linksManager.validateLink(url)?.let {
    linksManager.handleIncomingLink(it, SomeClass.serializer)
}
```

The library parses the query parameters of a URL into a map and decodes it into an object using a serializer.
This means query parameters should correspond to the serialized field names specified by the Serializer.

```kotlin
@Serializable 
data class Ingredient(val name: String)

@Serializable 
data class Recipe(val name: String, val ingredients: List<Ingredient>)

// Somewhere in the code
val url = "https://kaluga.splendo.com/?name=Carbonara&ingredients=Spaghetti&ingredients=Bacon&ingredients=Egg"
val linksManager = DefaultLinksManager.Builder().create()
val recipe = linksManager.handleIncomingLink(url, Recipe.serializer())

```
