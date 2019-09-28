package `in`.pintrest.imageloader.ui

import `in`.pintrest.imageloader.DisplayImageThumb
import `in`.pintrest.imageloader.R
import `in`.pintrest.imageloader.data.ImageUrlModel
import `in`.pintrest.imageloader.service.DownManager
import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ImageAdapter( private val items: ArrayList<ImageUrlModel>, private val context: Context) : RecyclerView.Adapter<MainActivityLib.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityLib.ViewHolder {

        return MainActivityLib.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))
    }


    override fun onBindViewHolder(holder: MainActivityLib.ViewHolder, position: Int) {

        try {


            DisplayImageThumb(holder.imageView,holder.imageDownload,items[position].imgurl,context)
            holder.imageView.setOnClickListener {
                if (holder.imageDownload.getVisibility() == View.VISIBLE) {
                    holder.imageDownload.setVisibility(View.GONE);
                    holder.imageStopDownload.setVisibility(View.VISIBLE);
                    DownManager(this.context, items[position].imgurl, items[position].id,true, holder.imageDownload,holder.imageStopDownload)
                }else{
                    holder.imageDownload.setVisibility(View.VISIBLE);
                    holder.imageStopDownload.setVisibility(View.GONE);
                    DownManager(this.context, items[position].imgurl, items[position].id,false,holder.imageDownload,holder.imageStopDownload)
                }
            }
        }catch (e:Exception){

        }

    }

    override fun onViewAttachedToWindow(holder: MainActivityLib.ViewHolder) {
        super.onViewAttachedToWindow(holder)

    }
    override fun getItemCount(): Int {
        return items.size
    }

}