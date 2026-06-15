/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.example.koin.initKoin as initCoreKoin
import org.koin.core.module.Module

/**
 * macOS bootstrap. Only [sharedFeaturesModule] is loaded — mobile-only features are not linked
 * into the macOS variant of `KalugaExampleShared.framework`. The Swift `main.swift` calls this
 * through `DependencyInjectionKt.doInitKoin(customModules: [])`.
 */
fun initKoin(customModules: List<Module> = emptyList()) = initCoreKoin(
    customModules = listOf(sharedFeaturesModule) + customModules,
)
