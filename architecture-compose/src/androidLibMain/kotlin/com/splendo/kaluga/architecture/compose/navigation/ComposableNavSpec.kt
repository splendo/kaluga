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

package com.splendo.kaluga.architecture.compose.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.DialogFragment
import com.splendo.kaluga.architecture.compose.activity
import com.splendo.kaluga.architecture.navigation.IntentFlag
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.navigation.toFlags
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * Spec for navigating with a [ComposableNavigator]
 */
sealed class ComposableNavSpec {

    companion object {
        /**
         * Creates a [ComposableNavSpec.Launcher] to open the camera to take a picture.
         * @param uri The [Uri] where the taken picture should be stored.
         * @param onResult Returns `true` if a picture was taken successfully.
         */
        inline fun <reified VM : BaseLifecycleViewModel> TakePicture(uri: Uri?, noinline onResult: VM.(Boolean) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.TakePicture(),
            { uri },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open the camera to capture a video.
         * @param uri The [Uri] where the captured video should be stored.
         * @param onResult Returns `true` if a video was captured successfully.
         */
        inline fun <reified VM : BaseLifecycleViewModel> CaptureVideo(uri: Uri?, noinline onResult: VM.(Boolean) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.CaptureVideo(),
            { uri },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.IntentLauncher] to open the email app.
         * @param settings The [NavigationSpec.Email.EmailSettings] to apply to the email.
         */
        fun SendEmail(settings: NavigationSpec.Email.EmailSettings) = IntentLauncher { settings.intent }

        /**
         * Creates a [ComposableNavSpec.Launcher] to open Photo app to pick an image or video
         * @param type The [ActivityResultContracts.PickVisualMedia.VisualMediaType] to be selected.
         * @param onResult Returns an [Uri] of the image or video selected, or `null` if nothing was selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> PickVisualMedia(type: ActivityResultContracts.PickVisualMedia.VisualMediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo,
            noinline onResult: VM.(Uri?) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.PickVisualMedia(),
            { PickVisualMediaRequest(mediaType = type) },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open Photo app to pick a number of images or videos
         * @param type The [ActivityResultContracts.PickVisualMedia.VisualMediaType] to be selected.
         * @param onResult Returns a list of [Uri] of the images or videos selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> PickMultipleVisualMedia(maxItems: Int? = null, type: ActivityResultContracts.PickVisualMedia.VisualMediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo,
            noinline onResult: VM.(List<Uri>) -> Unit) = Launcher(
            VM::class,
            maxItems?.let { ActivityResultContracts.PickMultipleVisualMedia(it) } ?: ActivityResultContracts.PickMultipleVisualMedia(),
            { PickVisualMediaRequest(mediaType = type) },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open the file manager app to pick a file.
         * @param types List of mime types allowed to be selected.
         * @param onResult Returns an [Uri] of the file selected, or `null` if nothing was selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> OpenDocument(types: List<String>, noinline onResult: VM.(Uri?) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.OpenDocument(),
            { types.toTypedArray() },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open the file manager app to pick a number of files.
         * @param types List of mime types allowed to be selected.
         * @param onResult Returns a list of [Uri] of the documents selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> OpenMultipleDocument(types: List<String>, noinline onResult: VM.(List<Uri>) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.OpenMultipleDocuments(),
            { types.toTypedArray() },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open create a new file.
         * @param fileName The suggested name of the file.
         * @param mimeType The mime type of the file.
         * @param onResult Returns the [Uri] of the document created.
         */
        inline fun <reified VM : BaseLifecycleViewModel> CreateDocument(fileName: String, mimeType: String, noinline onResult: VM.(Uri?) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.CreateDocument(mimeType),
            { fileName },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open the file manager app to pick a folder.
         * @param startingLocation Optional [Uri] of the folder to start in.
         * @param onResult Returns an [Uri] of the folder selected, or `null` if nothing was selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> OpenDocumentTree(startingLocation: Uri? = null, noinline onResult: VM.(Uri?) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.OpenDocumentTree(),
            { startingLocation },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to open the contacts list to select a contact.
         * @param onResult Returns an [Uri] of the contact selected, or `null` if nothing was selected.
         */
        inline fun <reified VM : BaseLifecycleViewModel> PickContact(noinline onResult: VM.(Uri?) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.PickContact(),
            { null },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to request a permission.
         * @param permission Key of the permission to be requested. E.g. [android.Manifest.permission.ACCESS_COARSE_LOCATION].
         * @param onResult Returns `true` if the permission was granted, `false` otherwise.
         */
        inline fun <reified VM : BaseLifecycleViewModel> RequestPermission(permission: String, noinline onResult: VM.(Boolean) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.RequestPermission(),
            { permission },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.Launcher] to request multiple permissions.
         * @param permissions Keys of the permission to be requested. E.g. [android.Manifest.permission.ACCESS_COARSE_LOCATION].
         * @param onResult Returns map containing whether the permission was granted for each permission in [permissions].
         */
        inline fun <reified VM : BaseLifecycleViewModel> RequestMultiplePermissions(permissions: List<String>, noinline onResult: VM.(Map<String, Boolean>) -> Unit) = Launcher(
            VM::class,
            ActivityResultContracts.RequestMultiplePermissions(),
            { permissions.toTypedArray() },
            onResult
        )

        /**
         * Creates a [ComposableNavSpec.IntentLauncher] to the phone screen.
         * @param phoneNumber The phoneNumber to dial.
         * @param type The [NavigationSpec.Phone.Type] to apply.
         */
        fun Phone(phoneNumber: String, type: NavigationSpec.Phone.Type) = IntentLauncher {
            val intent = when (type) {
                is NavigationSpec.Phone.Type.Dial -> Intent(Intent.ACTION_DIAL)
                is NavigationSpec.Phone.Type.Call -> Intent(Intent.ACTION_CALL)
            }.apply {
                data = Uri.parse("tel:${phoneNumber}")
            }
            if (intent.resolveActivity(packageManager) != null) intent else null
        }

        /**
         * Creates a [ComposableNavSpec.IntentLauncher] to the settings screen.
         * @param type The [NavigationSpec.Settings.Type] to apply.
         */
        fun Settings(type: NavigationSpec.Settings.Type) = IntentLauncher {
            val intent = type.intent(this)
            if (intent.resolveActivity(packageManager) != null) intent else null
        }

        /**
         * Creates a [ComposableNavSpec.IntentLauncher] to the text messages screen.
         * @param settings The [NavigationSpec.TextMessenger.TextMessengerSettings] to apply.
         */
        fun Messenger(settings: NavigationSpec.TextMessenger.TextMessengerSettings) = IntentLauncher {
            val intent = settings.intent
            if (intent.resolveActivity(packageManager) != null) intent else null
        }

        /**
         * Creates a [ComposableNavSpec.IntentLauncher] to the browser.
         * @param url The [URL] to open.
         * @param type The [NavigationSpec.Browser.Type] to apply.
         */
        fun Browser(url: URL, type: NavigationSpec.Browser.Type = NavigationSpec.Browser.Type.Normal) = IntentLauncher {
            val intent = when (type) {
                is NavigationSpec.Browser.Type.CustomTab -> {
                    val builder = CustomTabsIntent.Builder()
                    builder.build().intent.apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse(url.toURI().toString())
                    }
                }
                is NavigationSpec.Browser.Type.Normal -> {
                    val uri = Uri.parse(url.toURI().toString())
                    Intent(Intent.ACTION_VIEW, uri)
                }
            }
            if (intent.resolveActivity(packageManager) != null) intent else null
        }
    }

    /**
     * A [ComposableNavSpec] that launched into a new screen.
     */
    sealed class LaunchedNavigation : ComposableNavSpec() {
        @Composable
        protected abstract fun createLauncher(viewModel: BaseLifecycleViewModel): () -> Unit

        /**
         * Launch this [LaunchedNavigation] for a given [viewModel].
         */
        @Composable
        fun Launch(viewModel: BaseLifecycleViewModel) {
            val launcher = createLauncher(viewModel)
            LaunchedEffect(launcher) {
                launcher.invoke()
            }
        }
    }

    /**
     * Navigates using an [ActivityResultLauncher] and a valid input.
     * @param viewModelClass The class of the ViewModel the result of this launcher is to be applied to.
     * @param activityResultContract The [ActivityResultContract] to launch with.
     * @param getInput The input to be provided to [activityResultContract]
     * @param onResult Callback to handle the result of [activityResultContract]
     */
    data class Launcher<VM : BaseLifecycleViewModel, I, O>(
        val viewModelClass: KClass<VM>,
        val activityResultContract: ActivityResultContract<I, O>,
        val getInput: @Composable () -> I?,
        val onResult: VM.(O) -> Unit
    ) : LaunchedNavigation() {

        @Composable
        override fun createLauncher(viewModel: BaseLifecycleViewModel): () -> Unit {
            val launcher = rememberLauncherForActivityResult(activityResultContract) { result ->
                viewModelClass.safeCast(viewModel)?.let {
                    it.onResult(result)
                }
            }
            val input = getInput()
            return {
                input?.let {
                    launcher.launch(it)
                }
            }
        }
    }

    /**
     * Closes the current [android.app.Activity]
     * @param result Optional result code and
     */
    data class CloseActivity(val result: Pair<Int, NavigationBundle<*>?>? = null) : LaunchedNavigation() {
        @Composable
        override fun createLauncher(viewModel: BaseLifecycleViewModel): () -> Unit {
            val activity = LocalContext.current.activity ?: return {}
            return {
                result?.let { (resultCode, bundle) ->
                    val data = Intent().apply {
                        bundle?.let {
                            putExtras(it.toBundle())
                        }
                    }
                    activity.setResult(resultCode, data)
                }
                activity.finish()
            }
        }
    }

    /**
     * Shows a [DialogFragment]
     * @param tag Optional tag to add to the Dialog
     * @param createDialog Function to create the [DialogFragment] to display
     */
    data class Dialog(
        val tag: String? = null,
        val createDialog: () -> DialogFragment
    ) : LaunchedNavigation() {

        @Composable
        override fun createLauncher(viewModel: BaseLifecycleViewModel): () -> Unit {
            val fragmentManager = LocalContext.current.activity?.supportFragmentManager ?: return {}
            return {
                createDialog().show(fragmentManager, tag)
            }
        }
    }

    /**
     * Navigates according to a given [Intent]
     * @param createIntent Creates the [Intent] to navigate to
     */
    data class IntentLauncher(val createIntent: @Composable Activity.() -> Intent?) : LaunchedNavigation() {

        @Composable
        override fun createLauncher(viewModel: BaseLifecycleViewModel): () -> Unit {
            val context = LocalContext.current
            val intent = context.activity?.intent ?: return {}
            return {
                context.startActivity(intent)
            }
        }
    }
}

/**
 * Creates a [ComposableNavSpec.Launcher] that takes the [SingleValueNavigationAction.value] as the input of its [ActivityResultContract].
 * @param activityResultContract The [ActivityResultContract] to launch.
 * @param onResult The result received after the launched screen has completed.
 */
inline fun <reified VM : BaseLifecycleViewModel, I, O> SingleValueNavigationAction<I>.Launcher(
    activityResultContract: ActivityResultContract<I, O>,
    noinline onResult: VM.(O) -> Unit
) = ComposableNavSpec.Launcher(VM::class, activityResultContract, { value }, onResult)

/**
 * Creates a [ComposableNavSpec.Launcher] that takes an [Intent] containing the [NavigationAction.bundle] as the input of its [ActivityResultContract].
 * @param activityResultContract The [ActivityResultContract] to launch.
 * @param flags A set of [IntentFlag] to be applied to the next screen.
 * @param onResult The result received after the launched screen has completed.
 */
inline fun <reified A : Activity, reified VM : BaseLifecycleViewModel, O> NavigationAction<*>.Launcher(
    activityResultContract: ActivityResultContract<Intent, O>,
    flags: Set<IntentFlag> = emptySet(),
    noinline onResult: VM.(O) -> Unit
) {
    val getInput = @Composable {
        LocalContext.current.activity?.let { activity ->
            Intent(activity, A::class.java).apply {
                bundle?.let {
                    putExtras(it.toBundle())
                }
                this.flags = flags.toFlags()
            }
        }
    }
    ComposableNavSpec.Launcher(VM::class, activityResultContract, getInput, onResult)
}

/**
 * Creates a [ComposableNavSpec.IntentLauncher] to show a given [Activity]
 * @param flags A set of [IntentFlag] to be applied to the next screen.
 */
inline fun <reified  A : Activity> NavigationAction<*>.ShowActivity(
    flags: Set<IntentFlag> = emptySet()
) = ComposableNavSpec.IntentLauncher {
    Intent(this, A::class.java).apply {
        bundle?.let {
            putExtras(it.toBundle())
        }
        this.flags = flags.toFlags()
    }
}

/**
 * Creates a [ComposableNavSpec.CloseActivity] for a given result code.
 * @param resultCode If not `null`, this will set the [NavigationAction.bundle] as the result of the activity.
 */
fun NavigationAction<*>.CloseActivity(resultCode: Int?) = ComposableNavSpec.CloseActivity(
    resultCode?.let { it to bundle }
)
