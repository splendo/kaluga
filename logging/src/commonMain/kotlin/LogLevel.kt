package com.splendo.kaluga.logging
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

enum class LogLevel(val logLevel: ru.pocketbyte.kydra.log.LogLevel) {

    /**
     * Debug Log Level. Should be shown only for debugging.
     */
    DEBUG(ru.pocketbyte.kydra.log.LogLevel.DEBUG),

    /**
     * Information Log Level.
     */
    INFO(ru.pocketbyte.kydra.log.LogLevel.INFO),

    /**
     * Warning Log Level. Should be used to log some warnings.
     */
    WARNING(ru.pocketbyte.kydra.log.LogLevel.WARNING),

    /**
     * Error Log Level. Should be used to log some errors.
     */
    ERROR(ru.pocketbyte.kydra.log.LogLevel.ERROR)
}