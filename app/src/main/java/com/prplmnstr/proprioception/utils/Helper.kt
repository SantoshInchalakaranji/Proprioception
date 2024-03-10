package com.prplmnstr.proprioception.utils

import android.content.Context
import android.content.res.Configuration
import java.text.DateFormatSymbols
import java.text.DecimalFormat

class Helper {
    companion object {


        val decimalFormat = DecimalFormat("00.0")
        fun getDateInStringFormat(day: Int, month: Int, year: Int): String {
            val monthString = DateFormatSymbols().months[month - 1].substring(0, 3)
            return String.format("%d %s %d", day, monthString, year)
        }
         fun isLightTheme(context: Context): Boolean {
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_NO
        }

    }
}