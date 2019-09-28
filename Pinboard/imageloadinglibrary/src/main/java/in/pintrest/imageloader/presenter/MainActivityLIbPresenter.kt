package `in`.pintrest.imageloader.presenter
import `in`.pintrest.imageloader.data.ImageUrlModel
import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivityLIbPresenter(view:View) {
    private var ingUrlModel: ImageUrlModel? = null
    private var view: View? = null
    val client = OkHttpClient();
    var arrayList_url: ArrayList<ImageUrlModel> = ArrayList();
    init {
        this.ingUrlModel = ImageUrlModel()
        this.view = view
    }
    fun getImgUrls(url:String){

        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //progress.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body()!!.string()
                val jsonArray = JSONArray(str_response)

                for (i in 0 until 10) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    var urls = jsonObject.getString("urls");
                    val urlsObj = JSONObject(urls)
                    var imgUrlModel:ImageUrlModel= ImageUrlModel();
                    imgUrlModel.id=(i.toString())
                    imgUrlModel.imgurl=(urlsObj.getString("raw"))
                    arrayList_url.add(imgUrlModel)
                }
                 view?.setUrlstoAdaptor(arrayList_url)

            }
        })
    }
    interface View {
        fun setUrlstoAdaptor(arryListUrlModel: ArrayList<ImageUrlModel>)

    }


}