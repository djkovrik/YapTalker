/*
 * Copyright (C) 2011 Micah Hainline
 * Copyright (C) 2012 Triposo
 * Copyright (C) 2013 Paul Imhoff
 * Copyright (C) 2014 Shahin Yousefi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sedsoftware.yaptalker.presentation.custom.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.Layout.Alignment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextUtils
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * A [android.widget.TextView] that ellipsizes more intelligently.
 * This class supports ellipsizing multiline text through setting `android:ellipsize`
 * and `android:maxLines`.
 */
@Suppress("UnsafeCallOnNullableType")
class EllipsizingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : IconicsTextView(context, attrs, defStyle) {

    interface EllipsizeListener {
        fun ellipsizeStateChanged(ellipsized: Boolean)
    }

    companion object {
        private const val ELLIPSIS = "\u2026"
        private val DEFAULT_END_PUNCTUATION = Pattern.compile("[.!?,;:\u2026]*$", Pattern.DOTALL)
    }

    private val mEllipsizeListeners = ArrayList<EllipsizeListener>()
    private var mEllipsizeStrategy: EllipsizeStrategy? = null
    private var isEllipsized: Boolean = false
    private var isStale: Boolean = false
    private var programmaticChange: Boolean = false
    private var mFullText: CharSequence? = null
    private var mMaxLines: Int = 0
    private var mLineSpacingMult = 1.0f
    private var mLineAddVertPad = 0.0f

