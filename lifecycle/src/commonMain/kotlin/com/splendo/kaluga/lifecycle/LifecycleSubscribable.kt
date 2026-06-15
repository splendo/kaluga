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

package com.splendo.kaluga.lifecycle

/**
 * Marker for any object that wants to bind to a platform host's lifecycle. Implementations live
 * in platform-specific source sets ([ActivityLifecycleSubscribable] on Android,
 * [ViewControllerLifecycleSubscribable] on iOS, [WindowLifecycleSubscribable] on macOS) and own a
 * `manager` property exposing the host handle (`Activity`, `UIViewController`, `NSWindow`).
 *
 * Cross-platform consumers (typically Kaluga ViewModels or service builders) hold a
 * `LifecycleSubscribable` so they can be wired into any host. The wiring itself is done either
 * by a Kaluga `BaseLifecycleViewModel` or by the Compose adapter in
 * `:lifecycle-compose` (`LifecycleSubscribable.AttachToCompose()`), which dispatches to the
 * appropriate platform subtype based on the current Compose context.
 */
interface LifecycleSubscribable
