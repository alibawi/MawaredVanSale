package com.mawared.mawaredvansale.services.repositories.masterdata

import androidx.lifecycle.LiveData
import com.mawared.mawaredvansale.data.db.entities.md.*
import com.mawared.mawaredvansale.data.db.entities.md.Currency
import com.mawared.mawaredvansale.services.netwrok.responses.ResponseSingle
import org.threeten.bp.LocalDate

interface IMDataRepository {
    // Customer method
    //suspend fun getCustomer(): LiveData<List<Customer>>
    fun getCustomers(sm_Id: Int): LiveData<List<Customer>>
    fun customers_getSchedule(sm_Id: Int): LiveData<List<Customer>>
    fun getCustomersByOrg(org_Id: Int?): LiveData<List<Customer>>
    fun getCustomerById(cu_Id: Int): LiveData<Customer>
    fun insertCustomer(baseEo: Customer): LiveData<Customer>
    suspend fun customerSaveOrUpdate(baseEo: Customer): ResponseSingle<Customer>
    // Customer Payment Type
    fun getCptAll(term: String): LiveData<List<Customer_Payment_Type>>

    // Customer Group
    fun getCustomersGroups(term: String): LiveData<List<Customer_Group>>
    // Customer Category
    fun customersCategory_GetByTerm(term: String): LiveData<List<Customer_Category>>
    // Product method
    fun getProducts(term: String, warehouseId: Int?, priceCode: String): LiveData<List<Product>>
    fun getProductsByPriceTerm(term: String, priceCode: String): LiveData<List<Product>>
    fun getProductsByUserWarehouse(term: String, userId: Int?, priceCode: String): LiveData<List<Product>>
    fun getProductsBySearch(term: String): LiveData<List<Product>>
    fun productGetByBarcode(barcode: String, warehouseId: Int?, priceCode: String): LiveData<Product>
    fun getProductById(prod_Id: Int): LiveData<Product>
    // Product Price
    fun getProductPrice(prod_Id: Int): LiveData<Product_Price_List>
    fun product_getLastPrice(prod_Id: Int, PriceCode: String): LiveData<Product_Price_List>
    // Currency
    fun getCurrency(cl_Id: Int): LiveData<Currency>

    fun getCurrencyByCode(cr_symbol: String): LiveData<Currency>
    // Currency Rate
    fun getRate(cr_Id: Int): LiveData<Currency_Rate>
    // Salesman
    fun getSalesman(pda_code: String): LiveData<Salesman>
    fun salesman_GetAll(): LiveData<List<Salesman>>
    // Voucher
    fun getVoucherByCode(vo_code: String): LiveData<Voucher>
    // Price Category
    fun priceCat_GetAll(): LiveData<List<PriceCategory>>
    fun priceCat_GetById(prc_Id: Int): LiveData<PriceCategory>
    fun getDiscountItem(pr_Id: Int, currentDate: LocalDate, org_Id: Int?): LiveData<Discount>
    fun warehouse_GetAll(): LiveData<List<Warehouse>>
    fun warehouse_GetBySalesman(sm_Id: Int): LiveData<List<Warehouse>>
    fun cancelJob()
}