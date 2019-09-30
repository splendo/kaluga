package com.splendo.mpp

import com.splendo.mpp.util.byOrdinalOrDefault
import kotlin.test.Test
import kotlin.test.assertEquals

class ByOrdinalOrDefaultTest {
     enum class Numbers {
         one, two
     }

    @Test
    fun test() {
        assertEquals(Numbers.one, Enum.byOrdinalOrDefault(0, Numbers.two))
        assertEquals(Numbers.two, Enum.byOrdinalOrDefault(1, Numbers.one))
        assertEquals(Numbers.two, Enum.byOrdinalOrDefault(-1, Numbers.two))
        assertEquals(Numbers.one, Enum.byOrdinalOrDefault(2, Numbers.one))
    }

}