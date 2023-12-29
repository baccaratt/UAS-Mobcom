package com.example.FurniSnap

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean{
	val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val network = cm.activeNetwork
	val capabilities = cm.getNetworkCapabilities(network)

	return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}