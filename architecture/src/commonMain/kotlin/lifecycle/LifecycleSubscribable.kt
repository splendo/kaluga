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

package com.splendo.kaluga.architecture.lifecycle

import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel

/**
 * This interface can be provided to a [BaseLifecycleViewModel] to bind to platform specific lifecycle.
 * Extend this on classes that need to have some setup during lifecycle events.
 */
interface LifecycleSubscribable
