package com.yefeng.androidarchitecturedemo.data.source.book.remote;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.support.http.HttpRes;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by yefeng on 06/02/2017.
 * http api
 */

public interface BookApi {
    @GET("getBooks")
    Flowable<HttpRes<List<Book>>> getBooks();

    @POST("saveBook")
    Flowable<HttpRes<String>> saveBook(@Body @NonNull Book book);

    @GET("deleteBook")
    Flowable<HttpRes<String>> deleteBook(@Query("id") @NonNull String id);
}
