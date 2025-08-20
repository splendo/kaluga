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

import com.palantir.gradle.gitversion.VersionDetails
import org.gradle.api.Project
import org.gradle.internal.extensions.core.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate

internal fun calculateVersion(
    version: String,
    releaseType: String,
    branchName: String?,
    gitHash: String?,
    buildNumber: String?,
): String {
    var appendix = when (releaseType) {
        "release" -> ""
        "-branch-alpha" -> branchName?.let { branch ->
            val sanitizedBranch = branch.substringAfterLast('/')
                .replace(' ', '-')
                .replace('_', '-')
                .filter { it.isLetterOrDigit() || it == '-' }
                .split('-')
                .filterNot { it.all(Char::isDigit) } // filter out purely numerics such as issue numbers
                .joinToString("-")
            "-$sanitizedBranch-alpha"
        } ?: error("Trying to append a branch name to the version, but no branch is present.")
        "-commit-alpha" -> "-$gitHash-alpha"
        else -> "-alpha"
    }

    if (appendix.isNotEmpty() && buildNumber != null) {
        appendix += ".$buildNumber"
    }

    return "$version$appendix"
}

val Project.kalugaVersion: String
    get() {
        val releaseTypeProvider = providers.gradleProperty("releaseType")
        val releaseType = releaseTypeProvider.getOrElse("alpha")

        val versionDetails: groovy.lang.Closure<VersionDetails> by project.extra
        val details = versionDetails()
        val buildNumberProvider = providers.environmentVariable("GITHUB_RUN_NUMBER")

        return calculateVersion(
            version = project.version.toString(),
            releaseType = releaseType,
            branchName = details.branchName,
            gitHash = details.gitHash,
            buildNumber = buildNumberProvider.orNull,
        )
    }
