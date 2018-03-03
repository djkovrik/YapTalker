/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 David Medenjak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sedsoftware.yaptalker.presentation.features.gallery.adapter

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

@Suppress("MagicNumber")
class LinePagerIndicatorDecoration : RecyclerView.ItemDecoration() {

  companion object {
    private val DP = Resources.getSystem().displayMetrics.density
  }

  private val colorActive = -0x1
  private val colorInactive = 0x66FFFFFF
  private val indicatorHeight = (DP * 16).toInt()
  private val indicatorStrokeWidth = DP * 2
  private val indicatorItemLength = DP * 16
  private val indicatorItemPadding = DP * 4
  private val interpolator = AccelerateDecelerateInterpolator()

  private val mPaint = Paint()

  init {
    mPaint.strokeCap = Paint.Cap.ROUND
    mPaint.strokeWidth = indicatorStrokeWidth
    mPaint.style = Paint.Style.STROKE
    mPaint.isAntiAlias = true
  }

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
    super.onDrawOver(c, parent, state)

    val itemCount = parent.adapter.itemCount

    // center horizontally, calculate width and subtract half from center
    val totalLength = indicatorItemLength * itemCount
    val paddingBetweenItems = Math.max(0, itemCount - 1) * indicatorItemPadding
    val indicatorTotalWidth = totalLength + paddingBetweenItems
    val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

    // center vertically in the allotted space
    val indicatorPosY = parent.height - indicatorHeight / 2f

    drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


    // find active page (which should be highlighted)
    val layoutManager = parent.layoutManager as LinearLayoutManager
    val activePosition = layoutManager.findFirstVisibleItemPosition()
    if (activePosition == RecyclerView.NO_POSITION) {
      return
    }

    // find offset of active page (if the user is scrolling)
    val activeChild = layoutManager.findViewByPosition(activePosition)
    val left = activeChild.left
    val width = activeChild.width

    // on swipe the active item will be positioned from [-width, 0]
    // interpolate offset for smooth animation
    val progress = interpolator.getInterpolation(left * -1 / width.toFloat())

    drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
  }

  private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
    mPaint.color = colorInactive

    // width of item indicator including padding
    val itemWidth = indicatorItemLength + indicatorItemPadding

    var start = indicatorStartX
    for (i in 0 until itemCount) {
      // draw the line for every item
      c.drawLine(start, indicatorPosY, start + indicatorItemLength, indicatorPosY, mPaint)
      start += itemWidth
    }
  }

  private fun drawHighlights(
    c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
    highlightPosition: Int, progress: Float, itemCount: Int
  ) {
    mPaint.color = colorActive

    // width of item indicator including padding
    val itemWidth = indicatorItemLength + indicatorItemPadding

    if (progress == 0f) {
      // no swipe, draw a normal indicator
      val highlightStart = indicatorStartX + itemWidth * highlightPosition
      c.drawLine(
        highlightStart, indicatorPosY,
        highlightStart + indicatorItemLength, indicatorPosY, mPaint
      )
    } else {
      var highlightStart = indicatorStartX + itemWidth * highlightPosition
      // calculate partial highlight
      val partialLength = indicatorItemLength * progress

      // draw the cut off highlight
      c.drawLine(
        highlightStart + partialLength, indicatorPosY,
        highlightStart + indicatorItemLength, indicatorPosY, mPaint
      )

      // draw the highlight overlapping to the next item as well
      if (highlightPosition < itemCount - 1) {
        highlightStart += itemWidth
        c.drawLine(
          highlightStart, indicatorPosY,
          highlightStart + partialLength, indicatorPosY, mPaint
        )
      }
    }
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
    super.getItemOffsets(outRect, view, parent, state)
    outRect.bottom = indicatorHeight
  }
}
