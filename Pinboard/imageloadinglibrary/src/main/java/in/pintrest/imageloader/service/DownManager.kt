package `in`.pintrest.imageloader.service

import `in`.pintrest.imageloader.R
import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment

import android.util.Log
import java.util.ArrayList
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT

import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.ImageView

class DownManager(val context: Context,var url:String,var id:String,var action:Boolean,var bmDownImage: ImageView,var bmStopDownImage: ImageView) {

    private lateinit var downloadManager: DownloadManager
    private var refid: Long = 0
    private var Download_Uri: Uri? = null
    internal var list = ArrayList<Long>()
    internal var onComplete: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(ctxt: Context, intent: Intent) {
            bmDownImage.setVisibility(View.VISIBLE);
            bmStopDownImage.setVisibility(View.GONE);
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            list.remove(referenceId)
            if (list.isEmpty()) {
                Log.e("INSIDE", "" + referenceId)
                val mBuilder = NotificationCompat.Builder(ctxt)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("image_downloader")
                        .setContentText("Download completed")
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val CHANNEL_ID = "image_downloader_1"
                    var importance = NotificationManager.IMPORTANCE_HIGH
                    var notificationChannel:NotificationChannel =  NotificationChannel(CHANNEL_ID, "image_downloader", importance);
                    mBuilder.setChannelId(CHANNEL_ID);
                    notificationManager.createNotificationChannel(notificationChannel)
                }
               notificationManager.notify(455, mBuilder.build())
            }

        }
    }
    init {
        if (action) {
            Log.e("url-", url)
            downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            list.clear()
            Download_Uri = Uri.parse(url)
            val request = DownloadManager.Request(Download_Uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("GadgetSaint Downloading " + "Sample" + ".png")
            request.setDescription("Downloading " + "Sample" + ".png")
            request.setVisibleInDownloadsUi(true)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/" + "/" + "Sample" + ".png")
            refid = downloadManager!!.enqueue(request)
             list.add(id.toInt(), refid)
        }
    else{
        downloadManager.remove(list.get(id.toInt()))
            bmDownImage.setVisibility(View.VISIBLE);
            bmStopDownImage.setVisibility(View.GONE);
    }}


}