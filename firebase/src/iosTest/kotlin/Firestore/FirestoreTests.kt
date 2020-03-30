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

package com.splendo.kaluga.firebase.firestore

import Firebase.FirebaseCore.FIRApp
import kotlin.test.BeforeClass
import kotlin.test.Test
import kotlin.test.assertNotNull

class FirestoreTests {

    // @BeforeAll
    companion object {
        @BeforeClass
        fun setUp() {
            FIRApp.configure()
        }
    }

    @Test
    fun `Test if Firestore instance exists`() {
        getFirestoreInstance()
    }

    @Test
    fun `Test document method`() {
        assertNotNull(
            getFirestoreInstance().document("/some/path")
        )
    }
}
