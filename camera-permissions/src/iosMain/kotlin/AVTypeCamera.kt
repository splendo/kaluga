/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.permissions.camera

import com.splendo.kaluga.permissions.base.av.AVType
import platform.AVFoundation.AVMediaTypeVideo

private const val NS_CAMERA_USAGE_DESCRIPTION = "NSCameraUsageDescription"

/**
 * The [AVType] of the [CameraPermission]
 */
class AVTypeCamera : AVType() {
    override val avMediaType = AVMediaTypeVideo
    override val declarationName = NS_CAMERA_USAGE_DESCRIPTION
}
