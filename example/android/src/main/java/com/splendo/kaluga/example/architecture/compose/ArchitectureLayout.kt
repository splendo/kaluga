package com.splendo.kaluga.example.architecture.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.mutableState
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRouteController
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetSheetContentRouteController
import com.splendo.kaluga.architecture.compose.navigation.ModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.navigation.NavHostRouteController
import com.splendo.kaluga.architecture.compose.navigation.NavigatingModalBottomSheetLayout
import com.splendo.kaluga.architecture.compose.navigation.composable
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.storeAndRemember
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.example.bottomSheet.ui.BottomSheetLayout
import com.splendo.kaluga.example.bottomSheet.ui.BottomSheetSubPageLayout
import com.splendo.kaluga.example.bottomSheet.viewModel.architectureNavigationRouteMapper
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.resources.compose.Composable

class ArchitectureActivity : AppCompatActivity() {

    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this
            ) {
                ArchitectureLayout()
            }
        }
    }
}

@Composable
fun ArchitectureLayout() {
    MdcTheme {
        val architectureRouteController = BottomSheetRouteController(
            NavHostRouteController(rememberNavController()),
            BottomSheetSheetContentRouteController(
                rememberNavController(),
                rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
                rememberCoroutineScope()
            )
        )

        architectureRouteController.NavigatingModalBottomSheetLayout(
            sheetContent = { contentNavHostController, sheetContentNavHostController, sheetState ->
                composable(ArchitectureNavigationAction.BottomSheet.route()) {
                    BottomSheetLayout(contentNavHostController, sheetContentNavHostController, sheetState)
                }
                composable(BottomSheetNavigation.SubPage.route()) {
                    BottomSheetSubPageLayout(
                        contentNavHostController, sheetContentNavHostController, sheetState
                    )
                }
            },
            contentRoot = { contentNavHostController, sheetContentNavHostController, sheetState ->
                ArchitectureLayoutLayoutContent(contentNavHostController, sheetContentNavHostController, sheetState)
            },
            content = { contentNavHostController, _, _ ->
                composable<InputDetails, ArchitectureNavigationAction.Details>(
                    type = NavigationBundleSpecType.SerializedType(InputDetails.serializer())
                ) { inputDetails ->
                    ArchitectureDetailsLayout(inputDetails, contentNavHostController)
                }
            }
        )
    }
}

@Composable
fun ArchitectureLayoutLayoutContent(contentNavHostController: NavHostController, sheetNavHostController: NavHostController, sheetState: ModalBottomSheetState) {

    val navigator = ModalBottomSheetNavigator(
        NavHostRouteController(contentNavHostController),
        BottomSheetSheetContentRouteController(
            sheetNavHostController,
            sheetState,
            rememberCoroutineScope()
        ),
        ::architectureNavigationRouteMapper
    )

    val viewModel = storeAndRemember {
        ArchitectureViewModel(navigator)
    }

    ViewModelComposable(viewModel) {
        val nameInput = viewModel.nameInput.mutableState()
        val isNameValid by viewModel.isNameValid.state()
        val numberInput = viewModel.numberInput.mutableState()
        val isNumberValid by viewModel.isNumberValid.state()

        Column(Modifier.fillMaxWidth()) {
            viewModel.showDetailsButton.Composable(modifier = Modifier.fillMaxWidth())
        }
    }
}