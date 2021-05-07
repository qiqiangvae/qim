package online.qiqiang.qim.client.service;

import online.qiqiang.qim.common.vo.ChatLineVO;
import online.qiqiang.qim.common.vo.QimResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author qiqiang
 */
public interface HistoryMessageService {
    @GET("historyMessage/{userId}")
    Call<QimResponse<List<ChatLineVO>>> historyMessage(@Path("userId") Long userId);
}