package com.invitation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.mehta.akshaywedding.R
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment() {

    private var runnable = Runnable {
        ivControl.visibility = View.GONE
    }

    private var handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()
        setupImages()
    }

    private fun setup() {
        val path = "android.resource://${activity?.packageName}/${R.raw.prewedding}"
        Log.d("PATH", Uri.parse(path).toString())
        videoView.setVideoURI(Uri.parse(path))
        videoView.setOnClickListener {
            if (ivControl.visibility == View.VISIBLE) {
                handler.removeCallbacks(runnable)
                ivControl.visibility = View.GONE
            } else {
                ivControl.visibility = View.VISIBLE
                startTimer()
            }
        }
        ivControl.setOnClickListener {
            it.isSelected = it.isSelected.not()
            if (it.isSelected) {
                videoView.start()
            } else {
                videoView.pause()
            }
        }
    }

    private fun startTimer() {
        handler.postDelayed(runnable, 4000)
    }

    private fun setupImages() {
        viewPager.adapter = CustomPagerAdapter(context!!, object : OnImageClickListener {
            override fun onImageClick(position: Int) {
                val intent = Intent(this@GalleryFragment.context, ImageActivity::class.java).apply {
                    putExtra("position", position)
                }
                startActivity(intent)
            }
        })
        ivPrevious.setOnClickListener { viewPager.currentItem = viewPager.currentItem - 1 }
        ivNext.setOnClickListener { viewPager.currentItem = viewPager.currentItem + 1 }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) = Unit

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) = Unit

            override fun onPageSelected(position: Int) {
                ivPrevious.visibility = if (position == 0) View.GONE else View.VISIBLE
                ivNext.visibility =
                    if (position == viewPager.adapter?.count?.minus(1)) View.GONE else View.VISIBLE
            }
        })
    }

    override fun onPause() {
        super.onPause()
        ivControl.isSelected = false
        videoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.suspend()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
