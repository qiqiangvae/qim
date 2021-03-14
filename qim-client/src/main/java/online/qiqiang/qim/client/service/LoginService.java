package online.qiqiang.qim.client.service;

import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.vo.QimResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * @author qiqiang
 */
public interface LoginService {
    @POST("login")
    @FormUrlEncoded
    Call<QimResponse<ImServerInfo>> login(@Field("userId") String userId, @Field("password") String password);
}