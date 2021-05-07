package online.qiqiang.qim.client.service;

import online.qiqiang.qim.common.vo.GroupPushUserVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.common.vo.QimUserVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author qiqiang
 */
public interface GroupService {
    @GET("group/{groupId}")
    Call<QimResponse<List<QimUserVO>>> group(@Path("groupId") String groupId);

    @POST("group/push")
    Call<QimResponse<Void>> push(@Body GroupPushUserVO vo);
}