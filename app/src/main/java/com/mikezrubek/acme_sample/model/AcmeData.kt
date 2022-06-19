package com.mikezrubek.acme_sample.model

/**
 * Created by Mike Zrubek, 6/17/22.
 * Copyright (C) 2019 MIZR Corporation. ALL RIGHTS RESERVED.
 */
data class AcmeData(var shipments: List<String> = emptyList(),
                    var drivers: List<String> = emptyList(),
                    /** Holds error message if there is a data error, null otherwise. */
                    var errorMessage: String? = null) {


    /** Returns true if no error exists in the data. */
    fun noError() = errorMessage.isNullOrEmpty()
}

/*
class AcmeData {
    lateinit var shipments: List<String>
    lateinit var drivers: List<String>

    /** Holds error message if there is a data error, null otherwise. */
    var errorMessage: String? = null
}
 */