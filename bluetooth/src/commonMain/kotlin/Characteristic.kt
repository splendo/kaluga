package com.splendo.kaluga.bluetooth

interface BaseAttribute {
    val uuid: UUID
}

data class Attribute(val uuid: UUID)

interface BaseCharacteristic : BaseAttribute {
    fun getIsNotifying(): Boolean
    fun getData(): ByteArray?
}

expect class Characteristic : BaseCharacteristic

class CharacteristicCache {
    private val cache = mutableMapOf<UUID, Characteristic>()

    fun clear() {
        cache.clear()
    }

    fun cache(characteristic: Characteristic) {
        cache[characteristic.uuid] = characteristic
    }

    fun adapter(uuid: UUID): Characteristic? {
        return cache[uuid]
    }

}
