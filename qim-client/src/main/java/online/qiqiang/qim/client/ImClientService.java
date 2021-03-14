package online.qiqiang.qim.client;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author qiqiang
 */
public class ImClientService {

    private final String routeAddress;

    public ImClientService(String routeAddress) {
        this.routeAddress = routeAddress;
    }

    public <T> T getService(Class<T> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(routeAddress)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}