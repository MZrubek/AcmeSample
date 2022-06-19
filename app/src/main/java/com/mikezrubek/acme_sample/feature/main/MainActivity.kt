package com.mikezrubek.acme_sample.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mikezrubek.acme_sample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MainFragment(), "MainFragment")
            .commit()
    }
}