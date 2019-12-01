package com.example.focus390;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleAPIService {
    @GET("https://serpapi.com/search.json?engine=google&google_domain=google.com&tbm=isch&api_key=d7d59dc6f0aa37e2220f32f89005f77f4a8bca01c0c3303f2efc2bfcab9e36c7")
    Single<ImageResult> getImageData(@Query("q") String search_text);
}