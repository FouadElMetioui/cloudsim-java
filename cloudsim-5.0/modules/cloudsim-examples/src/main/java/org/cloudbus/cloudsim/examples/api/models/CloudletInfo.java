package org.cloudbus.cloudsim.examples.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CloudletInfo {
    private int cloudletId;
    private String status;
    private int dataCenterId;
    private int vmId;
    private double time;
    private double startTime;
    private double finishTime;
}
