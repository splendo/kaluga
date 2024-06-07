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

package com.splendo.kaluga.example

import android.app.Application
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.example.keyboard.compose.composeKeyboardViewModel
import com.splendo.kaluga.example.keyboard.xml.XMLKeyboardActivity
import com.splendo.kaluga.example.shared.di.initKoin
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.keyboard.ViewKeyboardManager
import com.splendo.kaluga.keyboard.compose.ComposeKeyboardManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationHolder.application = this

        initKoin(
            listOf(
                module {
                    viewModel(named(composeKeyboardViewModel)) { (keyboardBuilder: ComposeKeyboardManager.Builder) ->
                        KeyboardViewModel(keyboardBuilder)
                    }
                    viewModel(named(XMLKeyboardActivity.VIEW_MODEL_NAME)) { (keyboardBuilder: ViewKeyboardManager.Builder) ->
                        KeyboardViewModel(keyboardBuilder)
                    }
                },
            ),
        )
    }
}
