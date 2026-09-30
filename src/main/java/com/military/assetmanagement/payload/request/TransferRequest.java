package com.military.assetmanagement.payload.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TransferRequest {
    private Long sourceBaseId;
    private Long destinationBaseId;
    private Long equipmentId;
    private Integer quantity;
    private LocalDate transferDate;
    private String remarks;
}
