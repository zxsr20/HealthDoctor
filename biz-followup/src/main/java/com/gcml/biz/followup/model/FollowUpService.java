package com.gcml.biz.followup.model;

import com.gcml.biz.followup.model.entity.FollowUpList;
import com.gcml.biz.followup.model.entity.HealthTagEntity;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FollowUpService {


    /**
     * 获取随访信息列表
     *
     * @param doctorId 医生 id
     * @param status   随访状态
     * @param page     页码
     * @param limit    页数
     * @return 随访信息列表
     */
    @GET("ZZB/api/web/follow/getFollowsNew")
    Observable<HttpResult<FollowUpList>> followUpList(
            @Query("planDoctorId") String doctorId,
            @Query("followStatus") String status,
            @Query("page") int page,
            @Query("limit") int limit
    );

    /**
     * 获取居民健康状态标签列表
     *
     * @param code 健康状态标签分类码 ｛职业病管理：professional_user_type， 慢性病： chronic_user_type｝
     * @return 健康状态标签列表
     */
    @GET("ZZB/api/type/getItemByCode")
    Observable<HttpResult<List<HealthTagEntity>>> healthTagList(
            @Query("code") String code
    );
}
