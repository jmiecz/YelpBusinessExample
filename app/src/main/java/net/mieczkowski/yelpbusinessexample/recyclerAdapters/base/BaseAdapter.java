package net.mieczkowski.yelpbusinessexample.recyclerAdapters.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/20/2017.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    protected List<T> objects;

    public BaseAdapter(List<T> objects) {
        this.objects = objects;
    }

    protected View getView(ViewGroup parent, @LayoutRes int layoutID) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.setBaseAdapter(this);

        if (position >= objects.size()) {
            holder.onBind(null);
        } else {
            holder.onBind(objects.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return objects == null ? 0 : objects.size();
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    protected void onRowClick(int position) {

    }

    public void removeItem(int position) {
        objects.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(T object) {
        int position = objects.indexOf(object);
        objects.remove(object);
        notifyItemRemoved(position);
    }

    public void addItem(T object, int... position) {
        int positionAdded = objects.size();

        if (position.length > 0) {
            if (position[0] > objects.size()) {
                objects.add(object);
            } else {
                positionAdded = position[0];
                objects.add(positionAdded, object);
            }
        } else {
            objects.add(object);
        }

        notifyItemInserted(positionAdded);
    }

    public void updatedItem(T object) {
        int position = objects.indexOf(object);
        notifyItemChanged(position);
    }

    public void updatedItem(int position) {
        notifyItemChanged(position);
    }

    public void replaceItem(T object) {
        int index = objects.indexOf(object);
        objects.remove(index);
        objects.add(index, object);

        notifyItemChanged(index);
    }

    public T getItem(int position) {
        return objects.get(position);
    }

    public void moveObjects(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(objects, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(objects, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

}
