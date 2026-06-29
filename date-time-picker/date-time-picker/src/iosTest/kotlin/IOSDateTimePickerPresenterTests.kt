package com.splendo.kaluga.test

import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import platform.UIKit.UIViewController

class IOSDateTimePickerPresenterTests : DateTimePickerPresenterTests() {

    override val builder get() = DateTimePickerPresenter.Builder(UIViewController(null, null))
}
