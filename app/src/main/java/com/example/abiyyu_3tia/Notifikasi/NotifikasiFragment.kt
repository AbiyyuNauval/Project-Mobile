package com.example.abiyyu_3tia.Notifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.abiyyu_3tia.R
import com.example.abiyyu_3tia.databinding.FragmentNotifikasiBinding

class NotifikasiFragment : Fragment() {
    private var _binding: FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!

    private val notifikasiList = listOf(
        NotifikasiModel("HARI INI", "aldi.m menjawab pertanyaanmu", "Bagaimana cara mencari turunan dari f(x) = 3x² + 2x − 5?", "1 jam lalu",
            R.drawable.ic_chat, R.color.primary_container, R.color.primary, true, false),
        NotifikasiModel("", "Jawabanmu mendapat 5 dukungan baru", "Kapan pakai present perfect dan kapan simple past?", "3 jam lalu",
            R.drawable.ic_arrow_up, R.color.subj_math_bg, R.color.subj_math_fg, true, false),
        NotifikasiModel("", "putri_n menjawab pertanyaanmu", "Bagaimana cara mencari turunan dari f(x) = 3x² + 2x − 5?", "5 jam lalu",
            R.drawable.ic_chat, R.color.primary_container, R.color.primary, true, false),
        NotifikasiModel("MINGGU INI", "Jawabanmu terpilih sebagai Jawaban Terbaik! +15 poin", "Apa fungsi mitokondria di dalam sel?", "Kemarin",
            R.drawable.ic_star, R.color.secondary_container, R.color.warning, false, false),
        NotifikasiModel("", "Pertanyaanmu mendapat 10 dukungan", "Bagaimana cara mencari turunan dari f(x) = 3x² + 2x − 5?", "2 hari lalu",
            R.drawable.ic_arrow_up, R.color.subj_math_bg, R.color.subj_math_fg, false, false),
        NotifikasiModel("", "Kamu naik ke Level 4!", "Terus bantu teman untuk mengumpulkan poin.", "4 hari lalu",
            R.drawable.ic_star, R.color.secondary_container, R.color.warning, false, true),
        NotifikasiModel("", "Selamat datang di TanyaBareng", "Lengkapi profilmu agar mudah dikenali teman.", "6 hari lalu",
            R.drawable.ic_person, R.color.subj_other_bg, R.color.subj_other_fg, false, true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Notifikasi"
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = "3 belum dibaca"

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.notif_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_mark_all -> {
                        Toast.makeText(requireContext(), "${menuItem.title} Clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        val adapter = NotifikasiAdapter(requireContext(), notifikasiList)
        binding.listNotifikasi.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
