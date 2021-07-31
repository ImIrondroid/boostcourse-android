package com.boostcourse.iron.ui.base;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.boostcourse.iron.R;

public abstract class BaseActivity<VM extends BaseViewModel, VDB extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;

    protected VDB binding;

    protected Toolbar toolbar;

    protected boolean isBackButtonVisible() {
        return false;
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @StringRes
    protected abstract int getTitleRes();

    protected Class<VM> getViewModelClazz() {
        return null;
    }

    protected VM getViewModel() {
        return new ViewModelProvider(this).get(getViewModelClazz());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setToolbar();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitleRes());
        if (isBackButtonVisible()) toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }

    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
        binding.setLifecycleOwner(this);

        if (getViewModelClazz() != null && getViewModel() != null) {
            viewModel = getViewModel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
