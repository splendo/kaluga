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

import com.splendo.kaluga.firebase.DataTask
import kotlinx.cinterop.StableRef
import platform.Foundation.NSError
import kotlin.native.concurrent.freeze

actual typealias CollectionReference = Firebase.FirebaseFirestore.FIRCollectionReference

actual fun CollectionReference.document(documentPath: String?): DocumentReference {
    return if (documentPath != null) {
        documentWithPath(documentPath)
    } else {
        documentWithAutoID()
    }
}

actual val CollectionReference.id: String
    get() = collectionID

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val CollectionReference.path: String
    get() = path

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val CollectionReference.parent: DocumentReference?
    get() = parent

@Suppress("UNCHECKED_CAST")
actual fun CollectionReference.addDocument(data: Map<String, Any?>): DataTask<DocumentReference> {
    val dataTask = DataTask<DocumentReference>()
    val ref = StableRef.create(dataTask)
    val completion = { error: NSError? ->
        val task = ref.get()
        ref.dispose()
        if (error != null) {
            task.failure(error)
        } else {
            task.success()
        }
    }
    dataTask.value = addDocumentWithData(data as Map<Any?, *>, completion.freeze())
    return dataTask
}
