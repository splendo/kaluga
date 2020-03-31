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

package kotlin

import com.splendo.kaluga.firebase.DataTask
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.cinterop.StableRef
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey

class DataTaskTests {

    @Test
    fun `test DataTask success offline case`() {

        val dataTask = DataTask<String>()

        fun successfulTask(): String {
            // No callback called here
            return "OK"
        }

        dataTask.value = successfulTask()
        dataTask.addOnSuccessListener {
            assertEquals(it, "OK")
        }
        dataTask.addOnFailureListener {
            // Shouldn't be called
            assertTrue(false)
        }
    }

    @Test
    fun `test DataTask success online case`() {

        val dataTask = DataTask<String>()
        val ref = StableRef.create(dataTask)
        val completion = { error: NSError? ->
            val task = ref.get()
            ref.dispose()
            if (error != null) {
                task.error = error
            }
        }

        fun successfulTask(completion: (NSError?) -> Unit): String {
            // No error case
            completion(null)
            return "OK"
        }

        dataTask.value = successfulTask(completion.freeze())
        dataTask.addOnSuccessListener {
            assertEquals(it, "OK")
        }
        dataTask.addOnFailureListener {
            // Shouldn't be called
            assertTrue(false)
        }
    }

    @Test
    fun `test DataTask failed case`() {

        val dataTask = DataTask<String>()
        val ref = StableRef.create(dataTask)
        val completion = { error: NSError? ->
            val task = ref.get()
            ref.dispose()
            if (error != null) {
                task.error = error
            }
        }

        fun failedTask(completion: (NSError?) -> Unit): String {
            completion(NSError.errorWithDomain(
                "com.splendo.kaluga.firebase",
                0,
                mapOf(NSLocalizedDescriptionKey to "ERROR")
            ))
            return "OK"
        }

        dataTask.value = failedTask(completion.freeze())
        dataTask.addOnSuccessListener {
            assertEquals(it, "OK")
        }
        dataTask.addOnFailureListener {
            assertEquals(it.message, "ERROR")
        }
    }
}
