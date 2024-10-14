/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.media

import com.splendo.kaluga.media.BasePlaybackStateRepo
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [BasePlaybackStateRepo]
 * This will only use [MockPlaybackState]
 * @param createInitialState method for creating an initial [MockPlaybackState]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
class MockPlaybackStateRepo(createInitialState: () -> MockPlaybackState, coroutineContext: CoroutineContext) :
    BasePlaybackStateRepo(
        { createInitialState().playbackState },
        coroutineContext,
    ) {

    /**
     * Constructor that initializes to a [MockPlaybackState.Uninitialized] State with a given [MockPlaybackState.Configuration]
     * @param configuration the [MockPlaybackState.Configuration] to configure the initial state as
     * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
     */
    constructor(configuration: MockPlaybackState.Configuration, coroutineContext: CoroutineContext) : this(
        { MockPlaybackState.Uninitialized(configuration) },
        coroutineContext,
    )
}
