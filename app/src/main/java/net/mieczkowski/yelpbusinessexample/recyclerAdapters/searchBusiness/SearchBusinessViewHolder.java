package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchBusiness;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Josh Mieczkowski on 10/22/2017.
 */

public class SearchBusinessViewHolder extends BaseViewHolder<YelpBusiness>{

    @BindView(R.id.imgIcon)
    ImageView imgIcon;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txtRatings)
    TextView txtRatings;

    public SearchBusinessViewHolder(View itemView) {
        super(itemView);

        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_star_rate_white_18dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.parseColor("#FFD700"));
        txtRatings.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    @Override
    public void onBind(YelpBusiness object) {
        Picasso.with(itemView.getContext())
                .load(object.getBusinessDetails().getImgUrl())
                .into(imgIcon);

        txtTitle.setText(object.getName());

        txtRatings.setText(String.valueOf(object.getBusinessDetails().getRating()));
    }
}
