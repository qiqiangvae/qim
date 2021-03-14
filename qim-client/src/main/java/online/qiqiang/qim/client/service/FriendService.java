package online.qiqiang.qim.client.service;

import online.qiqiang.qim.common.user.QimUser;
import online.qiqiang.qim.common.vo.FriendAddVO;
import online.qiqiang.qim.common.vo.QimResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author qiqiang
 */
public interface FriendService {
    @GET("friend/{userId}")
    Call<QimResponse<List<QimUser>>> allFriends(@Path("userId") Long userId);

    @POST("friend/add")
    Call<QimResponse<Void>> add(@Body FriendAddVO friendAddVO);
}