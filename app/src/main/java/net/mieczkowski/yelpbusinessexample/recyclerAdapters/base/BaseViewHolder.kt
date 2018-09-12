package net.mieczkowski.yelpbusinessexample.recyclerAdapters.base

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var baseAdapter: BaseAdapter<T>

    init {

        if (adapterHandleClick()) {
            this.itemView.setOnClickListener { baseAdapter.onRowClick(adapterPosition) }
        } else {
            this.itemView.setOnClickListener(null)
        }
    }

    fun setBaseAdapter(baseAdapter: BaseAdapter<T>) {
        this.baseAdapter = baseAdapter
    }

    protected fun adapterHandleClick(): Boolean {
        return true
    }

    abstract fun onBind(item: T?)

}