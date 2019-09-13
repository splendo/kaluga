import com.splendo.mpp.log.LogTransformer
import org.junit.Test
import kotlin.test.assertEquals

class LogTransformerTest {

    @Test
    fun testTransformTag() {
        val initialTag = "ABCDEFGHIKLMNOPQRSTUVWXYZ"
        val tag = LogTransformer().transformTag(initialTag)

        assertEquals(23, tag!!.length)
        assertEquals("ABCDEFGHIKLMNOPQRSTUVWX", tag)
    }
}
