package org.cloudbus.cloudsim.examples.api.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatacenterModel {
    private String arch;
    private String os;
    private String vmm;
    private List<HostModel> hosts;
}
