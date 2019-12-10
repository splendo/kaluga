package com.splendo.kaluga.bluetooth

actual data class UUID(val uuid: java.util.UUID) : BaseUUID {

    override val uuidString: String
        get() = uuid.toString()

    override val isValid: Boolean
        get() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UUID

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }


}