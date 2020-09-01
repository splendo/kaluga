/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.salesforce

import BaseSalesForceViewModel
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.ui.SalesforceActivityDelegate
import com.salesforce.androidsdk.ui.SalesforceActivityInterface
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity

/**
 * Convenience [AppCompatActivity] that is bound to a [ViewModel] and handles SalesForce Login
 */
abstract class KalugaSalesForceLoginActivity<VM : BaseSalesForceViewModel>(@LayoutRes layout: Int = 0) : KalugaViewModelActivity<VM>(layout), SalesforceActivityInterface {

    private val delegate = SalesforceActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate()
    }

    public override fun onResume() {
        super.onResume()
        delegate.onResume(true)
    }

    public override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    public override fun onDestroy() {
        delegate.onDestroy()
        super.onDestroy()
    }

    override fun onResume(client: RestClient?) {
        // here the SalesForce resources should be ready to use: restClient holds authentication info
        // UI should change automatically to/from Login WebView
        // TODO: inject AuthRepo(restClient) into VM here
    }

    override fun onLogoutComplete() {
        viewModel.onLogoutComplete()
    }

    override fun onUserSwitched() {
        viewModel.onUserSwitched()
    }
}
