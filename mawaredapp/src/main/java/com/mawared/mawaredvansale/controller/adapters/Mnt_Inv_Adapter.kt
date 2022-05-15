package com.mawared.mawaredvansale.controller.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.data.db.entities.sales.Sale
import com.mawared.mawaredvansale.databinding.MntInvoiceRowBinding
import java.util.*

class Mnt_Inv_Adapter(context: Context, @LayoutRes private val layoutResource: Int):
    ArrayAdapter<Sale>(context, layoutResource), Filterable {

    private var mdataList: List<Sale> = arrayListOf()
    private var allList: List<Sale> = arrayListOf()
    fun setInvoices(dataList: List<Sale>){
        mdataList = dataList.map {
            return@map it
        }
        allList = dataList.map{
            return@map it
        }
        notifyDataSetChanged()
    }
    override fun getCount(): Int {
        return mdataList.count()
    }

    override fun getItem(position: Int): Sale? {
        return mdataList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MntInvoiceRowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.mnt_invoice_row, parent, false)
        binding.entityEo = getItem(position)
        return binding.root
    }
    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                clear()
                if(filterResults.values != null){
                    addAll(filterResults.values as List<Sale>)
                    notifyDataSetChanged()
                }
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase(Locale.ENGLISH)
                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    mdataList
                else
                    allList.filter {
                        it.sl_refNo!!.toLowerCase(Locale.ENGLISH).contains(queryString)
                    }
                return filterResults
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Sale).sl_refNo as CharSequence
            }
        }
    }
}