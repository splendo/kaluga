package com.splendo.kaluga.loadingIndicator

import platform.UIKit.UIModalPresentationOverFullScreen
import platform.UIKit.UIModalTransitionStyleCrossDissolve
import platform.UIKit.UIViewController

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

actual typealias View = UIViewController

class IOSLoadingIndicator private constructor(private val view: View): LoadingIndicator {

    class Builder: LoadingIndicator.Builder {
        override var view: View? = null
        override fun create(): LoadingIndicator {
            require(view != null) { "Please set a view first" }
            return IOSLoadingIndicator(view!!)
        }
    }

    init {
        view.modalPresentationStyle = UIModalPresentationOverFullScreen
        view.modalTransitionStyle = UIModalTransitionStyleCrossDissolve
    }

    override fun present(parent: View) {
        parent.presentViewController(view, true, null)
    }

    override fun dismiss() {
        view.dismissViewControllerAnimated(true, null)
    }
}
