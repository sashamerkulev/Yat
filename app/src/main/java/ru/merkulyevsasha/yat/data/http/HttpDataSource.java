package ru.merkulyevsasha.yat.data.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.yat.data.pojo.Trans;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public interface HttpDataSource {

    Call<ResponseBody> translate(String text, String lang, String ui);
    Call<Trans> translateToPojo(String text, String lang, String ui);

}
