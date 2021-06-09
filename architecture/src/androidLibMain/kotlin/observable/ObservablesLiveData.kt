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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.splendo.kaluga.architecture.observable.BaseSimpleDisposable
import com.splendo.kaluga.architecture.observable.BasicSubject
import com.splendo.kaluga.architecture.observable.Initialized
import com.splendo.kaluga.architecture.observable.ObservableOptional
import com.splendo.kaluga.architecture.observable.Uninitialized
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val <T>Uninitialized<T>.liveData:LiveData<T?>
    get() = stateFlow.asLiveData()

val <R:T, T>Initialized<R, T>.liveData:LiveData<R>
    get() = stateFlow.asLiveData()

val <R:T, T, OO: ObservableOptional<R>> BasicSubject<R, T, OO>.liveDataObserver: Observer<T>
    get() = Observer<T> { this.post(it) }

val <T> UninitializedSubject<T>.liveData: MutableLiveData<T?>
    get() {
        val mediatorLiveData = MediatorLiveData<T?>()
        mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
            mediatorLiveData.postValue(value)
        }
        boundCoroutine?.let { coroutineScope ->
            val observer = Observer<T?> { value -> value?.let { post(it) } }
            coroutineScope.launch(Dispatchers.Main.immediate) {
                mediatorLiveData.observeForever(observer)
                val neverCompleting = EmptyCompletableDeferred()
                neverCompleting.await()
            }.invokeOnCompletion {
                mediatorLiveData.removeObserver(observer)
            }
        }
        return mediatorLiveData
    }

val <T> InitializedSubject<T>.liveData: MutableLiveData<T>
    get() = mutableLiveData(liveDataObserver)

val <T, R:T> DefaultSubject<R, T>.liveData: MutableLiveData<R>
    get() {
        val observer = Observer<R> { post(it) }
        return mutableLiveData(observer)
    }

private fun <B, R:T, T, OO: ObservableOptional<R>> B.mutableLiveData(observer: Observer<R>): MutableLiveData<R> where B : BasicSubject<R, T, OO>, B : WithMutableState<R> {
    val mediatorLiveData = MediatorLiveData<R>()
    mediatorLiveData.addSource(stateFlow.asLiveData()) { value ->
        mediatorLiveData.postValue(value)
    }

    boundCoroutine?.let { coroutineScope ->
        coroutineScope.launch(Dispatchers.Main.immediate) {
            mediatorLiveData.observeForever(observer)
            val neverCompleting = EmptyCompletableDeferred()
            neverCompleting.await()
        }.invokeOnCompletion {
            mediatorLiveData.removeObserver(observer)
        }
    }
    return mediatorLiveData
}
