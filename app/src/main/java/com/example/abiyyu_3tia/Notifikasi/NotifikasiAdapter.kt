package com.example.abiyyu_3tia.Notifikasi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.abiyyu_3tia.DetailActivity
import com.example.abiyyu_3tia.R
import com.example.abiyyu_3tia.databinding.ItemNotifikasiBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotifikasiAdapter(
    context: Context,
    private val notifikasiList: List<NotifikasiModel>
) : ArrayAdapter<NotifikasiModel>(context, 0, notifikasiList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemNotifikasiBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        val data = notifikasiList[position]

        if (data.section.isNotEmpty()) {
            binding.tvSection.text = data.section
            binding.tvSection.visibility = View.VISIBLE
        }

        binding.imgIcon.setImageResource(data.icon)
        binding.imgIcon.backgroundTintList = ContextCompat.getColorStateList(context, data.iconBg)
        binding.imgIcon.imageTintList = ContextCompat.getColorStateList(context, data.iconColor)
        binding.tvTitle.text = data.title
        binding.tvBody.text = data.body
        binding.tvTime.text = data.time

        if (data.unread) {
            binding.layoutRow.setBackgroundColor(ContextCompat.getColor(context, R.color.unread_bg))
            binding.tvTitle.setTypeface(binding.tvTitle.typeface, Typeface.BOLD)
            binding.viewUnread.visibility = View.VISIBLE
        }

        binding.layoutRow.setOnClickListener {
            if (data.bukaProfil) {
                val bottomNav = (context as Activity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                bottomNav.selectedItemId = R.id.profil
            } else {
                context.startActivity(Intent(context, DetailActivity::class.java))
            }
        }
        return view
    }
}
