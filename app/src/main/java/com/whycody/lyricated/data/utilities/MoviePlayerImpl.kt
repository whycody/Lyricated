package com.whycody.lyricated.data.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri

class MoviePlayerImpl(private val context: Context): MoviePlayer {

    private val netflixURL = "https://www.netflix.com/watch/"

    override fun playMovie(netflixid: Int, time: String) = tryPlayMovie(netflixid, getSecondsFromTime(time)-2)

    private fun getSecondsFromTime(time: String): Int {
        val hours = time.substring(0, 2).toInt()
        val minutes = time.substring(3, 5).toInt()
        val seconds = time.substring(6, 8).toInt()
        return hours * 3600 + minutes * 60 + seconds
    }

    private fun tryPlayMovie(netflixid: Int, seconds:Int) =
        try { runNetflixApp(netflixid, seconds)
        } catch (e: Exception) { runWebApp(netflixid, seconds) }

    private fun runNetflixApp(netflixid: Int, seconds: Int) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName("com.netflix.mediaclient",
            "com.netflix.mediaclient.ui.launch.UIWebViewActivity")
        intent.data = Uri.parse("$netflixURL$netflixid?t=$seconds")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun runWebApp(netflixid: Int, seconds: Int) {
        val uri = Uri.parse("$netflixURL$netflixid?t=$seconds")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}