    private var mEndPunctPattern: Pattern? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.maxLines), defStyle, 0)
        maxLines = attr.getInt(0, Integer.MAX_VALUE)
        attr.recycle()
        setEndPunctuationPattern(DEFAULT_END_PUNCTUATION)
    }

    private fun setEndPunctuationPattern(pattern: Pattern) {
        mEndPunctPattern = pattern
    }

    @SuppressLint("Override")
    override fun getMaxLines(): Int = mMaxLines

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        mMaxLines = maxLines
        isStale = true
    }

    fun ellipsizingLastFullyVisibleLine(): Boolean = mMaxLines == Integer.MAX_VALUE

    override fun setLineSpacing(add: Float, mult: Float) {
        mLineAddVertPad = add
        mLineSpacingMult = mult
        super.setLineSpacing(add, mult)
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        if (!programmaticChange) {
            mFullText = text
            isStale = true
        }
        super.setText(text, type)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (ellipsizingLastFullyVisibleLine()) isStale = true
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        if (ellipsizingLastFullyVisibleLine()) isStale = true
    }

    override fun onDraw(canvas: Canvas) {
        if (isStale) resetText()
        super.onDraw(canvas)
    }

    private fun resetText() {
        val maxLines = maxLines
        var workingText = mFullText
        var ellipsized = false

        if (maxLines != -1) {
            if (mEllipsizeStrategy == null) ellipsize = null
            workingText = mEllipsizeStrategy!!.processText(mFullText)
            ellipsized = !mEllipsizeStrategy!!.isInLayout(mFullText)
        }

        if (workingText != text) {
            programmaticChange = true
            try {
                text = workingText
            } finally {
                programmaticChange = false
            }
        }

        isStale = false
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized
            for (listener in mEllipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized)
            }
        }
    }

    override fun setEllipsize(where: TruncateAt?) {
        if (where == null) {
            mEllipsizeStrategy = EllipsizeNoneStrategy()
            return
        }

        when (where) {
            TextUtils.TruncateAt.END -> mEllipsizeStrategy = EllipsizeEndStrategy()
            TextUtils.TruncateAt.START -> mEllipsizeStrategy = EllipsizeStartStrategy()
            TextUtils.TruncateAt.MIDDLE -> mEllipsizeStrategy = EllipsizeMiddleStrategy()
            TextUtils.TruncateAt.MARQUEE -> {
                super.setEllipsize(where)
                isStale = false
                mEllipsizeStrategy = EllipsizeNoneStrategy()
            }
            else -> mEllipsizeStrategy = EllipsizeNoneStrategy()
        }
    }

    private abstract inner class EllipsizeStrategy {

        protected val linesCount: Int
            get() {
                return if (ellipsizingLastFullyVisibleLine()) {
                    val fullyVisibleLinesCount = fullyVisibleLinesCount
                    if (fullyVisibleLinesCount == -1) 1 else fullyVisibleLinesCount
                } else {
                    mMaxLines
                }
            }

        protected val fullyVisibleLinesCount: Int
            get() {
                val layout = createWorkingLayout("")
                val height = height - compoundPaddingTop - compoundPaddingBottom
                val lineHeight = layout.getLineBottom(0)
                return height / lineHeight
            }

        fun processText(text: CharSequence?): CharSequence? =
            if (!isInLayout(text)) createEllipsizedText(text) else text

        fun isInLayout(text: CharSequence?): Boolean {
            val layout = createWorkingLayout(text)
            return layout.lineCount <= linesCount
        }

        protected fun createWorkingLayout(workingText: CharSequence?): Layout =
            StaticLayout(
                workingText, paint,
                measuredWidth - paddingLeft - paddingRight,
                Alignment.ALIGN_NORMAL, mLineSpacingMult,
                mLineAddVertPad, false /* includepad */
            )

        protected abstract fun createEllipsizedText(fullText: CharSequence?): CharSequence?
    }

    private inner class EllipsizeNoneStrategy : EllipsizeStrategy() {

        override fun createEllipsizedText(fullText: CharSequence?): CharSequence? = fullText
    }

    private inner class EllipsizeEndStrategy : EllipsizeStrategy() {

        override fun createEllipsizedText(fullText: CharSequence?): CharSequence {
            val layout = createWorkingLayout(fullText)
            val cutOffIndex = layout.getLineEnd(mMaxLines - 1)
            val textLength = fullText!!.length
            var cutOffLength = textLength - cutOffIndex
            if (cutOffLength < ELLIPSIS.length) cutOffLength = ELLIPSIS.length
            var workingText = TextUtils.substring(fullText, 0, textLength - cutOffLength).trim { it <= ' ' }
            var strippedText = stripEndPunctuation(workingText)

            while (!isInLayout(strippedText + ELLIPSIS)) {
                val lastSpace = workingText.lastIndexOf(' ')
                if (lastSpace == -1) break
                workingText = workingText.substring(0, lastSpace).trim { it <= ' ' }
                strippedText = stripEndPunctuation(workingText)
            }

            workingText = strippedText + ELLIPSIS
            val dest = SpannableStringBuilder(workingText)

            if (fullText is Spanned) {
                TextUtils.copySpansFrom(fullText as Spanned?, 0, workingText.length, null, dest, 0)
            }
            return dest
        }

        fun stripEndPunctuation(workingText: CharSequence): String =
            mEndPunctPattern!!.matcher(workingText).replaceFirst("")
    }

    private inner class EllipsizeStartStrategy : EllipsizeStrategy() {

        override fun createEllipsizedText(fullText: CharSequence?): CharSequence {
            val layout = createWorkingLayout(fullText)
            val cutOffIndex = layout.getLineEnd(mMaxLines - 1)
            val textLength = fullText!!.length
            var cutOffLength = textLength - cutOffIndex
            if (cutOffLength < ELLIPSIS.length) cutOffLength = ELLIPSIS.length
            var workingText = TextUtils.substring(fullText, cutOffLength, textLength).trim { it <= ' ' }

            while (!isInLayout(ELLIPSIS + workingText)) {
                val firstSpace = workingText.indexOf(' ')
                if (firstSpace == -1) break
                workingText = workingText.substring(firstSpace, workingText.length).trim { it <= ' ' }
            }

            workingText = ELLIPSIS + workingText
            val dest = SpannableStringBuilder(workingText)

            if (fullText is Spanned) {
                TextUtils.copySpansFrom(
                    fullText as Spanned?, textLength - workingText.length,
                    textLength, null, dest, 0
                )
            }
            return dest
        }
    }

    private inner class EllipsizeMiddleStrategy : EllipsizeStrategy() {

        override fun createEllipsizedText(fullText: CharSequence?): CharSequence {
            val layout = createWorkingLayout(fullText)
            val cutOffIndex = layout.getLineEnd(mMaxLines - 1)
            val textLength = fullText!!.length
            var cutOffLength = textLength - cutOffIndex
            if (cutOffLength < ELLIPSIS.length) cutOffLength = ELLIPSIS.length
            cutOffLength += cutOffIndex % 2    // Make it even.
            var firstPart = TextUtils.substring(
                fullText, 0, textLength / 2 - cutOffLength / 2
            ).trim { it <= ' ' }
            var secondPart = TextUtils.substring(
                fullText, textLength / 2 + cutOffLength / 2, textLength
            ).trim { it <= ' ' }

            while (!isInLayout(firstPart + ELLIPSIS + secondPart)) {
                val lastSpaceFirstPart = firstPart.lastIndexOf(' ')
                val firstSpaceSecondPart = secondPart.indexOf(' ')
                if (lastSpaceFirstPart == -1 || firstSpaceSecondPart == -1) break
                firstPart = firstPart.substring(0, lastSpaceFirstPart).trim { it <= ' ' }
                secondPart = secondPart.substring(firstSpaceSecondPart, secondPart.length).trim { it <= ' ' }
            }

            val firstDest = SpannableStringBuilder(firstPart)
            val secondDest = SpannableStringBuilder(secondPart)

            if (fullText is Spanned) {
                TextUtils.copySpansFrom(fullText as Spanned?, 0, firstPart.length, null, firstDest, 0)
                TextUtils.copySpansFrom(
                    fullText as Spanned?, textLength - secondPart.length,
                    textLength, null, secondDest, 0
                )
            }
            return TextUtils.concat(firstDest, ELLIPSIS, secondDest)
        }
    }
}
