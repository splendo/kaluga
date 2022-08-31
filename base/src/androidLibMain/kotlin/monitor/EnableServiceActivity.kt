/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.monitor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CompletableDeferred

class EnableServiceActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CALLBACK_ID = "EXTRA_CALLBACK_ID"
        private const val EXTRA_SETTING_ID = "EXTRA_CALLBACK_ID"
        val enablingHandlers: MutableMap<Pair<String, String>, CompletableDeferred<Boolean>> = mutableMapOf()

        fun showEnableServiceActivity(context: Context, identifier: String, settingsIntent: Intent): CompletableDeferred<Boolean> {
            val settingKey = settingsIntent.action.orEmpty()
            val intent = Intent(context, EnableServiceActivity::class.java).apply {
                putExtra(EXTRA_CALLBACK_ID, identifier)
                putExtra(EXTRA_SETTING_ID, settingsIntent)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val completableDeferred = enablingHandlers.getOrPut(identifier to settingKey) { CompletableDeferred() }
            context.startActivity(intent)
            return completableDeferred
        }
    }

    private val settingsIntent: Intent? get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_SETTING_ID, android.content.Intent::class.java)
    } else {
        intent.getParcelableExtra(EXTRA_SETTING_ID)
    }

    private val enableResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        complete(result.resultCode == Activity.RESULT_OK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // windowActionBar=false
        supportActionBar?.elevation = 0F // windowContentOverlay=@null

        settingsIntent?.let {
            enableResult.launch(it)
        }
    }

    private fun complete(success: Boolean) {
        val actionKey = settingsIntent?.action.orEmpty()
        intent.getStringExtra(EXTRA_CALLBACK_ID)?.let {
            enablingHandlers[it to actionKey]?.complete(success)
            enablingHandlers.remove(it to actionKey)
        }
        finish()
    }
}