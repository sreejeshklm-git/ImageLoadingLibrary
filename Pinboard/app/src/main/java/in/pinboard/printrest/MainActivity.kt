package `in`.pinboard.printrest


import `in`.pintrest.imageloader.LibMain
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var startImageLoader: Button
        startImageLoader=findViewById(R.id.startimageloader)
        startImageLoader?.setOnClickListener {
          var libClassObj=LibMain(this)
            libClassObj.startGPS()
        }
    }

    }


