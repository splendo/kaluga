/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.scientific

import com.splendo.kaluga.architecture.navigation.DefaultNavigator
import com.splendo.kaluga.scientific.PhysicalQuantity

fun ScientificConverterNavigator(onSelectLeftUnit: (PhysicalQuantity) -> Unit, onSelectRightUnit: (PhysicalQuantity) -> Unit, onClose: () -> Unit) =
    DefaultNavigator<ScientificConverterNavigationAction<*>> { action ->
        when (action) {
            is ScientificConverterNavigationAction.SelectUnit.Left -> onSelectLeftUnit(action.value)
            is ScientificConverterNavigationAction.SelectUnit.Right -> onSelectRightUnit(action.value)
            is ScientificConverterNavigationAction.Close -> onClose()
        }
    }
