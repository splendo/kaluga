package  com.splendo.kaluga.architecture.compose.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.KalugaViewModelComposeActivity
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private class ComposeOnBackPressedCallback(
    private val coroutineScope: CoroutineScope,
    private val onBackButtonClickHandler: suspend () -> Unit
) : OnBackPressedCallback(true) {

    override fun handleOnBackPressed() {
        coroutineScope.launch {
            onBackButtonClickHandler()
        }
    }

}

/**
 * Current implementation expects [KalugaViewModelComposeActivity] to be a host since it provides [LocalAppCompatActivity].
 */
@Composable
fun LifecycleSubscribable.bind() {
    LocalAppCompatActivity.current?.let {
        DisposableEffect(Unit) {
            subscribe(it)

            onDispose {
                unsubscribe()
            }
        }
    }

}

@Composable
fun linkHwBackButtonNavigation(onBackButtonClickHandler: suspend () -> Unit) {
    LocalOnBackPressedDispatcherOwner.current?.let {
        val onBackPressedDispatcher = it.onBackPressedDispatcher
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        val coroutineScope = rememberCoroutineScope()
        DisposableEffect(Unit) {
            val onBackPressedCallback = ComposeOnBackPressedCallback(
                coroutineScope,
                onBackButtonClickHandler
            )

            val lifecycleObserver = object : LifecycleObserver {

                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    onBackPressedCallback.remove()
                    onBackPressedDispatcher.addCallback(onBackPressedCallback)
                }

            }
            lifecycle.addObserver(lifecycleObserver)

            onDispose {
                lifecycle.removeObserver(lifecycleObserver)
                onBackPressedCallback.remove()
            }
        }
    }
}
