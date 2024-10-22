/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.plugin.extensions

import com.splendo.kaluga.plugin.helpers.gitBranch
import com.splendo.kaluga.plugin.helpers.jvmTarget
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLoggingContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * An extension to a [Project] that allows for easily reusing logic in Splendo Health
 */
sealed class BaseKalugaExtension(protected val versionCatalog: VersionCatalog, objects: ObjectFactory) {

    companion object {
        const val BASE_GROUP = "com.splendo.kaluga"
        private const val OSSRH_USERNAME = "OSSRH_USERNAME"
        private const val OSSRH_PASSWORD = "OSSRH_PASSWORD"
        private const val SIGNING_KEY_ID = "SIGNING_KEY_ID"
        private const val SIGNING_PASSWORD = "SIGNING_PASSWORD"
        private const val SIGNING_SECRET_KEY_RING_FILE = "SIGNING_SECRET_KEY_RING_FILE"
    }

    val Project.kalugaVersion: String get() {
        val kalugaBaseVersion = versionCatalog.findVersion("kaluga").get().displayName
        return gitBranch.toVersion(kalugaBaseVersion)
    }
    internal data class TestLoggingContainerAction(val action: Action<in TestLoggingContainer>)
    private val testLogging: Property<TestLoggingContainerAction> = objects.property(TestLoggingContainerAction::class.java)

    /**
     * Configure a [TestLoggingContainer]
     */
    fun testLogging(action: Action<in TestLoggingContainer>) {
        testLogging.set(TestLoggingContainerAction(action))
    }

