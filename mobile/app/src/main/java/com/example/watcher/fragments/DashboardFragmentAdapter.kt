package com.example.watcher.fragments

import android.content.Context
import android.text.SpannableString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class DashboardFragmentAdapter(fm: FragmentManager, context: Context, private val tabs: Array<String>): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return tabs.size
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            1 ->  ContactsFragment()
            2 -> DeviceFragment()
            else -> BroadcastFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence { // Generate title based on item position
//        val image: Drawable = context.getResources().getDrawable(imageResId.get(position))
//        image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
//        // Replace blank spaces with image icon
        val sb = SpannableString(tabs[position])
//        val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
    }
}
