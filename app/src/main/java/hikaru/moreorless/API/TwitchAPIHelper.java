package hikaru.moreorless.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitchAPIHelper {

    public final TwitchAPI api;

    public TwitchAPIHelper() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/kraken/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(TwitchAPI.class);
    }
}
