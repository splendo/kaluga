package com.splendo.kaluga.alerts

import androidx.activity.viewModels
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity

class TestActivity : KalugaViewModelActivity<AlertsViewModel>() {

    override val viewModel: AlertsViewModel by viewModels()
}
