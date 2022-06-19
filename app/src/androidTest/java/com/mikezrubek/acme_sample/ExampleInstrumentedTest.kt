package com.mikezrubek.acme_sample

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mikezrubek.acme_sample.repository.AcmeDataRepository

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("com.mikezrubek.acme_sample", appContext.packageName)
    }

    // test data function reading from the json file.
    @Test
    fun readAcmeDataFile() {
        val acmeData = AcmeDataRepository(appContext).getData()
        assertNull("Error Reading Data: ${acmeData.errorMessage}", acmeData.errorMessage)

        // using an assumption of 10 drivers and 10 shipments, check for that count in the data:
        assertEquals("AcmeData incorrect Drivers count", 10, acmeData.drivers.size)
        assertEquals("AcmeData incorrect Shipments count", 10, acmeData.shipments.size)
    }
}