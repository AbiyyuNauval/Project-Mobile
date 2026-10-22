package com.example.abiyyu_3tia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.abiyyu_3tia.Cari.CariFragment
import com.example.abiyyu_3tia.Home.HomeFragment
import com.example.abiyyu_3tia.Notifikasi.NotifikasiFragment
import com.example.abiyyu_3tia.Profil.ProfilFragment
import com.example.abiyyu_3tia.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        replaceFragment(HomeFragment())

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.beranda -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.cari -> {
                    replaceFragment(CariFragment())
                    true
                }
                R.id.notifikasi -> {
                    replaceFragment(NotifikasiFragment())
                    true
                }
                R.id.profil -> {
                    replaceFragment(ProfilFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
