package `in`.pintrest.imageloader


import `in`.pintrest.imageloader.ui.MainActivityLib
import android.app.Activity
import android.content.Context
import android.content.Intent

import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast

public class LibMain  constructor(internal var activity: Activity) {


    fun startGPS() {
        //val intent = Intent(activity, MainActivityLib::class.java)

        Utils.startActivity(activity, MainActivityLib::class.java)
        Toast.makeText(activity, "Your GPS started", Toast.LENGTH_LONG).show()
    }

    class Utils {

        companion object {
            fun startActivity(context: Context, clazz: Class<*>) {

                val intent = Intent(context, clazz)

                // start your next activity

                context.startActivity(intent)

            }
        }
    }
}
