package com.mawared.mawaredvansale.controller.adapters.pagination.reports

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.controller.reports.fms.CashbookStatementViewModel
import com.mawared.mawaredvansale.data.db.entities.reports.fms.ReportRowHeader
import com.mawared.mawaredvansale.data.db.entities.reports.fms.CashbookStatement
import com.mawared.mawaredvansale.databinding.CashbookItemBinding
import com.mawared.mawaredvansale.databinding.CashbookItemFooterBinding
import com.mawared.mawaredvansale.databinding.CashbookItemHeaderBinding
import com.mawared.mawaredvansale.services.repositories.NetworkState
import kotlinx.android.synthetic.main.network_state_item.view.*

class CashbookPagedListAdapter(private val viewModel: CashbookStatementViewModel, val context: Context):
    PagedListAdapter<CashbookStatement, RecyclerView.ViewHolder>(DiffCallBack()) {

    val MAIN_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    val HEADER_VIEW_TYPE = 3
    val FOOTER_VIEW_TYPE = 4
    private var header: ReportRowHeader? = null

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //val view: View
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == HEADER_VIEW_TYPE){
            val bind: CashbookItemHeaderBinding = DataBindingUtil.inflate(inflater, R.layout.cashbook_item_header, parent, false)
            return HeaderViewHolder(bind)
        }else if(viewType == FOOTER_VIEW_TYPE){
            val bind : CashbookItemFooterBinding = DataBindingUtil.inflate(inflater, R.layout.cashbook_item_footer, parent, false)
            return FooterViewHolder(bind)
        }else if(viewType == NETWORK_VIEW_TYPE){
            val view = inflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }else {
            val bind : CashbookItemBinding = DataBindingUtil.inflate(inflater, R.layout.cashbook_item, parent, false)
            return ItemViewHolder(bind)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == HEADER_VIEW_TYPE) {
            (holder as HeaderViewHolder).bind(header)
        } else if (viewType == FOOTER_VIEW_TYPE) {
            (holder as FooterViewHolder).bind(getItem(1), viewModel)
        } else if (viewType == MAIN_VIEW_TYPE) {
            (holder as ItemViewHolder).bind(getItem(position), viewModel)
        } else  {
            (holder as NetworkStateItemViewHolder).bind(networkState, context)
        }
    }

    private fun hasExtraRow(): Boolean{
        var retValue = networkState != null && networkState != NetworkState.LOADED
        if(!retValue){
            val counts = super.getItemCount()
            retValue = (counts !=0 && counts < 20)
            if(retValue)
                networkState = NetworkState.ENDOFLIST
        }
        return retValue
    }

    override fun getItemCount(): Int {
        val counts = super.getItemCount()
        return counts + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if(hasExtraRow() && position == itemCount ) // Network type
            return NETWORK_VIEW_TYPE
        else if(position == 0)   // Header Type
            return HEADER_VIEW_TYPE
        else if(position == itemCount - 1) // Footer Type
            return FOOTER_VIEW_TYPE
        // Item Type
        return  MAIN_VIEW_TYPE
    }

    fun setHeader(_header: ReportRowHeader){
        this.header = _header
    }

    fun refersh(){
        notifyItemRangeRemoved(0, super.getItemCount())
    }

    class DiffCallBack: DiffUtil.ItemCallback<CashbookStatement>(){
        override fun areItemsTheSame(oldItem: CashbookStatement, newItem: CashbookStatement): Boolean {
            return oldItem.row_no == newItem.row_no
        }

        override fun areContentsTheSame(oldItem: CashbookStatement, newItem: CashbookStatement): Boolean {
            return oldItem.equals(newItem)
        }
    }

    class ItemViewHolder(private val viewBinding: CashbookItemBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: CashbookStatement?, viewModel: CashbookStatementViewModel){
            viewBinding.entityEo = baseEo
            viewBinding.viewmodel = viewModel
        }
    }

    class HeaderViewHolder(private val viewBinding: CashbookItemHeaderBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: ReportRowHeader?){
            viewBinding.entityEo = baseEo
        }
    }

    class FooterViewHolder(private val viewBinding: CashbookItemFooterBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: CashbookStatement?, viewModel: CashbookStatementViewModel){
            viewBinding.entityEo = baseEo
            viewBinding.viewmodel = viewModel
        }
    }

    class NetworkStateItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?, context: Context){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.progress_bar_item.visibility = View.VISIBLE
            }else{
                itemView.progress_bar_item?.visibility = View.GONE
            }

            if(networkState != null){// && networkState == NetworkState.ERROR){
                val pack = context.packageName
                val id = context.resources.getIdentifier(networkState.msg,"string", pack)
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = context.resources.getString(id)
            }else{
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(networkState: NetworkState){
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){                                //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())     //remove the progressbar at the end
            }else{                                          //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())    //add the progressbar at the end
            }
        }else if(hasExtraRow && previousState != networkState){ // hasExtraRow is true and hadExtraRow true
            notifyItemChanged(itemCount-1)
        }
    }
}