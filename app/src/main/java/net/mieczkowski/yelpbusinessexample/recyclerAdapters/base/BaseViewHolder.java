package net.mieczkowski.yelpbusinessexample.recyclerAdapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private BaseAdapter<T> baseAdapter;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        if (adapterHandleClick()) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseAdapter.onRowClick(getAdapterPosition());
                }
            });
        } else {
            this.itemView.setOnClickListener(null);
        }
    }

    void setBaseAdapter(BaseAdapter<T> baseAdapter) {
        this.baseAdapter = baseAdapter;
    }

    protected boolean adapterHandleClick() {
        return true;
    }

    public abstract void onBind(T object);

}