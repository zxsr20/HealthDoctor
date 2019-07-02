package com.gcml.module_mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcml.module_mine.api.MineApi;
import com.gcml.module_mine.api.MineRouterApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/mine/main")
public class MainMineFragment extends StateBaseFragment implements View.OnClickListener {
    private CircleImageView mCivHead;
    /**
     * 李雷
     */
    private TextView mTvName;
    /**
     * 朝明社区
     */
    private TextView mTvCommunity;
    private LinearLayout mLlServiceHistory;
    private LinearLayout mLlSetup;
    private LinearLayout mLlMyMoney;
    private TextView mTvMoney;
    private int moneyAmmount;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.mine_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {
        showSuccess();
    }

    @Override
    public void initView(View view) {

        mCivHead = (CircleImageView) view.findViewById(R.id.civ_head);
        mCivHead.setOnClickListener(this);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvCommunity = (TextView) view.findViewById(R.id.tv_community);
        mLlServiceHistory = (LinearLayout) view.findViewById(R.id.ll_service_history);
        mLlServiceHistory.setOnClickListener(this);
        mLlSetup = (LinearLayout) view.findViewById(R.id.ll_setup);
        mLlSetup.setOnClickListener(this);
        mLlMyMoney = view.findViewById(R.id.ll_my_money);
        mLlMyMoney.setOnClickListener(this);
        mTvMoney = view.findViewById(R.id.tv_money);
        fillData();
    }

    private void fillData() {
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(MineApi.class)
                .getMoneyAmmount(user.getDocterid() + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        moneyAmmount = integer;
                        mTvMoney.setText(integer + "元");
                    }
                });
        Glide.with(Box.getApp())
                .load(user.getDocterPhoto())
                .apply(new RequestOptions().placeholder(R.drawable.de_head))
                .into(mCivHead);
        mTvName.setText(user.getDoctername());
        mTvCommunity.setText(user.getHosname());
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        fillData();
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_my_money) {
            Routerfit.register(MineRouterApi.class).skipMyMoneyPacketActivity(moneyAmmount);
        } else if (i == R.id.ll_service_history) {
            Routerfit.register(MineRouterApi.class).skipMyServiceHistoryActivity();
        } else if (i == R.id.ll_setup) {
            Routerfit.register(MineRouterApi.class).skipSetupActivity();
        } else if (i == R.id.civ_head) {
            Routerfit.register(MineRouterApi.class).skipMyInformationActivity();
        }
    }
}
