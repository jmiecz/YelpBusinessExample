package net.mieczkowski.yelpbusinessexample.recyclerAdapters.searchHistory;

import android.view.View;
import android.widget.TextView;

import net.mieczkowski.yelpbusinessexample.R;
import net.mieczkowski.yelpbusinessexample.models.PreviousSearch;
import net.mieczkowski.yelpbusinessexample.recyclerAdapters.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

public class SearchHistoryViewHolder extends BaseViewHolder<PreviousSearch> {

    @BindView(R.id.txtSearchHistory)
    TextView txtSearchHistory;

    public SearchHistoryViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(PreviousSearch object) {
        txtSearchHistory.setText(object.getSearchTerm());
    }
}
