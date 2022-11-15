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
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * Spec used by [Navigator] to determine how to perform Navigation
 */
sealed class NavigationSpec {

    companion object {
        inline fun <reified A : android.app.Activity> Activity(
            flags: Set<IntentFlag> = emptySet(),
            launchType: Activity.LaunchType = Activity.LaunchType.NoResult
        ) = Activity(A::class.java, flags, launchType)
    }

    /**
     * Navigates to a new [android.app.Activity]
     * The [Navigator] automatically adds the [NavigationBundle] of the [NavigationAction] as a [Bundle] to its [Intent]
     * @param activityClass [Class] of the Activity to navigate to
     * @param flags Set of [IntentFlag] to add to the navigation [Intent]
     * @param launchType The [LaunchType] to determine how the [Activity] should be launched
     */
    data class Activity<A : android.app.Activity>(
        val activityClass: Class<A>,
        val flags: Set<IntentFlag> = emptySet(),
        val launchType: LaunchType = LaunchType.NoResult
    ) : NavigationSpec() {

        /**
         * Determines how [NavigationSpec.Activity] will handle launching its intent
         */
        sealed class LaunchType {

            companion object {
                inline fun <reified A : android.app.Activity> ActivityContract(noinline contract: A.() -> ActivityResultLauncher<Intent>) = ActivityContract(A::class, contract)
            }

            /**
             * [Activity] will launch using [android.app.Activity.startActivity]
             */
            object NoResult : LaunchType()

            /**
             * [Activity] will launch using [android.app.Activity.startActivityForResult]
             * @param requestCode The requestCode to be sent to [android.app.Activity.startActivityForResult]
             */
            data class ActivityResult(val requestCode: Int) : LaunchType()

            /**
             * [Activity] will launch using an [ActivityResultLauncher] generated for an instance of [activityClass]
             * @param activityClass The [KClass] of the activity to generate the [ActivityResultLauncher] for
             * @param provideResultLauncher A callback method called on an instance of [activityClass] to provide an [ActivityResultLauncher] to launch the new activity with.
             * Note that this method may be called after the instance of [activityClass] has been created.
             */
            class ActivityContract<A : android.app.Activity>(
                val activityClass: KClass<A>,
                val provideResultLauncher: A.() -> ActivityResultLauncher<Intent>
            ) : LaunchType() {
                fun tryAndGetContract(activity: android.app.Activity): ActivityResultLauncher<Intent>? = activityClass.safeCast(activity)?.provideResultLauncher()
            }
        }

        @Deprecated("Legacy constructor", ReplaceWith("NavigationSpec.Activity(activityClass, flags, launchType)"))
        constructor(
            activityClass: Class<A>,
            flags: Set<IntentFlag> = emptySet(),
            requestCode: Int?
        ) : this(activityClass, flags, requestCode?.let { LaunchType.ActivityResult(it) } ?: LaunchType.NoResult)
    }

    /**
     * Closes the current [android.app.Activity]
     * @param result Optional result code
     */
    data class Close(val result: Int? = null) : NavigationSpec()

    /**
     * Shows a [androidx.fragment.app.Fragment]
     * @param containerId The identifier of the View containing the fragment
     * @param type The [Type] of transaction. Defaults to [Type.Replace]
     * @param tag Optional tag of the fragment transaction
     * @param backStackSettings The [BackStackSettings] of the transaction. Defaults to [BackStackSettings.DontAdd]
     * @param animationSettings Optional [AnimationSettings] for the transaction
     * @param createFragment Function to create the [androidx.fragment.app.Fragment]
     */
    data class Fragment(
        @IdRes val containerId: Int,
        val type: Type = Type.Replace,
        val tag: String? = null,
        val backStackSettings: BackStackSettings = BackStackSettings.DontAdd,
        val animationSettings: AnimationSettings? = null,
        val createFragment: () -> androidx.fragment.app.Fragment
    ) : NavigationSpec() {

        /**
         * Type of fragment transaction.
         */
        sealed class Type {
            /**
             * Adds a [androidx.fragment.app.Fragment] without removing any other fragments that may have been added
             */
            object Add : Type()

            /**
             * Replaces the existing [androidx.fragment.app.Fragment]. If no fragment has been added yet, this just adds the fragment.
             */
            object Replace : Type()
        }

        /**
         * Settings for whether the [androidx.fragment.app.Fragment] should be added to the Backstack
         */
        sealed class BackStackSettings {
            /**
             * Doesn't add the [androidx.fragment.app.Fragment] to the backstack
             */
            object DontAdd : BackStackSettings()

            /**
             * Adds the [androidx.fragment.app.Fragment] to the backstack
             * @param name The name with which to add the fragment
             */
            data class Add(val name: String? = null) : BackStackSettings()
        }

        /**
         * Settings for animation during the fragment transaction
         * @param enter Enter animation
         * @param exit Exit animation
         * @param popEnter Pop Enter Animation
         * @param popExit Pop Exit Animation
         */
        data class AnimationSettings(
            @AnimatorRes @AnimRes val enter: Int = 0,
            @AnimatorRes @AnimRes val exit: Int = 0,
            @AnimatorRes @AnimRes val popEnter: Int = 0,
            @AnimatorRes @AnimRes val popExit: Int = 0
        )
    }

