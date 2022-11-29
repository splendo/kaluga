package com.splendo.kaluga.example.shared.viewmodel.datetimepicker

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import com.splendo.kaluga.datetimepicker.buildDatePicker
import com.splendo.kaluga.datetimepicker.buildTimePicker
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DateTimePickerViewModel(val dateTimePickerPresenterBuilder: DateTimePickerPresenter.Builder) : BaseLifecycleViewModel() {

    companion object {
        private val formatter = KalugaDateFormatter.dateTimeFormat(DateFormatStyle.Long, DateFormatStyle.Long)
    }

    private val selectedDate = MutableStateFlow(
        DefaultKalugaDate.now().apply {
            second = 0
            millisecond = 0
        }
    )
    val currentTimeTitle = "current_time".localized()
    val dateLabel = selectedDate.map { formatter.format(it) }.toInitializedObservable("", coroutineScope)

    val selectDateButton = KalugaButton.Plain("select_date".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            dateTimePickerPresenterBuilder.buildDatePicker(
                this,
                DefaultKalugaDate.epoch(),
                DefaultKalugaDate.now()
            ) {
                setSelectedDate(this@DateTimePickerViewModel.selectedDate.value)
                setCancelButtonTitle("cancel_selection".localized())
                setConfirmButtonTitle("confirm_selection".localized())
            }.show()?.let {
                selectedDate.value = it
            }
        }
    }

    val selectTimeButton = KalugaButton.Plain("select_time".localized(), ButtonStyles.default) {
        coroutineScope.launch {
            dateTimePickerPresenterBuilder.buildTimePicker(this) {
                setSelectedDate(this@DateTimePickerViewModel.selectedDate.value)
                setCancelButtonTitle("cancel_selection".localized())
                setConfirmButtonTitle("confirm_selection".localized())
            }.show()?.let {
                selectedDate.value = it
            }
        }
    }
}
