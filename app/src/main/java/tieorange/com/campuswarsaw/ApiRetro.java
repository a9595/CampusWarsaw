package tieorange.com.campuswarsaw;

import retrofit2.Call;
import retrofit2.http.GET;
import tieorange.com.campuswarsaw.api.Events;

/**
 * Created by tieorange on 03/05/16.
 */
public interface ApiRetro {

    @GET("store/connector/f322a560-c40a-48f6-bee6-c11c2838e1e4/_query?input=webpage/url:http%3A%2F%2Frender.import.io%2F%3Furl%3Dhttps%3A%2F%2Fwww.campus.co%2Fwarsaw%2Fen%2Fevents&&_apikey=ac4a2596030246eea01a153a5b50f8bf8d83fcfebeb20555e1c978bf8baa34cc8783b48aa9648c98236227aa39e38c716a3280346535778f39005f54d0a00eb45cdf4387ab49e5af783d95afa60b5c37")
    public Call<Events> getEvents();
}
