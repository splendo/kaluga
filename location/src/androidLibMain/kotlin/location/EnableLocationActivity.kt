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
import android.app.Instrumentation
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException


class EnableLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()//windowActionBar=false
        supportActionBar?.elevation = 0F//windowContentOverlay=@null

        val resolvableApiException = getResolvableApiException(intent)

        resolvableApiException?.let {
            try {
                startIntentSenderForResult(it.intentSender, REQUEST_CHECK_SETTINGS, null, 0, 0, 0)
            } catch (e: IntentSender.SendIntentException) {
                complete(false)
            }
        } ?: run {
            complete(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                complete(resultCode == Activity.RESULT_OK)
            }
        }
    }

    private fun complete(success: Boolean) {
        getLocationManagerId(intent)?.let {
            LocationManager.enablingHandlers[it]?.complete(false)
        }
        finish()
    }

    companion object {
        private const val EXTRA_RESOLVABLE_API_EXCEPTION = "EXTRA_RESOLVABLE_API_EXCEPTION"
        private const val EXTRA_LOCATION_MANAGER_ID = "EXTRA_LOCATION_MANAGER_ID"
        private const val REQUEST_CHECK_SETTINGS = 12568

        fun intent(context: Context, locationManagerId: String, resolvableApiException: ResolvableApiException): Intent {
            val intent = Intent(context, EnableLocationActivity::class.java)

            intent.putExtra(EXTRA_LOCATION_MANAGER_ID, locationManagerId)
            intent.putExtra(EXTRA_RESOLVABLE_API_EXCEPTION, resolvableApiException.resolution)

            return intent
        }

        fun getResolvableApiException(intent: Intent): PendingIntent? {
            return intent.getParcelableExtra(EXTRA_RESOLVABLE_API_EXCEPTION)
        }

        fun getLocationManagerId(intent: Intent): String? {
            return intent.getStringExtra(EXTRA_LOCATION_MANAGER_ID)
        }
    }

}