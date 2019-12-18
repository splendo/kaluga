package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

abstract class BaseDescriptor(initialValue: ByteArray? = null, stateRepoAccessor: StateRepoAccesor<DeviceState>) : Attribute<DeviceAction.Read.Descriptor, DeviceAction.Write.Descriptor>(initialValue, stateRepoAccessor)

expect class Descriptor : BaseDescriptor