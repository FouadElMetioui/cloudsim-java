package org.cloudbus.cloudsim.examples.api.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class HostModel {

    private List<Integer> mips;
    private int ram;
    private long storage;
    private long bw;
}
