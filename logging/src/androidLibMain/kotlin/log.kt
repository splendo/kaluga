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

@file:JvmName("AndroidLogKT")

package com.splendo.kaluga.logging

import android.os.Build
import io.github.aakira.napier.DebugAntilog
import java.lang.Integer.min

/**
 * The default [Logger] to be used if no logger is set.
 */
actual val defaultLogger: Logger =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        TransformLogger(
            NapierLogger(DebugAntilog()),
            transformTag = { tag ->
                tag?.let {
                    val maxSize = min(23, tag.length - 1)
                    if (maxSize >= 1) {
                        tag.substring(0..maxSize)
                    } else {
                        tag
                    }
                }
            },
        )
    } else {
        NapierLogger(DebugAntilog())
    }

/**
 * The standard [Logger] to be used when logging.
 *
 * This is used by the package level logging methods, and might be used directly as well
 */
actual var logger: Logger = defaultLogger
