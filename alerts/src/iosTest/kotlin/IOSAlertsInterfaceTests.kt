package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertPresenter
import platform.UIKit.UIViewController

class IOSAlertsInterfaceTests : AlertsInterfaceTests() {

    override val builder get() = AlertPresenter.Builder(UIViewController(null, null))
}
