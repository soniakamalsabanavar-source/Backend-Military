package com.military.assetmanagement.payload.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseRequest {
    private Long baseId;
    private Long equipmentId;
    private Integer quantity;
    private LocalDate purchaseDate;
    private Double unitCost;
    private String supplierDetails;
}
