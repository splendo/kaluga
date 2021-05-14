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
@file:JvmName("AndroidObservable")

package com.splendo.kaluga.architecture.observable

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.splendo.kaluga.architecture.observable.BaseSimpleDisposable
import com.splendo.kaluga.architecture.observable.BasicSubject
import com.splendo.kaluga.architecture.observable.Initialized
import com.splendo.kaluga.architecture.observable.ObservableOptional
import com.splendo.kaluga.architecture.observable.Uninitialized

val <T>Uninitialized<T>.liveData:LiveData<T?>
    get() = stateFlow.asLiveData()

val <R:T, T>Initialized<R, T>.liveData:LiveData<R>
    get() = stateFlow.asLiveData()

val <R:T, T, OO: ObservableOptional<R>> BasicSubject<R, T, OO>.liveDataObserver: Observer<T>
    get() = Observer<T> { this.post(it) }
