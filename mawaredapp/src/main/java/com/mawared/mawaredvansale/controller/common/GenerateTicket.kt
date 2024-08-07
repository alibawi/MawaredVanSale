package com.mawared.mawaredvansale.controller.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.util.Log
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.itextpdf.text.Element
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.data.db.entities.fms.Payable
import com.mawared.mawaredvansale.data.db.entities.fms.Receivable
import com.mawared.mawaredvansale.data.db.entities.reports.stock.StockStatement
import com.mawared.mawaredvansale.data.db.entities.sales.*
import org.threeten.bp.LocalTime
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class GenerateTicket(val ctx: Context, val lang: String){
    val res = ctx.resources

    fun create1(baseEo: Sale, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                    lines.add(Ticket(logo, LineType.Image, AlignText.CENTER, bmp = null))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        //var xPos =0
        var yPos = 0
        // number and date
        lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 100, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 100, yPos = yPos, exPos = 570, eyPos = yPos))
        lines.add(Ticket("${res.getString(R.string.lbl_no)}", LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket("${baseEo.sl_refNo.toString()}", LineType.Text, xPos = 110, yPos = yPos))
        //lines.add(Ticket("${res.getString(R.string.lbl_no)}: ${baseEo.sl_refNo.toString().padEnd(24, ' ')}", LineType.Text))
        //val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.sl_doc_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)} ${LocalTime.now()}"
        //lines.add(Ticket(s, LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 100, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 100, yPos = yPos, exPos = 200, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 200, yPos = yPos, exPos = 300, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 300, yPos = yPos, exPos = 570, eyPos = yPos))
        lines.add(Ticket("${res.getString(R.string.lbl_doc_date)}", LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket("${returnDateString(baseEo.sl_doc_date!!)}", LineType.Text, xPos = 110, yPos = yPos))

        lines.add(Ticket("${res.getString(R.string.lbl_time)}", LineType.Text, xPos = 200, yPos = yPos))
        lines.add(Ticket("${LocalTime.now()}", LineType.Text, xPos = 300, yPos = yPos))

        yPos = yPos + 48
        lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 100, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 100, yPos = yPos, exPos = 570, eyPos = yPos))

        lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}", LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket("${baseEo.sl_customer_name}", LineType.Text, xPos = 110, yPos = yPos))

        //lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.sl_customer_name!!.padEnd(20, ' ')}", LineType.Text))

        //lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        yPos = yPos + 48
        lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 400, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 400, yPos = yPos, exPos = 490, eyPos = yPos))
        lines.add(Ticket(null, LineType.Rectangle, xPos = 490, yPos = yPos, exPos = 570, eyPos = yPos))
        lines.add(Ticket(res.getString(R.string.lbl_prod_name), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(res.getString(R.string.lbl_qty).padEnd(10, ' '), LineType.Text, xPos = 410, yPos = yPos))
        lines.add(Ticket(res.getString(R.string.lbl_line_total), LineType.Text, xPos = 500, yPos = yPos))

        //lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        for (d: Sale_Items in baseEo.items){

            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.sld_prod_name_ar != null) prod_name = d.sld_prod_name_ar!!
            }else{
                prod_name = if(d.sld_prod_name != null) d.sld_prod_name!! else d.sld_prod_name_ar!!
            }
            val prod = prod_name.trim()
            val qty = d.sld_unit_qty!!.toString()
            val net = d.sld_net_total!!.toString()


            if(prod.length > 30){
                yPos = yPos + 48
                val cpos = prod.substring(0,30).lastIndexOf(' ')
                val pr_name1 = prod.substring(0,cpos)
                //val num = pr_name1.length + qty.length + net.length
                //val line = pr_name1.padEnd(34, ' ') + qty + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(pr_name1, LineType.Text, xPos = 0, yPos = yPos))
                lines.add(Ticket(qty, LineType.Text, xPos = 410, yPos = yPos))
                lines.add(Ticket(net, LineType.Text, xPos = 500, yPos = yPos))

                yPos = yPos + 48
                //lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                val pr_name2 = prod.substring(cpos)
                lines.add(Ticket(pr_name2, LineType.Text, xPos = 0, yPos = yPos))
                //val line1 = pr_name2.padEnd(47)

                //lines.add(Ticket(line1, LineType.Text, AlignText.LEFT))

                lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 400, eyPos = yPos))
                lines.add(Ticket(null, LineType.Rectangle, xPos = 400, yPos = yPos, exPos = 490, eyPos = yPos))
                lines.add(Ticket(null, LineType.Rectangle, xPos = 490, yPos = yPos, exPos = 570, eyPos = yPos))

            }else{
                yPos = yPos + 48
                lines.add(Ticket(null, LineType.Rectangle, xPos = 0, yPos = yPos, exPos = 400, eyPos = yPos))
                lines.add(Ticket(null, LineType.Rectangle, xPos = 400, yPos = yPos, exPos = 490, eyPos = yPos))
                lines.add(Ticket(null, LineType.Rectangle, xPos = 490, yPos = yPos, exPos = 570, eyPos = yPos))

                lines.add(Ticket(prod, LineType.Text, xPos = 0, yPos = yPos))
                lines.add(Ticket(qty, LineType.Text, xPos = 410, yPos = yPos))
                lines.add(Ticket(net, LineType.Text, xPos = 500, yPos = yPos))
            }
        }

        //lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        val tqty = baseEo.items.sumByDouble { x->x.sld_unit_qty ?: 0.0 }
        val tgqty = baseEo.items.sumByDouble { x->x.sld_gift_qty ?: 0.0 }

        //lines.add(Ticket("${res.getString(R.string.lbl_total_qty)}".padStart(35, ' ') + tqty.toString().padStart(10,' ') , LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(res.getString(R.string.lbl_total_qty), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(tqty.toString(), LineType.Text, xPos = 200, yPos = yPos))

        //lines.add(Ticket("${res.getString(R.string.lbl_total_gift)}".padStart(35, ' ') + tgqty.toString().padStart(10,' ') , LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(res.getString(R.string.lbl_total_gift), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(tgqty.toString(), LineType.Text, xPos = 200, yPos = yPos))

        //lines.add(Ticket("${res.getString(R.string.lbl_total)} ".padStart(35, ' ') + if(baseEo.sl_total_amount == null) "0.00".padStart(10,' ') else baseEo.sl_total_amount.toString().padStart(10,' '), LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(res.getString(R.string.lbl_total), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(if(baseEo.sl_total_amount == null) "0.00" else baseEo.sl_total_amount.toString(), LineType.Text, xPos = 200, yPos = yPos))

        //lines.add(Ticket("${res.getString(R.string.lbl_total_discount)} ".padStart(35, ' ') + if(baseEo.sl_total_discount == null) "0.00".padStart(10,' ') else baseEo.sl_total_discount.toString().padStart(10,' '), LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(res.getString(R.string.lbl_total_discount), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(if(baseEo.sl_total_discount == null) "0.00" else baseEo.sl_total_discount.toString(), LineType.Text, xPos = 200, yPos = yPos))

        //lines.add(Ticket("${res.getString(R.string.lbl_net_amount)} ".padStart(36, ' ') + if(baseEo.sl_net_amount == null) "0.00".padStart(10,' ') else baseEo.sl_net_amount.toString().padStart(10,' '), LineType.Text))
        yPos = yPos + 48
        lines.add(Ticket(res.getString(R.string.lbl_net_amount), LineType.Text, xPos = 0, yPos = yPos))
        lines.add(Ticket(if(baseEo.sl_net_amount == null) "0.00" else baseEo.sl_net_amount.toString(), LineType.Text, xPos = 200, yPos = yPos))

        //lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!baseEo.sl_refNo.isNullOrEmpty()){
            lines.add(Ticket("\n", LineType.Text))
            lines.add(Ticket(baseEo.sl_refNo, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun create(baseEo: Sale, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER, bmp = null))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)}: ${baseEo.sl_refNo.toString().padEnd(24, ' ')}", LineType.Text))
        val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.sl_doc_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)} ${LocalTime.now().hour}:${LocalTime.now().minute}:${LocalTime.now().second}"
        lines.add(Ticket(s, LineType.Text))
        var phone: String = ""
        if(!baseEo.sl_customer_phone.isNullOrEmpty()){
            phone = baseEo.sl_customer_phone!!.trim()
        }
        if(baseEo.sl_customer_name!!.length >= 18){
            val pos = baseEo.sl_customer_name!!.substring(0,18).lastIndexOf(' ')
            val cu_name1 = baseEo.sl_customer_name!!.substring(0,pos)
            val cu_name2 = baseEo.sl_customer_name!!.substring(pos)

            lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${cu_name1.padEnd(18, ' ')} ${res.getString(R.string.rpt_phone)}:${phone}", LineType.Text))
            lines.add(Ticket("${cu_name2}", LineType.Text))
        }else{
            lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.sl_customer_name!!.padEnd(18, ' ')} ${res.getString(R.string.rpt_phone)}:${phone}", LineType.Text))
        }

        lines.add(Ticket("${res.getString(R.string.lbl_salesman_name)}: ${baseEo.sl_salesman_name!!.padEnd(18, ' ')}${res.getString(R.string.rpt_phone)}:${baseEo.sl_salesman_phone}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        lines.add(Ticket(res.getString(R.string.lbl_prod_name).padEnd(20, ' ') + res.getString(R.string.lbl_qty).padEnd(10, ' ')+ res.getString(R.string.lbl_unit_price).padEnd(10, ' ') + res.getString(R.string.lbl_line_total), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        for (d: Sale_Items in baseEo.items){
            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.sld_prod_name_ar != null) prod_name = d.sld_prod_name_ar!!
            }else{
                prod_name = if(d.sld_prod_name != null) d.sld_prod_name!! else d.sld_prod_name_ar!!
            }
            val prod = prod_name.trim()//.padEnd(34, ' ')
            val qty = d.sld_unit_qty!!.toString().padEnd(6, ' ').padStart(4,' ')
            val up = d.sld_unit_price!!.toString().padEnd(8, ' ').padStart(4,' ')
            val net = d.sld_net_total!!.toString()//.padEnd(7, ' ')


            if(prod.length > 22){
                val cpos = prod.substring(0,22).lastIndexOf(' ')
                val pr_name1 = prod.substring(0,cpos)
                val num = pr_name1.length + qty.length + up.length + net.length
                val line = pr_name1.padEnd(22, ' ') + qty + up + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                val pr_name2 = prod.substring(cpos)
                val line1 = pr_name2.padEnd(47)
                lines.add(Ticket(line1, LineType.Text, AlignText.LEFT))
            }else{
                val num = prod.length + qty.length + net.length
                val line = prod.padEnd(22, ' ')  + qty + up + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
            }
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        val tqty = baseEo.items.sumByDouble { x->x.sld_unit_qty ?: 0.0 }
        var tgqty = baseEo.items.sumByDouble { x->x.sld_gift_qty ?: 0.0 }
        val tgqty1= baseEo.items.sumByDouble { if(it.sld_isGift == false) 0.0 else it.sld_unit_qty ?: 0.0 }
        tgqty += tgqty1
        lines.add(Ticket("${res.getString(R.string.lbl_total_qty)}".padStart(34, ' ') + tqty.toString().padStart(10,' ') , LineType.Text))
        lines.add(Ticket("${res.getString(R.string.lbl_total_gift)}".padStart(34, ' ') + tgqty.toString().padStart(10,' ') , LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_total)} ".padStart(36, ' ') + if(baseEo.sl_total_amount == null) "0.00".padStart(10,' ') else baseEo.sl_total_amount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_total_discount)} ".padStart(35, ' ') + if(baseEo.sl_total_discount == null) "0.00".padStart(10,' ') else baseEo.sl_total_discount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_net_amount)} ".padStart(36, ' ') + if(baseEo.sl_net_amount == null) "0.00".padStart(10,' ') else baseEo.sl_net_amount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!baseEo.sl_refNo.isNullOrEmpty()){
            lines.add(Ticket("\n", LineType.Text))
            lines.add(Ticket(baseEo.sl_refNo, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun create(baseEo: Sale_Order, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER, bmp = null))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)}: ${baseEo.so_refNo.toString().padEnd(24, ' ')}", LineType.Text))
        val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.so_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)} ${LocalTime.now().hour}:${LocalTime.now().minute}:${LocalTime.now().second}"
        lines.add(Ticket(s, LineType.Text))


        lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.so_customer_name!!.padEnd(18, ' ')} ", LineType.Text))


        lines.add(Ticket("${res.getString(R.string.lbl_salesman_name)}: ${baseEo.so_salesman_name!!.padEnd(18, ' ')}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        lines.add(Ticket(res.getString(R.string.lbl_prod_name).padEnd(20, ' ') + res.getString(R.string.lbl_qty).padEnd(10, ' ')+ res.getString(R.string.lbl_unit_price).padEnd(10, ' ') + res.getString(R.string.lbl_line_total), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        for (d: Sale_Order_Items in baseEo.items){
            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.sod_prod_name != null) prod_name = d.sod_prod_name!!
            }else{
                prod_name = if(d.sod_prod_name != null) d.sod_prod_name!! else d.sod_prod_name!!
            }
            val prod = prod_name.trim()//.padEnd(34, ' ')
            val qty = d.sod_unit_qty!!.toString().padEnd(6, ' ').padStart(4,' ')
            val up = d.sod_unit_price!!.toString().padEnd(8, ' ').padStart(4,' ')
            val net = d.sod_net_total!!.toString()//.padEnd(7, ' ')


            if(prod.length > 22){
                val cpos = prod.substring(0,22).lastIndexOf(' ')
                val pr_name1 = prod.substring(0,cpos)
                val num = pr_name1.length + qty.length + up.length + net.length
                val line = pr_name1.padEnd(22, ' ') + qty + up + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                val pr_name2 = prod.substring(cpos)
                val line1 = pr_name2.padEnd(47)
                lines.add(Ticket(line1, LineType.Text, AlignText.LEFT))
            }else{
                val num = prod.length + qty.length + net.length
                val line = prod.padEnd(22, ' ')  + qty + up + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
            }
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        val tqty = baseEo.items.sumByDouble { x->x.sod_unit_qty ?: 0.0 }
        var tgqty = baseEo.items.sumByDouble { x->x.sod_gift_qty ?: 0.0 }
        val tgqty1= baseEo.items.sumByDouble { if(it.sod_isGift == false) 0.0 else it.sod_unit_qty ?: 0.0 }
        tgqty += tgqty1
        lines.add(Ticket("${res.getString(R.string.lbl_total_qty)}".padStart(34, ' ') + tqty.toString().padStart(10,' ') , LineType.Text))
        lines.add(Ticket("${res.getString(R.string.lbl_total_gift)}".padStart(34, ' ') + tgqty.toString().padStart(10,' ') , LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_total)} ".padStart(36, ' ') + if(baseEo.so_total_amount == null) "0.00".padStart(10,' ') else baseEo.so_total_amount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_total_discount)} ".padStart(35, ' ') + if(baseEo.so_total_discount == null) "0.00".padStart(10,' ') else baseEo.so_total_discount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_net_amount)} ".padStart(36, ' ') + if(baseEo.so_net_amount == null) "0.00".padStart(10,' ') else baseEo.so_net_amount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!baseEo.so_refNo.isNullOrEmpty()){
            lines.add(Ticket("\n", LineType.Text))
            lines.add(Ticket(baseEo.so_refNo, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }
//    var type: LineType = LineType.Text,
//    var cols : ArrayList<String>?,
//    var width: ArrayList<Int>?,
//    var align: ArrayList<Int>?,
//    var isBold: Boolean,
//    var text: String?,
//    var textAlign: Int,
//    var fontSize: Int = 12,
//    var bmp: Bitmap?

    fun createSunmiTicket(baseEo: Sale, logo: Bitmap?, systemInfo: String?, message: String?, cmsMessage: String?): List<SunmiTicket>{
        val lines: ArrayList<SunmiTicket> = arrayListOf()
        if(logo != null) {
            try {
                lines.add(SunmiTicket(LineType.Image, null, null, null, false, "", 1, 12, logo))
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        //SunmiTicket : LineType, Cols, Width, Align, isBold, text, textAling, fontSize, bmp

        var cols = arrayOf<String>(res.getString(R.string.lbl_no), baseEo.sl_refNo.toString())
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,4), intArrayOf(0,0), true, null, null, 21, null))

        cols = arrayOf<String>(res.getString(R.string.lbl_doc_date), returnDateString(baseEo.sl_doc_date!!), res.getString(R.string.lbl_time), "${LocalTime.now().hour}:${LocalTime.now().minute}")
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2, 1, 1), intArrayOf(0,0,0,0), true, null, null, 21, null))

        var phone: String = ""
        if(!baseEo.sl_customer_phone.isNullOrEmpty()){
            phone = baseEo.sl_customer_phone!!.trim()
        }
