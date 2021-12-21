package com.mawared.mawaredvansale.controller.adapters.pagination.reports

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.controller.reports.stock.StockViewModel
import com.mawared.mawaredvansale.data.db.entities.reports.fms.ReportRowHeader
import com.mawared.mawaredvansale.data.db.entities.reports.stock.StockStatement
import com.mawared.mawaredvansale.databinding.StockRowBinding
import com.mawared.mawaredvansale.databinding.StockRowFooterBinding
import com.mawared.mawaredvansale.databinding.StockRowHeaderBinding
import com.mawared.mawaredvansale.services.repositories.NetworkState
import kotlinx.android.synthetic.main.network_state_item.view.*

class StockPagedListAdapter(private val viewModel: StockViewModel, val context: Context): PagedListAdapter<StockStatement, RecyclerView.ViewHolder>(DiffCallBack()) {
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
            val bind: StockRowHeaderBinding = DataBindingUtil.inflate(inflater, R.layout.stock_row_header, parent, false)
            return HeaderViewHolder(bind)
        }else if(viewType == FOOTER_VIEW_TYPE){
            val bind : StockRowFooterBinding = DataBindingUtil.inflate(inflater, R.layout.stock_row_footer, parent, false)
            return FooterViewHolder(bind)
        }else if(viewType == NETWORK_VIEW_TYPE){
            val view = inflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }else{
            val bind : StockRowBinding = DataBindingUtil.inflate(inflater, R.layout.stock_row, parent, false)
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
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if(hasExtraRow() && position == itemCount -1) // Network type
            return NETWORK_VIEW_TYPE
        else if(position == 0)   // Header Type
            return HEADER_VIEW_TYPE
        else if(position == itemCount - 2) // Footer Type
            return FOOTER_VIEW_TYPE
        // Item Type
        return  MAIN_VIEW_TYPE
    }

    fun setHeader(_header: ReportRowHeader){
        this.header = _header
    }

    class DiffCallBack: DiffUtil.ItemCallback<StockStatement>(){
        override fun areItemsTheSame(oldItem: StockStatement, newItem: StockStatement): Boolean {
            return oldItem.row_no == newItem.row_no
        }

        override fun areContentsTheSame(oldItem: StockStatement, newItem: StockStatement): Boolean {
            return oldItem.equals(newItem)
        }
    }

    class ItemViewHolder(private val viewBinding: StockRowBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: StockStatement?, viewModel: StockViewModel){
            viewBinding.entityEo = baseEo
            viewBinding.viewmodel = viewModel
        }
    }

    class HeaderViewHolder(private val viewBinding: StockRowHeaderBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: ReportRowHeader?){
            viewBinding.entityEo = baseEo
        }
    }

    class FooterViewHolder(private val viewBinding: StockRowFooterBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bind(baseEo: StockStatement?, viewModel: StockViewModel){
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
//            }else if(networkState != null && networkState == NetworkState.ENDOFLIST){
//                val pack = context.packageName
//                val id = context.resources.getIdentifier(networkState.msg,"string", pack)
//                itemView.error_msg_item.visibility = View.VISIBLE
//                itemView.error_msg_item.text = context.resources.getString(id)
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