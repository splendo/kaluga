package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertBuilder
import platform.UIKit.UIViewController

class IOSAlertsInterfaceTests : AlertsInterfaceTests() {

    override val builder get() = AlertBuilder(UIViewController(null, null))
}
