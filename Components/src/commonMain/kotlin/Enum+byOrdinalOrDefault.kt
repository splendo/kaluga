package com.splendo.kaluga.util
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
 * Returns the enum by matching ordinals position, or the given default value if no ordinals match.
 *
 * @param ordinal The ordinal value in this enum
 * @param defaultValue The default value to return if the ordinal is out of range
 *
 * @return The enum value matching the ordinal, or the default.
 *
 */
inline fun <reified T: Enum<T>> Enum.Companion.byOrdinalOrDefault(ordinal:Int, defaultValue:T): T {
    return if (ordinal !in enumValues<T>().indices)
        defaultValue
    else
        enumValues<T>()[ordinal]
}