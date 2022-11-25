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

package com.splendo.kaluga.architecture.compose.lifecycle

import androidx.compose.runtime.Composable
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribableMarker
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

interface ComposableLifecycleSubscribable : LifecycleSubscribableMarker {
    val modifier: @Composable (@Composable () -> Unit) -> Unit
}

@Suppress("UNCHECKED_CAST")
internal val <VM : BaseLifecycleViewModel> VM.ComposableLifecycleSubscribable: List<ComposableLifecycleSubscribable> get() = this::class.memberProperties
    .filter { it !is KMutableProperty1 }
    .mapNotNull { it as? KProperty1<VM, Any?> }
    .filter {
        it.getter.visibility == KVisibility.PUBLIC &&
            it.getter.returnType.isSubtypeOf(LifecycleSubscribableMarker::class.starProjectedType)
    }
    .mapNotNull { it.getter(this) as? ComposableLifecycleSubscribable }
