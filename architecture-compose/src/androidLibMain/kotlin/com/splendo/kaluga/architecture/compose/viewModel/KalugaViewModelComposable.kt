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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

@Composable
inline fun <reified ViewModel : BaseViewModel> getViewModel(vararg params: Any?): ViewModel {
    val owner = ViewModelOwner.from(LocalViewModelStoreOwner.current!!)
    return getKoin().getViewModel(owner = { owner }) {
        parametersOf(*params)
    }
}

/** Composable which manages [viewModel] lifecycle. */
@Composable
fun <ViewModel : BaseViewModel> ViewModelComposable(
    viewModel: ViewModel,
    isViewModelStoreElement: Boolean,
    content: @Composable ((ViewModel) -> Unit)? = null
) {
    if (!isViewModelStoreElement) {
        handleLocalViewModelStore(viewModel)
    }

    viewModel.linkLifecycle()

    content?.invoke(viewModel)
}

@Composable
private fun <ViewModel : BaseViewModel> handleLocalViewModelStore(viewModel: ViewModel) {
    // we delegate VM cleanup to the ViewModelStore, which lives in scope of the current @Composable
    val viewModelStoreOwner = rememberComposableViewModelStoreOwner(viewModel)

    // ViewModelProvider is the one, who can access ViewModelStore.put()
    val viewModelProvider = remember(viewModelStoreOwner) {
        ViewModelProvider(
            viewModelStoreOwner,
            @Suppress("UNCHECKED_CAST")
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T =
                    viewModel as T
            }
        )
    }
    // actual injection of the VM into the ViewModelStore
    viewModelProvider.get(viewModel::class.java)
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
private fun <VM : BaseViewModel> VM.linkLifecycle() {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        val lifecycleObserver = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() = didResume()

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() = didPause()

        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