//        if(baseEo.sl_customer_name!!.length >= 18){
//            val pos = baseEo.sl_customer_name!!.substring(0,18).lastIndexOf(' ')
//            val cu_name1 = baseEo.sl_customer_name!!.substring(0,pos)
//            val cu_name2 = baseEo.sl_customer_name!!.substring(pos)
//            cols = arrayOf<String>(res.getString(R.string.lbl_customer_name), cu_name1)
//            lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 11, null))
//
//            cols = arrayOf<String>("", cu_name2)
//            lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0, 0), true, null, null, 11, null))
//        }else{
//
//        }
        cols = arrayOf<String>(res.getString(R.string.lbl_customer_name), baseEo.sl_customer_name!!)
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))
        cols = arrayOf<String>(res.getString(R.string.rpt_phone), phone)
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        // Salesman
        cols = arrayOf<String>(res.getString(R.string.lbl_salesman_name), baseEo.sl_salesman_name!!)
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        cols = arrayOf<String>(res.getString(R.string.rpt_phone), baseEo.sl_salesman_phone.toString())
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))
        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        // Print Table Header
        //cols = arrayOf<String>(res.getString(R.string.lbl_prod_name), res.getString(R.string.lbl_qty), res.getString(R.string.lbl_unit_price), res.getString(R.string.lbl_line_total))
        cols = arrayOf<String>(res.getString(R.string.lbl_prod_name), res.getString(R.string.lbl_qty), res.getString(R.string.lbl_line_total))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(4,1,1), intArrayOf(0,0,0), true, null, null, 21, null))
        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        for (d: Sale_Items in baseEo.items){
            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.sld_prod_name_ar != null) prod_name = d.sld_prod_name_ar!!
            }else{
                prod_name = if(d.sld_prod_name != null) d.sld_prod_name!! else d.sld_prod_name_ar!!
            }
            val prod = prod_name.trim()
