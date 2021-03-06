package com.splendo.kaluga.datetimepicker

import androidx.activity.viewModels
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity

class TestActivity : KalugaViewModelActivity<DateTimePickerViewModel>() {

    override val viewModel: DateTimePickerViewModel by viewModels()

    suspend fun showDatePicker() {
        datePickerPresenterBuilder().buildDatePicker(viewModel.coroutineScope) {
            setMessage("Activity")
            setConfirmButtonTitle("OK")
            setCancelButtonTitle("Cancel")
        }.show(false)
    }

    suspend fun showTimePicker() {
        datePickerPresenterBuilder().buildTimePicker(viewModel.coroutineScope) {
            setMessage("Activity")
            setConfirmButtonTitle("OK")
            setCancelButtonTitle("Cancel")
        }.show(false)
    }
}
