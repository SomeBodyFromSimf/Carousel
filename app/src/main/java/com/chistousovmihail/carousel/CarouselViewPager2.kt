package com.chistousovmihail.carousel

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_carousel.view.*

class CarouselViewPager2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_carousel, this, true)
    }

    fun <T:Parcelable> initList(@LayoutRes itemRes: Int, onBind: (RecyclerView.ViewHolder, T)->Unit ) {
        pager.adapter = CarouselAdapter(itemRes,onBind)
    }

    fun <T:Parcelable >setData(list: List<T>) {
        (pager.adapter as? CarouselAdapter<T>)?.setData(list)
    }
}

class CarouselAdapter<T : Parcelable> (
    @LayoutRes private val itemRes: Int,
    private val onBind: (RecyclerView.ViewHolder, T)->Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<T> = mutableListOf()

    fun setData(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int) : Boolean {
        if((0 until itemCount).contains(pos)) {
            list.removeAt(pos)
            notifyItemRemoved(pos)
            return true
        }
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = object : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(itemRes, parent, false)) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = onBind(holder, list[position])

    override fun getItemCount(): Int = list.size

}
