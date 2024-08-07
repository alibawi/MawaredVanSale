package com.mawared.mawaredvansale.controller.sales.order.addorder

//import com.google.zxing.integration.android.IntentIntegrator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.mawared.mawaredvansale.App
import com.mawared.mawaredvansale.R
import com.mawared.mawaredvansale.controller.adapters.CustomerAdapter1
import com.mawared.mawaredvansale.controller.adapters.atc_prod_expiry_Adapter
import com.mawared.mawaredvansale.controller.base.ScopedFragmentLocation
import com.mawared.mawaredvansale.data.db.entities.md.Product
import com.mawared.mawaredvansale.data.db.entities.sales.Sale_Order_Items
import com.mawared.mawaredvansale.databinding.AddOrderFragmentBinding
import com.mawared.mawaredvansale.interfaces.IAddNavigator
import com.mawared.mawaredvansale.interfaces.IMessageListener
import com.mawared.mawaredvansale.services.repositories.NetworkState
import com.mawared.mawaredvansale.utilities.snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.add_order_fragment.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate
import java.util.*
import com.google.zxing.integration.android.IntentIntegrator
import com.mawared.mawaredvansale.controller.Barcode.ContinuousActivity

class AddOrderFragment : ScopedFragmentLocation() , KodeinAware, IMessageListener, IAddNavigator<Sale_Order_Items> {

    override val kodein by kodein()

    private val factory: AddOrderViewModelFactory by instance()

