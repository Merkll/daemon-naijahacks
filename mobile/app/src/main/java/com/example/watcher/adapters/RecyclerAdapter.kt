package com.example.watcher.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watcher.R
import com.example.watcher.extentions.inflate

class RecyclerAdapter<T>(private val data: ArrayList<T>, private val singleItemLayout: Int, private val bindAction: (item: T, view: View) -> Unit ): RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.Holder {
        val inflatedView = parent.inflate(singleItemLayout, false)

        return Holder(inflatedView)
    }

    fun updateAdapter(newData: ArrayList<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item: T = data[position]
        bindAction(item, holder.view)
    }


    class Holder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
            Log.i("RECYCLER", view.toString())
        }

        //4
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {

        }

//        fun bindPhoto(item: T) {
//            Log.i("RECYCLER BIND", "${view.toString()}, $item")
//
//            return item
//        }
    }

}