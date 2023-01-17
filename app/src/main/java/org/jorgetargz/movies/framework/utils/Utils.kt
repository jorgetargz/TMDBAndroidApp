package org.jorgetargz.movies.framework.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import org.jorgetargz.movies.R
import org.jorgetargz.movies.network.Config

object Utils {

    fun hasInternetConnection(context: Context?): Boolean {
        context ?: return false

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    val completeUrl = Config.IMAGE_URL + url
    this.load(completeUrl) {
        scale(Scale.FIT)
        crossfade(true)
        transformations(RoundedCornersTransformation(20f))
        placeholder(R.drawable.arrows_rotate_solid)
        error(R.drawable.ic_cloud_off)
    }
}