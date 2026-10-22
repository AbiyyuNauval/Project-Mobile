package com.example.abiyyu_3tia.Profil

import android.content.Context
import android.content.Intent
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
import com.example.abiyyu_3tia.DetailActivity
import com.example.abiyyu_3tia.LoginActivity
import com.example.abiyyu_3tia.R
import com.example.abiyyu_3tia.databinding.FragmentProfilBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Profil Saya"

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna") ?: "Pengguna"
        binding.tvName.text = username
        binding.tvAvatar.text = username.take(2).uppercase()

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profil_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit_profile, R.id.action_settings -> {
                        Toast.makeText(requireContext(), "${menuItem.title} Clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_logout -> {
                        showLogoutDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        val posts = listOf(binding.cardPost1, binding.cardPost2, binding.cardPost3)
        for (post in posts) {
            post.setOnClickListener {
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }
        }
    }

    private fun showLogoutDialog() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { dialog, _ ->
                sharedPref.edit().clear().apply()
                dialog.dismiss()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
