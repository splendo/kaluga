/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.ksp.helpers

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

const val RETURN = "return"
const val WHEN = "when"
const val WITH = "with"
const val THIS = "this"

const val FORMAT = "format"
const val FROM_SERVICE = "fromService"

const val EXCEPTION = "exception"
const val IDENTIFIER = "identifier"
const val SERVICE = "service"
const val CHARACTERISTIC = "characteristic"
const val DESCRIPTOR = "descriptor"

const val CONFIGURE = "configure"
const val DELEGATE = "Delegate"

const val READ = "read"
const val ON_READ = "onRead"
const val WRITE = "write"
const val ON_WRITE = "onWrite"
const val ON_FAILED_TO_WRITE = "onFailedToWrite"
const val ON_SUBSCRIBE = "onSubscribeTo"
const val ON_UNSUBSCRIBE = "onUnsubscribeTo"
const val SUBSCRIBERS = "Subscribers"
const val ACTION = "Action"

const val NOTIFY = "notify"
const val NOTIFY_ALL = "notifyAll"
const val CHANGED = "Changed"

const val BUILD = "build"
const val BUILDER = "builder"

const val OFFSET = "offset"

val KSPropertyDeclaration.onReadMethodName: String get() = "$ON_READ${simpleName.asString().replaceFirstChar { it.uppercase() }}"
val KSPropertyDeclaration.onWriteMethodName: String get() = "$ON_WRITE${simpleName.asString().replaceFirstChar { it.uppercase() }}"