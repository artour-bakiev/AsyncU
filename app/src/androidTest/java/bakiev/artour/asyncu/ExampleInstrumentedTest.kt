package bakiev.artour.asyncu

import androidx.test.platform.app.InstrumentationRegistry
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class ExampleInstrumentedTest {
    @Test
    fun shouldUseApplicationContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext


        appContext.packageName shouldBeEqualTo "bakiev.artour.asyncu"
    }
}