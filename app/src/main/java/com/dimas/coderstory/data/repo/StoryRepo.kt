package com.dimas.coderstory.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dimas.coderstory.data.response.UploadResponse
import com.dimas.coderstory.data.retrofit.ApiService
import com.dimas.coderstory.data.result.Result
import com.dimas.coderstory.data.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryRepo(private val apiService: ApiService){

    fun getStories(token: String): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories("Bearer $token")
            emit(Result.Success(client))
        }catch (e: Exception)
        {
            Log.d("StoryRepo","getStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory("Bearer $token",imageMultipart, desc)
            emit(Result.Success(client))
        }catch (e : Exception){
            e.printStackTrace()
            Log.d("StoryRepo", "uploadStory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepo? = null
        fun getInstance(
            apiService: ApiService
        ): StoryRepo =
            instance ?: synchronized(this) {
                instance ?: StoryRepo(apiService)
            }.also { instance = it }
    }
}
