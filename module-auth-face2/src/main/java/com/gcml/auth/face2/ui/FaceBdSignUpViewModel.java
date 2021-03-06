package com.gcml.auth.face2.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face2.model.FaceBdRepository;
import com.gcml.auth.face2.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class FaceBdSignUpViewModel extends BaseViewModel {
    private FaceBdRepository mFaceBdRepository = new FaceBdRepository();

    public FaceBdSignUpViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<String> addFace(String image, String userId) {
        return mFaceBdRepository.addFace(image, userId, "");
    }

    public Observable<String> addFaceByApi(String userId, String imageData, byte[] image) {
        return mFaceBdRepository.addFaceByApi(userId, imageData, image);
    }

    public Observable<String> addFaceByApi(byte[] image, String userId) {
        return mFaceBdRepository.addFaceByApi(image, userId);
    }

    public Observable<String> verifyLive(List<String> images) {
        return mFaceBdRepository.verifyLive(images);
    }

    public ObservableTransformer<List<String>, String> ensureLive() {
        return mFaceBdRepository.ensureLive();
    }
}
