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

package com.splendo.kaluga.plugin.container

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import javax.inject.Inject

open class MultiplatformDependencyContainer @Inject constructor(
    objects: ObjectFactory,
) {

    sealed class TargetDependencyContainer {
        internal val mainDependencies = mutableListOf<Action<KotlinDependencyHandler>>()
        internal val testDependencies = mutableListOf<Action<KotlinDependencyHandler>>()

        fun main(action: Action<KotlinDependencyHandler>) {
            mainDependencies.add(action)
        }

        fun test(action: Action<KotlinDependencyHandler>) {
            testDependencies.add(action)
        }
    }

    open class Common : TargetDependencyContainer()
    open class Android : TargetDependencyContainer() {

        internal val instrumentedTestDependencies = mutableListOf<Action<KotlinDependencyHandler>>()

        fun instrumented(action: Action<KotlinDependencyHandler>) {
            instrumentedTestDependencies.add(action)
        }
    }

    open class Apple : TargetDependencyContainer()
    open class IOS : TargetDependencyContainer()
    open class JS : TargetDependencyContainer()
    open class JVM : TargetDependencyContainer()

    internal val common = objects.newInstance(Common::class.java)
    internal val android = objects.newInstance(Android::class.java)
    internal val apple = objects.newInstance(Apple::class.java)
    internal val ios = objects.newInstance(IOS::class.java)
    internal val js = objects.newInstance(JS::class.java)
    internal val jvm = objects.newInstance(JVM::class.java)
    fun common(action: Action<Common>) {
        action.execute(common)
    }

    fun android(action: Action<Android>) {
        action.execute(android)
    }

    fun apple(action: Action<Apple>) {
        action.execute(apple)
    }

    fun ios(action: Action<IOS>) {
        action.execute(ios)
    }

    fun js(action: Action<JS>) {
        action.execute(js)
    }

    fun jvm(action: Action<JVM>) {
        action.execute(jvm)
    }
}
