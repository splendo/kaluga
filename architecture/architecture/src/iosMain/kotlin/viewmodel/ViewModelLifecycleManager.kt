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

package com.splendo.kaluga.architecture.viewmodel

import com.splendo.kaluga.architecture.observable.Disposable
import com.splendo.kaluga.architecture.observable.DisposeBag
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.willMoveToParentViewController

/**
 * Holds the lifecycle of a [BaseLifecycleViewModel].
 * A single lifecycle starts at [UIViewController.viewDidAppear] and ends at [UIViewController.viewDidDisappear] of the bound [UIViewController]
 * This convenience class is the result of [BaseLifecycleViewModel.addLifecycleManager].
 * Invoke [unbind] to unbind the Lifecycle from its bound [UIViewController]
 */
class LifecycleManager internal constructor(clearViewModel: () -> Unit) {

    internal val disposeBag: DisposeBag = DisposeBag()
    private var onUnbind: (() -> Unit)? = clearViewModel

    /**
     * Unbinds the [BaseLifecycleViewModel]
     */
    fun unbind() {
        onUnbind?.invoke()
        onUnbind = null
    }
}

/**
 * Callback invoked at the start of a lifecycle (corresponding to [UIViewController.viewDidAppear] of the bound ViewController.
 * Returns a List [Disposable] that are cleaned automatically at the end of each lifecycle.
 */
typealias onLifeCycleChanged = () -> List<Disposable>

internal class ViewModelLifecycleManager<ViewModel : BaseLifecycleViewModel>(private val viewModel: ViewModel, private val onLifecycle: onLifeCycleChanged) :
    UIViewController(null, null) {

    internal val lifecycleManager = LifecycleManager {
        viewModel.clear()
        willMoveToParentViewController(null)
        view.removeFromSuperview()
        removeFromParentViewController()
    }

    override fun viewDidAppear(animated: Boolean) {
        super.viewDidAppear(animated)

        viewModel.didResume()
        onLifecycle().forEach {
            lifecycleManager.disposeBag.add(it)
        }
    }

    override fun viewDidDisappear(animated: Boolean) {
        super.viewDidDisappear(animated)

        viewModel.didPause()
        lifecycleManager.disposeBag.dispose()
    }
}

/**
 * Adds a manager to automatically bind the [LifecycleManager] of a [BaseLifecycleViewModel] to a [UIViewController].
 * This is achieved by adding an invisible child [UIViewController].
 * @param parent The containing [UIViewController]
 * @param onLifecycle This callback is invoked on each start of a new lifecycle.
 * @return The [LifecycleManager] bound to the [BaseLifecycleViewModel]
 */
fun <ViewModel : BaseLifecycleViewModel> ViewModel.addLifecycleManager(parent: UIViewController, onLifecycle: onLifeCycleChanged): LifecycleManager {
    val lifecycleManager = ViewModelLifecycleManager(this, onLifecycle)
    parent.addChildViewController(lifecycleManager)
    parent.view.addSubview(lifecycleManager.view)
    lifecycleManager.view.userInteractionEnabled = false
    lifecycleManager.didMoveToParentViewController(parent)
    return lifecycleManager.lifecycleManager
}
