package com.splendo.kaluga.bluetooth

actual data class Service(override val uuid: UUID, override val characteristics: List<Characteristic>) : BaseService