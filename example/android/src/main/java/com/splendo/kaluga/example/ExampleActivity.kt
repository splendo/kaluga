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

package com.splendo.kaluga.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.arch.AppRootScreen
import com.splendo.kaluga.example.arch.DeepLinkBus
import com.splendo.kaluga.example.architecture.ArchitectureActivity
import com.splendo.kaluga.example.datetimepicker.DateTimePickerActivity
import com.splendo.kaluga.example.keyboard.KeyboardActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.shared.MobileFeatureIds
import com.splendo.kaluga.example.resources.ResourcesActivity

class ExampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.postDeepLink()
        setContent {
            MaterialTheme {
                AppRootScreen(onNativeLaunch = ::launchFeature)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // singleTask launch mode: an inbound universal link reuses the running instance. The CMP
        // root observes the bus, so just push the URL and it'll route there itself — no need to
        // recreate the Activity, which means the existing nav back-stack stays intact.
        setIntent(intent)
        intent.postDeepLink()
    }

    private fun launchFeature(id: String) {
        val cls = when (id) {
            MobileFeatureIds.ALERTS -> AlertsActivity::class.java
            MobileFeatureIds.ARCHITECTURE -> ArchitectureActivity::class.java
            MobileFeatureIds.DATE_TIME_PICKER -> DateTimePickerActivity::class.java
            MobileFeatureIds.KEYBOARD -> KeyboardActivity::class.java
            MobileFeatureIds.HUD -> LoadingActivity::class.java
            MobileFeatureIds.RESOURCES -> ResourcesActivity::class.java
            else -> error("Native launch for unknown feature id: $id")
        }
        startActivity(Intent(this, cls))
    }
}

private fun Intent.postDeepLink() {
    if (action != Intent.ACTION_VIEW) return
    val url = data?.toString() ?: return
    DeepLinkBus.postUrl(url)
}
