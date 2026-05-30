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

package com.splendo.kaluga.example.feature.media

import com.splendo.kaluga.example.arch.FeatureContribution
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

const val MEDIA_FEATURE_ID = "media"

private class MediaContribution : FeatureContribution {
    override val id = MEDIA_FEATURE_ID
    override val label = "Media"
    override val isCompose = false
}

val mediaFeatureModule: Module = module {
    single { MediaContribution() } bind FeatureContribution::class
}
