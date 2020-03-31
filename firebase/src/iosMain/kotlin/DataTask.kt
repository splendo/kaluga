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
        get() = if (internalValue != null) {
            println("value is read")
            internalValue as T
        } else {
            throw Exception("Value is not set")
        }

        set(value) {
            println("value is set")
            internalValue = value
            postUpdates()
        }

    var error: NSError
        get() = if (internalError != null) {
            println("error is read")
            internalError as NSError
        } else {
            throw Exception("Error is not set")
        }
        set(value) {
            println("error is set")
            internalError = value
            postUpdates()
        }

    private var internalValue: T? = null
    private var internalError: NSError? = null

    private var onSuccessListeners: MutableList<((T) -> Unit)> = mutableListOf()
    private var onFailureListeners: MutableList<((Exception) -> Unit)> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    private fun postUpdates() {
        if (internalValue != null) {
            onSuccessListeners.forEach { it.invoke(value) }
        }
        if (internalError != null) {
            onFailureListeners.forEach { it.invoke(Exception(error.localizedDescription)) }
        }
    }

    actual fun addOnSuccessListener(onSuccessListener: (T) -> Unit) {
        println("success added")
        onSuccessListeners.add(onSuccessListener)
        postUpdates()
    }

    actual fun addOnFailureListener(onFailureListener: (Exception) -> Unit) {
        println("failure added")
        onFailureListeners.add(onFailureListener)
        postUpdates()
    }
}
