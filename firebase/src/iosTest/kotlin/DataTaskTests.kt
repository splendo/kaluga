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
import kotlinx.cinterop.StableRef
import platform.Foundation.NSError
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataTaskTests {

    @Test
    fun `test if data task works`() {
        val dataTask = DataTask<String>()
        val ref = StableRef.create(dataTask)
        val completion = { error: NSError? ->
            val task = ref.get()
            ref.dispose()
            if (error != null) {
                task.failure(error)
            } else {
                task.success()
            }
            println("completion was called")
        }
        fun asyncTask(completion: (NSError?) -> Unit): String {
            completion(NSError.errorWithDomain("eee", 123, null))
            return "OK"
        }
        // This is called before listeners are set
        dataTask.value = asyncTask(completion.freeze())
        dataTask.addOnSuccessListener {
            println("HERE IS CALLED OK")
            assertEquals(it, "OK")
        }
        dataTask.addOnFailureListener {
            println("HERE IS CALLEDFAIL")
            assertTrue(false)
        }
        println("FAILEDSDD")
    }
}
