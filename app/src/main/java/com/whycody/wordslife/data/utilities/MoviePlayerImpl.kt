package com.whycody.wordslife.data.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri

class MoviePlayerImpl(private val context: Context): MoviePlayer {

    override fun playMovie(url: String, time: String) = tryPlayMovie(url, getSecondsFromTime(time))

    private fun getSecondsFromTime(time: String): Int {
        val hours = time.substring(0, 2).toInt()
        val minutes = time.substring(3, 5).toInt()
        val seconds = time.substring(6, 8).toInt()
        return hours * 3600 + minutes * 60 + seconds
    }

    private fun tryPlayMovie(url: String, seconds:Int) =
        try { runNetflixApp(url, seconds)
        } catch (e: Exception) { runWebApp(url, seconds) }

    private fun runNetflixApp(url: String, seconds: Int) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName("com.netflix.mediaclient",
            "com.netflix.mediaclient.ui.launch.UIWebViewActivity")
        intent.data = Uri.parse("https://$url?t=${seconds-5}")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun runWebApp(url: String, seconds: Int) {
        val uri = Uri.parse("https://$url?t=${seconds-5}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}