package com.splendo.kaluga.example.keyboardManager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.keyboardManager.KeyboardInterface
import com.splendo.kaluga.keyboardManager.KeyboardManagerBuilder
import com.splendo.kaluga.keyboardManager.KeyboardView
import kotlinx.android.synthetic.main.activity_keyboard_manager.*

class KeyboardManagerActivity : AppCompatActivity(R.layout.activity_keyboard_manager) {

    private lateinit var keyboardManager: KeyboardInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keyboardManager = KeyboardManagerBuilder(this).create()

        val keyboardView = KeyboardView(edit_field)
        btn_show_keyboard.setOnClickListener {keyboardManager.show(keyboardView)}
        btn_hide_keyboard.setOnClickListener { keyboardManager.dismiss() }
    }
}