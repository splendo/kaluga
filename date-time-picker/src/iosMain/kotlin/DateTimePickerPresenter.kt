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
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.CoroutineScope
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UILabel
import platform.UIKit.UIModalPresentationBlurOverFullScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.insertSubview
import platform.UIKit.leadingAnchor
import platform.UIKit.systemBackgroundColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.objc.sel_registerName

actual class DateTimePickerPresenter(
    private val datePicker: DateTimePicker,
    private val parent: UIViewController
) : BaseDateTimePickerPresenter(datePicker) {

    private inner class DateTimePickerViewController(private val datePicker: DateTimePicker, private val completion: (KalugaDate?) -> Unit) : UIViewController(null, null) {

        private lateinit var datePickerView: UIDatePicker

        init {
            this.modalPresentationStyle = UIModalPresentationBlurOverFullScreen
        }

        override fun viewDidLoad() {
            super.viewDidLoad()
            val containerView = UIView().apply {
                backgroundColor = UIColor.systemBackgroundColor
                layer.cornerRadius = 15.0 as CGFloat
                view.addSubview(this)
                translatesAutoresizingMaskIntoConstraints = false
                centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).active = true
                centerYAnchor.constraintEqualToAnchor(view.centerYAnchor).active = true
                leadingAnchor.constraintGreaterThanOrEqualToAnchor(view.leadingAnchor, 20.0 as CGFloat).active = true
                trailingAnchor.constraintLessThanOrEqualToAnchor(view.trailingAnchor, -20.0 as CGFloat).active = true
            }

            datePickerView = UIDatePicker().apply {
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
                when {
                    IOSVersion.systemVersion >= IOSVersion(14, 0, 0) -> {
                        preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleInline
                    }
                    IOSVersion.systemVersion >= IOSVersion(13, 4, 0) -> {
                        preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
                    }
                }

                containerView.addSubview(this)
                translatesAutoresizingMaskIntoConstraints = false
                leadingAnchor.constraintEqualToAnchor(containerView.leadingAnchor, 10.0 as CGFloat).active = true
                trailingAnchor.constraintEqualToAnchor(containerView.trailingAnchor, -10.0 as CGFloat).active = true
                bottomAnchor.constraintEqualToAnchor(containerView.bottomAnchor, -10.0 as CGFloat).active = true
            }

            val topAnchor = datePicker.message?.let {
                UILabel().apply {
                    text = it
                    containerView.addSubview(this)
                    translatesAutoresizingMaskIntoConstraints = false
                    bottomAnchor.constraintEqualToAnchor(
                        datePickerView.topAnchor,
                        -15.0 as CGFloat
                    ).active = true
                    containerView.leadingAnchor.constraintEqualToAnchor(
                        leadingAnchor,
                        -20.0 as CGFloat
                    ).active = true
                    containerView.trailingAnchor.constraintEqualToAnchor(
                        trailingAnchor,
                        20.0 as CGFloat
                    ).active = true
                    bottomAnchor
                }.topAnchor
            } ?: datePickerView.topAnchor

            topAnchor.constraintEqualToAnchor(containerView.topAnchor, 10.0 as CGFloat).active = true

            UIButton().apply {
                addTarget(this@DateTimePickerViewController, sel_registerName("onSelected"), UIControlEventTouchUpInside)
                view.insertSubview(this, 0)
                translatesAutoresizingMaskIntoConstraints = false
                view.topAnchor.constraintEqualToAnchor(this.topAnchor).active = true
                view.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
                view.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
                view.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            }
        }

        @ObjCAction
        private fun onSelected() {
            completion(DefaultKalugaDate.epoch((datePickerView.date.timeIntervalSince1970 * 1000.0).toLong(), datePicker.selectedDate.timeZone, datePicker.locale))
            dismissDateTimePicker(true)
        }
    }

    actual class Builder(private val viewController: UIViewController) : BaseDateTimePickerPresenter.Builder() {
        actual override fun create(coroutineScope: CoroutineScope) =
            DateTimePickerPresenter(createDateTimePicker(), viewController)
    }

    override fun dismissDateTimePicker(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }

    override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        parent.presentViewController(DateTimePickerViewController(datePicker, completion), animated, null)
    }
}
