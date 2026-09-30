package com.military.assetmanagement.payload.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenditureRequest {
    private Long baseId;
    private Long equipmentId;
    private Integer quantity;
    private LocalDate expenditureDate;
    private String remarks;
}
