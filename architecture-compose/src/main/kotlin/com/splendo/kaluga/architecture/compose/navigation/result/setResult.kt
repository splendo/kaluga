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

package com.splendo.kaluga.architecture.compose.navigation.result

import androidx.navigation.NavBackStackEntry
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.navigation.toBundle

internal fun NavBackStackEntry.setResult(result: Route.Result) = when (result) {
    is Route.Result.Empty -> savedStateHandle.remove(Route.Result.KEY)
    is Route.Result.Data<*, *> -> savedStateHandle[Route.Result.KEY] = result.bundle.toBundle()
}
