package com.example.data.repository

import android.util.Log
import com.example.data.network.entity.FeedResponse
import com.example.data.utils.XmlParser
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.IOException

class FeedRepository(private val xmlParser: XmlParser

) : Repository {

    fun getFeed(feedUrl: String): Single<FeedResponse> {
        return getRSSFeedXml(feedUrl)
            .map { xmlParser.parseFeed(it) }
            .map { removeEmptyItems(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getRSSFeedXml(feedUrl: String): Single<String> {
        val request = Request.Builder().url(feedUrl).build()
        return Single.create { emitter ->
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    emitter.onError(e)
                    Log.e("FEED RESPONSE ERROR", e.localizedMessage ?: "")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful && response.body != null) {
                        emitter.onSuccess(response.body!!.string())
                    } else emitter.onError(Throwable("Feed response unsuccessful"))
                }
            })
        }
    }

    private fun removeEmptyItems(feed: FeedResponse): FeedResponse {
        val list = feed.episodes as MutableList
        list.removeAt(list.lastIndex)
        feed.episodes = list
        return feed
    }

}