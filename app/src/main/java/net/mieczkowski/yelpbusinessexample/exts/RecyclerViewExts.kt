package net.mieczkowski.yelpbusinessexample.exts

import android.support.v7.widget.RecyclerView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration


/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
fun RecyclerView.addDividerLine(builder: (HorizontalDividerItemDecoration.Builder) -> HorizontalDividerItemDecoration.Builder) {
    addItemDecoration(builder(HorizontalDividerItemDecoration.Builder(context)).build())
}

fun RecyclerView.addVerticalDividerLine(builder: (VerticalDividerItemDecoration.Builder) -> VerticalDividerItemDecoration.Builder) {
    addItemDecoration(builder(VerticalDividerItemDecoration.Builder(context)).build())
}