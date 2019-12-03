package com.mawared.mawaredvansale.data.db.entities.sales

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Sale_Order_Items(
    var sod_rowNo: Int?,            // Current Item Row Number
    var sod_so_Id: Int?,          // Order Id
    var sod_prod_Id: Int?,          // Product Id
    var sod_uom_Id: Int?,           // Unit of Measurement
    var sod_pack_qty: Double?,      // Pack Quantity
    var sod_pack_size: Double?,     // Pack Size
    var sod_unit_qty: Double?,      // Unit Quantity
    var sod_unit_price: Double?,    // Unit Price
    var sod_line_total: Double?,    // Line Total
    var sod_discount: Double?,      // Percentage Discount
    var sod_disvalue: Double?,      // Fixed Amount Discount
    var sod_net_total: Double?,
    var sod_lotno: String?,         // Lot Number
    var sod_isPromotion: String?,   // Is Current Item Promotion Or Not : Y:Yes, N: No
    var sod_promotionId: Int?,      // Promotion Id
    var created_at: String?,        // created datetime
    var created_by: String?,        // created user
    var updated_at: String?,        // Updated datetime
    var updated_by: String?         // Updated user
) {
    @PrimaryKey(autoGenerate = false)
    var sod_id: Int = 0
    @Ignore
    var sod_prod_name: String? = null
    @Ignore
    var sod_barcode: String? = null
    @Ignore
    var sod_uom_name: String? = null
}
