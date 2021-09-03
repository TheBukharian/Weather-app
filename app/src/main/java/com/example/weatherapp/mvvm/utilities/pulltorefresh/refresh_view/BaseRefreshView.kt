package com.example.weatherapp.mvvm.utilities.pulltorefresh.refresh_view

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import com.example.weatherapp.mvvm.utilities.pulltorefresh.PullToRefreshView

abstract class BaseRefreshView(context: Context?, layout: PullToRefreshView?) : Drawable(),
    Drawable.Callback, Animatable {

    private val mRefreshLayout: PullToRefreshView? = layout
    private var mEndOfRefreshing = false
    val context: Context?
        get() = mRefreshLayout?.context
    val refreshLayout: PullToRefreshView?
        get() = mRefreshLayout

    val opacitY: Int get() = PixelFormat.TRANSLUCENT


    abstract fun setPercent(percent: Float, invalidate: Boolean)
    abstract fun offsetTopAndBottom(offset: Int)
    override fun invalidateDrawable( who: Drawable) {
        val callback: Callback? = callback
        callback?.invalidateDrawable(this)
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        this.callback?.scheduleDrawable(this, what, `when`)
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        this.callback?.unscheduleDrawable(this, what)
    }
    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}



    fun setEndOfRefreshing(endOfRefreshing: Boolean) {
        mEndOfRefreshing = endOfRefreshing
    }

}