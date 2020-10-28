package com.splendo.kaluga.test

import com.splendo.kaluga.alerts.AlertPresenter
import platform.UIKit.UIViewController

class IOSAlertsPresenterTests : AlertsPresenterTests() {

    override val builder get() = AlertPresenter.Builder(UIViewController(null, null))
}
