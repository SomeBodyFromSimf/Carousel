package com.chistousovmihail.carousel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_view.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carousel.initList<CustomObj>(R.layout.item_view) { holder, item ->
            with(holder.itemView) {
                setOnClickListener {
                    Toast.makeText(
                        context,
                        "${item.str}, position is ${carousel.getCurrentPos()}, realPosition is ${carousel.getCurrentPosInList()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                tvStr.text = item.str
                tvAge.text = item.age.toString()
                tvChas.text = item.chas
            }
        }
        button.setOnClickListener {
            carousel.removeItem(carousel.getCurrentPos())
        }
        carousel.setData(CustomObj.getList())
    }
}