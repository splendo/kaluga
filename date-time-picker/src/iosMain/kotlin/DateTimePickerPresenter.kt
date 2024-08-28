/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
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
import platform.UIKit.systemBackgroundColor
import platform.objc.sel_registerName
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * A [BaseDateTimePickerPresenter] for presenting a [DateTimePicker]
 * @param dateTimePicker the [DateTimePicker] to present
 * @param parent The [UIViewController] to present the [DateTimePicker]
 */
actual class DateTimePickerPresenter(dateTimePicker: DateTimePicker, private val parent: UIViewController) : BaseDateTimePickerPresenter(dateTimePicker) {

    /**
     * A [BaseDateTimePickerPresenter.Builder] for creating a [DateTimePickerPresenter]
     * @param viewController The [UIViewController] to present any [DateTimePickerPresenter] built using this builder.
     */
    actual class Builder(private val viewController: UIViewController) : BaseDateTimePickerPresenter.Builder() {

        /**
         * Creates a [DateTimePickerPresenter]
         *
         * @param dateTimePicker The [DateTimePicker] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [DateTimePickerPresenter]
         */
        actual override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope) = DateTimePickerPresenter(dateTimePicker, viewController)
    }

    private inner class DateTimePickerViewController(private val completion: (KalugaDate?) -> Unit) : UIViewController(null, null) {

        private lateinit var datePickerView: UIDatePicker

        init {
            this.modalPresentationStyle = UIModalPresentationBlurOverFullScreen
        }

        override fun viewDidLoad() {
            super.viewDidLoad()
            val containerView = UIView().apply {
                backgroundColor = UIColor.systemBackgroundColor
                layer.cornerRadius = 15.0
                view.addSubview(this)
                translatesAutoresizingMaskIntoConstraints = false
                centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).active = true
                centerYAnchor.constraintEqualToAnchor(view.centerYAnchor).active = true
                leadingAnchor.constraintGreaterThanOrEqualToAnchor(view.leadingAnchor, 20.0).active = true
                trailingAnchor.constraintLessThanOrEqualToAnchor(view.trailingAnchor, -20.0).active = true
            }

            datePickerView = UIDatePicker().apply {
                calendar = NSCalendar.currentCalendar.apply {
                    this.locale = this@DateTimePickerPresenter.dateTimePicker.locale.nsLocale
                    this.timeZone = this@DateTimePickerPresenter.dateTimePicker.selectedDate.timeZone.timeZone
                }
                date = NSDate.dateWithTimeIntervalSince1970(this@DateTimePickerPresenter.dateTimePicker.selectedDate.durationSinceEpoch.toDouble(DurationUnit.SECONDS))
                locale = this@DateTimePickerPresenter.dateTimePicker.locale.nsLocale
                timeZone = this@DateTimePickerPresenter.dateTimePicker.selectedDate.timeZone.timeZone
                datePickerMode = when (val type = this@DateTimePickerPresenter.dateTimePicker.type) {
                    is DateTimePicker.Type.TimeType -> UIDatePickerMode.UIDatePickerModeTime
                    is DateTimePicker.Type.DateType -> {
                        type.latestDate?.let {
                            maximumDate = NSDate.dateWithTimeIntervalSince1970(it.durationSinceEpoch.toDouble(DurationUnit.SECONDS))
                        }
                        type.earliestDate?.let {
                            minimumDate = NSDate.dateWithTimeIntervalSince1970(it.durationSinceEpoch.toDouble(DurationUnit.SECONDS))
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
                leadingAnchor.constraintEqualToAnchor(containerView.leadingAnchor, 10.0).active = true
                trailingAnchor.constraintEqualToAnchor(containerView.trailingAnchor, -10.0).active = true
                bottomAnchor.constraintEqualToAnchor(containerView.bottomAnchor, -10.0).active = true
            }

            val topAnchor = this@DateTimePickerPresenter.dateTimePicker.message?.let {
                UILabel().apply {
                    text = it
                    containerView.addSubview(this)
                    translatesAutoresizingMaskIntoConstraints = false
                    bottomAnchor.constraintEqualToAnchor(
                        datePickerView.topAnchor,
                        -15.0,
                    ).active = true
                    containerView.leadingAnchor.constraintEqualToAnchor(
                        leadingAnchor,
                        -20.0,
                    ).active = true
                    containerView.trailingAnchor.constraintEqualToAnchor(
                        trailingAnchor,
                        20.0,
                    ).active = true
                    bottomAnchor
                }.topAnchor
            } ?: datePickerView.topAnchor

            topAnchor.constraintEqualToAnchor(containerView.topAnchor, 10.0).active = true

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
            completion(
                DefaultKalugaDate.epoch(
                    datePickerView.date.timeIntervalSince1970.seconds,
                    this@DateTimePickerPresenter.dateTimePicker.selectedDate.timeZone,
                    this@DateTimePickerPresenter.dateTimePicker.locale,
                ),
            )
            dismissDateTimePicker(true)
        }
    }

    actual override fun dismissDateTimePicker(animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }

    actual override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        parent.presentViewController(DateTimePickerViewController(completion), animated, null)
    }
}