    /**
     * Removes a [Fragment] with a given tag
     * @param tag The tag of the [Fragment] to remove
     */
    data class RemoveFragment(val tag: String) : NavigationSpec()

    /**
     * Shows a [DialogFragment]
     * @param tag Optional tag to add to the Dialog
     * @param createDialog Function to create the [DialogFragment] to display
     */
    data class Dialog(val tag: String? = null, val createDialog: () -> DialogFragment) : NavigationSpec()

    /**
     * Dismisses a [DialogFragment] with a given Tag
     * @param tag The tag of the [DialogFragment] to remove
     */
    data class DismissDialog(val tag: String) : NavigationSpec()

    /**
     * Shows the Camera
     * @param type The [Type] of media to capture
     * @param requestCode The request code added to the intent
     * @param uri Optiona [Uri] indicating where the result should be stored
     */
    data class Camera(val type: Type, val requestCode: Int, val uri: Uri?) : NavigationSpec() {

        /**
         * Type of media the camera can capture
         */
        sealed class Type {
            object Image : Type()
            object Video : Type()
        }
    }

    /**
     * Shows the Email app
     * @param emailSettings Settings for the email
     */
    data class Email(val emailSettings: EmailSettings) : NavigationSpec() {

        /**
         * The type of formatting used for composing the email
         */
        sealed class Type {
            object Plain : Type()
            object Stylized : Type()
        }

        /**
         * Settings for the email
         * @param type The [Type] of formatting to be used
         * @param to The list of recipent emails
         * @param cc The list of cc emails
         * @param bcc The list of bcc emails
         * @param subject Optional subject of the email
         * @param body Optional body of the email
         * @param attachments List of [Uri] pointing to attachments to add
         */
        data class EmailSettings(
            val type: Type = Type.Plain,
            val to: List<String> = emptyList(),
            val cc: List<String> = emptyList(),
            val bcc: List<String> = emptyList(),
            val subject: String? = null,
            val body: String? = null,
            val attachments: List<Uri> = emptyList()
        )
    }

    /**
     * Opens a File Selector
     * @param requestCode The request code added to the intent
     * @param fileSelectorSettings The [FileSelectorSettings] used to configure the file selection
     */
    data class FileSelector(val requestCode: Int, val fileSelectorSettings: FileSelectorSettings) : NavigationSpec() {

        /**
         * Settings to configure the file selection criteria
         * @param type List of file types to select
         * @param allowMultiple Used to indicate the multiple files can be selected
         * @param localOnly Used to indicate that only data that is on the local device should be returned
         */
        data class FileSelectorSettings(val type: String, val allowMultiple: Boolean = false, val localOnly: Boolean = true)
    }

    /**
     * Opens up the Phone screen
     * @param type The [Type] of phone screen to show
     * @param phoneNumber The phone number to dial
     */
    data class Phone(val type: Type, val phoneNumber: String) : NavigationSpec() {
        /**
         * Type of Phone Screen to open
         */
        sealed class Type {
            /**
             * Opens the Dialer screen and pre-fills the phone number
             */
            object Dial : Type()

            /**
             * Opens the phone screen and calls the phone number
             */
            object Call : Type()
        }
    }

    /**
     * Opens the Settings screen
     * @param type [Type] of settings screen to open
     */
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
            object InputMethod : Type()
            object Display : Type()
            object Security : Type()
            object LocationSource : Type()
            object InternalStorage : Type()
            object MemoryCard : Type()
            object AppDetails : Type()
        }
    }

    /**
     * Opens the Text messaeger screen
     * @param settings The [TextMessengerSettings] to configure the message
     */
    data class TextMessenger(val settings: TextMessengerSettings) : NavigationSpec() {
        /**
         * The type of Text Message to send
         */
        sealed class Type {
            object Plain : Type()
            object Image : Type()
            object Video : Type()
        }

        /**
         * Settings for the text message
         * @param type [Type] of text message to send
         * @param recipients List of recipients
         * @param subject Optional subject of the message
         * @param body Optional body of the message
         * @param attachments List of [Uri] pointing to attachments to add
         */
        data class TextMessengerSettings(val type: Type = Type.Plain, val recipients: List<String>, val subject: String? = null, val body: String? = null, val attachments: List<Uri> = emptyList())
    }

    /**
     * Opens the browser
     * @param url The [URL] to open in the browser
     */
    data class Browser(val url: URL, val viewType: Type) : NavigationSpec() {
        sealed class Type {
            object CustomTab : Type()
            object Normal : Type()
        }
    }

    /**
     * Opens a Third Party app if installed on the phone or navigates to the store if it does not exist.
     * @param packageName The name of the package for which to open the store
     * @param openMode The [OpenMode] used to determine how to handle whether to open the app or the PlayStore
     */
    data class ThirdPartyApp(val packageName: String, val openMode: OpenMode = OpenMode.FALLBACK_TO_STORE) : NavigationSpec() {
        enum class OpenMode {
            /**
             * Immediately opens the store
             */
            FORCE_STORE,

            /**
             * Only navigates when the app is installed
             */
            ONLY_WHEN_INSTALLED,

            /**
             * Opens the PlayStore if the app is not installed
             */
            FALLBACK_TO_STORE
        }
    }

    /**
     * Navigates according to a given [Intent]
     * @param intent The [Intent] to navigate to
     */
    data class CustomIntent(val intent: Intent) : NavigationSpec()
}
