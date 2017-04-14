package ru.merkulyevsasha.yat.data.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.merkulyevsasha.yat.data.pojo.Trans;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class HttpDataSourceImpl implements HttpDataSource {

    //private static final String API_KEY = "trnsl.1.1.20170410T110433Z.010ec26241c334d0.1259083967c77d0ee5aafe03a16f03c0e8318f0b";
    private static final String API_KEY = "dict.1.1.20170411T042617Z.0bde4877003e9c7a.3b869e72e446cb0f2c49a675d30e3e8f7721524f";

    interface RestApi {

//        @POST("/api/v1.5/tr.json/translate")
//        Call<ResponseBody> translate(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);
//        @POST("/api/v1.5/tr.json/translate")
//        Call<Trans> translateToPojo(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);

        @POST("/api/v1/dicservice.json/lookup")
        Call<ResponseBody> translate(@Query("key") String key, @Query("text") String text, @Query("lang") String lang, @Query("ui") String ui);

        @POST("/api/v1/dicservice.json/lookup")
        Call<Trans> translateToPojo(@Query("key") String key, @Query("text") String text, @Query("lang") String lang, @Query("ui") String ui);


    }

    private final RestApi anRestApi;

    public HttpDataSourceImpl(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://translate.yandex.net/")
                .baseUrl("https://dictionary.yandex.net/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        anRestApi = retrofit.create(RestApi.class);
    }

    @Override
    public Call<ResponseBody> translate(String text, String lang, String ui) {
        return anRestApi.translate(API_KEY, text, lang, ui);
    }

    @Override
    public Call<Trans> translateToPojo(String text, String lang, String ui) {
        return anRestApi.translateToPojo(API_KEY, text, lang, ui);
    }
}