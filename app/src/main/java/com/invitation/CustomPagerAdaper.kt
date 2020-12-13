package com.invitation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.mehta.akshaywedding.R
import kotlinx.android.synthetic.main.adapter_images.view.*

class CustomPagerAdapter(
    private val mContext: Context,
    private val listener: OnImageClickListener? = null
) : PagerAdapter() {

    private var images = mContext.resources.obtainTypedArray(R.array.gallery)

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.adapter_images, collection, false)
        layout.image.setImageResource(images.getResourceId(position, -1))
        layout.setOnClickListener { listener?.onImageClick(position) }
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount() = images.length()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}

interface OnImageClickListener {
    fun onImageClick(position: Int)
}