package com.splendo.kaluga.datetimepicker

import androidx.activity.viewModels
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity

class TestActivity : KalugaViewModelActivity<DateTimePickerViewModel>() {

    override val viewModel: DateTimePickerViewModel by viewModels()

    suspend fun showAlert() {
        alertPresenterBuilder().buildAlert(viewModel.coroutineScope) {
            setTitle("Activity")
            setMessage("Activity")
            setPositiveButton("OK")
        }.show(false)
    }
}
