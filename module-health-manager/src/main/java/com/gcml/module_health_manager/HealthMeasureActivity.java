package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gcml.module_health_manager.bean.FamilyDoctorServiceBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/healthmanager/health/measure")
public class HealthMeasureActivity extends StateBaseActivity {
    private CircleImageView mCivHead;
    /**
     * 李小大，您好！
     */
    private TextView mTvName;
    private LinearLayout mLlTitle;
    private RecyclerView mRvMenu;
    private ArrayList<FamilyDoctorServiceBean> familyDoctorServiceBeans = new ArrayList<FamilyDoctorServiceBean>() {{
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_bloodpressure, "血压检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_ecg, "心电检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_bloodsugar, "血糖检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_bloodoxygen, "血氧检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_temperature, "体温检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_weight, "体重检测"));
        add(new FamilyDoctorServiceBean(R.drawable.ic_measure_three_in_one, "三合一检测"));
    }};
    private BaseQuickAdapter<FamilyDoctorServiceBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_measure;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("健康检测");
        mCivHead = (CircleImageView) findViewById(R.id.civ_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        fillData();
        initRv();
    }

    private void fillData() {
        UserEntity user = Box.getSessionManager().getUser();
        Glide.with(Box.getApp())
                .load(user.getDocterPhoto())
                .into(mCivHead);
        mTvName.setText(user.getDoctername() + "，您好！");
    }

    private void initRv() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 24, Box.getColor(R.color.background_gray_f8f8f8)));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<FamilyDoctorServiceBean, BaseViewHolder>(R.layout.item_layout_family_doctor_menu, familyDoctorServiceBeans) {
            @Override
            protected void convert(BaseViewHolder helper, FamilyDoctorServiceBean item) {
                ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(item.getIcon());
                helper.setText(R.id.tv_title, item.getTitle());

            }
        });
        TextView foot = new TextView(this);
        foot.setText("该功能根据具体需求定制");
        foot.setGravity(Gravity.CENTER);
        foot.setTextColor(ContextCompat.getColor(this,R.color.text_normal_color));
        foot.setTextSize(16);
        adapter.addFooterView(foot);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Routerfit.register(HealthManagerRouterApi.class).skipHealthMeasureDetailActivity(position + "", familyDoctorServiceBeans);
//                if (position == 0) {
//                    //血压
//                } else if (position == 1) {
//                    //心电
//                } else if (position == 2) {
//                    //血糖
//                } else if (position == 3) {
//                    //血氧
//                } else if (position == 4) {
//                    //体温
//                } else if (position == 5) {
//                    //体重
//                } else if (position == 6) {
//                    //三合一
//                }
            }
        });
    }


    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
