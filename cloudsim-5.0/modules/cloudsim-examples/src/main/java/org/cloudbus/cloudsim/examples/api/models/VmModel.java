package org.cloudbus.cloudsim.examples.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class VmModel {

    private int mips;
    private long size;
    private int ram;
    private long bw;
    private int pesNumber;
    private String vmm;
    private List<CloudletModel> cloudlets;
}
