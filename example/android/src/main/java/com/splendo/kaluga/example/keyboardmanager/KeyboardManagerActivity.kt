package com.splendo.kaluga.example.keyboardmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.ExampleKeyboardManager
import com.splendo.kaluga.keyboardmanager.KeyboardManagerBuilder
import kotlinx.android.synthetic.main.activity_keyboard_manager.*

class KeyboardManagerActivity : AppCompatActivity(R.layout.activity_keyboard_manager) {

    private lateinit var keyboardManager: ExampleKeyboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keyboardManager = ExampleKeyboardManager(KeyboardManagerBuilder(this), edit_field)

        btn_show_keyboard.setOnClickListener {keyboardManager.show()}
        btn_hide_keyboard.setOnClickListener { keyboardManager.hide() }
    }
}