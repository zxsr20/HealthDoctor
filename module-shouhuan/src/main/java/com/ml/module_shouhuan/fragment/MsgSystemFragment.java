package com.ml.module_shouhuan.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.adapter.MsgSystemAdapter;
import com.ml.module_shouhuan.widget.MyItemDecoration;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/shouhuan/msgSystem")
public class MsgSystemFragment extends StateBaseFragment {
    private RecyclerView mRecyclerView;
    private MsgSystemAdapter mAdapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_msg_system;
    }

    @Override
    public void initParams(Bundle bundle) {
        showSuccess();
    }

    @Override
    public void initView(View view) {
        showSuccess();
        mRecyclerView = view.findViewById(R.id.rv_msg_system);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter = new MsgSystemAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
