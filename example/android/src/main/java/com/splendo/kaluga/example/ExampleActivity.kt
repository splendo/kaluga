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
import com.splendo.kaluga.example.architecture.ArchitectureActivity
import com.splendo.kaluga.example.beacons.BeaconsActivity
import com.splendo.kaluga.example.bluetooth.BluetoothActivity
import com.splendo.kaluga.example.datetime.TimerActivity
import com.splendo.kaluga.example.datetimepicker.DateTimePickerActivity
import com.splendo.kaluga.example.keyboard.KeyboardActivity
import com.splendo.kaluga.example.link.LinksActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.media.MediaListActivity
import com.splendo.kaluga.example.permissions.PermissionsListActivity
import com.splendo.kaluga.example.resources.ResourcesActivity
import com.splendo.kaluga.example.scientific.ScientificActivity
import com.splendo.kaluga.example.shared.ui.AppRootScreen
import com.splendo.kaluga.example.shared.ui.Feature
import com.splendo.kaluga.example.system.SystemActivity

class ExampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppRootScreen(
                    features = Feature.entries,
                    onFeatureSelected = ::launchFeature,
                )
            }
        }
    }

    private fun launchFeature(feature: Feature) {
        val cls = when (feature) {
            Feature.Alerts -> AlertsActivity::class.java
            Feature.Architecture -> ArchitectureActivity::class.java
            Feature.Beacons -> BeaconsActivity::class.java
            Feature.Bluetooth -> BluetoothActivity::class.java
            Feature.DateTime -> TimerActivity::class.java
            Feature.DateTimePicker -> DateTimePickerActivity::class.java
            Feature.Keyboard -> KeyboardActivity::class.java
            Feature.Links -> LinksActivity::class.java
            Feature.LoadingIndicator -> LoadingActivity::class.java
            Feature.Location -> LocationActivity::class.java
            Feature.Media -> MediaListActivity::class.java
            Feature.Permissions -> PermissionsListActivity::class.java
            Feature.Resources -> ResourcesActivity::class.java
            Feature.Scientific -> ScientificActivity::class.java
            Feature.System -> SystemActivity::class.java
        }
        startActivity(Intent(this, cls))
    }
}
