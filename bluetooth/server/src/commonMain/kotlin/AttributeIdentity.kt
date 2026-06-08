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

package com.splendo.kaluga.bluetooth.server

/**
 * Identity of a local Bluetooth attribute (service, characteristic or descriptor), used to correlate
 * a registered action with the matching incoming platform callback.
 *
 * Implementations equal each other when they refer to the same underlying platform attribute, so
 * distinct attributes that happen to share a [com.splendo.kaluga.bluetooth.UUID] do not collide.
 */
interface AttributeIdentity
