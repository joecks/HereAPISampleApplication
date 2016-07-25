package x0r.hereapisampleapplication.data;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import x0r.hereapisampleapplication.domain.HereAPI;
import x0r.hereapisampleapplication.model.APIParameter;

/**
 *
 * Used to build a {@link HereAPI} instance
 *
 */
public final class HereAPIFactory {

    private HereAPIFactory() {
    }

    /**
     * Constructs a new instance of the {@link HereAPI}
     * @param appId
     * @param appCode
     * @return
     */
    public static HereAPI getHereAPI(String appId, String appCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIParameter.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient(appId, appCode))
                .build();

        return retrofit.create(HereAPI.class);
    }

    /**
     * Constructs a new {@link OkHttpClient} instance. Automatically applies appId and appCode parameters to every outgoing request
     * @param appId
     * @param appCode
     * @return
     */
    private static OkHttpClient getHttpClient(String appId, String appCode) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter(APIParameter.Request.APP_ID, appId)
                    .addQueryParameter(APIParameter.Request.APP_CODE, appCode)
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();

            return chain.proceed(request);
        }).build();
    }
}
