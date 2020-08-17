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

/**
 * This class is used to wrap the KydraLog dependency.
 */
inline class InternalLogger(val logger: Logger) : ru.pocketbyte.kydra.log.Logger {

    override fun log(level: ru.pocketbyte.kydra.log.LogLevel, tag: String?, message: String) {
        logger.log(
            level.getLogLevel(),
            tag,
            message
        )
    }

    override fun log(level: ru.pocketbyte.kydra.log.LogLevel, tag: String?, exception: Throwable) {
        logger.log(
            level.getLogLevel(),
            tag,
            exception
        )
    }
}
