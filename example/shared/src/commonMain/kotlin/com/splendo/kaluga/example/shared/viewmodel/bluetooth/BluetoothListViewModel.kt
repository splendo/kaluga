package com.splendo.kaluga.example.shared.viewmodel.bluetooth

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.resources.localized

sealed class BluetoothListNavigationAction : NavigationAction<Nothing>(null) {

    data object Server : BluetoothListNavigationAction()
    data object Client : BluetoothListNavigationAction()
}

enum class Bluetooth(private val titleKey: String) {
    SERVER("feature_bluetooth_server"),
    CLIENT("feature_bluetooth_client"),
    ;

    val title: String get() = titleKey.localized()
}

class BluetoothListViewModel(navigator: Navigator<BluetoothListNavigationAction>) : NavigatingViewModel<BluetoothListNavigationAction>(navigator) {

    val bluetooth = observableOf(Bluetooth.entries)

    fun onBluetoothSelected(bluetooth: Bluetooth) {
        navigator.navigate(
            when (bluetooth) {
                Bluetooth.SERVER -> BluetoothListNavigationAction.Server
                Bluetooth.CLIENT -> BluetoothListNavigationAction.Client
            },
        )
    }
}
