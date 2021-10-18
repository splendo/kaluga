/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.links.manager

import com.splendo.kaluga.links.utils.LinksDecoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.serializer

interface ParametersDecoder {
    fun <T> decodeFromList(list: List<NameValue>, deserializer: DeserializationStrategy<T>): T
}

class DefaultParametersDecoder : ParametersDecoder {

    /**
     * Given a list of [NameValue] and a [DeserializationStrategy] this method will check if the size of the deserializer's elements and passed list
     * are the same. If previous condition is met it returns the received list's values. Otherwise it compares the given parameters' name and deserializer elements' name
     * and returns NULL for those that don't match.
     */
    override fun <T> decodeFromList(
        list: List<NameValue>,
        deserializer: DeserializationStrategy<T>
    ): T {
        val valuesList = if (list.size == deserializer.descriptor.elementsCount) {
            list.map { it.second }
        } else {
            list.map { element ->
                if (deserializer.descriptor.elementNames.firstOrNull { it == element.first } != null) {
                    element.second
                } else {
                    LinksDecoder.NULL_SYMBOL
                }
            }
        }

        val decoder = LinksDecoder(ArrayDeque(valuesList))
        return decoder.decodeSerializableValue(deserializer)
    }
}

inline fun <reified T> DefaultParametersDecoder.decodeFromList(list: List<NameValue>): T =
    decodeFromList(list, serializer())
