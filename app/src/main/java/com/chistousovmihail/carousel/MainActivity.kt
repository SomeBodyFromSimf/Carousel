package com.chistousovmihail.carousel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_view.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        carousel.initList<CustomObj>(R.layout.item_view){holder, item ->
            with(holder.itemView) {
                tvStr.text = item.str
                tvAge.text = item.age.toString()
                tvChas.text = item.chas
            }
        }
        carousel.setData(CustomObj.getList())
    }
}