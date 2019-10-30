package com.splendo.kaluga.example.loading

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.splendo.kaluga.example.R
import com.splendo.kaluga.loadingIndicator.AndroidLoadingIndicator
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

@SuppressLint("SetTextI18n")
class LoadingActivity : AppCompatActivity(R.layout.activity_loading) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_show_loading_indicator.setOnClickListener {
            showLoadingIndicator()
        }
    }

    private fun showLoadingIndicator() {
        val dialog = AlertDialog
            .Builder(this)
            .setMessage("Loading...")
            .setCancelable(false)
            .create()
        val indicator = AndroidLoadingIndicator
            .Builder()
            .setView(dialog)
            .create()
            .present()
        GlobalScope.launch {
            delay(3000)
            indicator.dismiss()
        }
    }
}
