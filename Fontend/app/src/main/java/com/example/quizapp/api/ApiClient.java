package com.example.quizapp.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.quizapp.activities.LoginActivity;
import com.example.quizapp.utils.Constants;
import com.example.quizapp.utils.SharedPrefManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static Context context;
    private static final String TAG = "ApiClient";

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
        Log.d(TAG, "ApiClient initialized");
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Logging Interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Auth Interceptor - Add token to header
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();

                    // Add token if available
                    if (context != null) {
                        SharedPrefManager prefManager = SharedPrefManager.getInstance(context);
                        String token = prefManager.getToken();
                        if (token != null && !token.isEmpty()) {
                            requestBuilder.header("Authorization", "Bearer " + token);
                            Log.d(TAG, "Token added to request: " + token.substring(0, Math.min(20, token.length())) + "...");
                        } else {
                            Log.w(TAG, "No token found");
                        }
                    }

                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);
                    Log.d(TAG, "Response: " + response.code() + " - " + response.message());
                    
                    // If 401 or 403, token is invalid - clear it and redirect to login
                    if (response.code() == 401 || response.code() == 403) {
                        Log.e(TAG, "Token invalid! Code: " + response.code() + ". Clearing token and redirecting to login.");
                        if (context != null) {
                            SharedPrefManager prefManager = SharedPrefManager.getInstance(context);
                            prefManager.logout();
                            
                            // Redirect to login activity
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                    }
                    
                    return response;
                }
            };

            // OkHttp Client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Retrofit Instance
            Log.d(TAG, "Creating Retrofit with BASE_URL: " + Constants.BASE_URL);
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
