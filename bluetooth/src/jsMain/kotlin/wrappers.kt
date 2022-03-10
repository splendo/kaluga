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

package com.splendo.kaluga.bluetooth

actual interface CharacteristicWrapper {
    actual val uuid: UUID
    actual val descriptors: List<DescriptorWrapper>
    actual val value: Value?
    actual val properties: Int
}

actual interface DescriptorWrapper {
    actual val uuid: UUID
    actual val value: Value?
}

actual interface ServiceWrapper {
    actual val characteristics: List<CharacteristicWrapper>
    actual val uuid: UUID
}
