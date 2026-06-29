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

package com.splendo.kaluga.base

import android.app.Application
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder.applicationContext

/**
 * Class holding reference to the [Application] [Context] running Kaluga
 * set [applicationContext] to your Application so default constructors work with the proper [Context]
 */
object ApplicationHolder {

    private var _applicationContext: Context? = null
        set(value) {
            check(field == null) { "Application object can only be set once." }
            field = value
        }

    /**
     * Indicates whether [Application] [Context] was set
     */
    val isInitialized: Boolean get() = _applicationContext != null

    /**
     * The [Context] of the [Application] running Kaluga
     */
    var applicationContext: Context
        get() {
            val context = _applicationContext
            checkNotNull(context) {
                "You've used ApplicationHolder.applicationContext without setting it on this holder " +
                    "(you should do this from Application.onCreate() or in your test)"
            }
            return context
        }
        set(value) {
            _applicationContext = value.applicationContext
        }
}
