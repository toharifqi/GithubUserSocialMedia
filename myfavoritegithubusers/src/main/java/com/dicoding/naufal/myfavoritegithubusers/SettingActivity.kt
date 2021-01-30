package com.dicoding.naufal.myfavoritegithubusers

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.dicoding.naufal.myfavoritegithubusers.databinding.ActivitySettingBinding
import com.dicoding.naufal.myfavoritegithubusers.fragment.SettingPreferenceFragment

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, SettingPreferenceFragment()).commit()

    }
}