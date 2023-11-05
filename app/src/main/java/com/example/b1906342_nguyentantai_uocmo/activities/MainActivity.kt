package com.example.b1906342_nguyentantai_uocmo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.b1906342_nguyentantai_uocmo.R
import com.example.b1906342_nguyentantai_uocmo.databinding.ActivityMainBinding
import com.example.b1906342_nguyentantai_uocmo.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, LoginFragment())
            .commit()
    }
}