package com.mawared.mawaredvansale.controller.inventory.stockout.stockoutlist

import androidx.lifecycle.ViewModel
import com.mawared.mawaredvansale.App
import com.mawared.mawaredvansale.data.db.entities.inventory.Stockout
import com.mawared.mawaredvansale.data.db.entities.md.Product_Category
import com.mawared.mawaredvansale.interfaces.IMainNavigator
import com.mawared.mawaredvansale.services.repositories.stockout.IStockOutRepository
import com.mawared.mawaredvansale.utilities.Coroutines
import com.mawared.mawaredvansale.utilities.lazyDeferred

class StockOutViewModel(private val repository: IStockOutRepository) : ViewModel() {
    private val userId: Int =  if(App.prefs.saveUser?.id != null)  App.prefs.saveUser!!.id else 0

    private var navigator: IMainNavigator<Stockout>? = null
    var term: String? = ""

    fun loadData(list: MutableList<Stockout>, term: String, pageCount: Int, loadMore: (List<Stockout>?, Int) -> Unit){
        try {
            Coroutines.ioThenMain({
                val tmp = repository.getOnPages(userId, term, pageCount)
                if(tmp != null){
                    list.addAll(tmp)
                }
            }, {loadMore(list, pageCount)})
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun setNavigator(navigator: IMainNavigator<Stockout>)
    {
        this.navigator = navigator
    }

    fun onItemDelete(baseEo: Stockout)
    {
        navigator?.onItemDeleteClick(baseEo)
    }

    fun onItemEdit(baseEo: Stockout)
    {
        navigator?.onItemEditClick(baseEo)
    }

    fun onItemView(baseEo: Stockout)
    {
        navigator?.onItemViewClick(baseEo)
    }

    fun cancelJob(){
        repository.cancelJob()
    }
}
