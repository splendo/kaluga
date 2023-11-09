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

import android.content.Intent
import android.os.Build

/**
 * Intent Flags supported for Navigation
 */
sealed class IntentFlag {

    abstract val value: Int

    data object GrantReadUriPermission : IntentFlag() {
        override val value: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    data object GrantWriteUriPermission : IntentFlag() {
        override val value: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
    data object FromBackground : IntentFlag() {
        override val value: Int = Intent.FLAG_FROM_BACKGROUND
    }
    data object DebugLogResolution : IntentFlag() {
        override val value: Int = Intent.FLAG_DEBUG_LOG_RESOLUTION
    }
    data object ExcludeStoppedPackages : IntentFlag() {
        override val value: Int = Intent.FLAG_EXCLUDE_STOPPED_PACKAGES
    }
    data object IncludeStoppedPackages : IntentFlag() {
        override val value: Int = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
    }
    data object GrantPersistableUriPermission : IntentFlag() {
        override val value: Int = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
    }
    data object GrantPrefixUriPermission : IntentFlag() {
        override val value: Int = Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
    }
    data object ActivityMatchExternal : IntentFlag() {
        override val value: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Intent.FLAG_ACTIVITY_MATCH_EXTERNAL
        } else {
            0
        }
    }
    data object ActivityNoHistory : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    data object ActivitySingleTop : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    data object ActivityNewTask : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    data object ActivityMultipleTask : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    }
    data object ActivityClearTop : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    data object ActivityForwardResult : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_FORWARD_RESULT
    }
    data object ActivityPreviousIsTop : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
    }
    data object ActivityExcludeFromRecents : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    }
    data object ActivityBroughtToFront : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
    }
    data object ActivityResetTaskIfNeeded : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }
    data object ActivityLaunchedFromHistory : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
    }
    data object ActivityNewDocument : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    }
    data object ActivityNoUserAction : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_NO_USER_ACTION
    }
    data object ActivityReorderToFront : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    }
    data object ActivityNoAnimation : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_NO_ANIMATION
    }
    data object ActivityClearTask : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    data object ActivityTaskOnHome : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_TASK_ON_HOME
    }
    data object ActivityRetainInRecents : IntentFlag() {
        override val value: Int = Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS
    }
    data object ActivityLaunchAdjacent : IntentFlag() {
        override val value: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
        } else {
            0
        }
    }
    data object ReceiverRegisterOnly : IntentFlag() {
        override val value: Int = Intent.FLAG_RECEIVER_REGISTERED_ONLY
    }
    data object ReceiverReplacePending : IntentFlag() {
        override val value: Int = Intent.FLAG_RECEIVER_REPLACE_PENDING
    }
    data object ReceiverForeground : IntentFlag() {
        override val value: Int = Intent.FLAG_RECEIVER_FOREGROUND
    }
    data object ReceiverNoAbort : IntentFlag() {
        override val value: Int = Intent.FLAG_RECEIVER_NO_ABORT
    }
    data object ReceiverVisibleToInstantApps : IntentFlag() {
        override val value: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent.FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS
        } else {
            0
        }
    }
}

/**
 * Transforms a set of [IntentFlag] to its corresponding [Int]
 * @return The int describing the set of [IntentFlag]
 */
fun Set<IntentFlag>.toFlags(): Int = this.fold(0) { acc, flag -> acc or flag.value }
