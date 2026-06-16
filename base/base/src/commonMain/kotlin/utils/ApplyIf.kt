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

package com.splendo.kaluga.base.utils

/**
 * Applies a given code block if a given condition is met
 * @param condition The condition that should be met
 * @param block The code block to execute
 */
inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T = apply {
    if (condition) block(this)
}
