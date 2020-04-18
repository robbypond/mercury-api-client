package com.robbypond.mercury.service;

import com.robbypond.mercury.data.MercuryArticle;
import com.robbypond.mercury.util.HtmlGenerator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface MercuryService {

    Logger log = LoggerFactory.getLogger(MercuryService.class);

    static MercuryService createInstance(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create()).build();
        return retrofit.create(MercuryService.class);
    }

    @GET("parser")
    Call<MercuryArticle> parse(@Query("url") String url);

    default String parseToHtml(String url) {
        log.info("Processing Extract for: [{}]", url);
        retrofit2.Response<MercuryArticle> response;
        try {
            response = parse(url).execute();
            if(response.isSuccessful()) {
                return new HtmlGenerator().generateHtml(response.body());
            } else if(response.errorBody() != null) {
                log.error("Error extraction [{}], Error: [{}]", url, response.errorBody().string());
            }
        } catch (IOException e) {
            log.error("Error extracting: [{}]", url, e);
        }
        log.error("Error extracting [{}], return null html", url);
        return null;
    }
}