//            if(prod.length > 22){
//                val cpos = prod.substring(0,22).lastIndexOf(' ')
//                val pr_name1 = prod.substring(0,cpos)
//                //cols = arrayOf<String>(pr_name1, d.sld_pack_qty.toString(), d.sld_unit_price.toString(), d.sld_net_total.toString())
//                cols = arrayOf<String>(pr_name1, d.sld_pack_qty.toString(), d.sld_net_total.toString())
//                lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(3,1,1), intArrayOf(0,0,0), true, null, null, 11, null))
//
//                val pr_name2 = prod.substring(cpos)
//                cols = arrayOf<String>(pr_name2)
//                lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1), intArrayOf(0), true, null, null, 11, null))
//            }else{
                //cols = arrayOf<String>(prod, d.sld_pack_qty.toString(), d.sld_unit_price.toString(), d.sld_net_total.toString())
                cols = arrayOf<String>(prod, d.sld_pack_qty.toString(), numberFormat(d.sld_net_total))
                lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(4,1,1), intArrayOf(0,0,0), true, null, null, 21, null))
            //}
        }
        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))
        val tqty = baseEo.items.sumByDouble { x->x.sld_unit_qty ?: 0.0 }
        var tgqty = baseEo.items.sumByDouble { x->x.sld_gift_qty ?: 0.0 }
        val tgqty1= baseEo.items.sumByDouble { if(it.sld_isGift == false) 0.0 else it.sld_unit_qty ?: 0.0 }
        tgqty += tgqty1
        // Print Total Quantity
        cols = arrayOf<String>(res.getString(R.string.lbl_total_qty), numberFormat(tqty))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // print Total Gift
        cols = arrayOf<String>(res.getString(R.string.lbl_total_gift), numberFormat(tgqty))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Line Amount
        cols = arrayOf<String>(res.getString(R.string.lbl_total), if(baseEo.sl_total_amount == null) "0.00" else numberFormat(baseEo.sl_total_amount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Discount
        cols = arrayOf<String>(res.getString(R.string.lbl_total_discount), if(baseEo.sl_total_discount == null) "0.00" else numberFormat(baseEo.sl_total_discount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Net Amount
        cols = arrayOf<String>(res.getString(R.string.lbl_net_amount), if(baseEo.sl_net_amount == null) "0.00" else numberFormat(baseEo.sl_net_amount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))

        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        // Print Footer
        if(!baseEo.sl_refNo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 14, null))
            lines.add(SunmiTicket(LineType.Barcode, null, null, null, true, baseEo.sl_refNo, 1, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))

        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "$cmsMessage \n", 0, 21, null))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "$systemInfo", 0, 16, null))
        }
        return lines
    }

    fun createSunmiTicket(baseEo: Sale_Order, logo: Bitmap?, systemInfo: String?, message: String?, cmsMessage: String?): List<SunmiTicket>{
        val lines: ArrayList<SunmiTicket> = arrayListOf()
        if(logo != null) {
            try {
                lines.add(SunmiTicket(LineType.Image, null, null, null, false, "", 1, 12, logo))
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        //SunmiTicket : LineType, Cols, Width, Align, isBold, text, textAling, fontSize, bmp

        var cols = arrayOf<String>(res.getString(R.string.lbl_no), baseEo.so_refNo.toString())
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,4), intArrayOf(0,0), true, null, null, 21, null))

        cols = arrayOf<String>(res.getString(R.string.lbl_doc_date), returnDateString(baseEo.so_date!!), res.getString(R.string.lbl_time), "${LocalTime.now().hour}:${LocalTime.now().minute}")
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2, 1, 1), intArrayOf(0,0,0,0), true, null, null, 21, null))


        cols = arrayOf<String>(res.getString(R.string.lbl_customer_name), baseEo.so_customer_name!!)
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        // Salesman
        cols = arrayOf<String>(res.getString(R.string.lbl_salesman_name), baseEo.so_salesman_name ?: "")
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        // Print Table Header
        //cols = arrayOf<String>(res.getString(R.string.lbl_prod_name), res.getString(R.string.lbl_qty), res.getString(R.string.lbl_unit_price), res.getString(R.string.lbl_line_total))
        cols = arrayOf<String>(res.getString(R.string.lbl_prod_name), res.getString(R.string.lbl_qty), res.getString(R.string.lbl_line_total))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(4,1,1), intArrayOf(0,0,0), true, null, null, 21, null))
        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        for (d: Sale_Order_Items in baseEo.items){
            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.sod_prod_name != null) prod_name = d.sod_prod_name!!
            }else{
                prod_name = if(d.sod_prod_name != null) d.sod_prod_name!! else d.sod_prod_name!!
            }
            val prod = prod_name.trim()

            cols = arrayOf<String>(prod, d.sod_pack_qty.toString(), numberFormat(d.sod_net_total))
            lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(4,1,1), intArrayOf(0,0,0), true, null, null, 21, null))
            //}
        }
        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))
        val tqty = baseEo.items.sumByDouble { x->x.sod_unit_qty ?: 0.0 }
        var tgqty = baseEo.items.sumByDouble { x->x.sod_gift_qty ?: 0.0 }
        val tgqty1= baseEo.items.sumByDouble { if(it.sod_isGift == false) 0.0 else it.sod_unit_qty ?: 0.0 }
        tgqty += tgqty1
        // Print Total Quantity
        cols = arrayOf<String>(res.getString(R.string.lbl_total_qty), numberFormat(tqty))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // print Total Gift
        cols = arrayOf<String>(res.getString(R.string.lbl_total_gift), numberFormat(tgqty))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Line Amount
        cols = arrayOf<String>(res.getString(R.string.lbl_total), if(baseEo.so_total_amount == null) "0.00" else numberFormat(baseEo.so_total_amount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Discount
        cols = arrayOf<String>(res.getString(R.string.lbl_total_discount), if(baseEo.so_total_discount == null) "0.00" else numberFormat(baseEo.so_total_discount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))
        // Print Total Net Amount
        cols = arrayOf<String>(res.getString(R.string.lbl_net_amount), if(baseEo.so_net_amount == null) "0.00" else numberFormat(baseEo.so_net_amount))
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(2,1), intArrayOf(0, 2), true, null, null, 20, null))

        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        // Print Footer
        if(!baseEo.so_refNo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 14, null))
            lines.add(SunmiTicket(LineType.Barcode, null, null, null, true, baseEo.so_refNo, 1, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))

        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "$cmsMessage \n", 0, 21, null))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "$systemInfo", 0, 16, null))
        }
        return lines
    }

    fun createSunmiTicket(baseEo: Sale_Order, logo: Bitmap?, systemInfo: String?): List<SunmiTicket>{
        val lines: ArrayList<SunmiTicket> = arrayListOf()
        if(logo != null) {
            try {
                lines.add(SunmiTicket(LineType.Image, null, null, null, false, "", 1, 12, logo))
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }
        //SunmiTicket : LineType, Cols, Width, Align, isBold, text, textAling, fontSize, bmp

        var cols = arrayOf<String>(res.getString(R.string.lbl_no), baseEo.so_refNo.toString())
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,4), intArrayOf(0,0), true, null, null, 21, null))

        cols = arrayOf<String>(res.getString(R.string.lbl_doc_date), returnDateString(baseEo.so_date!!), res.getString(R.string.lbl_time), "${LocalTime.now().hour}:${LocalTime.now().minute}")
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2, 1, 1), intArrayOf(0,0,0,0), true, null, null, 21, null))


        cols = arrayOf<String>(res.getString(R.string.lbl_customer_name), baseEo.so_customer_name!!)
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        // Salesman
        cols = arrayOf<String>(res.getString(R.string.lbl_salesman_name), baseEo.so_salesman_name ?: "")
        lines.add(SunmiTicket(LineType.Column, cols, intArrayOf(1,2), intArrayOf(0,0), true, null, null, 21, null))

        // Print Line ___________________
        lines.add(SunmiTicket(LineType.Line, null, null, null, false, "".padEnd(48, '_'), 0, 24, null))

        // Print Footer
        if(!baseEo.so_refNo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 14, null))
            lines.add(SunmiTicket(LineType.Barcode, null, null, null, true, baseEo.so_refNo, 1, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 22, null))

        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "\n", 0, 20, null))
            lines.add(SunmiTicket(LineType.Text, null, null, null, true, "$systemInfo", 0, 16, null))
        }
        return lines
    }

    fun create(baseEo: Sale_Return, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER, bmp = null))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)}: ${baseEo.sr_refno.toString().padEnd(24, ' ')}", LineType.Text))
        val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.sr_doc_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)} ${LocalTime.now()}"
        lines.add(Ticket(s, LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.sr_customer_name!!.padEnd(20, ' ')}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        lines.add(Ticket(res.getString(R.string.lbl_prod_name).padEnd(30, ' ') + res.getString(R.string.lbl_qty).padEnd(10, ' ') + res.getString(R.string.lbl_line_total), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        for (d: Sale_Return_Items in baseEo.items){
            var prod_name = ""
            if(lang == "ar_iq" && d.srd_prod_name_ar != null) {
                if(d.srd_prod_name_ar != null) prod_name = d.srd_prod_name_ar!!
            }else{
                prod_name = if(d.srd_prod_name != null) d.srd_prod_name!! else d.srd_prod_name_ar!!
            }
            val prod = prod_name.trim()//.padEnd(34, ' ')
            val qty = numberFormat(d.srd_unit_qty!!).padEnd(6, ' ').padStart(4,' ')
            val net = numberFormat(d.srd_net_total!!)//.padEnd(7, ' ')


            if(prod.length > 30){
                val cpos = prod.substring(0,30).lastIndexOf(' ')
                val pr_name1 = prod.substring(0,cpos)
                val num = pr_name1.length + qty.length + net.length
                val line = pr_name1.padEnd(34, ' ') + qty + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                val pr_name2 = prod.substring(cpos)
                val line1 = pr_name2.padEnd(47)
                lines.add(Ticket(line1, LineType.Text, AlignText.LEFT))
            }else{
                val num = prod.length + qty.length + net.length
                val line = prod.padEnd(34, ' ')  + qty + net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
            }
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        val tqty = baseEo.items.sumByDouble { x->x.srd_unit_qty ?: 0.0 }
        //val tgqty = baseEo.items.sumByDouble { x->x.srd_gift_qty ?: 0.0 }

        lines.add(Ticket("${res.getString(R.string.lbl_total_qty)}".padStart(35, ' ') + numberFormat(tqty).padStart(10,' ') , LineType.Text))
        //lines.add(Ticket("${res.getString(R.string.lbl_total_gift)}".padStart(35, ' ') + tgqty.toString().padStart(10,' ') , LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_total)} ".padStart(35, ' ') + if(baseEo.sr_total_amount == null) "0.00".padStart(10,' ') else numberFormat(baseEo.sr_total_amount).padStart(10,' '), LineType.Text))

        //lines.add(Ticket("${res.getString(R.string.lbl_total_discount)} ".padStart(35, ' ') + if(baseEo.sr_total_discount == null) "0.00".padStart(10,' ') else baseEo.sl_total_discount.toString().padStart(10,' '), LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_net_amount)} ".padStart(36, ' ') + if(baseEo.sr_net_amount == null) "0.00".padStart(10,' ') else numberFormat(baseEo.sr_net_amount).padStart(10,' '), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!baseEo.sr_refno.isNullOrEmpty()){
            lines.add(Ticket("\n", LineType.Text))
            lines.add(Ticket(baseEo.sr_refno, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun create(baseEo: Transfer, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            lines.add(Ticket(logo, LineType.Image, AlignText.CENTER))
        }
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)} ${baseEo.tr_ref_no.toString().padEnd(24, ' ')} ${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.tr_doc_date!!)}", LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_time).padStart(35,' ')} ${LocalTime.now()}", LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_to_warehouse)}: ${baseEo.tr_wr_name!!.padEnd(20, ' ')}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        lines.add(Ticket(res.getString(R.string.lbl_prod_name).padEnd(34, ' ') + res.getString(R.string.lbl_qty).padEnd(6, ' ') + res.getString(R.string.lbl_line_total), LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        for (d: Transfer_Items in baseEo.items){
            val prod = if(lang == "ar_iq") d.trd_prod_name_ar!!.trim().padEnd(34, ' ') else d.trd_prod_name!!.trim().padEnd(34, ' ')
            val qty = d.trd_unit_qty!!.toString().padEnd(6, ' ')
            val num = prod.length + qty.length
            val line = prod + qty + if(num < 47) "".padEnd(47-num) else ""
            lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!baseEo.tr_ref_no.isNullOrEmpty()){
            lines.add(Ticket("\n", LineType.Text))
            lines.add(Ticket(baseEo.tr_ref_no, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun create(baseEo: Receivable, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{

        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // Title
        lines.add(Ticket(res.getString(R.string.lbl_receipt_title).padStart(30,' '), LineType.Text))
        lines.add(Ticket("\n", LineType.Text))
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)} ${baseEo.rcv_ref_no.toString().padEnd(24, ' ')}", LineType.Text))
        val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.rcv_doc_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)}: ${LocalTime.now()}"
        lines.add(Ticket(s, LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.rcv_cu_name!!}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        lines.add(Ticket(res.getString(R.string.lbl_amount).padStart(10, ' ').padEnd(30, ' ') + res.getString(R.string.lbl_change_amount), LineType.Text))
        lines.add(Ticket("${baseEo.rcv_amount} ${baseEo.rcv_cr_symbol}", LineType.Text, xPos = 400, yPos = 0))
        lines.add(Ticket("${baseEo.rcv_change} ${baseEo.rcv_cr_symbol}", LineType.Text, xPos = 50, yPos = 0))
        lines.add(Ticket(" ", LineType.Text))
        lines.add(Ticket("${baseEo.rcv_lc_amount} ${baseEo.rcv_lc_cr_symbol}", LineType.Text, xPos = 400, yPos = 0))
        lines.add(Ticket("${baseEo.rcv_lc_change} ${baseEo.rcv_lc_cr_symbol}", LineType.Text, xPos = 50, yPos = 0))
        lines.add(Ticket(" ", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        if(baseEo.rcv_comment != null){
            lines.add(Ticket("${res.getString(R.string.lbl_comment)}", LineType.Text))
            lines.add(Ticket("${baseEo.rcv_comment}", LineType.Text))
        }
        lines.add(Ticket("\n", LineType.Text))
        lines.add(Ticket("\n", LineType.Text))

        lines.add(Ticket(res.getString(R.string.lbl_recipientby).padStart(10, ' ').padEnd(30, ' ') + res.getString(R.string.lbl_sign), LineType.Text))

        lines.add(Ticket("\n", LineType.Text))

        if(!baseEo.rcv_ref_no.isNullOrEmpty()){
            lines.add(Ticket(baseEo.rcv_ref_no, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun create(baseEo: Payable, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>{

        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // Title
        lines.add(Ticket(res.getString(R.string.lbl_payment_title).padStart(30,' '), LineType.Text))
        lines.add(Ticket("\n", LineType.Text))
        // number and date
        lines.add(Ticket("${res.getString(R.string.lbl_no)} ${baseEo.py_ref_no.toString().padEnd(24, ' ')}", LineType.Text))
        val s = "${res.getString(R.string.lbl_doc_date)}: ${returnDateString(baseEo.py_doc_date!!).padEnd(18,' ')} ${res.getString(R.string.lbl_time)}: ${LocalTime.now()}"
        lines.add(Ticket(s, LineType.Text))

        lines.add(Ticket("${res.getString(R.string.lbl_customer_name)}: ${baseEo.py_cu_name!!}", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        lines.add(Ticket(res.getString(R.string.lbl_amount).padStart(10, ' ').padEnd(30, ' ') + res.getString(R.string.lbl_change_amount), LineType.Text))
        lines.add(Ticket("${baseEo.py_amount} ${baseEo.py_cr_symbol}", LineType.Text, xPos = 400, yPos = 0))
        lines.add(Ticket("${baseEo.py_change} ${baseEo.py_cr_symbol}", LineType.Text, xPos = 50, yPos = 0))
        lines.add(Ticket(" ", LineType.Text))
        lines.add(Ticket("${baseEo.py_lc_amount} ${baseEo.py_lc_cr_symbol}", LineType.Text, xPos = 400, yPos = 0))
        lines.add(Ticket("${baseEo.py_lc_change} ${baseEo.py_lc_cr_symbol}", LineType.Text, xPos = 50, yPos = 0))
        lines.add(Ticket(" ", LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        if(baseEo.py_comment != null){
            lines.add(Ticket("${res.getString(R.string.lbl_comment)}", LineType.Text))
            lines.add(Ticket("${baseEo.py_comment}", LineType.Text))
        }
        lines.add(Ticket("\n", LineType.Text))
        lines.add(Ticket("\n", LineType.Text))

        lines.add(Ticket(res.getString(R.string.lbl_recipientby).padStart(10, ' ').padEnd(30, ' ') + res.getString(R.string.lbl_sign), LineType.Text))

        lines.add(Ticket("\n", LineType.Text))

        if(!baseEo.py_ref_no.isNullOrEmpty()){
            lines.add(Ticket(baseEo.py_ref_no, LineType.Barcode, AlignText.CENTER, Attribute.LARGE_FONT_BOLD_NO_UNDERLINE_HIGHLIGHT))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }

    fun createPdfTicket(baseEo: Transfer, logo: Int?, systemInfo: String?, message: String?, cmsMessage: String?): List<PdfTicket>{
        val lines: ArrayList<PdfTicket> = arrayListOf()

        if(logo != null) {
            val bmp: Bitmap = BitmapFactory.decodeResource(res, logo)
            lines.add(PdfTicket(null, null, LineType.Image, Element.ALIGN_CENTER, bmp = bmp))
        }
        // number and date

        lines.add(PdfTicket("${res.getString(R.string.lbl_no)}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "L"))
        lines.add(PdfTicket("${baseEo.tr_ref_no.toString()}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "V"))

        lines.add(PdfTicket("", null, LineType.NewLine))

        lines.add(PdfTicket("${res.getString(R.string.lbl_doc_date)}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "L"))
        lines.add(PdfTicket("${returnDateString(baseEo.tr_doc_date!!)}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "V"))


        lines.add(PdfTicket("", null, LineType.NewLine))

        lines.add(PdfTicket("${res.getString(R.string.lbl_time)}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "L"))
        lines.add(PdfTicket("${LocalTime.now()}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf", styleType = "V"))

        lines.add(PdfTicket("", null, LineType.NewLine))

        lines.add(PdfTicket("${res.getString(R.string.lbl_to_warehouse)}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf"))
        lines.add(PdfTicket("${baseEo.tr_wr_name!!}", null, LineType.Text, Element.ALIGN_LEFT, "assets/fonts/brandon_medium.otf"))

        lines.add(PdfTicket("", null, LineType.LineSeparator))

        var cols: HashMap<Int, ReportColumn> = HashMap()
        val rows: ArrayList<HashMap<Int, ReportColumn>> = arrayListOf()

        // Lines title
        cols.put(0, ReportColumn("${res.getString(R.string.lbl_prod_name)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
        cols.put(1, ReportColumn("${res.getString(R.string.lbl_barcode)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
        cols.put(2, ReportColumn("${res.getString(R.string.lbl_qty)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
        rows.add(cols)

        for (d: Transfer_Items in baseEo.items){
            cols = HashMap()
            val prod = if(lang == "ar_iq") d.trd_prod_name_ar!! else d.trd_prod_name!!.trim()

            cols.put(0, ReportColumn(prod, Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
            cols.put(1, ReportColumn("${d.trd_barcode}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
            cols.put(2, ReportColumn("${d.trd_unit_qty!!}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf"))
            rows.add(cols)
        }

        lines.add(PdfTicket(null, rows, LineType.Column ))

        if(!baseEo.tr_ref_no.isNullOrEmpty()){
            lines.add(PdfTicket(baseEo.tr_ref_no, null, LineType.Barcode, Element.ALIGN_CENTER))
            lines.add(PdfTicket("", null, LineType.NewLine))
        }

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(PdfTicket(cmsMessage, null, LineType.Text, Element.ALIGN_LEFT))
            lines.add(PdfTicket("", null, LineType.NewLine))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(PdfTicket(systemInfo, null, LineType.Text, Element.ALIGN_LEFT))
            lines.add(PdfTicket("", null, LineType.NewLine))
        }

        return lines
    }

    fun createPdf(baseEo: Transfer, logo: Int?, systemInfo: String?, message: String?, cmsMessage: String?): Template{
//        val lines: ArrayList<PdfTicket> = arrayListOf()
//
//        if(logo != null) {
//            val bmp: Bitmap = BitmapFactory.decodeResource(res, logo)
//            lines.add(PdfTicket(null, null, LineType.Image, Element.ALIGN_CENTER, bmp = bmp))
//        }
        // number and date

        var template = Template()
        var cells: HashMap<Int, ReportColumn> = HashMap()

        cells.put(0, ReportColumn("${res.getString(R.string.lbl_no)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
        cells.put(0, ReportColumn("${baseEo.tr_ref_no.toString()}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V"))

        template.header!!.add(PdfHeader(cells))

        cells = HashMap()
        cells.put(0, ReportColumn("${res.getString(R.string.lbl_doc_date)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
        cells.put(0, ReportColumn("${returnDateString(baseEo.tr_doc_date!!)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V"))

        template.header!!.add(PdfHeader(cells))

        cells = HashMap()
        cells.put(0, ReportColumn("${res.getString(R.string.lbl_time)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
        cells.put(0, ReportColumn("${LocalTime.now()}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V"))

        template.header!!.add(PdfHeader(cells))


        cells = HashMap()
        cells.put(0, ReportColumn("${res.getString(R.string.lbl_to_warehouse)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
        cells.put(0, ReportColumn("${baseEo.tr_wr_name!!}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V"))

        template.header!!.add(PdfHeader(cells))

        var cols: HashMap<Int, ReportColumn> = HashMap()
        val rows: ArrayList<HashMap<Int, ReportColumn>> = arrayListOf()

        // Lines title
        cols.put(0, ReportColumn("${res.getString(R.string.lbl_prod_name)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L", boderWidth = 1f))
        cols.put(1, ReportColumn("${res.getString(R.string.lbl_barcode)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L", boderWidth = 1f))
        cols.put(2, ReportColumn("${res.getString(R.string.lbl_qty)}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L", boderWidth = 1f))
        rows.add(cols)

        for (d: Transfer_Items in baseEo.items){
            cols = HashMap()
            val prod = if(lang == "ar_iq") d.trd_prod_name_ar!! else d.trd_prod_name!!.trim()

            cols.put(0, ReportColumn(prod, Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V", boderWidth = 1f))
            cols.put(1, ReportColumn("${d.trd_barcode}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V", boderWidth = 1f))
            cols.put(2, ReportColumn("${d.trd_unit_qty!!}", Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "V", boderWidth = 1f))
            rows.add(cols)
        }

        //lines.add(PdfTicket(null, rows, LineType.Column ))
        template.body = PdfBody()
        template.body!!.rows = rows

        if(!baseEo.tr_ref_no.isNullOrEmpty()){

            cells = HashMap()
            cells.put(0, ReportColumn("",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
            cells.put(0, ReportColumn("${res.getString(R.string.lbl_to_warehouse)}",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
            cells.put(0, ReportColumn("",Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
            template.footer!!.add(PdfFooter(cells))
        }

        if(!cmsMessage.isNullOrEmpty()){
            cells = HashMap()
            cells.put(0, ReportColumn(cmsMessage,Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
            template.footer!!.add(PdfFooter(cells))
        }

        if(!systemInfo.isNullOrEmpty()){
            cells = HashMap()
            cells.put(0, ReportColumn(systemInfo, Element.ALIGN_LEFT, 30, "assets/fonts/brandon_medium.otf", styleType = "L"))
            template.footer!!.add(PdfFooter(cells))
        }

        return template
    }

    fun create(dtxEoList: List<StockStatement>, dtTo: String?, wr_Name: String, logo: String?, systemInfo: String?, message: String?, cmsMessage: String?): List<Ticket>
    {
        val lines: ArrayList<Ticket> = arrayListOf()

        if(logo != null) {
            try {
                lines.add(Ticket(logo, LineType.Image, AlignText.CENTER, bmp = null))

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }
        // number and date

        lines.add(Ticket("${res.getString(R.string.lbl_warehouse_name)}: ${wr_Name}", LineType.Text))

        if(!dtTo.isNullOrEmpty()){
            lines.add(Ticket("${res.getString(R.string.rpt_todate)}: ${dtTo}", LineType.Text))
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        // Lines title
        lines.add(Ticket(res.getString(R.string.lbl_prod_name).padEnd(35, ' ') +  res.getString(R.string.lbl_qty) , LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        //var xPos :Int = 4
        //var yPos :Int = 10
        for (d: StockStatement in dtxEoList){
            var prod_name = ""
            if(lang == "ar_iq") {
                if(d.pr_name_ar != null) prod_name = d.pr_name_ar!!
            }else{
                prod_name = if(d.pr_name != null) d.pr_name!! else d.pr_name_ar!!
            }
            val prod = prod_name.trim()//.padEnd(34, ' ')
            //val qty = d.pr_barcode!!.toString().padEnd(10, ' ').padStart(4,' ')
            val qty = d.qty!!.toString()//.padEnd(7, ' ')


            if(prod.length > 35){
                val cpos = prod.substring(0,30).lastIndexOf(' ')
                val pr_name1 = prod.substring(0,cpos)
                val num = pr_name1.length + qty.length //+ net.length
                val line = pr_name1.padEnd(35, ' ') + qty //+ net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                val pr_name2 = prod.substring(cpos)
                val line1 = pr_name2.padEnd(47)
                lines.add(Ticket(line1, LineType.Text, AlignText.LEFT))
            }else{
                val line = prod.padEnd(35, ' ')  + qty //+ net// + if(num < 47) "".padEnd(47-num) else ""
                lines.add(Ticket(line, LineType.Text, AlignText.LEFT))
                //lines.add(Ticket(prod, LineType.Text, AlignText.LEFT, xPos = xPos, yPos = yPos))
                //lines.add(Ticket(prod, LineType.Text, AlignText.LEFT, xPos = xPos, yPos = yPos))
            }
        }

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))
        //val tqty = dtxEoList.sumByDouble { x->x.qty ?: 0.0 }
        val tqty = dtxEoList[0].tqty

        lines.add(Ticket("${res.getString(R.string.lbl_total_qty)}".padStart(35, ' ') + tqty.toString().padStart(10,' ') , LineType.Text))

        lines.add(Ticket("".padEnd(48, 'ـ'), LineType.Text))

        if(!cmsMessage.isNullOrEmpty()){
            lines.add(Ticket(cmsMessage, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        if(!systemInfo.isNullOrEmpty()){
            lines.add(Ticket(systemInfo, LineType.Text))
            lines.add(Ticket("\n", LineType.Text))
        }

        return lines
    }
//    fun returnDateString(isoString: String) : String{
//        // 2017-09-11T01:16:13.858Z converted to below
//        // Monday 4:35 PM format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//        val isLeftToRight = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
//        val pattern: String = if(isLeftToRight) "dd-MM-yyyy" else "yyyy-MM-dd"
//        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
//        var convertedDate = Date()
//        try {
//            convertedDate = isoFormatter.parse(isoString)
//        }catch (e: ParseException){
//            Log.d("PARSE", "Cannot parse date")
//        }
//
//        val outDateString = SimpleDateFormat(pattern, Locale.getDefault())
//        return  outDateString.format(convertedDate)
//    }

    fun returnDateString(isoString: String?) : String{
        try {
            if(isoString == null) return ""
            // 2017-09-11T01:16:13.858Z converted to below
            // Monday 4:35 PM format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val enLang = Locale("en")
            val isLeftToRight = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
            val pattern: String = if(!isLeftToRight) "dd-MM-yyyy" else "yyyy-MM-dd"
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

    fun numberFormat(value: Double?): String{
        val nf = "%,.0f".format(Locale.ENGLISH, value)
        return nf
    }
}
