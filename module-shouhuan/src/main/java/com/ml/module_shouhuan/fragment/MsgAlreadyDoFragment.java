package com.ml.module_shouhuan.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.bean.MsgBean;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.api.ShouhuanRouterApi;
import com.ml.module_shouhuan.presenter.MsgAlreadyDoPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Route(path = "/shouhuan/msgAlreadyDone")
public class MsgAlreadyDoFragment extends StateBaseFragment implements OnRefreshListener {
    private RecyclerView mRvMsgAlreadyDo;
    private MsgAlreadyDoPresenter msgAlreadyDoPresenter;
    private BaseQuickAdapter<MsgBean, BaseViewHolder> adapter;
    private ArrayList<MsgBean> msgBeans = new ArrayList<>();
    private SmartRefreshLayout mRefresh;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_msg_already_do;
    }

    @Override
    public void initParams(Bundle bundle) {
        showSuccess();
    }

    @Override
    public void initView(View view) {
        mRvMsgAlreadyDo = (RecyclerView) view.findViewById(R.id.rv_msg_already_do);
        mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setOnRefreshListener(this);
        mRefresh.autoRefresh();
        initRv();
    }

    @Override
    public IPresenter obtainPresenter() {
        msgAlreadyDoPresenter = new MsgAlreadyDoPresenter(this);
        return msgAlreadyDoPresenter;
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        mRefresh.finishRefresh();
        ArrayList<MsgBean> object = (ArrayList<MsgBean>) objects[0];
        msgBeans.clear();
        msgBeans.addAll(object);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataError(Object... objects) {
        mRefresh.finishRefresh(false);
    }

    @Override
    public void loadDataEmpty() {
        super.loadDataEmpty();
        mRefresh.finishRefresh();
    }

    @Override
    public void onNetworkError() {
        super.onNetworkError();
        mRefresh.finishRefresh();
    }

    private void initRv() {
        mRvMsgAlreadyDo.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMsgAlreadyDo.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 24, Box.getColor(R.color.background_gray_f8f8f8)));
        mRvMsgAlreadyDo.setAdapter(adapter = new BaseQuickAdapter<MsgBean, BaseViewHolder>(R.layout.item_msg_already_do, msgBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MsgBean item) {
                if (item.getWarningType().equals("0")) {
                    helper.setText(R.id.tv_msg_title, item.getUserName() + "测量数据异常");
                    helper.getView(R.id.msg_flag).setVisibility(View.GONE);
                } else if (item.getWarningType().equals("1")) {
                    helper.setText(R.id.tv_msg_title, item.getUserName() + " 发起紧急呼叫");
                    helper.getView(R.id.msg_flag).setVisibility(View.VISIBLE);
                } else if (item.getWarningType().equals("2")) {

                }
                helper.setText(R.id.tv_msg_content, item.getDealContent());
                helper.setText(R.id.tv_msg_deal_time, TimeUtils.milliseconds2String(item.getDealTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Routerfit.register(ShouhuanRouterApi.class).skipMsgAlreadyDoActivity(msgBeans.get(position));
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        msgAlreadyDoPresenter.preData();
    }
}
