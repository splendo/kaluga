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

package com.splendo.kaluga.firebase

import platform.Foundation.NSError

actual class DataTask<T> {

    @Suppress("UNCHECKED_CAST")
    var value: T
        get() = if (internal != null) {
            println("value is read")
            internal as T
        } else {
            throw Exception("Value is not set")
        }

        set(value) {
            println("value is set")
            internal = value
        }

    private var internal: T? = null

    private var onSuccessListeners: MutableList<((T) -> Unit)> = mutableListOf()
    private var onFailureListeners: MutableList<((Exception) -> Unit)> = mutableListOf()

    actual fun addOnSuccessListener(onSuccessListener: (T) -> Unit) {
        println("success added")
        onSuccessListeners.add(onSuccessListener)
    }

    actual fun addOnFailureListener(onFailureListener: (Exception) -> Unit) {
        println("failure added")
        onFailureListeners.add(onFailureListener)
    }

    fun success() {
        println("success called")
        onSuccessListeners.forEach {
            it.invoke(value)
        }
    }

    fun failure(error: NSError) {
        println("failure called")
        onFailureListeners.forEach {
            it.invoke(Exception(error.localizedDescription))
        }
    }
}
