package com.spartacus.solitude.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class ViewModelDialogFragment<T extends ViewModel, R extends ViewDataBinding> extends AppCompatDialogFragment {

    private R dataBinding;
    private T viewModel;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        this.viewModel = createViewModel(this.dataBinding);
        dataBinding.setVariable(getViewModelId(), viewModel);

        if (savedInstanceState != null) {
            viewModel.onRestoreInstanceState(savedInstanceState);
        }

        return dataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    protected abstract T createViewModel(R dataBinding);

    public abstract int getLayoutId();

    public abstract int getViewModelId();

    public R getDataBinding() {
        return dataBinding;
    }

    public T getViewModel() {
        return viewModel;
    }
}