    fun beforeProjectEvaluated(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                jvmTarget.set(versionCatalog.jvmTarget)
                freeCompilerArgs.addAll("-Xjvm-default=all")
            }
        }
        project.beforeEvaluated()
    }

    protected abstract fun Project.beforeEvaluated()

    /**
     * Sets up a [Project] with the configuration of this extension after it has been evaluated.
     */
    @OptIn(ExperimentalStdlibApi::class)
    @JvmName("handleProjectEvaluated")
    fun afterProjectEvaluated(project: Project) {
        project.group = BASE_GROUP
        project.version = project.kalugaVersion

        project.tasks.withType(Test::class.java) {
            testLogging {
                events = org.gradle.api.tasks.testing.logging.TestLogEvent.entries.toSet()
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                this@BaseKalugaExtension.testLogging.orNull?.action?.execute(this)
            }
        }

        project.extensions.configure(SigningExtension::class) {
            setRequired(
                {
                    project.logger.info("🪧signing has `:publishAllPublicationsToSonatypeRepository` task: "+project.gradle.taskGraph.hasTask(":publishAllPublicationsToSonatypeRepository"))
                    project.gradle.taskGraph.hasTask(":publishAllPublicationsToSonatypeRepository")
                },
            )
        }

        val jarTask by project.tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
        }

        project.extensions.configure(PublishingExtension::class) {
            configureRepositories(project)
            // Configure all publications
            publications.withType(MavenPublication::class) {
                // Stub javadoc.jar artifact
                artifact(jarTask.get())

                // Provide artifacts information requited by Maven Central
                pom {
                    name.set(project.name)
                    description.set("Collection of Kotlin Flow based libraries")
                    url.set("https://github.com/splendo/kaluga")

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("splendo")
                            name.set("Splendo Consulting BV")
                        }
                    }
                    scm {
                        url.set("https://github.com/splendo/kaluga")
                    }
                }
            }

            project.extensions.configure(SigningExtension::class) {
                val signingPassword = project.getStringPropertyOrSystemEnvironment("signingPassword", SIGNING_PASSWORD)

                project.logger.info("🪧signingPassword not null? ${signingPassword != null} is empty? ${signingPassword?.isEmpty()}")

                // Q: Is it weird/insecure to have this here? to put this here?
                // A: No. The files were in the repo before. This is just the same key, still passphrase protected.
                // When rolling the GPG key (expires 2025-05-03) we could consider putting this whole key in an env however.
                useInMemoryPgpKeys(
                    /* defaultKeyId = */ "CA7AF280",
                    // generated with `gpg --export-secret-keys --armor 728662F2CA7AF280` after `gpg --import secret-keys.gpg`
                    /* defaultSecretKey = */ """-----BEGIN PGP PRIVATE KEY BLOCK-----

                    lQdGBGCP7/oBEACdtKnyaXbwbYHw/BTi5nm1Pr9PBKOV/noW+TZTPisDfLuDVGBB
                    oJ4QtP9j/GG9kPKcZ/HpIXyFN3QTnsv8+VuGQMpEB9gQ6SwY6EbDp5jMN2qwr9q1
                    ROwjTZsaCltF3ZqR7G/mN6XB/n66sJVL9KA4R6cDv/zImnYQYBrHtpVHnjoygGS7
                    jSlicbeYuKQ5gQxgrIBQzY5vnKGD8UjLz3/YmI44ZFjEaKqnU9N+JO7VCKvnIiXP
                    7iSVHXux9DBDqJNfxL+eSsvz+yOfbu2WdPWdiJ7aVcrf3vUeMFYf3xGi/HUvXlph
                    cjZelS3LcSaqZ0kps2kniCSh7cDAA9du0Q1AYC5PoUcsRvYiqgOflKolXKd0Zkpc
                    oaZ2XEpzC/s6v3f/UuBv1YNoo0Xzl79JCJHPYBkLq10v6OzFQdWBeesG3K3v2V6R
                    1IbXMI2Wvy7T9A4Swl60UuAkTIMEcfJmhfqFxU4j+tudqPTPdjUn8qMLrz+M0v47
                    SYEaAMKBMGs4ttjDUdPTWLui18Z0eWxqh3XPO0k8c5iHPpgulnxoqQc0TWUKugwl
                    94AEh9+yniLJX+S7s559LvFbMBNmqDwbm6uTufIM2k/bmiij7KNMDa9wWVmdhSwI
                    OyTER8k2BwKY/iqhLTErBCxLLo78DMAkrLGRizpwz8dy2TIg+nGpAV6gkwARAQAB
                    /gcDAhNIkLOl+l9V9RKG14gEZ1VkbgrXnODv4szr4JhZrFVKC+A51UMyPQV/Ux7W
                    YxEQgjfx/IE+dqrstktiHqnsDTi/OkqXqA2oEm63TNcoJnXg672KXuERM/fuIdL5
                    GnlJZXrNIi/sz0OpCfWKJzGeR9R0cg418pBNclUdeRYucqKT/xlmxpsEZe8GFWyN
                    7vUGzL3eDDsTNK748iB6h6uZdwIhE13huQiyHQsDcfgSGlAJTS2vLOS2Lok8JRm9
                    Xx9Pz0/zsGBbQmb9JLwEBt+/AnjFdGDOYpV351t6shHpbbRrZJvcqtIZg1in/2Pc
                    fq5IKi+ldZ8V2U2FYlqmnBzIAb2+H98UZTIkjfq+22xkncJjBAtGL7IQegwE/Bxe
                    7olucZK11MpiIFwMmks/5v6O2Qepb/Rug5ZQm4NxJnOKCCo61aFPZhDWnWdZci9E
                    TqA3cmVyV/gTTpavQifnE8WlRhGVMzF3PzhiqmVI4Msui/sMVPind3vldetXOzrS
                    21GQFA8RoI7PpoB5qHDmQ1/2v39cpgJGeO3+68Xs0m90J02XqOMjZpOBJTBOitdi
                    zQFGxkEBMroKSRbCXt5bM/aHjYgimpBGalAF1yddU4LrzlMsYq/0orTZbgZ3porK
                    nHwNx8QKfCpgYMfwZ6HsdBH7hTEtleBTFqEuI+oKAmNh353mas7C5IzsGjPPMiDO
                    zypCLy9W+hMpqTNmIHBB14L4BVC1CSq1mgFN7laV86L4TThXCvSy6zA8NNyhPtlb
                    +xsR/nvB41LC+VmOB54bhQ9H5Nuj65As59Pj+pHs/Ju/+v/jR1VBeD9OadrIqMgM
                    qQb9pT1wKcZ0Ecj/GiKREQYAUTWg88TLw8sqkXZ0LUhGgAewkP3QGu1ZYcAWtbY5
                    ChZmpgFglo3kvseQD49hWhUe9Kib+UiTQ2a7gcWvesD6UhhVSoG1t/sDm1rjiTpf
                    OT9mQtQUQrlWzCQFPzDlB6Z+iExtKakc4n/MrCm6UmdylThhfLH6ufCdbu3Z7m+q
                    eSQXZt9YTmhTtOApO6xvyymV7SfVjtGK6XjfiaGNvtk69Zb7z0w+49M7S2YD2NfL
                    ve9+CzW+6mN9IpC97UYtgIdT3H6X6nL8QqLuhUjO8ZnokAe5ty2F0nm5sj+PYI/7
                    OvNqxV/K6GU1pZGfZ01oa2RlzcjKL+2E+P14k9fzZWTeTBT/bjKAhBW+jhlSLLDv
                    +1wSbzHn+do8JtK/q+UCja3sdjKYG3LU7+YW/frsHHRD5lYJhgm9nq5cimPhwRJN
                    v4YEjCFpuZNc/1bc+zbvl8Bz8ZGqvx4QeGhok2N2iIC8gpQ3jUbP6dhrDT3sLRcH
                    vv7IRos+s3r+KWYX+Hm4SE4WBery/1JdELAKp8AaF8RGaaxIL2K2m26ZQ1f0RweY
                    My1q6YTnUaZ/Wa5/nJP1CZ5A1nTQNG6cAcBDE6u7TNiBLwSyS6zTGKK4Y6sdb0ja
                    +ApsXaCbYIymnIv8lzUtWSembPvEjNefkffFDOU+KDajvUBVKqMELzrorAhrEw56
                    S9bzIMBiZYlACGohjVBI/1r03QvY4NtgbR9yKMGEwdSLM5p8y+sZ+YxdxHeOjXtT
                    bXAM8q2d4eo5nAfwhTfIR7Vc5wKjDnbK0GYGiL/KzRtcbzYEVBo+jxLrcou1P+GX
                    EoNLdgyy80Vx+/GDbAPzsYbTHd/10fn1YC436HuUqrB2stv5p6soxJjBOBPwYO4p
                    6ytWiwNcj3abRIm1FnYtQJU4zq68AceQMjpGctwm+cEWTtHorQkPi9u0HHl3RkxV
                    ZU0zIDx0aWpsQGhvdXRiZWNrZS5ycz6JAlQEEwEIAD4WIQTj4shDlrf9iC3Uma1y
                    hmLyynrygAUCYI/v+gIbAwUJB4Ye/gULCQgHAgYVCgkICwIEFgIDAQIeAQIXgAAK
                    CRByhmLyynrygIP5D/4lqLgxEY0uoHPXY16bhKiCg9i4IFv9XegXOk212zYkMF3G
                    zYYpyyYQQy9/qoSXY9hn5gBCVp9t8ialtbUlZSZU9vg34F5cTYYuHHGXLdTkfXGy
                    o0S1JlZwKFeKvQZwnG1SzA21xAHIXWYmr76Lla4RMAAIHgEeEK7qp/NbIzd3gBve
                    dVW1mpjDRlVtxHlCR2A6cnyxcRIZJk8sRWdbt8VIhUGyztuBMIiTUhgYU5ATjL0f
                    MCNKjqRHtihbciN2yzQMKZosq3oHvgiLqD3W2gySpcQNqhycrmx5MDXwP4PfQS/B
                    IV1fSLR78wuzIMcv9jUU0Q3WpoXr3U7bh/tdXSdjxqxa7tpAB4RLM69LabPpgNYF
                    W5qV37TuDXFgt9uKwyukSA3DmzXLW6NSA+NJoogZ9pkx+9hxE73ALHg69h1ThGmq
                    SR74wReErtPWlBTEqkc0u3s7VZsWZmue/7n1zGff7u1nDzaibpdXo09LgvvpRZEt
                    e9PX9D8/V183aSmXxm/8+yOoxHJwjydfsRua2AT1H+bTAoAg0IH25qyWK4lQNdxC
                    BQUGKVKhm1JRSQPcpKN9BflXO9qGwPIlFKHv56MOViVLllqmXFnqlSfplcnE5ZTK
                    dH48FFUSXVtjv5RfFWXVZHGR6Ml/7hOXdTot4ekRxKn4MNSXeGH1H9AWrH3HhZ0H
                    RgRgj+/6ARAAyM5Nn+WavvkiLLPCjC3hICrfQb2Rab3w1DMOUT+qH3qVTU1o6ffG
                    7c44598xdXWZTWafiBqvTxbbo6mnDHHCvUxd2PxwQZvF1tsiTaAr6X0MQaXKM2b4
                    TBRE7grocpu3hBHnVkdnKyXMfpAj5YkIL1ru8mTE2qkCR0tKJI1BcewMKrhYicTT
                    XxH2vyVAHLguAv1Q8WjgbcOBQlLn4JtNIGq0bSMTlAz/a2Po0SF/nxVT5VsENvLV
                    T7pHJY5bhGJUjK3scuIIg5oI1iKPhTQey5shR61WpprXehk1a5lcKI0ifXDZlogz
                    QBOv2P4VlprMIGQhA9R9ZLOBSyZLuH2cBHtD891NXMrtjJFREC9jxOgk/I3X0lKC
                    EJ/d01UJQ2kHO12eUGZBijCzmka4Sr8VssfWnWWBq3GSHyd6+ROLRFulFGqw5MEG
                    yIHu/OCU29F8Cjf9UcVDc/TTxxFa++bUbLuXMYF6SFKZMc0saCIlxlwIaVPOvhMA
                    GAIKLQEqRsbqNkVx/qf08Heu8SO/hkJ4WxBIn/MhbB4XE/s88Wftfclf6JwgWRXg
                    3jhhW/a37pmdgDofMy/rJykIpTIYM5X74IhUITtDAe1FAFJYMkolo5NtyUDDJgU9
                    eOHnzTrkBh+m2JWqU8NUtzCAS99qCVOvp/caqIgB6nm5r2TEjdwktJ8AEQEAAf4H
                    AwKXJ1+9oNOdb/Xp2ejrSSaVsue9CrsQLxJFwacDOdPSZ1XCS9cHWqvYrlUS0e4A
                    AtYIlAZELTvgKBETorq1mKSrk2i8m+xi5sLxvZEDTTfJyuhixrlzlBCGgixBmDjh
                    SIJ4kRPptk/W28ZqpFkycyWBkvUdX9sMP8uQKqXsTM/ROb4Fk9LJy6ZpnhgxUSzg
                    pvy2I9uVMMhCbUSC26EeYFl3Tg44aPTkdq0IETcmpUkhQAk9hG4eouZNLLgXXPe5
                    Taev+S/xXr2xG+h/+3dVNGeKMvppc+V9MIxk7jCZDq1xZMh8FmqIEUDyODkAVr3q
                    tzTNOojxuADPzizt6M4pqeE+0bNth101Pvjsl4mxATjnc45mzvU8lxW+2Ifz8rHO
                    WUL4S+Lk6FdttczhMUZ0ed7Kvs3zOuxQ/KtrqXdAo7eyGjeHll4WKeNaesjEVIGF
                    AwqKBKlpuQ7wX5R8q/Yyd0bPXlUZCR7A+AgQEqYk4I6t2NDNlhoq/CVHkAQEjorD
                    4o37ZT1kYWtUvsGmcCSxv5eQhTGI4xVJeBjHSKK1lQXS7Mj1E/3b24oaWu3yJhTw
                    xqq9c1j/8L+heX0ruv/zGPfAOyENJqV0kENJPxUxQE/msAp62CutEFDHcQ3ZK48v
                    W6CmtkM1NszNe3WZ6E7SQRrBHRqVO3bDhStVC9diJwFzrrJfz1nqAOHvh9sPwCe+
                    52iet1xhOs8nCHSLqx0f34km9Aokh6ynlZnqQshCadvFpYzHmJrWk0hrm5RPLD/T
                    fdu+NN11qyciGZwthOkR9OgcY2/M2ksMIQe2P09CO/nGp0lWc4nUxNbbMB7JyI5/
                    77rkMi2nszAGY4is1znutlEXiSosLlPZbFQOpsBUrfpW88crrGJ4UxIUPx4AGOZD
                    VCxpOwSJ5l1pDqySdFoaIHLfCHO2mpuPOUhKgEJxNQ5hB3pDkYzv4ahRCDkktjZc
                    kUMCGIEHbaFDVxISiCJIkA8dWwBYUalHUuIwHaQPZMzxzgXULHxP9AG2u4Rg+YcY
                    E+oBCwrfnpK3BXSx0cgwodTX8d5qEKuj5X1sxlPJJ9dNDD50YStgjA3Kju/tkrkq
                    W5h9cnDKIZI9M2cp1wKASfAC+w5NA178HTr5OpGc30L3u7eL1nQWIEZ7nCjWijgT
                    056PRR7YjNaG+EVMZ+BlWOrE8C1+3qSuWbBXZXt7woPZj+e9UMvMH+MrsNPZSGaB
                    HOA7eWpYmFsk+OiYCSR9bWtyzE5j+YNOFQauzkTyOjLaZHnnwwZ+Ac2DYdv8pws9
                    5H8Ju8zbvdTh+wD2GLMrH25K1uaurY5u179kmTbbhhfCr5H/yxyp3hYVxOgGSVVC
                    RaYd1/Ulg41S8/2SwagQM0bNf1v6wtrk6ZXKNoUcCm0clWu2Oog9r4wH3BUphThs
                    ZVd+4sulqahZ8dc2akyfhKluU6Gf4Mlv+3r8vdVjENQU2HU967wUxuMObKsSua8v
                    N4/cysgqucHbRGLQfRk6+icUQWHmDBarxfjygEuLnVcyOEib/b5Hg79/wYFj5IJ5
                    YjiUrtQZu6g88z1L50Dl4+oQi1ceZQ6GSz5RmcDCe9eQedb8TGV8tquIGgZpX/dS
                    3Gfznu3flAaaHPZDvoRR1HkXDAjkIbRiKGCgEy1x1x+T0yl/kNYt1Tpejub/eCjq
                    ACR7p/BZQzb8/VSfa7Thc38pv1k9tvPSmlupnytqWpmG7/yHRdGdxBL6FV0wFwjv
                    8B8r0gHIqe39C++onIl1qQ/ZdJwgnvmodCCxhVw4amEV52FEnqqeiQI8BBgBCAAm
                    FiEE4+LIQ5a3/Ygt1JmtcoZi8sp68oAFAmCP7/oCGwwFCQeGHv4ACgkQcoZi8sp6
                    8oBSJRAAnHI/VvILPCEA3YFWPnAQ2hJtGpeaPESnpJq7RoB8LWMQpm+Ivxs1E3EX
                    sHLafb2RZuaOUiP2qDLytYvw3C2e69CEs3JYXUmDMXEy9gGuuVyHK67GIq6y8tTf
                    7ltZuCJf/1EUjdD4rOdT+nk3nMP60PNa5Cy+StqKyOqCyCz7bp6TLAC6q8LyqvVd
                    W0Mvxg6JJHVczKNZGe4AtPG84uWx3djn9Lcy3XFY3qUqA2qT4oHLgPGPmnHD9ig7
                    xYXoJAt1wcqNVNUYk65QYKh4ESvrIZLRcloCjZHzI+PaH5OHbz4PF6hUipWjeT/+
                    bo1G800y9Grq3oWAH4dBqpBXbrU3F6iqfcDjY6r/4+yMR06exi931HvA0Fbu7ksd
                    W3dAOOZpQtw6uCLrL5LuvQYekTGhL/IuUbEgVfajFcu86S4hyRqoCpETVIu7wCrc
                    6DNhasodqJIUB0vs45nprATm2nL7VOy7jaKm8K2Jd+FRCHEV6SFWR2TNv43IWBAe
                    Mn8Gm9Mz1hpn6dJ+jpCyCFG+HaiY0wlq156rn8JHu8YmRZLQ6guouPZoTU1QTu/Q
                    pMI7QXmbCu8gN6qY4URLtzgYLoJ2aGFWFZs/eNCUii5mtYtPidBI5fCQCUFkML4l
                    ynsqpuAiADQoaVVd5rTLWU+YoovXZbrQfL8G9/Erm9V9dHSrC1c=
                    =mf0w
                    -----END PGP PRIVATE KEY BLOCK-----
                    """.trimIndent(),
                    /* defaultPassword = */ signingPassword
                )
                sign(publications)
                project.logger.info("🪧signatory not null: ${signatory != null}")
            }
        }

        project.afterProjectEvaluated()
    }

    /**
     * Abstract setup of a [Project] with the configuration of this extension after it has been evaluated.
     */
    protected abstract fun Project.afterProjectEvaluated()

    private fun PublishingExtension.configureRepositories(project: Project) {
        val ossrhUsername = project.getStringPropertyOrSystemEnvironment("ossrhUsername", OSSRH_USERNAME)
        val ossrhPassword = project.getStringPropertyOrSystemEnvironment("ossrhPassword", OSSRH_PASSWORD)

        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }

            maven {
                name = "snapshots"
                setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    private fun Project.getStringPropertyOrSystemEnvironment(propertyName: String, environmentPropertyName: String): String? =
        properties[propertyName] as? String ?: System.getenv(environmentPropertyName)
}
