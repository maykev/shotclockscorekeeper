package com.spartacus.solitude.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class BindingListViewAdapter<T> extends BaseAdapter {

    public final ObservableList<T> items;
    private final Context context;
    private final int layoutId;

    public abstract void bind(ViewDataBinding binding, T model);

    public BindingListViewAdapter(Context context, @LayoutRes int layoutId, ObservableList<T> items) {
        super();

        this.layoutId = layoutId;
        this.context = context;
        this.items = items;
        this.items.addOnListChangedCallback(new WeakOnListChangedCallback<>(changedCallback));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
            binding.getRoot().setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }


        T item = items.get(position);
        bind(binding, item);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    private ObservableList.OnListChangedCallback<ObservableList<T>> changedCallback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
        @Override
        public void onChanged(ObservableList<T> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }
    };
}
