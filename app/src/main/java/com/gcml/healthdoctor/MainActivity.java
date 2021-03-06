package com.gcml.healthdoctor;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.PermissionUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonApi;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.gzq.lib_resource.widget.BottomBar;
import com.gzq.lib_resource.widget.BottomBarTab;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import notchtools.geek.com.notchtools.NotchTools;
import timber.log.Timber;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/main/mainactivity")
public class MainActivity extends StateBaseActivity {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;
    private boolean isInit = false;

    @Override
    protected void onStart() {
        super.onStart();
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(CommonApi.class)
                .getProfile(user.getDocterid() + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        Box.getSessionManager().setUser(userEntity);
                    }
                });
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        requestPermissionss();
    }

    private void requestPermissionss() {
        initFragments();
        PermissionUtils.requestEach(this,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String message = e.getMessage();
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtils.showLong(message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();

                    }
                });
    }

    private void initFragments() {
        if (!isInit) {
            isInit = true;
            mFragments[FIRST] = Routerfit.register(AppRouterApi.class).getFirstFragment();
            mFragments[SECOND] = Routerfit.register(AppRouterApi.class).getSecondFragment();
            mFragments[THIRD] = Routerfit.register(AppRouterApi.class).getThirdFragment();
            mFragments[FOURTH] = Routerfit.register(AppRouterApi.class).getFourthFragment();

            loadMultipleRootFragment(R.id.fl_tab_container, 0,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        }
    }

    @Override
    public void initView() {
//        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        //设置状态栏的颜色
        StatusBarCompat.setFitsSystemWindows(getWindow(), true);
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
//        NotchTools.getFullScreenTools().fullScreenUseStatus(this);
        //加载页面成功
        showSuccess();
        mToolbar.setVisibility(View.GONE);
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        final BottomBarTab guardianship = new BottomBarTab(this, R.drawable.ic_first, "签约");
        final BottomBarTab sosDeal = new BottomBarTab(this, R.drawable.ic_second, "消息");
        final BottomBarTab healthManager = new BottomBarTab(this, R.drawable.ic_third, "业务");
        final BottomBarTab mine = new BottomBarTab(this, R.drawable.ic_fourth, "我的");

        //设置各个模块未读信息的数量
        Integer sosDealUnRead = (Integer) KVUtils.get(KVConstants.KEY_SOS_DEAL_UNREAD_NUM, 0);
        sosDeal.setUnreadCount(sosDealUnRead);

        mBottomBar
                .addItem(guardianship)
                .addItem(sosDeal)
                .addItem(healthManager)
                .addItem(mine);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
//                if (position == 3) {
////                    StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
//                } else {
//                    //设置状态栏的颜色
//                    StatusBarCompat.setStatusBarColor(MainActivity.this, Box.getColor(R.color.white));
//                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                final SupportFragment currentFragment = mFragments[position];

                Timber.w(currentFragment + "");

            }
        });

        AppStore.guardianship.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                guardianship.setUnreadCount(integer != null ? integer : 0);
            }
        });

//        healthManager.setUnreadCount(AppStore.healthManager.getValue()!= null ? AppStore.healthManager.getValue() : 0);
        AppStore.healthManager.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                healthManager.setUnreadCount(integer != null ? integer : 0);
            }
        });

//        sosDeal.setUnreadCount(AppStore.sosDeal.getValue()!= null ? AppStore.sosDeal.getValue() : 0);
        AppStore.sosDeal.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                sosDeal.setUnreadCount(integer != null ? integer : 0);
            }
        });
        AppStore.mine.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mine.setUnreadCount(integer != null ? integer : 0);
            }
        });

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultVerticalAnimator();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStore.guardianship.removeObservers(this);
        AppStore.healthManager.removeObservers(this);
        AppStore.sosDeal.removeObservers(this);
        AppStore.mine.removeObservers(this);
    }
}
