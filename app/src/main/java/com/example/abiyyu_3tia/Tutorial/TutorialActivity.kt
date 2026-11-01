package com.example.abiyyu_3tia.Tutorial

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.abiyyu_3tia.R
import com.example.abiyyu_3tia.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    private val pageCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateNavigation(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val fragmentsList = listOf(
            Tutorial1Fragment(),
            Tutorial2Fragment(),
            Tutorial3Fragment()
        )
        val adapter = TutorialFragmentAdapter(this, fragmentsList)
        binding.tutorialPager.adapter = adapter
        binding.dotIndicator.attachTo(binding.tutorialPager)

        binding.tutorialPager.registerOnPageChangeCallback(pageCallback)
        updateNavigation(binding.tutorialPager.currentItem)

        binding.btnSkip.setOnClickListener { finish() }
        binding.btnNext.setOnClickListener {
            val page = binding.tutorialPager.currentItem
            if (page == fragmentsList.lastIndex) {
                finish()
            } else {
                binding.tutorialPager.setCurrentItem(page + 1, true)
            }
        }
    }

    private fun updateNavigation(position: Int) {
        val pageCount = binding.tutorialPager.adapter?.itemCount ?: return
        val isLastPage = position == pageCount - 1
        binding.btnSkip.visibility = if (isLastPage) View.INVISIBLE else View.VISIBLE
        binding.btnNext.setText(if (isLastPage) R.string.tutorial_start else R.string.tutorial_next)
    }

    override fun onDestroy() {
        binding.tutorialPager.unregisterOnPageChangeCallback(pageCallback)
        super.onDestroy()
    }
}
