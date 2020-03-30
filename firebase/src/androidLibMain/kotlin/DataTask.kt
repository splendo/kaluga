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

import com.google.android.gms.tasks.Task

actual class DataTask<T>(private val task: Task<T>) {

    actual fun addOnSuccessListener(onSuccessListener: (T) -> Unit) {
        task.addOnSuccessListener(onSuccessListener)
    }

    actual fun addOnFailureListener(onFailureListener: (Exception) -> Unit) {
        task.addOnFailureListener(onFailureListener)
    }
}
