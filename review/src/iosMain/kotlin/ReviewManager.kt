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

package com.splendo.kaluga.review

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import platform.StoreKit.SKStoreReviewController

/**
 * Manager for requesting the system to show a Review Dialog.
 * This library does not guarantee such a dialog will be shown, as the OS may block such an action.
 */
actual class ReviewManager {

    /**
     * Builder for creating a [ReviewManager]
     */
    actual class Builder : LifecycleSubscribable {

        /**
         * Creates a [ReviewManager]
         */
        actual fun create() = ReviewManager()
    }

    /**
     * Attempts to show a dialog that asks the user to submit a review of the app.
     * This method does not guarantee such a dialog will be shown as the OS may block it.
     */
    actual suspend fun attemptToRequestReview() {
        SKStoreReviewController.requestReview()
    }
}
