package org.cloudbus.cloudsim.examples.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CloudletModel {
    private long length;
    private int pesNumber;
    private long fileSize;
    private long outputSize;

    public CloudletModel() {

    }
}
