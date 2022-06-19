package com.mikezrubek.acme_sample.repository

import android.content.Context
import com.google.gson.Gson
import com.mikezrubek.acme_sample.model.AcmeData
import java.lang.Exception

/**
 * Sample of simple file-based repository support for reading Acme json data.
 * Obtains data from the sample_data.json file in this project's assets package.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
class AcmeDataRepository(val context: Context) {

    /**
     * Returns the AcmeData, currently only from a file.
     */
    fun getData(): AcmeData {
        return getAcmeDataFromAssetFile()
    }


    /**
     * Obtains AcmeData instance from json data file located in assets package.
     * Errors are simply printed to stack trace. It could otherwise be useful to throw
     * error details up to caller or consumer of the data for better handling.
     *
     * @return AcmeData instance, with driver and shipment data if file was read and parsed
     * properly. If an error occurred, an appropriate error message is provided in the returned
     * instance.
     * ASSUMPTION here is that the file has data in it for this exercise. And empty data file
     * at this point would simple return an instance with no error and empty data lists.
     */
    private fun getAcmeDataFromAssetFile(): AcmeData {

        try {
            val json = context.assets.open(ACME_DATA_FILE_NAME).bufferedReader().use { it.readText() }
            return Gson().fromJson(json, AcmeData::class.java)
        } catch (exception: Exception) {

            // The possible exceptions are primarily IOException for file not found,
            // or JsonSyntaxException for json formatting / Gson parsing errors.
            // For this demo, just get the text of the exception and pass it in the result data.
            return AcmeData(errorMessage = exception.localizedMessage)
        }
    }


    companion object {
        private const val ACME_DATA_FILE_NAME = "sample_data.json"
    }
}