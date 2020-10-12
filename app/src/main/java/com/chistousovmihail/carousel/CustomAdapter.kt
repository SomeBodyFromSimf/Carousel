package com.chistousovmihail.carousel

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class CustomAdapter<T : Parcelable>(
    @LayoutRes private val itemRes: Int,
    private val onBind: (RecyclerView.ViewHolder, T) -> Unit,
    private var loop: Int = NORMAL
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<T> = mutableListOf()
        private set

    fun setData(list: List<T>) {
        this.list.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int): Boolean {
        if (list.isEmpty())
            return false
        list.removeAt(pos.rem(list.size))
        if (loop == INFINITE && list.size > 1) {
            for (i in pos - list.size * list.size..pos + list.size * list.size step list.size) {
                notifyItemRemoved(i)
                notifyItemRangeChanged(i, list.size)
            }
            return true
        } else {
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, list.size)
            notifyDataSetChanged()
        }
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(itemRes, parent, false)
        ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        onBind(holder, list[position.rem(list.size)])

    override fun getItemCount(): Int = when (loop) {
        NORMAL -> list.size
        INFINITE -> when (list.size) {
            0, 1 -> list.size
            else -> Int.MAX_VALUE
        }
        else -> 0
    }

    companion object {
        const val NORMAL = 0
        const val INFINITE = 1
    }
}