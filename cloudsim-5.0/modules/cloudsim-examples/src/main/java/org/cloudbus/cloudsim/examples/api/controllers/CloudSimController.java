package org.cloudbus.cloudsim.examples.api.controllers;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.examples.api.models.*;
import org.cloudbus.cloudsim.examples.api.services.CloudSimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CloudSimController {

    final CloudSimService cloudSimService;


    @PostMapping("/datacenters")
    public String handleDatacenter(@RequestBody DatacenterRequest datacenterRequest) {
        return this.cloudSimService.handleDatacenter(datacenterRequest);
    }

    @PostMapping("/virtual-machines")
    public List<CloudletInfo> handleVms(@RequestBody VmRequest vmRequest) {
        return this.cloudSimService.handleVms(vmRequest);
    }
}