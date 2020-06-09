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

package com.splendo.kaluga.permissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.splendo.kaluga.logging.info

/**
 * An [AppCompatActivity] responsible for requesting a [Permission]
 */
class PermissionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // windowActionBar=false
        supportActionBar?.elevation = 0F // windowContentOverlay=@null

        val requestedPermissions = getRequestedPermissions(intent)

        if (requestedPermissions.isEmpty()) {
            finish()
        } else {
            ActivityCompat.requestPermissions(this, requestedPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            permissions.forEach { permission ->
                AndroidPermissionsManager.waitingPermissions.remove(permission)

                val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
                info(TAG, "$permission was $permissionStatus")
            }
        }

        finish()
    }

    companion object {
        const val TAG = "PERMISSIONS"
        private const val PERMISSIONS_REQUEST_CODE = 32131
        private const val EXTRA_REQUESTED_PERMISSIONS = "EXTRA_REQUESTED_PERMISSIONS"

        fun intent(context: Context, vararg requestedPermissions: String): Intent {
            val intent = Intent(context, PermissionsActivity::class.java)

            intent.putExtra(EXTRA_REQUESTED_PERMISSIONS, requestedPermissions)

            PackageManager.PERMISSION_GRANTED

            return intent
        }

        fun getRequestedPermissions(intent: Intent): Array<String> {
            return intent.getStringArrayExtra(EXTRA_REQUESTED_PERMISSIONS) ?: emptyArray()
        }
    }
}
