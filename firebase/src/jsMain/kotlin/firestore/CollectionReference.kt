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

actual class CollectionReference : Query()

actual fun CollectionReference.document(documentPath: String?): DocumentReference {
    TODO("Not yet implemented")
}

actual val CollectionReference.id: String
    get() = TODO("Not yet implemented")
actual val CollectionReference.path: String
    get() = TODO("Not yet implemented")
actual val CollectionReference.parent: DocumentReference?
    get() = TODO("Not yet implemented")

actual fun CollectionReference.addDocument(data: Map<String, Any?>): DataTask<DocumentReference> {
    TODO("Not yet implemented")
}
