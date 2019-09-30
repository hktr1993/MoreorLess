package hikaru.moreorless.API;

import hikaru.moreorless.Model.Stream.Channel;
import hikaru.moreorless.Model.Stream.Channels;
import hikaru.moreorless.Model.Stream.StreamDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitchAPI {
    @GET("streams?limit=100&client_id=replaceWithClientID")
    Call<Channels> getStreamData();
}