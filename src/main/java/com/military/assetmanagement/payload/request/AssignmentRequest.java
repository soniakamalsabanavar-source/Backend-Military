package com.military.assetmanagement.payload.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AssignmentRequest {
    private Long baseId;
    private Long equipmentId;
    private String personnel;
    private Integer quantity;
    private LocalDate assignmentDate;
}
