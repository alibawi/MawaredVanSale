package com.mawared.mawaredvansale.controller.sales.psorder.psorderlist

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
import com.mawared.mawaredvansale.databinding.PsordersFragmentBinding
import com.mawared.mawaredvansale.interfaces.IMainNavigator
import com.mawared.mawaredvansale.interfaces.IMessageListener
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

class PSOrdersFragment : ScopedFragment(), KodeinAware, IMainNavigator<Sale_Order>, IMessageListener {

    override val kodein by kodein()

    private val factory: PSOrdersViewModelFactory by instance()

    private lateinit var viewModel: PSOrdersViewModel
    private lateinit var binding: PsordersFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this, factory).get(PSOrdersViewModel::class.java)

        // initialize binding
        binding = DataBindingUtil.inflate(inflater, R.layout.psorders_fragment, container, false)

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
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.layout_psorder_list_title)
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
                navController.navigate(R.id.action_PSOrdersFragment_to_PSOrderEntryFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUI() = GlobalScope.launch(Main) {
        viewModel.orders.observe(viewLifecycleOwner, Observer {
            //llProgressBar?.visibility = View.GONE
            initRecyclerView(it.sortedByDescending { it.so_date }.toOrderRow())
        })

        viewModel.deleteRecord.observe(viewLifecycleOwner, Observer {
            //llProgressBar?.visibility = View.GONE
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

    private fun initRecyclerView(saleItem: List<PSOrderRow>){
       try {
           val groupAdapter = GroupAdapter<ViewHolder>().apply {
               addAll(saleItem)
           }

           rcv_orders.apply {
               layoutManager = LinearLayoutManager(this@PSOrdersFragment.context)
               setHasFixedSize(true)
               adapter = groupAdapter
           }
       }catch (e: Exception){
           Log.e("ErrorOF", "Error ${e.message}")
       }
    }

    private fun List<Sale_Order>.toOrderRow(): List<PSOrderRow>{
        return this.map {
            PSOrderRow(it, viewModel)
        }
    }


    override fun onItemDeleteClick(baseEo: Sale_Order) {
        showDialog(context!!, getString(R.string.delete_dialog_title), getString(R.string.msg_confirm_delete), baseEo){
            onStarted()
            viewModel.confirmDelete(it)
        }
    }

    override fun onItemEditClick(baseEo: Sale_Order) {
        val action = PSOrdersFragmentDirections.actionPSOrdersFragmentToPSOrderEntryFragment()
        action.orderId = baseEo.so_id
        action.mode ="Edit"
        navController.navigate(action)
    }

    override fun onItemViewClick(baseEo: Sale_Order) {
        val action = PSOrdersFragmentDirections.actionPSOrdersFragmentToPSOrderEntryFragment()
        action.orderId = baseEo.so_id
        action.mode ="View"
        navController.navigate(action)
    }

    override fun onStarted() {
        //llProgressBar?.visibility = View.VISIBLE
    }

    override fun onSuccess(message: String) {
        //llProgressBar?.visibility = View.GONE
        order_list_cl.snackbar(message)
    }

    override fun onFailure(message: String) {
        //llProgressBar?.visibility = View.GONE
        order_list_cl.snackbar(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJob()
    }
}