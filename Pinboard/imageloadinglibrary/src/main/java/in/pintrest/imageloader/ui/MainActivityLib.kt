package `in`.pintrest.imageloader.ui
import `in`.pintrest.imageloader.DisplayImageThumb
import `in`.pintrest.imageloader.R
import `in`.pintrest.imageloader.data.ImageUrlModel
import `in`.pintrest.imageloader.presenter.MainActivityLIbPresenter
import `in`.pintrest.imageloader.service.DownManager
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.view.LayoutInflater
import android.widget.ToggleButton
import okhttp3.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar

class MainActivityLib : AppCompatActivity(), MainActivityLIbPresenter.View  {
    private var recyclerView: RecyclerView? = null
    var presenter:MainActivityLIbPresenter?=null
    companion object {
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lib_activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = GridLayoutManager(this, 3)
        presenter= MainActivityLIbPresenter(this)
        presenter!!.getImgUrls("https://pastebin.com/raw/wgkJgazE")

    }
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
           // write()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //write()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById(R.id.item_imageView) as ImageView
        val imageDownload = view.findViewById(R.id.item_download) as ImageView
        val imageStopDownload = view.findViewById(R.id.item_stopdownload) as ImageView

    }
    override fun setUrlstoAdaptor(arryListUrlModel: ArrayList<ImageUrlModel>) {
       runOnUiThread {
            //stuff that updates ui
            recyclerView?.adapter = ImageAdapter(arryListUrlModel, applicationContext)

        }
    }

}

