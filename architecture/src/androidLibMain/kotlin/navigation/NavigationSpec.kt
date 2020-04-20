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

import android.net.Uri
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import java.net.URL

sealed class NavigationSpec {
    data class Activity<A: android.app.Activity>(val activityClass: Class<A>, val flags: Set<IntentFlag> = emptySet(), val requestCode: Int? = null) : NavigationSpec()
    data class Close(val result: Int? = null) : NavigationSpec()
    data class Fragment(
        @IdRes val containerId: Int,
        val type: Type = Type.Replace,
        val tag: String? = null,
        val backStackSettings: BackStackSettings = BackStackSettings.DontAdd,
        val animationSettings: AnimationSettings? = null,
        val createFragment: () -> androidx.fragment.app.Fragment) : NavigationSpec() {

        sealed class Type {
            object Add : Type()
            object Replace : Type()
        }

        sealed class BackStackSettings {
            object DontAdd : BackStackSettings()
            data class Add(val name: String? = null) : BackStackSettings()
        }
        data class AnimationSettings(
            @AnimatorRes @AnimRes val enter: Int = 0,
            @AnimatorRes @AnimRes val exit: Int = 0,
            @AnimatorRes @AnimRes val popEnter: Int = 0,
            @AnimatorRes @AnimRes val popExit: Int = 0)
    }
    data class Dialog(val tag: String? = null, val createDialog: () -> DialogFragment): NavigationSpec()
    data class Camera(val type: Type, val requestCode: Int, val uri: Uri?) : NavigationSpec() {
        sealed class Type {
            object Image: Type()
            object Video: Type()
        }
    }
    data class Email(val emailSettings: EmailSettings) : NavigationSpec() {
        sealed class Type {
            object Plain : Type()
            object Stylized : Type()
        }
        data class EmailSettings(
            val type: Type = Type.Plain,
            val to: List<String> = emptyList(),
            val cc: List<String> = emptyList(),
            val bcc: List<String> = emptyList(),
            val subject: String? = null,
            val body: String? = null,
            val attachments: List<Uri> = emptyList())
    }
    data class FileSelector(val requestCode: Int, val fileSelectorSettings: FileSelectorSettings) : NavigationSpec() {
        data class FileSelectorSettings(val type: String, val allowMultiple: Boolean = false, val localOnly: Boolean = true)
    }
    data class Phone(val type: Type, val phoneNumber: Int) : NavigationSpec() {
        sealed class Type {
            object Dial: Type()
            object Call: Type()
        }
    }
    data class Settings(val type: Type) : NavigationSpec() {
        sealed class Type {
            object General : Type()
            object Wireless : Type()
            object AirplaneMode : Type()
            object Wifi : Type()
            object Apn : Type()
            object Bluetooth : Type()
            object Date : Type()
            object Locale : Type()
            object InputMethod: Type()
            object Display : Type()
            object Security : Type()
            object LocationSource : Type()
            object InternalStorage : Type()
            object MemoryCard : Type()
        }
    }
    data class TextMessenger(val settings: TextMessengerSettings) : NavigationSpec() {
        sealed class Type {
            object Plain : Type()
            object Image : Type()
            object Video : Type()
        }

        data class TextMessengerSettings(val type: Type = Type.Plain, val phoneNumber: Int?, val subject: String? = null, val body: String? = null, val attachments: List<Uri> = emptyList())

    }
    data class Browser(val url: URL) : NavigationSpec()
}

