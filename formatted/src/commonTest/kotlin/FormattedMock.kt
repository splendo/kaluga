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

package com.splendo.kaluga.formatted

internal class Dummy
internal data class MockFormatted(
    override val value: Dummy?,
    override val formatter: Formatter<Dummy>,
    override val modifier: Modifier<Dummy>? = null
) :
    Formatted<Dummy, MockFormatted> {
    class SpawnCall {
        class Args {
            var value: Dummy? = null
            var formatter: Formatter<Dummy>? = null
            var modifier: Modifier<Dummy>? = null
        }

        var args = Args()
        var toReturn: MockFormatted? = null

        var executed = false
    }

    var spawn: SpawnCall = SpawnCall()
    override fun spawn(value: Dummy?, formatter: Formatter<Dummy>, modifier: Modifier<Dummy>?): MockFormatted {
        spawn.executed = true
        spawn.args.value = value
        spawn.args.formatter = formatter
        spawn.args.modifier = modifier
        return spawn.toReturn!!
    }
}

internal class MockFormatter : Formatter<Dummy> {
    class ValueCall {
        class Args {
            var string: String? = null
        }

        var args = Args()
        var toReturn: Dummy? = null
        var executed = false
    }

    class StringCall {
        class Args {
            var value: Dummy? = null
        }

        var args = Args()
        var toReturn: String? = null
        var executed = false
    }

    var value = ValueCall()
    override fun value(string: String): Dummy {
        value.executed = true
        value.args.string = string
        return value.toReturn!!
    }

    var string = StringCall()
    override fun string(value: Dummy): String {
        string.executed = true
        string.args.value
        return string.toReturn!!
    }
}

internal class MockModifier : Modifier<Dummy> {
    class ApplyCall {
        class Args {
            var value: Dummy? = null
        }

        var args = Args()
        var toReturn: Dummy? = null
        var executed = false
    }

    var apply = ApplyCall()
    override fun apply(value: Dummy): Dummy {
        apply.executed = true
        apply.args.value = value
        return apply.toReturn!!
    }
}