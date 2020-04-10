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
    class Activity(val activityClass: Class<android.app.Activity>, val flags: Set<IntentFlag>, val requestCode: Int?) : NavigationSpec()
    class Close(val result: Int?) : NavigationSpec()
    class Fragment(
        val type: Type = Type.Replace,
        @IdRes val containerId: Int,
        val tag: String? = null,
        val backStackSettings: BackStackSettings = BackStackSettings.DontAdd,
        val animationSettings: AnimationSettings? = null,
        val createFragment: (NavigationBundle<*>?) -> androidx.fragment.app.Fragment) : NavigationSpec() {

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
    class Dialog(val tag: String?, val createDialog: (NavigationBundle<*>?) -> DialogFragment): NavigationSpec()
    class Camera(val type: Type, val requestCode: Int, val createUri: (NavigationBundle<*>?) -> Uri?) : NavigationSpec() {
        sealed class Type {
            object Image: Type()
            object Video: Type()
        }
    }
    class Email(val emailSettings: (NavigationBundle<*>?) -> EmailSettings) : NavigationSpec() {
        sealed class Type {
            object Plain : Type()
            object Stylized : Type()
        }
        data class EmailSettings(
            val type: Type = Type.Plain,
            val to: String? = null,
            val cc: String? = null,
            val bcc: String? = null,
            val subject: String? = null,
            val body: String? = null,
            val attachements: List<Uri>)
    }
    class FileSelector(val requestCode: Int, val fileSelectorSettings: (NavigationBundle<*>?) -> FileSelectorSettings) : NavigationSpec() {
        data class FileSelectorSettings(val type: String, val allowMultiple: Boolean = false, val localOnly: Boolean = true)
    }
    class Phone(val type: Type, val phoneNumber: (NavigationBundle<*>?) -> Int) : NavigationSpec() {
        sealed class Type {
            object Dial: Type()
            object Call: Type()
        }
    }
    class Settings(val type: (NavigationBundle<*>?) -> Type) : NavigationSpec() {
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
    class TextMessenger(val settings: (NavigationBundle<*>?) -> TextMessengerSettings) : NavigationSpec() {
        sealed class Type {
            object Plain : Type()
            object Image : Type()
            object Video : Type()
        }

        data class TextMessengerSettings(val type: Type = Type.Plain, val phoneNumber: Int?, val subject: String? = null, val body: String? = null, val attachements: List<Uri> = emptyList())

    }
    class Browser(val url: (NavigationBundle<*>?) -> URL) : NavigationSpec()
}

