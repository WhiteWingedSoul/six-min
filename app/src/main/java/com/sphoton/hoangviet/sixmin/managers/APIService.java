package com.sphoton.hoangviet.sixmin.managers;

import com.sphoton.hoangviet.sixmin.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hoangviet on 10/11/16.
 */
public interface APIService {
    @GET(APIManager.API_GETTOPIC)
    Call<List<String>> listTopics();
    @GET(APIManager.API_GETPOST)
    Call<Post> getPost(@Path("topic") String topicName);
}
