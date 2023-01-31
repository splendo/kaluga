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

actual class ReviewManager(
    private val reviewManager: com.google.android.play.core.review.ReviewManager,
    private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver()
) {

    enum class Type {
        Live,
        Fake;

        internal fun reviewManager(context: Context): com.google.android.play.core.review.ReviewManager = when (this) {
            Live -> ReviewManagerFactory.create(context)
            Fake -> FakeReviewManager(context)
        }
    }

    actual class Builder(
        private val type: Type = Type.Live,
        private val context: Context = ApplicationHolder.applicationContext,
        private val lifecycleManagerObserver: LifecycleManagerObserver = LifecycleManagerObserver()
    ) : LifecycleSubscribable, ActivityLifecycleSubscribable by lifecycleManagerObserver {
        actual fun create(): ReviewManager = ReviewManager(type.reviewManager(context), lifecycleManagerObserver)
    }

    actual suspend fun attemptToRequestReview() {
        val info = reviewManager.requestReview()
        lifecycleManagerObserver.manager?.activity?.let {
            reviewManager.launchReview(it, info)
        }
    }
}
