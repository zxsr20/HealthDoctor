package com.gcml.module_health_record.network;

import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.LatestDetecBean;
import com.gcml.module_health_record.bean.PulseHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/20 10:31
 * created by:gzq
 * description:TODO
 */
public interface HealthRecordServer {
    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<TemperatureHistory>>> getTemperatureHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<BloodPressureHistory>>> getBloodpressureHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<BloodSugarHistory>>> getBloodSugarHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<BloodOxygenHistory>>> getBloodOxygenHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<HeartRateHistory>>> getHeartRateHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<PulseHistory>>> getPulseHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<CholesterolHistory>>> getCholesterolHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<BUA>>> getBUAHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<ECGHistory>>> getECGHistory(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @POST("ZZB/br/cl_data")
    Observable<HttpResult<List<WeightHistory>>> getWeight(
            @Query("bid") String userID,
            @Query("starttime") String start,
            @Query("endtime") String end,
            @Query("temp") String temp);

    @GET("ZZB/bl/getPatientHealthData")
    Observable<HttpResult<List<LatestDetecBean>>> getLatestDetectionData(@Query("userId") String userId);
}
