package com.example.abiyyu_3tia.Home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.abiyyu_3tia.DetailActivity
import com.example.abiyyu_3tia.databinding.ItemPertanyaanBinding

class PertanyaanAdapter(
    context: Context,
    private val pertanyaanList: List<PertanyaanModel>
) : ArrayAdapter<PertanyaanModel>(context, 0, pertanyaanList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemPertanyaanBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        val data = pertanyaanList[position]

        Glide.with(context)
            .load(data.avatarUrl)
            .into(binding.imgAvatar)

        binding.tvUser.text = data.userName
        binding.tvMeta.text = data.meta
        binding.tvSubject.text = data.subject
        binding.tvSubject.backgroundTintList = ContextCompat.getColorStateList(context, data.subjectBg)
        binding.tvSubject.setTextColor(ContextCompat.getColor(context, data.subjectColor))
        binding.tvTitle.text = data.title
        binding.tvSnippet.text = data.snippet
        binding.btnVote.text = data.votes.toString()
        binding.tvAnswers.text = "${data.answers} jawaban"

        if (data.status == "terbaik") {
            binding.tvBest.visibility = View.VISIBLE
        } else if (data.status == "belum") {
            binding.tvUnanswered.visibility = View.VISIBLE
        }

        view.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_USER", data.userName)
            intent.putExtra("EXTRA_META", data.meta)
            intent.putExtra("EXTRA_SUBJECT", data.subject)
            intent.putExtra("EXTRA_SUBJECT_BG", data.subjectBg)
            intent.putExtra("EXTRA_SUBJECT_COLOR", data.subjectColor)
            intent.putExtra("EXTRA_TITLE", data.title)
            intent.putExtra("EXTRA_DESC", data.snippet)
            intent.putExtra("EXTRA_VOTES", data.votes)
            intent.putExtra("EXTRA_ANSWERS", data.answers)
            intent.putExtra("EXTRA_AVATAR", data.avatarUrl)
            intent.putExtra("EXTRA_FOTO", data.adaFoto)
            context.startActivity(intent)
        }
        return view
    }
}
