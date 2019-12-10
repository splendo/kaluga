package com.splendo.kaluga.bluetooth

actual data class UUID(override val uuidString: String) : BaseUUID {

    companion object {
        val validationRegex = "/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i".toRegex()
    }

    override val isValid: Boolean
        get() = validationRegex.matches(uuidString)
}