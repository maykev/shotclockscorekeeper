package com.spartacus.solitude.freeplay;


import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.databinding.FragmentFreePlayBinding;
import com.spartacus.solitude.databinding.ViewModelFragment;


public class FreePlayFragment extends ViewModelFragment<FreePlayViewModel, FragmentFreePlayBinding> {

    @Override
    protected FreePlayViewModel createViewModel(FragmentFreePlayBinding dataBinding) {
        return new FreePlayViewModel();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_free_play;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }
}
