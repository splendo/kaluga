package com.splendo.kaluga.bluetooth

interface BaseService {
    val uuid: UUID
    val characteristics: List<Characteristic>
}

expect class Service : BaseService