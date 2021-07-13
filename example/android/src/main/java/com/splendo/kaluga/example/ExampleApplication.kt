/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example

import android.app.Application
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.example.di.utilitiesModule
import com.splendo.kaluga.example.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalStdlibApi
class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationHolder.application = this

        startKoin {
            androidContext(this@ExampleApplication)
            modules(utilitiesModule, viewModelModule)
        }
    }
}
