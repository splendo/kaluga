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

package com.splendo.kaluga.example.platformspecific.compose.contacts

import androidx.compose.runtime.Composable
import com.splendo.kaluga.architecture.compose.viewModel.KalugaViewModelComposeActivity
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.platformspecific.compose.contacts.ui.ContactsLayout

class ContactsActivity : KalugaViewModelComposeActivity<BaseLifecycleViewModel>() {
    override val viewModel = BaseLifecycleViewModel()

    @Composable
    override fun Layout(viewModel: BaseLifecycleViewModel) {
        ContactsLayout()
    }
}
