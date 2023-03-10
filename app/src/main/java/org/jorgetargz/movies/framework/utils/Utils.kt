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
import org.jorgetargz.movies.network.common.Constantes

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
    load(url) {
        scale(Scale.FILL)
        crossfade(true)
        transformations(RoundedCornersTransformation(Constantes.CORNER_RADIUS))
        placeholder(R.drawable.arrows_rotate_solid)
        error(R.drawable.ic_cloud_off)
    }
}

fun ImageView.loadUrlFromTMDB(url: String) {
    val completeUrl = Config.IMAGE_URL + Constantes.ORIGINAL_SIZE + url
    this.load(completeUrl) {
        scale(Scale.FIT)
        crossfade(true)
        transformations(RoundedCornersTransformation(Constantes.CORNER_RADIUS))
        placeholder(R.drawable.arrows_rotate_solid)
        error(R.drawable.ic_cloud_off)
    }
}

fun ImageView.loadUrlFromTMDBW342Size(url: String) {
    val completeUrl = Config.IMAGE_URL + Constantes.W342_SIZE + url
    this.load(completeUrl) {
        scale(Scale.FIT)
        crossfade(true)
        transformations(RoundedCornersTransformation(Constantes.CORNER_RADIUS))
        placeholder(R.drawable.arrows_rotate_solid)
        error(R.drawable.ic_cloud_off)
    }
}