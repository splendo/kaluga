/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands
 
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

package com.splendo.kaluga.resources

expect class Color

expect val Color.red: Double
expect val Color.redInt: Int
expect val Color.green: Double
expect val Color.greenInt: Int
expect val Color.blue: Double
expect val Color.blueInt: Int
expect val Color.alpha: Double
expect val Color.alphaInt: Int

expect fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double = 1.0): Color
expect fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int = 255): Color
