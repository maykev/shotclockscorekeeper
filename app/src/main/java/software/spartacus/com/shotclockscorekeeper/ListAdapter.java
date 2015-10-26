package software.spartacus.com.shotclockscorekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class ListAdapter<T> extends BaseAdapter {

    private final Object listLock = new Object();

    private final List<T> items = new ArrayList<>();
    private final Context context;
    private final int layout;
    private int dropDownViewResource;

    public ListAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
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
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout, parent, false);
        }
        bindView(view, items.get(position), position);
        return view;
    }

    protected abstract void bindView(View view, T item, int position);

    public void add(int index, T item) {
        synchronized (listLock) {
            items.add(index, item);
        }

        notifyDataSetChanged();
    }

    public void add(T item) {
        synchronized (listLock) {
            items.add(item);
        }

        notifyDataSetChanged();
    }

    public void remove(T item) {
        synchronized (listLock) {
            items.remove(item);
        }

        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (listLock) {
            items.clear();
        }

        notifyDataSetChanged();
    }

    public void replaceAll(Collection<T> collection) {
        synchronized (listLock) {
            items.clear();
            items.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> collection) {
        synchronized (listLock) {
            items.addAll(collection);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(dropDownViewResource, parent, false);
        }
        bindView(view, items.get(position), position);
        return view;
    }

    public void setDropDownViewResource(int dropDownViewResource) {
        this.dropDownViewResource = dropDownViewResource;
    }
}

