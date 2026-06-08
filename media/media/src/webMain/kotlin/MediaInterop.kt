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

package com.splendo.kaluga.media

// The web has no separate "player" + "surface": an HTMLMediaElement is both. The live elements can't be
// held in shared webMain (they are per-target JS handles), so they live in a JS-side registry keyed by
// the string ids used here, and every operation/event is bridged through `js(...)` with primitive +
// callback parameters. A `<video>` element is used throughout — it plays audio too, and only needs to be
// in the DOM (attached to a surface) when its video should be visible.

private var mediaIdCounter = 0
private var soundIdCounter = 0

internal fun nextMediaId(): String = "kaluga-media-${mediaIdCounter++}"
internal fun nextSoundId(): String = "kaluga-sound-${soundIdCounter++}"

// Maps an HTMLMediaElement error code to the matching PlaybackError (MEDIA_ERR_NETWORK = 2, DECODE = 3,
// SRC_NOT_SUPPORTED = 4; ABORTED = 1 and anything else are treated as unknown).
internal fun playbackErrorForCode(code: Int): PlaybackError = when (code) {
    2 -> PlaybackError.IO
    3 -> PlaybackError.MalformedMediaSource
    4 -> PlaybackError.Unsupported
    else -> PlaybackError.Unknown
}

internal fun ensureMediaRegistry() {
    js("if (!globalThis.__kalugaMedia) globalThis.__kalugaMedia = { elements: {}, sounds: {} };")
}

internal fun mediaCreate(id: String) {
    js(
        """
        if (typeof document === 'undefined') return;
        var element = document.createElement('video');
        element.playsInline = true;
        element.preload = 'auto';
        globalThis.__kalugaMedia.elements[id] = element;
        """,
    )
}

internal fun mediaRegisterListeners(
    id: String,
    onPrepared: () -> Unit,
    onEnded: () -> Unit,
    onError: (code: Int) -> Unit,
    onRate: (rate: Double) -> Unit,
    onSeeked: () -> Unit,
    onVolume: (volume: Double) -> Unit,
    onResize: () -> Unit,
) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (!element) return;
        element.addEventListener('loadedmetadata', function () { onPrepared(); });
        element.addEventListener('ended', function () { onEnded(); });
        element.addEventListener('error', function () { onError(element.error ? element.error.code : 0); });
        element.addEventListener('ratechange', function () { onRate(element.playbackRate); });
        element.addEventListener('seeked', function () { onSeeked(); });
        element.addEventListener('volumechange', function () { onVolume(element.volume); });
        element.addEventListener('resize', function () { onResize(); });
        """,
    )
}

internal fun mediaSetSource(id: String, url: String) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (element) { element.src = url; element.load(); }
        """,
    )
}

internal fun mediaPlay(id: String, rate: Double) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (!element) return;
        element.playbackRate = rate;
        var promise = element.play();
        if (promise && promise.catch) promise.catch(function () {});
        """,
    )
}

internal fun mediaPause(id: String) {
    js("var element = globalThis.__kalugaMedia.elements[id]; if (element) element.pause();")
}

internal fun mediaStop(id: String) {
    js("var element = globalThis.__kalugaMedia.elements[id]; if (element) { element.pause(); element.currentTime = 0; }")
}

internal fun mediaSeek(id: String, seconds: Double) {
    js("var element = globalThis.__kalugaMedia.elements[id]; if (element) element.currentTime = seconds;")
}

internal fun mediaSetVolume(id: String, volume: Double) {
    js("var element = globalThis.__kalugaMedia.elements[id]; if (element) element.volume = volume;")
}

internal fun mediaDuration(id: String): Double = js("(function () { var e = globalThis.__kalugaMedia.elements[id]; return (e && isFinite(e.duration)) ? e.duration : 0.0; })()")

internal fun mediaCurrentTime(id: String): Double = js("(function () { var e = globalThis.__kalugaMedia.elements[id]; return e ? e.currentTime : 0.0; })()")

internal fun mediaWidth(id: String): Int = js("(function () { var e = globalThis.__kalugaMedia.elements[id]; return e ? e.videoWidth : 0; })()")

internal fun mediaHeight(id: String): Int = js("(function () { var e = globalThis.__kalugaMedia.elements[id]; return e ? e.videoHeight : 0; })()")

internal fun mediaHasMetadata(id: String): Boolean = js("(function () { var e = globalThis.__kalugaMedia.elements[id]; return !!(e && e.readyState >= 1); })()")

internal fun mediaAttachToSurface(id: String, elementId: String) {
    js(
        """
        if (typeof document === 'undefined') return;
        var element = globalThis.__kalugaMedia.elements[id];
        var container = document.getElementById(elementId);
        if (element && container) {
            element.style.width = '100%';
            element.style.height = '100%';
            container.appendChild(element);
        }
        """,
    )
}

internal fun mediaDetachFromSurface(id: String) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (element && element.parentNode) element.parentNode.removeChild(element);
        """,
    )
}

internal fun mediaReset(id: String) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (!element) return;
        element.pause();
        element.removeAttribute('src');
        element.load();
        if (element.parentNode) element.parentNode.removeChild(element);
        """,
    )
}

internal fun mediaRelease(id: String) {
    js(
        """
        var element = globalThis.__kalugaMedia.elements[id];
        if (element) {
            element.pause();
            if (element.parentNode) element.parentNode.removeChild(element);
        }
        delete globalThis.__kalugaMedia.elements[id];
        """,
    )
}

internal fun soundCreate(id: String, url: String) {
    js(
        """
        if (typeof Audio === 'undefined') return;
        globalThis.__kalugaMedia.sounds[id] = new Audio(url);
        """,
    )
}

internal fun soundPlay(id: String) {
    js(
        """
        var sound = globalThis.__kalugaMedia.sounds[id];
        if (!sound) return;
        sound.currentTime = 0;
        var promise = sound.play();
        if (promise && promise.catch) promise.catch(function () {});
        """,
    )
}

internal fun soundClose(id: String) {
    js(
        """
        var sound = globalThis.__kalugaMedia.sounds[id];
        if (sound) sound.pause();
        delete globalThis.__kalugaMedia.sounds[id];
        """,
    )
}
