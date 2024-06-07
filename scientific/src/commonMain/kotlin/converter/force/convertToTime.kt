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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("forceDivYank")
infix operator fun <ForceUnit : Force, YankUnit : Yank> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(yank: ScientificValue<PhysicalQuantity.Yank, YankUnit>) =
    yank.unit.per.time(this, yank)
