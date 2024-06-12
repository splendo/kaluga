package com.splendo.kaluga.architecture.compose.viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.splendo.kaluga.architecture.compose.activity
import com.splendo.kaluga.architecture.compose.lifecycle.ComposableLifecycleSubscribable
import com.splendo.kaluga.architecture.compose.lifecycle.composableLifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.architecture.viewmodel.LifecycleSubscribableManager

/**
 * Composable which manages [viewModel] lifecycle and binds to all its [BaseLifecycleViewModel.activeLifecycleSubscribables].
 * This automatically modifies the content using [ComposableLifecycleSubscribable.modifier].
 * @param ViewModel the type of [BaseLifecycleViewModel] to manage.
 * @param viewModel [ViewModel] to manage
 * @param content content based on [viewModel]
 */
@Composable
fun <ViewModel : BaseLifecycleViewModel> ViewModelComposable(viewModel: ViewModel, content: @Composable (ViewModel.() -> Unit)? = null) {
    LocalContext.current.activity?.let {
        ViewModelComposable(it, it.supportFragmentManager, viewModel, content)
    }
}

/**
 * Composable which manages [viewModel] lifecycle and binds to all its [ActivityLifecycleSubscribable] using [fragmentManager].
 * This automatically modifies the content using [ComposableLifecycleSubscribable.modifier].
 * @param ViewModel the type of [BaseLifecycleViewModel] to manage.
 * @param viewModel [ViewModel] to manage
 * @param fragmentManager The [FragmentManager] to bind to all [ActivityLifecycleSubscribable] in [BaseLifecycleViewModel.activeLifecycleSubscribables].
 * @param content content based on [viewModel]
 */
@Composable
fun <ViewModel : BaseLifecycleViewModel> FragmentViewModelComposable(viewModel: ViewModel, fragmentManager: FragmentManager, content: @Composable (ViewModel.() -> Unit)? = null) =
    ViewModelComposable(LocalContext.current.activity, fragmentManager, viewModel, content)

@Composable
private fun <ViewModel : BaseLifecycleViewModel> ViewModelComposable(
    activity: AppCompatActivity?,
    fragmentManager: FragmentManager,
    viewModel: ViewModel,
    content: @Composable (ViewModel.() -> Unit)? = null,
) {
    // Link the ViewModel to existing LifecycleSubscribable
    viewModel.linkLifecycle(activity, fragmentManager)

    // Get a List of all ComposableLifecycleSubscribable of the viewModel.
    val composeLifecycleSubscribables = viewModel.composableLifecycleSubscribable()

    // If no ComposableLifecycleSubscribable available, just show content
    if (composeLifecycleSubscribables.value.isEmpty()) {
        content?.invoke(viewModel)
    } else {
        // Otherwise, modify the content using the modifier of all ComposableLifecycleSubscribable
        // Reduce right so the first ComposableLifecycleSubscribable acts as the first modifier (since we're wrapping)
        val modifier = composeLifecycleSubscribables.value.reduceRight { new, acc ->
            object : ComposableLifecycleSubscribable {
                override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = { modifiedContent ->
                    new.modifier(this) { acc.modifier(this, modifiedContent) }
                }
            }
        }
        modifier.modifier(viewModel) { content?.invoke(viewModel) }
    }
}

@Composable
private fun <ViewModel : BaseLifecycleViewModel> handleLocalViewModelStore(viewModel: ViewModel): ViewModel {
    // we delegate VM cleanup to the ViewModelStore, which lives in scope of the current @Composable
    val viewModelStoreOwner = rememberComposableViewModelStoreOwner(viewModel)

    // ViewModelProvider is the one, who can access ViewModelStore.put()
    val viewModelProvider = remember(viewModelStoreOwner) {
        ViewModelProvider(
            viewModelStoreOwner,
            @Suppress("UNCHECKED_CAST")
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T = viewModel as T
            },
        )
    }
    // actual injection of the VM into the ViewModelStore
    viewModelProvider.get(viewModel::class.java)

    return viewModel
}

@Composable
private fun rememberComposableViewModelStoreOwner(viewModel: BaseLifecycleViewModel): ViewModelStoreOwner {
    val viewModelStoreOwner = remember(viewModel) {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = ViewModelStore()
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModelStoreOwner.viewModelStore.clear()
        }
    }

    return viewModelStoreOwner
}

@Composable
private fun <ViewModel : BaseLifecycleViewModel> ViewModel.linkLifecycle(activity: AppCompatActivity?, fragmentManager: FragmentManager): ViewModel {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        val observer = VmObserver(this@linkLifecycle, activity, fragmentManager)
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            observer.onDispose()
        }
    }
    return this
}

private class VmObserver<ViewModel : BaseLifecycleViewModel>(
    private val viewModel: ViewModel,
    activity: AppCompatActivity?,
    fragmentManager: FragmentManager,
) : DefaultLifecycleObserver {

    private val manager = LifecycleSubscribableManager(viewModel, activity, fragmentManager)
    private var resumed = false

    override fun onCreate(owner: LifecycleOwner) {
        manager.onCreate(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        viewModel.didResume().also { resumed = true }
    }
    override fun onPause(owner: LifecycleOwner) {
        viewModel.didPause().also { resumed = false }
    }

    fun onDispose() {
        if (resumed) {
            viewModel.didPause()
        }
        manager.onDestroy()
    }
}
