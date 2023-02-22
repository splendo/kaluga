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

package com.splendo.kaluga.links.manager

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val name: String,
    val surname: String,
    val spokenLanguages: List<Languages> = emptyList()
) {
    companion object {
        val dummyUrl = "http://url.com?name=Corrado&surname=Quattrocchi&spokenLanguageSize=3&spokenLanguages=ITALIAN&spokenLanguages=ENGLISH&spokenLanguages=DUTCH"
        val dummyPerson = Person(
            "Corrado",
            "Quattrocchi",
            listOf(Languages.ITALIAN, Languages.ENGLISH, Languages.DUTCH)
        )
    }
}

enum class Languages {
    ITALIAN,
    ENGLISH,
    DUTCH,
}
