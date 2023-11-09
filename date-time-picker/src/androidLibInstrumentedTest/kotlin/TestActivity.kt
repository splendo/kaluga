package com.splendo.kaluga.datetimepicker

import android.os.Bundle
import androidx.activity.viewModels
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.test.datetimepicker.datePickerPresenterBuilder

class TestActivity : KalugaViewModelActivity<DateTimePickerViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
    }
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
