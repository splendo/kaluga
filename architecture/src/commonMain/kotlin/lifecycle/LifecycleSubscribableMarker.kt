/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.lifecycle

import com.splendo.kaluga.architecture.viewmodel.LifecycleViewModel

/**
 * Implementing this interface as a public property of a [LifecycleViewModel] allows for automatic binding to platform specific lifecycle aware manager.
 *
 * Be aware that classes implementing this interface that reside on a ViewModel as a property will have their getter invoked on instantiating the ViewModel to check if the instance is lifecycle aware (if the platform supports this).
 * This means, for example, that `lazy` delegation will have little effect.
 */
interface LifecycleSubscribableMarker
