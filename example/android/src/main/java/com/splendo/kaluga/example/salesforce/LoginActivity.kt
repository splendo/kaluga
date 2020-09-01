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

package com.splendo.kaluga.example.salesforce

import android.os.Bundle
import androidx.lifecycle.Observer
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.salesforce.SalesForceViewModel
import com.splendo.kaluga.salesforce.KalugaSalesForceLoginActivity
import kotlinx.android.synthetic.main.activity_salesforce_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : KalugaSalesForceLoginActivity<SalesForceViewModel>(R.layout.activity_salesforce_login) {

    override val viewModel: SalesForceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.username.observe(this, Observer {
            username.text = it
        })
    }
}
