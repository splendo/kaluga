package com.splendo.kaluga.architecture.compose.viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
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
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribableMarker
import com.splendo.kaluga.architecture.lifecycle.subscribe
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * Composable which manages [viewModel] lifecycle and optionally adds it to local [ViewModelStore].
 * @param viewModel view model to manage
 * @param content content based on [viewModel]
 */
@Composable
fun <ViewModel : BaseLifecycleViewModel> ViewModelComposable(
    viewModel: ViewModel,
    content: @Composable (ViewModel.() -> Unit)? = null
) {
    LocalContext.current.activity?.let {
        ViewModelComposable(it, it.supportFragmentManager, viewModel, content)
    }
}

@Composable
fun <ViewModel : BaseLifecycleViewModel> FragmentViewModelComposable(
    fragmentManager: FragmentManager,
    viewModel: ViewModel,
    content: @Composable (ViewModel.() -> Unit)? = null
) = ViewModelComposable(LocalContext.current.activity, fragmentManager, viewModel, content)

@Composable
private fun <ViewModel : BaseLifecycleViewModel> ViewModelComposable(
    activity: AppCompatActivity?,
    fragmentManager: FragmentManager,
    viewModel: ViewModel,
    content: @Composable (ViewModel.() -> Unit)? = null
) {
    viewModel.linkLifecycle(activity, fragmentManager)
    val composeLifecycleSubscribables = remember(viewModel) {
        viewModel.ComposableLifecycleSubscribable
    }
    if (composeLifecycleSubscribables.isEmpty()) {
        content?.invoke(viewModel)
    } else {
        val modifier = composeLifecycleSubscribables.reduceRight { new, acc ->
            object : ComposableLifecycleSubscribable {
                override val modifier: @Composable BaseLifecycleViewModel.(@Composable BaseLifecycleViewModel.() -> Unit) -> Unit = { content ->
                    new.modifier(this) { acc.modifier(this, content) }
                }
            }
        }
        modifier.modifier(viewModel) { content?.invoke(viewModel) }
    }
}

/**
 * Stores a view model in the local [ViewModelStore]. Use if the view model
 * was created manually and is not located in Activity/Fragment [ViewModelStore].
 */
@Composable
@Deprecated(
    "Does not work for configuration changes (e.g. rotation).",
    replaceWith = ReplaceWith("viewModel()", "androidx.lifecycle.viewmodel.compose.viewModel")
)
fun <VM : BaseLifecycleViewModel> store(provider: @Composable () -> VM): VM =
    provider().also { handleLocalViewModelStore(it) }

/**
 * Stores and remembers a view model in the local [ViewModelStore].
 * Use if the view model was created manually and is not located in Activity/Fragment [ViewModelStore].
 * provider will only be evaluated during the composition. Recomposition will always return the value produced by provider.
 */
@Composable
@Deprecated(
    "Does not work for configuration changes (e.g. rotation).",
    replaceWith = ReplaceWith("viewModel()", "androidx.lifecycle.viewmodel.compose.viewModel")
)
fun <VM : BaseLifecycleViewModel> storeAndRemember(provider: @DisallowComposableCalls () -> VM): VM = store {
    remember(provider)
}

/**
 * Stores and remembers a view model in the local [ViewModelStore].
 * Use if the view model was created manually and is not located in Activity/Fragment [ViewModelStore].
 * provider will only be evaluated during the composition. Recomposition will always return the value produced by provider.
 */
@Composable
@Deprecated(
    "Does not work for configuration changes (e.g. rotation).",
    replaceWith = ReplaceWith("viewModel()", "androidx.lifecycle.viewmodel.compose.viewModel")
)
fun <VM : BaseLifecycleViewModel> storeAndRemember(key: Any?, provider: @DisallowComposableCalls () -> VM): VM = store {
    remember(key, provider)
}

@Composable
private fun <VM : BaseLifecycleViewModel> handleLocalViewModelStore(viewModel: VM): VM {
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
private fun rememberComposableViewModelStoreOwner(viewModel: BaseLifecycleViewModel): ViewModelStoreOwner {
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
private fun <VM : BaseLifecycleViewModel> VM.linkLifecycle(activity: AppCompatActivity?, fragmentManager: FragmentManager): VM {
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

private class VmObserver<VM : BaseLifecycleViewModel>(private val viewModel: VM, private val activity: AppCompatActivity?, private val fragmentManager: FragmentManager) : DefaultLifecycleObserver {
    private var resumed = false

    private val lifecycleSubscribables: List<LifecycleSubscribable> by lazy {
        @Suppress("UNCHECKED_CAST")
        viewModel::class.memberProperties
            .filter { it !is KMutableProperty1 }
            .mapNotNull { it as? KProperty1<VM, Any?> }
            .filter {
                it.getter.visibility == KVisibility.PUBLIC &&
                    it.getter.returnType.isSubtypeOf(LifecycleSubscribableMarker::class.starProjectedType)
            }
            .mapNotNull { it.getter(viewModel) as? LifecycleSubscribable }
    }

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleSubscribables.forEach { it.subscribe(activity, owner, fragmentManager) }
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
        lifecycleSubscribables.forEach { it.unsubscribe() }
    }
}
