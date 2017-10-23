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

    @BindView(R.id.txtReviewCount)
    TextView txtReviewCount;


    public SearchBusinessViewHolder(View itemView) {
        super(itemView);

        Drawable ratingsDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_star_rate_white_18dp);
        ratingsDrawable = DrawableCompat.wrap(ratingsDrawable);
        DrawableCompat.setTint(ratingsDrawable, Color.parseColor("#FFD700"));
        txtRatings.setCompoundDrawablesWithIntrinsicBounds(ratingsDrawable, null, null, null);

        Drawable reviewCountDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_edit_black_18dp);
        reviewCountDrawable = DrawableCompat.wrap(reviewCountDrawable);
        DrawableCompat.setTint(reviewCountDrawable, Color.parseColor("#A9A9A9"));
        txtReviewCount.setCompoundDrawablesWithIntrinsicBounds(reviewCountDrawable, null, null, null);
    }

    @Override
    public void onBind(YelpBusiness object) {

        if(object.getBusinessDetails().getImgUrl() != null && !object.getBusinessDetails().getImgUrl().isEmpty()) {
            Picasso.with(itemView.getContext())
                    .load(object.getBusinessDetails().getImgUrl())
                    .into(imgIcon);
        }else{
            imgIcon.setImageResource(R.drawable.ic_close_black_24dp);
        }

        txtTitle.setText(object.getName());

        txtRatings.setText(String.valueOf(object.getBusinessDetails().getRating()));
        txtReviewCount.setText(String.valueOf(object.getBusinessDetails().getReviewCount()));
    }
}
