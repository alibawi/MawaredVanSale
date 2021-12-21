package com.mawared.mawaredvansale.controller.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseViewModel : ViewModel() {

    fun returnDateString(isoString: String?) : String{
        try {
            if(isoString == null) return ""
            // 2017-09-11T01:16:13.858Z converted to below
            // Monday 4:35 PM format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val enLang = Locale("en")
            val isLeftToRight = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
            val pattern: String = if(isLeftToRight) "dd-MM-yyyy" else "yyyy-MM-dd"
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormatter.parse(isoString)!!
            }catch (e: ParseException){
                Log.d("PARSE", "Cannot parse date")
            }

            val outDateString = SimpleDateFormat(pattern, enLang)
            return  outDateString.format(convertedDate)
        }catch (e: Exception){
            Log.i("Exc", "Error in BaseViewModel returnDateString($isoString)")
            return ""
        }
    }

    fun returnUKDateString(isoString: String?) : String{
        try {
            if(isoString == null) return ""
            // 2017-09-11T01:16:13.858Z converted to below
            // Monday 4:35 PM format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val enLang = Locale("en")
            val pattern: String = "yyyy-MM-dd"
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormatter.parse(isoString)!!
            }catch (e: ParseException){
                Log.d("PARSE", "Cannot parse date")
            }

            val outDateString = SimpleDateFormat(pattern, enLang)
            return  outDateString.format(convertedDate)
        }catch (e: Exception){
            Log.i("Exc", "Error in BaseViewModel returnDateString($isoString)")
            return ""
        }
    }

    fun returnDateTimeString(isoString: String?) : String{
        try {
            if(isoString ==null) return ""
            // 2017-09-11T01:16:13.858Z converted to below
            // Monday 4:35 PM format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val enLang = Locale("en")
            val isLeftToRight = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
            val pattern: String = if(isLeftToRight) "dd-MM-yyyy HH:mm:ss" else "yyyy-MM-dd HH:mm:ss"
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormatter.parse(isoString)!!
            }catch (e: ParseException){
                Log.d("PARSE", "Cannot parse date")
            }

            val outDateString = SimpleDateFormat(pattern, enLang)
            return  outDateString.format(convertedDate)
        }catch (e: Exception){
            Log.i("Exc", "Error in BaseViewModel returnDateString($isoString)")
            return ""
        }
    }

    fun getColor(value: String?): Int{
        if(value == null) return Color.parseColor("#FFFFFF")
        return Color.parseColor(value)
    }

    fun numberFormat(value: Double?): String{
        val enLang = Locale("en")
        if(value == null) return "0.00"
        val formatedNumber = NumberFormat.getInstance(enLang).format(value)

        return formatedNumber
    }

    fun hideKeyboard(activity: Activity){

        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText){
            val view: View = activity.currentFocus!!
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}