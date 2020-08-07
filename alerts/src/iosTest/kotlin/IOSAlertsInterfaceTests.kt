package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertInterface
import platform.UIKit.UIViewController

class IOSAlertsInterfaceTests : AlertsInterfaceTests() {

    override val builder get() = AlertInterface.Builder(UIViewController(null, null))
}
