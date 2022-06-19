package com.mikezrubek.acme_sample.model

import java.util.Locale


/**
 * Driver information.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
data class Driver(val name: String) {

    var numberOfVowels: Int
    var numberOfConsonants: Int

    init {
        numberOfVowels = 0
        numberOfConsonants = 0

        // check each character, looking for vowels first; ignores all non-alpha characters
        // ASSUMPTION: string is in english and uses the US character set.
        val lowercaseName = name.lowercase(Locale.getDefault())
        for (i in lowercaseName.indices) {
            when(lowercaseName[i]) {
                'a','e' ,'i' ,'o' ,'u' -> numberOfVowels++
                in 'a'..'z' -> numberOfConsonants++
            }
        }
    }
}
