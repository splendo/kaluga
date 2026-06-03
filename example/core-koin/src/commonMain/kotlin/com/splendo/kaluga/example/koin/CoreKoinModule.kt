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

package com.splendo.kaluga.example.koin

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/** The only cross-cutting singleton in `:core-koin`: a shared [Logger] consumed by feature
 *  modules (notably `:feature-bluetooth` and `:feature-location`, which thread it into their
 *  Kaluga manager builders). Anything feature-specific lives in that feature's own Koin module. */
val loggerModule: Module = module {
    single<Logger> { RestrictedLogger(RestrictedLogLevel.None) }
}

internal fun bootstrap(appDeclaration: KoinAppDeclaration, customModules: List<Module>) = startKoin {
    appDeclaration()
    modules(loggerModule, *customModules.toTypedArray())
}

/** Bootstrap Koin once per process. The host (Android `Application.onCreate`, iOS `AppDelegate`,
 *  macOS `applicationDidFinishLaunching`) passes the feature Koin modules it wants loaded; the
 *  platform actual sets up the platform's Koin context (e.g. `androidContext`). */
expect fun initKoin(customModules: List<Module> = emptyList()): Unit
