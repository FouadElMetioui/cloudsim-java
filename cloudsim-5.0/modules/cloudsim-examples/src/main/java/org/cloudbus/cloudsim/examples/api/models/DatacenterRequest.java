package org.cloudbus.cloudsim.examples.api.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatacenterRequest {
    private List<DatacenterModel> datacenters;
}
