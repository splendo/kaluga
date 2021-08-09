/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

@file:JvmName("DisposableJVMKt")
package com.splendo.kaluga.architecture.observable

actual class SimpleDisposable actual constructor(onDispose: DisposeHandler) : BaseSimpleDisposable(onDispose)

internal actual fun <R : T, T, OO : ObservableOptional<R>> addObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    observation.observers.add(observer)
}

internal actual fun <R : T, T, OO : ObservableOptional<R>> removeObserver(observation: Observation<R, T, OO>, observer: (R) -> Unit) {
    observation.observers.remove(observer)
}

internal actual fun <R : T, T, OO : ObservableOptional<R>> observers(observation: Observation<R, T, OO>): List<(R) -> Unit> =
    observation.observers
