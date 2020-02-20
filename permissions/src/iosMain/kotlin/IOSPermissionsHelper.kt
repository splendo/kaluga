package com.splendo.kaluga.permissions

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

import com.splendo.kaluga.log.debug
import com.splendo.kaluga.log.error
import platform.Foundation.NSBundle

class IOSPermissionsHelper {

    companion object {

        private const val TAG = "Permissions"
        fun checkDeclarationInPList(bundle: NSBundle, vararg declarationName: String): MutableList<String> {

            val missingDeclarations = declarationName.toMutableList()
            missingDeclarations.forEach { declaration ->
                try {

                    val objectForInfoDictionaryKey = bundle.objectForInfoDictionaryKey(declaration)

                    if (objectForInfoDictionaryKey == null) {
                        error(TAG, "$declaration was not declared")
                    } else {
                        debug(TAG, "$declaration was declared")
                        missingDeclarations.remove(declaration)
                    }

                } catch (error: Exception) {
                    error(TAG, error)
                }
            }

            return missingDeclarations
        }
    }
}