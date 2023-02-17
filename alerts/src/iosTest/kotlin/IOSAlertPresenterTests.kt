package com.splendo.kaluga.alerts

import platform.UIKit.UIViewController

class IOSAlertPresenterTests : AlertPresenterTests() {

    override val builder get() = AlertPresenter.Builder(UIViewController(null, null))
}
