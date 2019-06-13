package com.splendo.components.location

expect class DefaultLocationManager: LocationManager {
    constructor(configuration: Configuration = Configuration.default)
}
