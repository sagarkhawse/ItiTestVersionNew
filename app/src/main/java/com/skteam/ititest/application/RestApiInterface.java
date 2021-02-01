package com.skteam.ititest.application;

import com.skteam.ititest.restModel.editProdile.EditProfileResponse;
import com.skteam.ititest.setting.AppConstance;

import java.io.File;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestApiInterface {


    @Multipart
    @POST(AppConstance.UPDATE_DP_RETROFIT)
    Call<EditProfileResponse> UploadDp(@Part MultipartBody.Part profile_pic, @Part("user_id") RequestBody user_id);
}
