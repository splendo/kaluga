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

package com.splendo.kaluga.architecture.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.DefaultActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable

/**
 * Class that can trigger a given [NavigationAction]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 */
actual interface Navigator<Action : NavigationAction<*>> : LifecycleSubscribable {

    /**
     * Triggers a given [NavigationAction]
     * @param action The [A] to trigger
     * @throws [NavigationException] if navigation fails.
     */
    actual fun navigate(action: Action)
}

object MissingLifecycleManagerNavigationException : NavigationException("LifecycleManager not attached")
object MissingActivityNavigationException : NavigationException("LifecycleManager does not have an activity")

/**
 * Implementation of [Navigator]. Takes a mapper function to map all [NavigationAction] to a [NavigationSpec]
 * Whenever [navigate] is called, this class maps it to a [NavigationSpec] and performs navigation according to that
 * Requires to be subscribed to an activity via [subscribe] to work
 * @param Action The type of [NavigationAction] handled by this navigator.
 * @param navigationMapper A function mapping the [Action] to [NavigationSpec]
 */
class ActivityNavigator<Action : NavigationAction<*>>(private val navigationMapper: (Action) -> NavigationSpec) :
    Navigator<Action>,
    ActivityLifecycleSubscribable by DefaultActivityLifecycleSubscribable() {

    override fun navigate(action: Action) {
        navigate(navigationMapper.invoke(action), action.bundle)
    }

    private fun navigate(spec: NavigationSpec, bundle: NavigationBundle<*>?) {
        when (spec) {
            is NavigationSpec.Activity<*> -> navigateToActivity(spec, bundle)
            is NavigationSpec.Contract<*, *> -> navigateForContract(spec)
            is NavigationSpec.Close -> closeActivity(spec, bundle)
            is NavigationSpec.Fragment -> navigateToFragment(spec, bundle)
            is NavigationSpec.RemoveFragment -> removeFragment(spec, bundle)
            is NavigationSpec.PopFragment -> popFragment(spec, bundle)
            is NavigationSpec.PopFragmentTo -> popFragmentTo(spec, bundle)
            is NavigationSpec.Dialog -> navigateToDialog(spec, bundle)
            is NavigationSpec.DismissDialog -> dismissDialog(spec, bundle)
            is NavigationSpec.Camera -> navigateToCamera(spec)
            is NavigationSpec.Email -> navigateToEmail(spec)
            is NavigationSpec.FileSelector -> navigateToFileSelector(spec)
            is NavigationSpec.Phone -> navigateToPhone(spec)
            is NavigationSpec.Settings -> navigateToSettings(spec)
            is NavigationSpec.TextMessenger -> navigateToMessenger(spec)
            is NavigationSpec.Browser -> navigateToBrowser(spec)
            is NavigationSpec.ThirdPartyApp -> navigateToThirdPartyApp(spec)
            is NavigationSpec.CustomIntent -> navigateToIntent(spec)
        }
    }

    private fun navigateToActivity(activitySpec: NavigationSpec.Activity<*>, bundle: NavigationBundle<*>?) {
        val activity = getActivity()
        val intent = Intent(activity, activitySpec.activityClass).apply {
            bundle?.let {
                putExtras(it.toBundle())
            }
            flags = activitySpec.flags.toFlags()
        }

        when (val requestType = activitySpec.launchType) {
            is NavigationSpec.Activity.LaunchType.NoResult -> activity.startActivity(intent)
            is NavigationSpec.Activity.LaunchType.ActivityResult -> activity.startActivityForResult(intent, requestType.requestCode)
            is NavigationSpec.Activity.LaunchType.ActivityContract<*> -> requestType.tryAndGetContract(activity)
                ?.launch(intent) ?: throw RuntimeException("Activity is not an instance of ${requestType.activityClass.simpleName}")
        }
    }

    private fun navigateForContract(contractSpec: NavigationSpec.Contract<*, *>) {
        val activity = getActivity()
        contractSpec.tryAndLaunch(activity) ?: throw RuntimeException("Activity is not an instance of ${contractSpec.activityClass.simpleName}")
    }

    private fun closeActivity(closeSpec: NavigationSpec.Close, bundle: NavigationBundle<*>?) {
        val activity = getActivity()
        closeSpec.result?.let { resultCode ->
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
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = fragmentSpec.getFragmentManager(manager)
        val transaction = fragmentManager.beginTransaction().let {
            when (val backtrackSettings = fragmentSpec.backStackSettings) {
                is NavigationSpec.Fragment.BackStackSettings.Add -> it.addToBackStack(
                    backtrackSettings.name,
                )
                else -> it
            }
        }

        fragmentSpec.animationSettings?.let { animationSettings ->
            transaction.setCustomAnimations(
                animationSettings.enter,
                animationSettings.exit,
                animationSettings.popEnter,
                animationSettings.popExit,
            )
        }

        val fragment = fragmentSpec.createFragment().apply {
            arguments = bundle?.toBundle()
        }
        when (fragmentSpec.type) {
            is NavigationSpec.Fragment.Type.Add -> transaction.add(
                fragmentSpec.containerId,
                fragment,
                fragmentSpec.tag,
            )
            is NavigationSpec.Fragment.Type.Replace -> transaction.replace(
                fragmentSpec.containerId,
                fragment,
                fragmentSpec.tag,
            )
        }

        transaction.commit()
    }

    private fun removeFragment(removeFragmentSpec: NavigationSpec.RemoveFragment, bundle: NavigationBundle<*>?) {
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = removeFragmentSpec.getFragmentManager(manager).apply {
            removeFragmentSpec.fragmentRequestKey?.let { key ->
                bundle?.let { setFragmentResult(key, it.toBundle()) }
            }
        }
        val fragment = fragmentManager.findFragmentByTag(removeFragmentSpec.tag) ?: return

        val transaction = fragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }

    private fun popFragment(popFragmentSpec: NavigationSpec.PopFragment, bundle: NavigationBundle<*>?) {
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = popFragmentSpec.getFragmentManager(manager).apply {
            popFragmentSpec.fragmentRequestKey?.let { key ->
                bundle?.let { setFragmentResult(key, it.toBundle()) }
            }
        }
        if (popFragmentSpec.immediate) {
            fragmentManager.popBackStackImmediate()
        } else {
            fragmentManager.popBackStack()
        }
    }

    private fun popFragmentTo(popToFragmentSpec: NavigationSpec.PopFragmentTo, bundle: NavigationBundle<*>?) {
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = popToFragmentSpec.getFragmentManager(manager).apply {
            popToFragmentSpec.fragmentRequestKey?.let { key ->
                bundle?.let { setFragmentResult(key, it.toBundle()) }
            }
        }
        val flags = if (popToFragmentSpec.inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
        if (popToFragmentSpec.immediate) {
            fragmentManager.popBackStackImmediate(popToFragmentSpec.name, flags)
        } else {
            fragmentManager.popBackStack(popToFragmentSpec.name, flags)
        }
    }

    private fun navigateToDialog(dialogSpec: NavigationSpec.Dialog, bundle: NavigationBundle<*>?) {
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = dialogSpec.getFragmentManager(manager)
        dialogSpec.createDialog().apply {
            arguments = bundle?.toBundle()
            show(fragmentManager, dialogSpec.tag)
        }
    }

    private fun dismissDialog(spec: NavigationSpec.DismissDialog, bundle: NavigationBundle<*>?) {
        val manager = manager ?: throw MissingLifecycleManagerNavigationException
        val fragmentManager = spec.getFragmentManager(manager).apply {
            spec.fragmentRequestKey?.let { key ->
                bundle?.let { setFragmentResult(key, it.toBundle()) }
            }
        }
        val dialog = fragmentManager.findFragmentByTag(spec.tag) as? DialogFragment ?: return
        dialog.dismiss()
    }

    private fun navigateToCamera(cameraSpec: NavigationSpec.Camera) {
        val activity = getActivity()
        val intent = when (cameraSpec.type) {
            is NavigationSpec.Camera.Type.Image -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            is NavigationSpec.Camera.Type.Video -> Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        }

        cameraSpec.uri?.let { uri ->
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivityForResult(intent, cameraSpec.requestCode)
        }
    }

    private fun navigateToEmail(emailSpec: NavigationSpec.Email) {
        val activity = getActivity()
        val intent = emailSpec.emailSettings.intent

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToFileSelector(fileSelectorSpec: NavigationSpec.FileSelector) {
        val activity = getActivity()
        val settings = fileSelectorSpec.fileSelectorSettings
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = settings.type
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, settings.allowMultiple)
            putExtra(Intent.EXTRA_LOCAL_ONLY, settings.localOnly)
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivityForResult(intent, fileSelectorSpec.requestCode)
        }
    }

    private fun navigateToPhone(phoneSpec: NavigationSpec.Phone) {
        val activity = getActivity()

        val intent = when (phoneSpec.type) {
            is NavigationSpec.Phone.Type.Dial -> Intent(Intent.ACTION_DIAL)
            is NavigationSpec.Phone.Type.Call -> Intent(Intent.ACTION_CALL)
        }.apply {
            data = Uri.parse("tel:${phoneSpec.phoneNumber}")
        }

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToSettings(settingsSpec: NavigationSpec.Settings) {
        val activity = getActivity()
        val intent = settingsSpec.type.intent(activity)

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToMessenger(messengerSpec: NavigationSpec.TextMessenger) {
        val activity = getActivity()
        val intent = messengerSpec.settings.intent

        intent.resolveActivity(activity.packageManager)?.let {
            activity.startActivity(intent)
        }
    }

    private fun navigateToBrowser(browserSpec: NavigationSpec.Browser) {
        val activity = getActivity()

        when (browserSpec.viewType) {
            NavigationSpec.Browser.Type.CustomTab -> {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                customTabsIntent.launchUrl(
                    activity,
                    Uri.parse(browserSpec.url.toURI().toString()),
                )
            }
            NavigationSpec.Browser.Type.Normal -> {
                val uri = Uri.parse(browserSpec.url.toURI().toString())
                val intent = Intent(Intent.ACTION_VIEW, uri)

                intent.resolveActivity(activity.packageManager)?.let {
                    activity.startActivity(intent)
                }
            }
        }
    }

    private fun navigateToThirdPartyApp(thirdPartyAppSpec: NavigationSpec.ThirdPartyApp) {
        when (thirdPartyAppSpec.openMode) {
            NavigationSpec.ThirdPartyApp.OpenMode.ONLY_WHEN_INSTALLED -> navigateToThirdPartyApp(
                thirdPartyAppSpec.packageName,
            )
            NavigationSpec.ThirdPartyApp.OpenMode.FORCE_STORE -> navigateToPlayStore(
                thirdPartyAppSpec.packageName,
            )
            NavigationSpec.ThirdPartyApp.OpenMode.FALLBACK_TO_STORE -> {
                if (!navigateToThirdPartyApp(thirdPartyAppSpec.packageName)) {
                    navigateToPlayStore(thirdPartyAppSpec.packageName)
                }
            }
        }
    }

    private fun navigateToThirdPartyApp(packageName: String): Boolean {
        val activity = getActivity()

        val intent = activity.packageManager.getLaunchIntentForPackage(packageName) ?: return false
        return try {
            activity.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    private fun navigateToPlayStore(packageName: String) {
        assert(manager?.activity != null)
        val activity = manager?.activity ?: return

        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName"),
                ),
            )
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
                ),
            )
        }
    }

    private fun navigateToIntent(intentSpec: NavigationSpec.CustomIntent) {
        val activity = getActivity()

        activity.startActivity(intentSpec.intent)
    }

    private fun getActivity(): Activity = when (val manager = manager) {
        null -> throw MissingLifecycleManagerNavigationException
        else -> manager.activity ?: throw MissingActivityNavigationException
    }
}
