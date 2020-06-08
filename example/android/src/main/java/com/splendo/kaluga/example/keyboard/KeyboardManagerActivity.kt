package com.splendo.kaluga.example.keyboard

import android.os.Bundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.keyboard.KeyboardManagerBuilder
import kotlinx.android.synthetic.main.activity_keyboard_manager.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class KeyboardManagerActivity : KalugaViewModelActivity<KeyboardViewModel>(R.layout.activity_keyboard_manager) {

    override val viewModel: KeyboardViewModel by viewModel { parametersOf({KeyboardManagerBuilder(this)}, {edit_field}) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_show_keyboard.setOnClickListener { viewModel.onShowPressed() }
        btn_hide_keyboard.setOnClickListener { viewModel.onHidePressed() }
    }

}