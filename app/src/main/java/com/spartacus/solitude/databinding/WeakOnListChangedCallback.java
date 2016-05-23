package com.spartacus.solitude.databinding;

import android.databinding.ObservableList;

import java.lang.ref.WeakReference;


public class WeakOnListChangedCallback<T extends ObservableList> extends ObservableList.OnListChangedCallback<T> {

    private final WeakReference<ObservableList.OnListChangedCallback<T>> weakCallback;

    public WeakOnListChangedCallback(ObservableList.OnListChangedCallback<T> callback) {
        this.weakCallback = new WeakReference<>(callback);
    }

    @Override
    public void onChanged(T sender) {
        ObservableList.OnListChangedCallback<T> callback = weakCallback.get();
        if (callback != null) {
            callback.onChanged(sender);
        }
    }

    @Override
    public void onItemRangeChanged(T sender, int positionStart, int itemCount) {
        ObservableList.OnListChangedCallback<T> callback = weakCallback.get();
        if (callback != null) {
            callback.onItemRangeChanged(sender, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(T sender, int positionStart, int itemCount) {
        ObservableList.OnListChangedCallback<T> callback = weakCallback.get();
        if (callback != null) {
            callback.onItemRangeInserted(sender, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeMoved(T sender, int fromPosition, int toPosition, int itemCount) {
        ObservableList.OnListChangedCallback<T> callback = weakCallback.get();
        if (callback != null) {
            callback.onItemRangeMoved(sender, fromPosition, toPosition, itemCount);
        }
    }

    @Override
    public void onItemRangeRemoved(T sender, int positionStart, int itemCount) {
        ObservableList.OnListChangedCallback<T> callback = weakCallback.get();
        if (callback != null) {
            callback.onItemRangeRemoved(sender, positionStart, itemCount);
        }
    }
}
