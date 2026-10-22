package com.example.abiyyu_3tia.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.abiyyu_3tia.BuatPertanyaanActivity
import com.example.abiyyu_3tia.R
import com.example.abiyyu_3tia.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val pertanyaanList = listOf(
        PertanyaanModel("rina_a", "SMA · Kelas 11 · 2 jam lalu", "Matematika", R.color.subj_math_bg, R.color.subj_math_fg,
            "Bagaimana cara mencari turunan dari f(x) = 3x² + 2x − 5?",
            "Sudah coba pakai rumus dasar tapi hasilnya beda dengan kunci jawaban…",
            12, 3, "terbaik", "https://avatar.iran.liara.run/public/1", true),
        PertanyaanModel("dimas.p", "SMP · Kelas 8 · 3 jam lalu", "Fisika", R.color.subj_physics_bg, R.color.subj_physics_fg,
            "Kenapa dua benda dengan berat berbeda bisa jatuh bersamaan?",
            "Di video percobaan bola besi dan bulu jatuh bareng di ruang hampa, kok bisa?",
            5, 1, "", "https://avatar.iran.liara.run/public/2", false),
        PertanyaanModel("nadia_k", "SMA · Kelas 10 · 5 jam lalu", "Kimia", R.color.subj_chem_bg, R.color.subj_chem_fg,
            "Apa bedanya ikatan ion dan ikatan kovalen?",
            "Masih bingung cara membedakannya kalau dikasih rumus senyawa.",
            3, 0, "belum", "https://avatar.iran.liara.run/public/3", false),
        PertanyaanModel("fajar21", "SMA · Kelas 12 · Kemarin", "B. Inggris", R.color.subj_eng_bg, R.color.subj_eng_fg,
            "Kapan pakai present perfect dan kapan simple past?",
            "Contoh: “I have visited Bali” vs “I visited Bali”, artinya beda?",
            8, 2, "terbaik", "https://avatar.iran.liara.run/public/4", false),
        PertanyaanModel("salsa.m", "SMP · Kelas 9 · Kemarin", "Biologi", R.color.subj_bio_bg, R.color.subj_bio_fg,
            "Apa fungsi mitokondria di dalam sel?",
            "Untuk tugas besok, butuh penjelasan singkat yang gampang dipahami.",
            2, 0, "belum", "https://avatar.iran.liara.run/public/5", false),
        PertanyaanModel("bima_s", "SMA · Kelas 11 · 2 hari lalu", "Sejarah", R.color.subj_history_bg, R.color.subj_history_fg,
            "Apa penyebab utama terjadinya Perang Diponegoro?",
            "Di buku ada beberapa penyebab, mana yang paling utama?",
            4, 1, "", "https://avatar.iran.liara.run/public/6", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna")

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "TanyaBareng"
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = "Halo, $username"

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_notification -> {
                        pindahTab(R.id.notifikasi)
                        true
                    }
                    R.id.action_profile -> {
                        pindahTab(R.id.profil)
                        true
                    }
                    R.id.action_create -> {
                        startActivity(Intent(requireContext(), BuatPertanyaanActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        binding.btnTanya.setOnClickListener {
            startActivity(Intent(requireContext(), BuatPertanyaanActivity::class.java))
        }

        val adapter = PertanyaanAdapter(requireContext(), pertanyaanList)
        binding.listPertanyaan.adapter = adapter
    }

    private fun pindahTab(menuId: Int) {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.selectedItemId = menuId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
