package com.splendo.kaluga.alerts

import androidx.activity.viewModels
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.test.alerts.alertPresenterBuilder

class TestActivity : KalugaViewModelActivity<AlertsViewModel>() {

    override val viewModel: AlertsViewModel by viewModels()

    suspend fun showAlert() {
        alertPresenterBuilder().buildAlert(viewModel.coroutineScope) {
            setTitle("Activity")
            setMessage("Activity")
            setPositiveButton("OK")
        }.show(false)
    }
}
