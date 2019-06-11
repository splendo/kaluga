package location

interface LocationProvider {
    fun getLocation()
}

expect open class DefaultLocationProvider: LocationProvider {

}
