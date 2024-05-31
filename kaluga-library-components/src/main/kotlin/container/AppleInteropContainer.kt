/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package container

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultCInteropSettings
import javax.inject.Inject

class AppleInteropContainer @Inject constructor(
    objects: ObjectFactory,
) {
    internal val main = mutableListOf<Action<NamedDomainObjectContainer<DefaultCInteropSettings>>>()
    internal val test = mutableListOf<Action<NamedDomainObjectContainer<DefaultCInteropSettings>>>()

    fun main(action: Action<NamedDomainObjectContainer<DefaultCInteropSettings>>) {
        main.add(action)
    }

    fun test(action: Action<NamedDomainObjectContainer<DefaultCInteropSettings>>) {
        test.add(action)
    }
}
