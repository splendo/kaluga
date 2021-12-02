/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CompletableDeferred

class EnableLocationActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CALLBACK_ID = "EXTRA_CALLBACK_ID"
        val enablingHandlers: MutableMap<String, CompletableDeferred<Boolean>> = mutableMapOf()

        fun showEnableLocationActivity(context: Context, identifier: String): CompletableDeferred<Boolean> {
            val intent = Intent(context, EnableLocationActivity::class.java).apply {
                putExtra(EXTRA_CALLBACK_ID, identifier)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val completableDeferred = enablingHandlers.getOrPut(identifier) { CompletableDeferred() }
            context.startActivity(intent)
            return completableDeferred
        }
    }

    private val enableResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        complete(result.resultCode == Activity.RESULT_OK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // windowActionBar=false
        supportActionBar?.elevation = 0F // windowContentOverlay=@null

        enableResult.launch(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun complete(success: Boolean) {
        intent.getStringExtra(EXTRA_CALLBACK_ID)?.let {
            enablingHandlers[it]?.complete(success)
            enablingHandlers.remove(it)
        }
        finish()
    }
}
