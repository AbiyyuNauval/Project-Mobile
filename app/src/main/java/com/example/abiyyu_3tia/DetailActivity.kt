package com.example.abiyyu_3tia

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.abiyyu_3tia.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, 0, systemBars.right, maxOf(systemBars.bottom, ime.bottom))
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val user = intent.getStringExtra("EXTRA_USER") ?: "rina_a"
        val meta = intent.getStringExtra("EXTRA_META") ?: "SMA · Kelas 11 · 2 jam lalu"
        val subject = intent.getStringExtra("EXTRA_SUBJECT") ?: "Matematika"
        val subjectBg = intent.getIntExtra("EXTRA_SUBJECT_BG", R.color.subj_math_bg)
        val subjectColor = intent.getIntExtra("EXTRA_SUBJECT_COLOR", R.color.subj_math_fg)
        val title = intent.getStringExtra("EXTRA_TITLE") ?: getString(R.string.detail_q_title)
        val desc = intent.getStringExtra("EXTRA_DESC") ?: getString(R.string.detail_q_body)
        val votes = intent.getIntExtra("EXTRA_VOTES", 12)
        val answers = intent.getIntExtra("EXTRA_ANSWERS", 3)
        val avatarUrl = intent.getStringExtra("EXTRA_AVATAR") ?: "https://avatar.iran.liara.run/public/1"
        val adaFoto = intent.getBooleanExtra("EXTRA_FOTO", true)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.title = "Detail Pertanyaan"
            this.subtitle = "$subject · $answers jawaban"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        Glide.with(this)
            .load(avatarUrl)
            .into(binding.imgAvatar)

        binding.tvUser.text = user
        binding.tvMeta.text = meta
        binding.tvSubject.text = subject
        binding.tvSubject.backgroundTintList = ContextCompat.getColorStateList(this, subjectBg)
        binding.tvSubject.setTextColor(ContextCompat.getColor(this, subjectColor))
        binding.tvQuestionTitle.text = title
        binding.tvQuestionBody.text = desc
        binding.btnVoteQuestion.text = votes.toString()
        binding.tvAnswerCount.text = "$answers Jawaban"

        if (!adaFoto) {
            binding.layoutQuestionPhoto.visibility = View.GONE
        }

        if (answers < 3) {
            binding.cardAnswer3.visibility = View.GONE
        }
        if (answers < 2) {
            binding.cardAnswer2.visibility = View.GONE
        }
        if (answers < 1) {
            binding.cardAnswer1.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_bookmark, R.id.action_share, R.id.action_report -> {
                Toast.makeText(this, "${item.title} Clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
