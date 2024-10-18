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
import com.splendo.kaluga.base.ApplicationHolder.Companion.application

/**
 * Class holding reference to the [Application] running Kaluga
 * set [application] to your Application so default constructors work with the proper [Context]
 */
class ApplicationHolder {
    companion object {

        /**
         * The [Application] running Kaluga
         */
        var application: Application? = null
            set(application) {
                check(field == null) { "Application object can only be set once." }
                field = application
            }

        /**
         * The [Context] of the [application]
         */
        val applicationContext: Context
            get() {
                val application = this.application
                checkNotNull(application) {
                    "You've used ApplicationHolder.applicationContext without setting the Application on this holder " +
                        "(you should do this from Application.onCreate() or in your test)"
                }

                val context = application.applicationContext
                checkNotNull(context) {
                    "ApplicationContext is null, this should not happen during runtime if you set a real Application instance on this holder." +
                        "For testing make sure you set an Application with a real or mock ApplicationContext available."
                }

                return context
            }
    }
}
