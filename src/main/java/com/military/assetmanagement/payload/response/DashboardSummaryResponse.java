package com.military.assetmanagement.payload.response;
import lombok.Data;

@Data
public class DashboardSummaryResponse {
    private int totalOpeningBalance;
    private int totalClosingBalance;
    private int netMovement;
    private int totalPurchases;
    private int totalTransfersIn;
    private int totalTransfersOut;
    private int totalAssignedAssets;
    private int totalExpendedAssets;
}
