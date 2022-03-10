package com.splendo.kaluga.architecture.compose.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel

/**
 * Composable which manages [viewModel] lifecycle and optionally adds it to local [ViewModelStore].
 * @param viewModel view model to manage
 * @param content content based on [viewModel]
 */
@Composable
fun <ViewModel : BaseViewModel> ViewModelComposable(
    viewModel: ViewModel,
    content: @Composable (ViewModel.() -> Unit)? = null
) {
    viewModel.linkLifecycle()
    content?.invoke(viewModel)
}

/**
 * Stores a view model in the local [ViewModelStore]. Use if the view model
 * was created manually and is not located in Activity/Fragment [ViewModelStore].
 */
@Composable fun <VM : BaseViewModel> store(provider: @Composable () -> VM): VM =
    provider().also { handleLocalViewModelStore(it) }

@Composable
private fun <VM : BaseViewModel> handleLocalViewModelStore(viewModel: VM): VM {
    // we delegate VM cleanup to the ViewModelStore, which lives in scope of the current @Composable
    val viewModelStoreOwner = rememberComposableViewModelStoreOwner(viewModel)

    // ViewModelProvider is the one, who can access ViewModelStore.put()
    val viewModelProvider = remember(viewModelStoreOwner) {
        ViewModelProvider(
            viewModelStoreOwner,
            @Suppress("UNCHECKED_CAST")
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                    viewModel as T
            }
        )
    }
    // actual injection of the VM into the ViewModelStore
    viewModelProvider.get(viewModel::class.java)

    return viewModel
}

@Composable
private fun rememberComposableViewModelStoreOwner(viewModel: BaseViewModel): ViewModelStoreOwner {
    val viewModelStoreOwner = remember(viewModel) {
        val viewModelStore = ViewModelStore()
        ViewModelStoreOwner { viewModelStore }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModelStoreOwner.viewModelStore.clear()
        }
    }

    return viewModelStoreOwner
}

@Composable
private fun <VM : BaseViewModel> VM.linkLifecycle(): VM {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        val observer = VmObserver(this@linkLifecycle)

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            observer.onDispose()
        }
    }
    return this
}

private class VmObserver<VM : BaseViewModel>(private val viewModel: VM) : LifecycleObserver {
    private var resumed = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() = viewModel.didResume().also { resumed = true }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = viewModel.didPause().also { resumed = false }

    fun onDispose() {
        if (resumed) {
            viewModel.didPause()
        }
    }
}
