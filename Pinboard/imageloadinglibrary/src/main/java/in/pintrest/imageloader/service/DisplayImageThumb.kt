package `in`.pintrest.imageloader


import `in`.appybot.miidemo.util.Utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.util.LruCache
import android.view.View
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class DisplayImageThumb(var bmImage: ImageView,var bmDownImage: ImageView,var url:String,context:Context)  {
    private val maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8
    private val memoryCache: LruCache<String, Bitmap>
    var urldisplay:String
    var notFound:String
    var screenWidth:Int
    var screenHeight:Int
    var mContext:Context
    var notfoundBitmap:Bitmap
    var resourceFound:Boolean
    private val imageViewMap = Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    init {
        memoryCache = object : LruCache<String, Bitmap>(maxCacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.byteCount / 1024
            }
        }
        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        urldisplay=url
        mContext=context
        resourceFound=false
        notFound="https://images.unsplash.com/photo-1464550883968-cec281c19761"
        notfoundBitmap = BitmapFactory.decodeResource(context.getResources(), `in`.pintrest.imageloader.R.drawable.filenotfound)

        val bitmap = checkImageInCache(urldisplay)

        bitmap?.let {

            loadImageIntoImageView(bmImage, it, urldisplay)
            Log.e("from cache","=amma");
        } ?: run {
            Log.e("from url","=amma");
            DisplayImageThumbUrl(bmImage).execute(urldisplay)

        }

    }

    @Synchronized
    private  fun loadImageIntoImageView(imageView: ImageView, bitmap: Bitmap?, imageUrl: String) {

        require(bitmap != null) {
            "ImageLoader:loadImageIntoImageView - Bitmap should not be null"
        }

        val scaledBitmap = Utils.scaleBitmapForLoad(bitmap, imageView.width, imageView.height)

        scaledBitmap?.let {
            if(!isImageViewReused(ImageRequest(imageUrl, imageView))) imageView.setImageBitmap(scaledBitmap)
        }
    }

    @Synchronized
    private fun checkImageInCache(imageUrl: String): Bitmap? = memoryCache.get(imageUrl)

    private fun isImageViewReused(imageRequest: ImageRequest): Boolean {
        val tag = imageViewMap[imageRequest.imageView]
        return tag == null || tag != imageRequest.imgUrl
    }

    inner class ImageRequest(var imgUrl: String, var imageView: ImageView)

    inner class DisplayImageThumbUrl(var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            urldisplay = urls[0]
            var bmp: Bitmap? = null

            try {
                if(doesURLExist(URL(urldisplay))) {
                    resourceFound=true
                    val `in` = URL(urldisplay).openStream()
                    bmp = BitmapFactory.decodeStream(`in`)
                    memoryCache.put(urldisplay, bmp)
                }else {

                    resourceFound=false
                    bmp =notfoundBitmap
                }
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return bmp
        }

        override fun onPostExecute(result: Bitmap) {
            val resizedBitmap = resizeBitmap(result,screenWidth,screenHeight)
           if(result!=null){
               bmImage.setImageBitmap(resizedBitmap)
               if(resourceFound) {
                   bmDownImage.setVisibility(View.VISIBLE);
               }
           }else{
               val id = mContext.getResources().getIdentifier("`in`.pintrest.imageloader:drawable/filenotfound", null, null)
               bmImage.setImageResource(id)
        }


        }
        @Throws(IOException::class)
        fun doesURLExist(url: URL): Boolean {
            // We want to check the current URL
            HttpURLConnection.setFollowRedirects(false)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            // We don't need to get data
            httpURLConnection.setRequestMethod("HEAD")
            // Some websites don't like programmatic access so pretend to be a browser
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)")
            val responseCode = httpURLConnection.getResponseCode()
            // We only accept response code 200
            return responseCode == HttpURLConnection.HTTP_OK
        }
    }

    private fun resizeBitmap(bitmap:Bitmap, width:Int, height:Int):Bitmap{

        return Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false
        )
    }
}