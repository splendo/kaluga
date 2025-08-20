/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.plugin.helpers

import org.junit.Assert.assertEquals
import org.junit.Test

class VersioningTest {

    private val baseVersion = "1.2.3"

    @Test
    fun `test release version`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "release",
            branchName = null,
            gitHash = null,
            buildNumber = null,
        )
        assertEquals("1.2.3", version)
    }

    @Test
    fun `test alpha version with build number`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "alpha",
            branchName = null,
            gitHash = null,
            buildNumber = "123",
        )
        assertEquals("1.2.3-alpha.123", version)
    }

    @Test
    fun `test alpha version without build number`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "alpha",
            branchName = null,
            gitHash = null,
            buildNumber = null,
        )
        assertEquals("1.2.3-alpha", version)
    }

    @Test
    fun `test commit alpha version`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "-commit-alpha",
            branchName = null,
            gitHash = "abcdef",
            buildNumber = "456",
        )
        assertEquals("1.2.3-abcdef-alpha.456", version)
    }

    @Test
    fun `test branch alpha version with sanitization`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "-branch-alpha",
            branchName = "feature/new_login#123",
            gitHash = null,
            buildNumber = "789",
        )
        assertEquals("1.2.3-new-login123-alpha.789", version)
    }

    @Test
    fun `test branch alpha version with numeric only branch`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "-branch-alpha",
            branchName = "feature/123",
            gitHash = null,
            buildNumber = "789",
        )
        assertEquals("1.2.3--alpha.789", version)
    }

    @Test
    fun `test branch alpha version with complex branch name`() {
        val version = calculateVersion(
            version = baseVersion,
            releaseType = "-branch-alpha",
            branchName = "feature/#123-new_login",
            gitHash = null,
            buildNumber = null,
        )
        assertEquals("1.2.3-new-login-alpha", version)
    }
}
