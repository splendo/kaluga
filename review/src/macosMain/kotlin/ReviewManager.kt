/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.review

import com.splendo.kaluga.lifecycle.LifecycleSubscribable
import platform.StoreKit.SKStoreReviewController

/**
 * macOS implementation of [ReviewManager]. macOS 10.14+ ships `SKStoreReviewController`, which
 * takes no window argument — the system schedules the request against the active scene — so
 * the [Builder] only needs to be the [LifecycleSubscribable] marker (no [WindowLifecycleSubscribable]
 * implementation required for the prompt itself).
 */
actual class ReviewManager {

    actual class Builder : LifecycleSubscribable {
        actual fun create() = ReviewManager()
    }

    actual suspend fun attemptToRequestReview() {
        SKStoreReviewController.requestReview()
    }
}
