package com.mawared.mawaredvansale.controller.sales.order.orderslist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.controller.base.ScopedFragment
import com.mawared.mawaredvansale.data.db.entities.sales.Sale_Order
import com.mawared.mawaredvansale.databinding.OrdersFragmentBinding
import com.mawared.mawaredvansale.interfaces.IMainNavigator
import com.mawared.mawaredvansale.interfaces.IMessageListener
import com.mawared.mawaredvansale.utilities.hide
import com.mawared.mawaredvansale.utilities.show
import com.mawared.mawaredvansale.utilities.snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.orders_fragment.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class OrdersFragment : ScopedFragment(), KodeinAware, IMainNavigator<Sale_Order>, IMessageListener {

    override val kodein by kodein()

    private val factory: OrdersViewModelFactory by instance()

    private lateinit var viewModel: OrdersViewModel
    private lateinit var binding: OrdersFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this, factory).get(OrdersViewModel::class.java)

        // initialize binding
        binding = DataBindingUtil.inflate(inflater, R.layout.orders_fragment, container, false)

        viewModel.navigator = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        bindUI()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.layout_order_list_title)
        (activity as AppCompatActivity).supportActionBar!!.subtitle = ""
    }

    // inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {

            }
            R.id.addBtn -> {
                navController.navigate(R.id.action_ordersFragment_to_addOrderFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUI() = GlobalScope.launch(Main) {
        viewModel.orders.observe(this@OrdersFragment, Observer {
            group_loading.hide()
            initRecyclerView(it.toOrderRow())
        })

        viewModel.deleteRecord.observe(this@OrdersFragment, Observer {
            group_loading.hide()
            if(it == "Successful"){
                onSuccess(getString(R.string.msg_success_delete))
                viewModel.setCustomer(null)
            }
            else{
                onFailure(getString(R.string.msg_failure_delete))
            }
        })

        viewModel.setCustomer(null)
    }

    private fun initRecyclerView(saleItem: List<OrderRow>){
       try {
           val groupAdapter = GroupAdapter<ViewHolder>().apply {
               addAll(saleItem)
           }

           rcv_orders.apply {
               layoutManager = LinearLayoutManager(this@OrdersFragment.context)
               setHasFixedSize(true)
               adapter = groupAdapter
           }
       }catch (e: Exception){
           Log.e("ErrorOF", "Error ${e.message}")
       }
    }

    private fun List<Sale_Order>.toOrderRow(): List<OrderRow>{
        return this.map {
            OrderRow(it, viewModel)
        }
    }


    override fun onItemDeleteClick(baseEo: Sale_Order) {
        showDialog(context!!, getString(R.string.delete_dialog_title), getString(R.string.msg_confirm_delete), baseEo){
            onStarted()
            viewModel.confirmDelete(it)
        }
    }

    override fun onItemEditClick(baseEo: Sale_Order) {
        val action = OrdersFragmentDirections.actionOrdersFragmentToAddOrderFragment()
        action.orderId = baseEo.so_id
        action.mode ="Edit"
        navController.navigate(action)
    }

    override fun onItemViewClick(baseEo: Sale_Order) {
        val action = OrdersFragmentDirections.actionOrdersFragmentToAddOrderFragment()
        action.orderId = baseEo.so_id
        action.mode ="View"
        navController.navigate(action)
    }

    override fun onStarted() {
        group_loading.show()
    }

    override fun onSuccess(message: String) {
        group_loading.hide()
        order_list_cl.snackbar(message)
    }

    override fun onFailure(message: String) {
        group_loading.hide()
        order_list_cl.snackbar(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJob()
    }
}
