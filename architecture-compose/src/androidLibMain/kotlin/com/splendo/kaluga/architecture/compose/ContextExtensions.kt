/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

internal val Context.activity: AppCompatActivity? get() = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.activity // recursive lookup
    else -> null
}
