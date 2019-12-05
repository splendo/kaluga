package com.splendo.kaluga.example.shared

import com.splendo.kaluga.hud.HUD

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

class HUD(private val builder: HUD.Builder) {

    fun showSystem() {
        // SYSTEM style by default
        // No title by default
        builder.build().present().dismissAfter(3_000)
    }
    
    fun showCustom() {
        builder.build { 
            setStyle(HUD.Style.CUSTOM)
            setTitle("This is a custom title")
        }.present().dismissAfter(3_000)
    }
}
