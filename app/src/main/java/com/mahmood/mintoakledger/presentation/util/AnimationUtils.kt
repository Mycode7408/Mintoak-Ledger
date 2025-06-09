package com.mahmood.mintoakledger.presentation.util

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

/**
 * Utility class for animations in the app.
 */
object AnimationUtils {
    
    private const val ANIMATION_DURATION = 300L
    
    /**
     * Animates the rotation of a view.
     * @param view The view to rotate.
     * @param from The starting rotation angle.
     * @param to The ending rotation angle.
     */
    fun rotateView(view: View, from: Float, to: Float) {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = ANIMATION_DURATION
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            view.rotation = valueAnimator.animatedValue as Float
        }
        animator.start()
    }
    
    /**
     * Animates the expansion or collapse of a view.
     * @param view The view to expand or collapse.
     * @param expand True to expand, false to collapse.
     */
    fun toggleExpansion(view: View, expand: Boolean) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val targetHeight = if (expand) view.measuredHeight else 0
        
        // Set initial height
        if (expand) {
            view.layoutParams.height = 0
            view.visibility = View.VISIBLE
        }
        
        // Create animator
        val animator = ValueAnimator.ofInt(view.height, targetHeight)
        animator.duration = ANIMATION_DURATION
        animator.interpolator = DecelerateInterpolator()
        
        // Update height during animation
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            view.layoutParams.height = value
            view.requestLayout()
        }
        
        // Handle completion
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!expand) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
        })
        
        animator.start()
    }
}