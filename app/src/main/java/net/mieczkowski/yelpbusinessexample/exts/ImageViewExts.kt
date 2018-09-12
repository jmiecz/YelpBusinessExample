package net.mieczkowski.yelpbusinessexample.exts

import android.widget.ImageView
import com.squareup.picasso.Picasso
import net.mieczkowski.yelpbusinessexample.R

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
fun ImageView.loadUrl(url: String?) {
    url?.let {
        if (it.isNotEmpty())
            Picasso.with(context)
                    .load(it)
                    .into(this)
        else
            setImageResource(R.drawable.ic_close_black_24dp)
    } ?: setImageResource(R.drawable.ic_close_black_24dp)
}