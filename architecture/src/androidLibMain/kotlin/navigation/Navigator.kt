/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.navigation

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

actual class Navigator<A : NavigationAction>(private val navigationMapper: (A) -> NavigationSpec) {

    private var activity: AppCompatActivity? = null

    actual suspend fun navigate(action: A) {
        navigate(navigationMapper.invoke(action), action.bundle)
    }

    private fun navigate(spec: NavigationSpec, bundle: NavigationBundle<*>?) {
        when(spec) {
            is NavigationSpec.Activity -> navigateToActivity(spec, bundle)
            is NavigationSpec.Close -> closeActivity(spec, bundle)
            is NavigationSpec.Fragment -> navigateToFragment(spec, bundle)
            is NavigationSpec.Dialog -> navigateToDialog(spec, bundle)
            is NavigationSpec.Camera -> navigateToCamera(spec, bundle)
            is NavigationSpec.Email -> navigateToEmail(spec, bundle)
            is NavigationSpec.FileSelector -> navigateToFileSelector(spec, bundle)
            is NavigationSpec.Settings -> navigateToSettings(spec, bundle)
            is NavigationSpec.TextMessenger -> navigateToMessenger(spec, bundle)
            is NavigationSpec.Browser -> navigateToBrowser(spec, bundle)
        }
    }

    private fun navigateToActivity(activitySpec: NavigationSpec.Activity, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val intent = Intent(activity, activitySpec.activityClass).apply {
            bundle?.let {
                putExtras(it.toBundle())
            }
            flags = activitySpec.flags.toFlags()
        }
        activitySpec.requestCode?.let {
            activity.startActivityForResult(intent, it)
        } ?: activity.startActivity(intent)
    }

    private fun closeActivity(closeSpec: NavigationSpec.Close, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        closeSpec.result?.let {resultCode ->
            val data = Intent().apply {
                bundle?.let {
                    putExtras(it.toBundle())
                }
            }
            activity.setResult(resultCode, data)
        }
        activity.finish()
    }

    private fun navigateToFragment(fragmentSpec: NavigationSpec.Fragment, bundle: NavigationBundle<*>?) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        val transaction = fragmentManager.beginTransaction()

        when(val backtrackSettings = fragmentSpec.backStackSettings) {
            is NavigationSpec.Fragment.BackStackSettings.Add -> transaction.addToBackStack(backtrackSettings.name)
        }

        fragmentSpec.animationSettings?.let { animationSettings ->
            transaction.setCustomAnimations(animationSettings.enter, animationSettings.exit, animationSettings.popEnter, animationSettings.popExit)
        }

        val fragment = fragmentSpec.createFragment(bundle)
        when(fragmentSpec.type) {
            is NavigationSpec.Fragment.Type.Add -> transaction.add(fragmentSpec.containerId, fragment, fragmentSpec.tag)
            is NavigationSpec.Fragment.Type.Replace -> transaction.replace(fragmentSpec.containerId, fragment, fragmentSpec.tag)
        }

        transaction.commit()
    }

    private fun navigateToDialog(dialogSpec: NavigationSpec.Dialog, bundle: NavigationBundle<*>?) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        dialogSpec.createDialog(bundle).show(fragmentManager, dialogSpec.tag)
    }

    private fun navigateToCamera(cameraSpec: NavigationSpec.Camera, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val intent = when(cameraSpec.type) {
            is NavigationSpec.Camera.Type.Image -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            is NavigationSpec.Camera.Type.Video -> Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        }

        cameraSpec.createUri(bundle)?.let { uri ->
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivityForResult(intent, cameraSpec.requestCode)
        }
    }

    private fun navigateToEmail(emailSpec: NavigationSpec.Email, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val settings = emailSpec.emailSettings(bundle)
        val intent = when(settings.attachements.size) {
            0 -> Intent(Intent.ACTION_SENDTO)
            1 -> Intent(Intent.ACTION_SEND).apply { putExtra(Intent.EXTRA_STREAM, settings.attachements[0]) }
            else -> Intent(Intent.ACTION_SEND_MULTIPLE).apply { putExtra(Intent.EXTRA_STREAM, ArrayList(settings.attachements)) }
        }.apply {
            data = Uri.parse("mailto:")
            type = when(settings.type) {
                is NavigationSpec.Email.Type.Plain -> "text/plain"
                is NavigationSpec.Email.Type.Stylized -> "*/*"
            }
            settings.to?.let { putExtra(Intent.EXTRA_EMAIL, it) }
            settings.cc?.let { putExtra(Intent.EXTRA_CC, it) }
            settings.bcc?.let { putExtra(Intent.EXTRA_BCC, it) }
            settings.subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            settings.body?.let {putExtra(Intent.EXTRA_TEXT, it) }
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToFileSelector(fileSelectorSpec: NavigationSpec.FileSelector, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val settings = fileSelectorSpec.fileSelectorSettings(bundle)
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = settings.type
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, settings.allowMultiple)
            putExtra(Intent.EXTRA_LOCAL_ONLY, settings.localOnly)
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivityForResult(intent, fileSelectorSpec.requestCode)
        }
    }

    private fun navigateToPhone(phoneSpec: NavigationSpec.Phone, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return

        val intent = when(phoneSpec.type) {
            is NavigationSpec.Phone.Type.Dial -> Intent(Intent.ACTION_DIAL)
            is NavigationSpec.Phone.Type.Call -> Intent(Intent.ACTION_CALL)
        }.apply {
            data = Uri.parse("tel:${phoneSpec.phoneNumber(bundle)}")
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToSettings(settingsSpec: NavigationSpec.Settings, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val intent = when(settingsSpec.type(bundle)) {
            is NavigationSpec.Settings.Type.General -> Intent(Settings.ACTION_SETTINGS)
            is NavigationSpec.Settings.Type.Wireless -> Intent(Settings.ACTION_WIRELESS_SETTINGS)
            is NavigationSpec.Settings.Type.AirplaneMode -> Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            is NavigationSpec.Settings.Type.Wifi -> Intent(Settings.ACTION_WIFI_SETTINGS)
            is NavigationSpec.Settings.Type.Apn -> Intent(Settings.ACTION_APN_SETTINGS)
            is NavigationSpec.Settings.Type.Bluetooth -> Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            is NavigationSpec.Settings.Type.Date -> Intent(Settings.ACTION_DATE_SETTINGS)
            is NavigationSpec.Settings.Type.Locale -> Intent(Settings.ACTION_LOCALE_SETTINGS)
            is NavigationSpec.Settings.Type.InputMethod -> Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            is NavigationSpec.Settings.Type.Display -> Intent(Settings.ACTION_DISPLAY_SETTINGS)
            is NavigationSpec.Settings.Type.Security -> Intent(Settings.ACTION_SECURITY_SETTINGS)
            is NavigationSpec.Settings.Type.LocationSource -> Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            is NavigationSpec.Settings.Type.InternalStorage -> Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS)
            is NavigationSpec.Settings.Type.MemoryCard -> Intent(Settings.ACTION_MEMORY_CARD_SETTINGS)
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToMessenger(messengerSpec: NavigationSpec.TextMessenger, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val settings = messengerSpec.settings(bundle)
        val intent = when(settings.attachements.size) {
            0 -> Intent(Intent.ACTION_SENDTO)
            1 -> Intent(Intent.ACTION_SEND).apply { putExtra(Intent.EXTRA_STREAM, settings.attachements[0]) }
            else -> Intent(Intent.ACTION_SEND_MULTIPLE).apply { putExtra(Intent.EXTRA_STREAM, ArrayList(settings.attachements)) }
        }.apply {
            data = Uri.parse("smsto:${settings.phoneNumber?.let { "$it" } ?: ""}")
            type = when(settings.type) {
                is NavigationSpec.TextMessenger.Type.Plain -> "text/plain"
                is NavigationSpec.TextMessenger.Type.Image -> "image/*"
                is NavigationSpec.TextMessenger.Type.Video -> "video/*"
            }
            settings.subject?.let { putExtra("subject", it) }
            settings.body?.let {putExtra("sms_body", it) }
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToBrowser(browserSpec: NavigationSpec.Browser, bundle: NavigationBundle<*>?) {
        val activity = this.activity ?: return
        val uri = Uri.parse(browserSpec.url(bundle).toURI().toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }
}

