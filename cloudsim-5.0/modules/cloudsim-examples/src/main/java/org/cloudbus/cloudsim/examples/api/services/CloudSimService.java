package org.cloudbus.cloudsim.examples.api.services;


import lombok.RequiredArgsConstructor;
import org.cloudbus.cloudsim.examples.api.models.CloudletInfo;
import org.cloudbus.cloudsim.examples.api.models.DatacenterRequest;
import org.cloudbus.cloudsim.examples.api.models.VmRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CloudSimService {
    private  CloudSimInstance cloudSimInstance;
    public void handleDatacenter(DatacenterRequest datacenterRequest){
        cloudSimInstance = new CloudSimInstance();
        cloudSimInstance.initialisiserCloud();
        cloudSimInstance.loaderDatacenters(datacenterRequest.getDatacenters());
    }

    public List<CloudletInfo> handleVms(VmRequest vmRequest){
        cloudSimInstance.loadBrocker();
       cloudSimInstance.createVms(vmRequest.getVms());
       return  cloudSimInstance.startSim();
    }
}
