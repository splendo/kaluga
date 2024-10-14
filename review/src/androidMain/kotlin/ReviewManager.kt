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

import android.content.Context
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleManagerObserver
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.base.ApplicationHolder

/**
 * Manager for requesting the system to show a Review Dialog.
 * This library does not guarantee such a dialog will be shown, as the OS may block such an action.
 *
 * @param reviewManager The [com.google.android.play.core.review.ReviewManager] managing the reviews.
 * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes.
 */
actual class ReviewManager(
    private val reviewManager: com.google.android.play.core.review.ReviewManager,
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
) {

    /**
     * Type of Review Manager in use
     */
    enum class Type {
        /**
         * Review Manager in use for Live production. This will only work for Apps distributed over the Google Play Store
         */
        Live,

        /**
         * Review Manager for testing purposes. This will not result in actual reviews being posted.
         */
        Fake, ;

        internal fun reviewManager(context: Context): com.google.android.play.core.review.ReviewManager = when (this) {
            Live -> ReviewManagerFactory.create(context)
            Fake -> FakeReviewManager(context)
        }
    }

    /**
     * A builder for creating a [ReviewManager]
     * @param type The [Type] of review manager to show.
     * @param context The [Context] of the Application used for getting the correct Review Manager.
     * @param lifecycleManagerObserver The [LifecycleManagerObserver] to observe lifecycle changes.
     */
    actual class Builder(
        private val type: Type = Type.Live,
        private val context: Context = ApplicationHolder.applicationContext,
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver(),
    ) : LifecycleSubscribable,
        ActivityLifecycleSubscribable by lifecycleManagerObserver {

        /**
         * Creates a [ReviewManager]
         * @return the created [ReviewManager]
         */
        actual fun create(): ReviewManager = ReviewManager(type.reviewManager(context), lifecycleManagerObserver)
    }

    /**
     * Attempts to show a dialog that asks the user to submit a review of the app.
     * This method does not guarantee such a dialog will be shown as the OS may block it.
     */
    actual suspend fun attemptToRequestReview() {
        val info = reviewManager.requestReview()
        lifecycleManagerObserver.manager?.activity?.let {
            reviewManager.launchReview(it, info)
        }
    }
}
