/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import org.codehaus.groovy.runtime.ProcessGroovyMethods

val GITHUB_GIT_BRANCH = System.getenv("GITHUB_REF_NAME") // could also be a tag name
val kaluga_branch = System.getProperty("kaluga_branch")
val MAVEN_CENTRAL_RELEASE = System.getenv("MAVEN_CENTRAL_RELEASE")
val release = MAVEN_CENTRAL_RELEASE?.toLowerCase()?.trim() == "true"
val branchFromGit = run {
    try {
        ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git rev-parse --abbrev-ref HEAD"))
    } catch (e: Exception) {
        logger.info("Unable to determine current branch through git CLI: ${e.message}")
        "unknown"
    }
}

// favour user definition of kaluga_branch (if present), otherwise take it from GIT branch:
// - if running on CI: favour github's branch detection
// - else: try to get it via the `git` CLI.
val branch = (kaluga_branch ?: GITHUB_GIT_BRANCH ?: branchFromGit ).replace('/', '-').trim().toLowerCase().also {
        if (it == "HEAD")
            logger.warn("Unable to determine current branch: Project is checked out with detached head!")
    }

val kaluga_branch_postfix = when(branch) {
    "master", "main", "develop" -> ""
    else -> "-"+branch
} + if (!release) "-SNAPSHOT" else ""

logger.lifecycle("decided branch: '$branch' to postfix '$kaluga_branch_postfix', isRelease: $release (from: GITHUB_GIT_BRANCH env: $GITHUB_GIT_BRANCH, kaluga_branch property: $kaluga_branch , MAVEN_CENTRAL_RELEASE env: $MAVEN_CENTRAL_RELEASE , git cli: $branchFromGit)")

System.setProperty("kaluga_branch_postfix", kaluga_branch_postfix)
