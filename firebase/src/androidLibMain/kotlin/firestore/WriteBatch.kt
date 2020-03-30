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

import com.google.firebase.firestore.WriteBatch

actual typealias WriteBatch = WriteBatch

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual fun WriteBatch.set(
    documentReference: DocumentReference,
    data: Map<String, Any?>
): WriteBatch = set(documentReference, data)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual fun WriteBatch.update(
    documentReference: DocumentReference,
    data: Map<String, Any?>
): WriteBatch = update(documentReference, data)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual fun WriteBatch.delete(documentReference: DocumentReference): WriteBatch = delete(documentReference)

actual fun WriteBatch.commit(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    commit()
        .addOnCompleteListener { onSuccess() }
        .addOnFailureListener(onFailure)
}