    val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(AddOrderViewModel::class.java)
    }

    lateinit var binding: AddOrderFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // initialize binding
        binding = DataBindingUtil.inflate(inflater, R.layout.add_order_fragment, container, false)
        viewModel.ctx = requireActivity()
        viewModel.addNavigator = this
        viewModel.msgListener = this
        viewModel.docDate.value = "${LocalDate.now()}"
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        bindUI()

        binding.scanCodeBtn.setOnClickListener {
            val scanner = Intent(requireContext(), ContinuousActivity::class.java)
            startActivityForResult(scanner, 12)
//            val scanner = IntentIntegrator(requireActivity())
//
//            scanner.setBeepEnabled(false)
//            scanner.setCameraId(0)
//            scanner.setPrompt("SCAN")
//            scanner.setBarcodeImageEnabled(false)
//            scanner.initiateScan()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            val args = AddOrderFragmentArgs.fromBundle(requireArguments())
            viewModel.mode = args.mode
            if(viewModel.mode == "View") viewModel.visible = View.GONE else viewModel.visible = View.VISIBLE
            if(viewModel.mode != "Add"){
                viewModel.setOrderId(args.orderId)
            }
        }
    }

    // enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.layout_entry_sub_title)
    }
    // inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(viewModel.mode == "View"){
            inflater.inflate(R.menu.view_menu, menu)
        }else{
            inflater.inflate(R.menu.add_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    // handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_btn ->{
                if(!viewModel.isRunning){
                    viewModel.isRunning = true
                    hideKeyboard()
                    showDialog(requireContext(), getString(R.string.save_dialog_title), getString(R.string.msg_save_confirm),null ,{
                        onStarted()
                        viewModel.location = getLocationData()
                        viewModel.onSave()
                    },{
                        viewModel.isRunning = false
                    })
                }
            }
            R.id.close_btn -> {
                hideKeyboard()
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            if(data.hasExtra("return_barcode")){
               viewModel.searchBarcode.value = data.getExtras()!!.getString("return_barcode")//  binding.edtxtBarcode.setText(data.getExtras()!!.getString("return_barcode"))
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bindUI() = GlobalScope.launch(Main){

        viewModel._baseEo.observe(viewLifecycleOwner, Observer {
            if(it != null){
                onSuccess(getString(R.string.msg_success_saved))
                viewModel.doPrint(it)
                requireActivity().onBackPressed()
            }else{
                onFailure(getString(R.string.msg_failure_saved))
            }

        })

        viewModel.product.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel.selectedProduct = it
                viewModel.unitPrice = viewModel.selectedProduct!!.pr_unit_price ?: 0.00
                viewModel.setProductId(viewModel.selectedProduct!!.pr_Id)
                viewModel.allowed_enter_gift_qty.value = false
                if(App.prefs.saveUser!!.hasGift != null && App.prefs.saveUser!!.hasGift == "Y") viewModel.allowed_enter_gift_qty.value = true
                viewModel.onAddItem()
            }
        })

        viewModel.entityEo.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel._entityEo = it
                viewModel.docNo.value = it.so_no?.toString()
                viewModel.docDate.value = viewModel.returnDateString(it.so_date!!)
                viewModel.oCu_Id = it.so_customerId!!
                viewModel.allowed_select_prod.value = true
                viewModel.cCustomer_Name.value = it.so_ccustomer_name
                viewModel.rowNo = it.items.maxByOrNull { it.sod_rowNo!! }?.sod_rowNo ?: 0
                viewModel.price_cat_code = it.so_price_cat_code!!
                binding.atcCustomer.setText("${it.so_customer_name}", true)
                viewModel.setItems(it.items)
            }
        })

        viewModel.soItems.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            initRecyclerView(it.toOrderItemRow())
            viewModel.setTotals()
        })

        // Customer autocomplete settings
        val adapter = CustomerAdapter1(requireContext(), R.layout.support_simple_spinner_dropdown_item )

        binding.atcCustomer.threshold = 0
        binding.atcCustomer.dropDownWidth = resources.displayMetrics.widthPixels - 10
        binding.atcCustomer.setAdapter(adapter)
        binding.btnOpenCustomer.setOnClickListener {
            binding.atcCustomer.showDropDown()
        }

        binding.atcCustomer.setOnItemClickListener { _, _, position, _ ->
            viewModel.allowed_select_prod.value = true
            viewModel.selectedCustomer = adapter.getItem(position)
            if(viewModel.oCu_Id != viewModel.selectedCustomer?.cu_ref_Id){
                viewModel.clearItems()
            }
            binding.atcCustomer.dismissDropDown()
            viewModel.setPriceCategory()
            viewModel.setTerm("")
        }

        binding.atcCustomer.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if(viewModel.selectedCustomer == null) viewModel.term.value = s.toString() else binding.atcCustomer.dismissDropDown()
            }
        })
        // bind customer to autocomplete
        viewModel.customerList.observe(viewLifecycleOwner, Observer { cu ->
            adapter.setCustomers(cu)
            if(viewModel.mode != "Add" && cu.isNotEmpty() && viewModel.entityEo.value != null){
                viewModel.selectedCustomer = cu.find { it.cu_ref_Id == viewModel.entityEo.value?.so_customerId}
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            progress_bar_order.visibility =  if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })


        binding.atcProduct.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if(viewModel.selectedProduct == null) viewModel._term.value = s.toString() else binding.atcProduct.dismissDropDown()
            }
        })
        // bind products to autocomplete
        viewModel.productList.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            initProductAutocomplete(it)
        })

        viewModel.mDiscount.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel.allowed_discount.value = false
                viewModel.discount = it
            }else{
                viewModel.allowed_discount.value = true
            }
        })

        viewModel.mVoucher.observe(viewLifecycleOwner, Observer {
            viewModel.voucher = it
        })

        viewModel.currencyRate.observe(viewLifecycleOwner, Observer {
            viewModel.rate = if(it.cr_rate != null) it.cr_rate!! else 0.0
        })

        viewModel.setVoucherCode("SaleOrder")

        viewModel.setCurrencyId(App.prefs.saveUser!!.sf_cr_Id!!)

        viewModel.setItems(arrayListOf())
        if(viewModel.mode != "Add") {
            viewModel.setTerm("")
        }else{
            viewModel.term.value = ""
        }
    }

    // init invoices items
    private fun initRecyclerView(rows: List<OrderItemRow>){
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(rows)
        }

        rcv_order_items.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = groupAdapter
        }
    }

    // convert invoice items to invoice items row
    private fun List<Sale_Order_Items>.toOrderItemRow(): List<OrderItemRow>{
        return this.map {
            OrderItemRow(it, viewModel)
        }
    }

    // init product autocomplete view
    private fun initProductAutocomplete(products: List<Product>){
        val adapter = atc_prod_expiry_Adapter(requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            products
        )
        binding.atcProduct.threshold = 0
        binding.atcProduct.dropDownWidth = resources.displayMetrics.widthPixels
        binding.atcProduct.setAdapter(adapter)
        binding.btnOpenItems.setOnClickListener {
            binding.atcProduct.showDropDown()
        }
//        binding.atcProduct.setOnFocusChangeListener { _, b ->
//            if(b) binding.atcProduct.showDropDown()
//        }
        binding.atcProduct.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedProduct = adapter.getItem(position)
            viewModel.unitPrice = viewModel.selectedProduct!!.pr_unit_price ?: 0.00
            viewModel.setProductId(viewModel.selectedProduct!!.pr_Id)
            viewModel.allowed_enter_gift_qty.value = false
            if(App.prefs.saveUser!!.hasGift != null && App.prefs.saveUser!!.hasGift == "Y") viewModel.allowed_enter_gift_qty.value = true
        }

    }

    override fun onDelete(baseEo: Sale_Order_Items) {
        showDialog(requireContext(), getString(R.string.delete_dialog_title), getString(R.string.msg_confirm_delete), baseEo,{
            viewModel.deleteItem(it)
        })
    }

    override fun onShowDatePicker(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, yr, monthOfYear, dayOfMonth ->

            viewModel.docDate.value = "${yr}-${monthOfYear + 1}-${dayOfMonth}"//"${dayOfMonth}-${monthOfYear + 1}-${yr}"

        }, year, month, day)
        dpd.show()
    }

    // clear selected customer
    override fun clear(code: String) {
        when(code) {
            "cu"-> {
                viewModel.oCu_Id = viewModel.selectedCustomer?.cu_Id
                viewModel.allowed_select_prod.value = false
                binding.atcCustomer.setText("", true)
            }
            "prod"-> {
                binding.atcProduct.setText("", true)
            }
        }

    }

    override fun onStarted() {
        progress_bar_order?.visibility = View.VISIBLE
    }

    override fun onSuccess(message: String) {
        add_order_layout?.snackbar(message)
        progress_bar_order?.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        add_order_layout?.snackbar(message)
        progress_bar_order?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJob()
    }
}
