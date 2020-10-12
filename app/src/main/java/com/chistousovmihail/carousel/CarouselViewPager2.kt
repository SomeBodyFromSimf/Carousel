package com.chistousovmihail.carousel

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.view_carousel.view.*
import kotlinx.coroutines.*
import kotlin.math.abs

class CarouselViewPager2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CoroutineScope by MainScope() {

    private var elementDistance = 0
    private var loop = NORMAL
    private var isNeedSlide = false
    private var sliderDuration = 3 // in sec
    private var scaleNotCurrent = false

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_carousel, this, true)
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.CarouselViewPager2).apply {
                elementDistance = getDimensionPixelSize(
                    R.styleable.CarouselViewPager2_elementDistance,
                    elementDistance
                )
                scaleNotCurrent =
                    getBoolean(R.styleable.CarouselViewPager2_scaleNotCurrent, scaleNotCurrent)
                isNeedSlide = getBoolean(R.styleable.CarouselViewPager2_slider, isNeedSlide)
                sliderDuration =
                    getInt(R.styleable.CarouselViewPager2_sliderDuration, sliderDuration)
                loop = getInt(R.styleable.CarouselViewPager2_loop, loop)
                recycle()
            }
        }
    }

    private val sliderCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {

            private var sliderJob: Job? = null

            fun getSliderJob(): Job = launch {
                delay(sliderDuration * 1000L)
                with(pager) {
                    if (currentItem != adapter?.itemCount)
                        currentItem++
                    else
                        cancel(CancellationException("final page"))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> sliderJob?.cancel()
                    ViewPager2.SCROLL_STATE_SETTLING -> sliderJob = getSliderJob()
                    ViewPager2.SCROLL_STATE_IDLE -> {
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                sliderJob?.cancel()
                sliderJob = getSliderJob()
            }
        }
    }

    fun <T : Parcelable> initList(
        @LayoutRes itemRes: Int,
        onBind: (RecyclerView.ViewHolder, T) -> Unit
    ) = with(pager) {
        clipToPadding = false
        clipChildren = false
        offscreenPageLimit = 3
        adapter = CustomAdapter(itemRes, onBind, loop)
        if (isNeedSlide)
            registerOnPageChangeCallback(sliderCallback)
        setPageTransformer(CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(elementDistance))
            if (scaleNotCurrent)
                addTransformer { page, position ->
                    (0.15f * (1 - abs(position))).let {
                        page.scaleY = 0.85f + it
                    }
                }
        })
    }

    fun <T : Parcelable> setData(list: List<T>) {
        (pager.adapter as? CustomAdapter<T>)?.setData(list)
        if (list.size > 1 && loop == INFINITE)
            pager.setCurrentItem((Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % list.size, false)
    }

    fun getCurrentPos(): Int = pager?.currentItem ?: 0

    fun getCurrentPosInList(): Int =
        (pager?.currentItem?.rem((pager.adapter as? CustomAdapter<*>)?.list?.size ?: 0)) ?: 0

    fun removeItem(pos: Int) = (pager.adapter as? CustomAdapter<*>)?.removeItem(pos)

    override fun onViewRemoved(child: View?) {
        cancel()
        super.onViewRemoved(child)
    }

    companion object {
        private const val NORMAL = 0
        private const val INFINITE = 1
    }
}
