/*

Copyright 2020 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.splendo.kaluga.datetimepicker

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.base.utils.Date
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UILabel
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DateTimePickerPresenter(
    private val datePicker: DateTimePicker,
    private val parent: UIViewController
) : BaseDateTimePickerPresenter(datePicker) {

    actual class Builder(private val viewController: UIViewController) : BaseDateTimePickerPresenter.Builder() {
        actual override fun create(coroutineScope: CoroutineScope) =
            DateTimePickerPresenter(createDateTimePicker(), viewController)
    }

    override fun dismissDateTimePicker(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }

    override fun showDateTimePicker(animated: Boolean, completion: (Date?) -> Unit) {
        UIAlertController.alertControllerWithTitle(null, null, UIAlertControllerStyleActionSheet).apply {
            val datePickerView = UIDatePicker().apply {
                calendar = NSCalendar.currentCalendar.apply {
                    this.locale = datePicker.locale.nsLocale
                    this.timeZone = datePicker.selectedDate.timeZone.timeZone
                }
                date = NSDate.dateWithTimeIntervalSince1970((datePicker.selectedDate.millisecondSinceEpoch.toDouble() / 1000.0) as NSTimeInterval)
                locale = datePicker.locale.nsLocale
                timeZone = datePicker.selectedDate.timeZone.timeZone
                datePickerMode = when (val type = datePicker.type) {
                    is DateTimePicker.Type.TimeType -> UIDatePickerMode.UIDatePickerModeTime
                    is DateTimePicker.Type.DateType -> {
                        type.latestDate?.let {
                            maximumDate = NSDate.dateWithTimeIntervalSince1970((it.millisecondSinceEpoch.toDouble() / 1000.0) as NSTimeInterval)
                        }
                        type.earliestDate?.let {
                            minimumDate = NSDate.dateWithTimeIntervalSince1970((it.millisecondSinceEpoch.toDouble() / 1000.0) as NSTimeInterval)
                        }
                        UIDatePickerMode.UIDatePickerModeDate
                    }
                }
                if (IOSVersion.systemVersion >= IOSVersion(13, 4, 0)) {
                    // TODO: When Moving to Kotlin 1.4.20 we should pass UIDatePickerStyleInline for iOS 14+
                    preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
                }
            }
            view.addSubview(datePickerView)
            addAction(UIAlertAction.actionWithTitle(datePicker.cancelButtonTitle, UIAlertActionStyleCancel) { completion(null) })
            addAction(
                UIAlertAction.actionWithTitle(datePicker.confirmButtonTitle, UIAlertActionStyleDefault) {
                    completion(Date.epoch((datePickerView.date.timeIntervalSince1970 * 1000.0).toLong(), datePicker.selectedDate.timeZone, datePicker.locale))
                }
            )
            val anchor = datePicker.message?.let {
                val label = UILabel()
                label.text = it
                view.addSubview(label)
                label.translatesAutoresizingMaskIntoConstraints = false
                label.topAnchor.constraintEqualToAnchor(view.topAnchor, 15.0).active = true
                label.bottomAnchor.constraintEqualToAnchor(datePickerView.topAnchor, -15.0).active = true
                label.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor, 20.0).active = true
                label.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor, -20.0).active = true
                label.bottomAnchor
            } ?: view.topAnchor
            datePickerView.translatesAutoresizingMaskIntoConstraints = false
            datePickerView.topAnchor.constraintEqualToAnchor(anchor, 15.0).active = true
            datePickerView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor, -120.0).active = true
            datePickerView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor, 0.0).active = true
            datePickerView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor, 0.0).active = true

            view.translatesAutoresizingMaskIntoConstraints = false
        }.run {
            parent.presentViewController(this, animated, null)
        }
    }
}
