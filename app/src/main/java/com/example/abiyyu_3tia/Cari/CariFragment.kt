package com.example.abiyyu_3tia.Cari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.abiyyu_3tia.databinding.FragmentCariBinding

class CariFragment : Fragment() {
    private var _binding: FragmentCariBinding? = null
    private val binding get() = _binding!!

    private val dataListWithDesc = listOf(
        mapOf("title" to "Matematika", "desc" to "128 Pertanyaan | 14 Belum Terjawab"),
        mapOf("title" to "Fisika", "desc" to "86 Pertanyaan | 9 Belum Terjawab"),
        mapOf("title" to "Kimia", "desc" to "64 Pertanyaan | 11 Belum Terjawab"),
        mapOf("title" to "Biologi", "desc" to "71 Pertanyaan | 6 Belum Terjawab"),
        mapOf("title" to "B. Indonesia", "desc" to "45 Pertanyaan | 4 Belum Terjawab"),
        mapOf("title" to "B. Inggris", "desc" to "93 Pertanyaan | 8 Belum Terjawab"),
        mapOf("title" to "Sejarah", "desc" to "38 Pertanyaan | 5 Belum Terjawab"),
        mapOf("title" to "Lainnya", "desc" to "27 Pertanyaan | 3 Belum Terjawab")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCariBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Cari"

        val adapter = SimpleAdapter(
            requireContext(),
            dataListWithDesc,
            android.R.layout.simple_list_item_2,
            arrayOf("title", "desc"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        binding.listViewMapel.adapter = adapter

        binding.listViewMapel.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = dataListWithDesc[position]
            val title = selectedItem["title"]
            val desc = selectedItem["desc"]
            Toast.makeText(requireContext(), "Kamu memilih: $title ($desc)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
