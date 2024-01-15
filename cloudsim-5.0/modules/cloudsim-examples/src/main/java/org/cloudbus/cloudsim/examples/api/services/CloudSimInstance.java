/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples.api.services;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.api.models.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;


@Service
public class CloudSimInstance {

	final int num_user = 1;
	Calendar calendar;
	boolean trace_flag;
	private static List<Cloudlet> cloudletList;
	private ArrayList<Datacenter> datacenterArrayList;
	Map<Integer, Integer> hostCloudletMap = new HashMap<>();

	private  static List<Vm> vmlist;
	DatacenterBroker broker;
	int brokerId;
	public void initialisiserCloud(){
		System.out.println("--------------- intialisation----------------");
		calendar = Calendar.getInstance();
		trace_flag = false;
		CloudSim.init(num_user, calendar, trace_flag);
		cloudletList = new ArrayList<Cloudlet>();
	}

	public void loaderDatacenters(List<DatacenterModel> datacenterModels){
		System.out.println("------- start loading------------");
		for(int i = 0 ; i<datacenterModels.size();i++){
			Datacenter datacenter = createDatacenter("datacenter_"+i,datacenterModels.get(i));
		}
	}

	public void loadBrocker(){
		System.out.println("loading brocker ....");
		this.broker = createBroker();
		brokerId = broker.getId();
	}

	public void createVms(List<VmModel> vms){
		System.out.println("-----starting creating vms....");
		vmlist = new ArrayList<Vm>();
		int vmid = 0;
		for (int i = 0; i < vms.size();i++) {
			Vm vm = new Vm(vmid++, brokerId, vms.get(i).getMips(), vms.get(i).getPesNumber(), vms.get(i).getRam(), vms.get(i).getBw(), vms.get(i).getSize(), vms.get(i).getVmm(), new CloudletSchedulerTimeShared());
			vmlist.add(vm);
		}
		broker.submitVmList(vmlist);


		cloudletList = new ArrayList<Cloudlet>();
		 vmid = 0;

		for (int i = 0; i < vms.size();i++){
		  //liste des vms
			Vm vm  = new Vm(vmid++, brokerId, vms.get(i).getMips(), vms.get(i).getPesNumber(), vms.get(i).getRam(), vms.get(i).getBw(), vms.get(i).getSize(), vms.get(i).getVmm(), new CloudletSchedulerTimeShared());
			 List<CloudletModel> clms;
			 clms = vms.get(i).getCloudlets();
			 int id = 0;
			for(int j= 0 ; j <clms.size(); j++){
				//list des cloudlets
				System.out.println("crrespandance entre vm cloudlet ...");

				UtilizationModel utilizationModel = new UtilizationModelFull();
				Cloudlet cloudlet = new Cloudlet(id, clms.get(j).getLength(), clms.get(j).getPesNumber(), clms.get(j).getFileSize(), clms.get(j).getOutputSize(), utilizationModel, utilizationModel, utilizationModel);
				System.out.println(cloudlet.toString());
				cloudlet.setUserId(this.brokerId);
				cloudletList.add(cloudlet);
				hostCloudletMap.put(vm.getId(),cloudlet.getCloudletId());
				id++;
			}
		}

		broker.submitCloudletList(cloudletList);


		for (Map.Entry<Integer, Integer> entry : hostCloudletMap.entrySet()) {
			int hostId = entry.getKey();
			int cloudletId = entry.getValue();
			System.out.println("Host ID: " + hostId + " | Cloudlet ID: " + cloudletId);
			broker.bindCloudletToVm(cloudletId,hostId);

		}

	}


	public void ajoutcloudlet(CloudletModel cm ){
		System.out.println("crrespandance entre vm cloudlet ...");
		int id = 0;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet cloudlet = new Cloudlet(id++, cm.getLength(), cm.getPesNumber(), cm.getFileSize(), cm.getOutputSize(), utilizationModel, utilizationModel, utilizationModel);
		System.out.println(cloudlet.toString());
		cloudlet.setUserId(this.brokerId);
		cloudletList.add(cloudlet);
	}

	public List<CloudletInfo> startSim(){
		CloudSim.startSimulation();

		List<Cloudlet> newList = broker.getCloudletReceivedList();
		System.out.println("voici les listes qu'on a arriver a geter form brocker -----------");
		CloudSim.stopSimulation();
		System.out.println(newList.toString());
		return printCloudletList(newList);


	}

	public static Datacenter createDatacenter(String name,DatacenterModel datacenterModel){
		System.out.println("------------------create data centers--------");
		System.out.println(datacenterModel);
		System.out.println("-----------------------------");
		List<Host> hostList = new ArrayList<Host>();
		List<HostModel> hosts = new ArrayList<>();
		hosts = datacenterModel.getHosts();
		List<Integer> mips = new ArrayList<>();
		int hostId=0;
		for (int i = 0 ; i< hosts.size();i++){
			mips = hosts.get(i).getMips();
			List<Pe> peList = new ArrayList<Pe>();
			for (int mip : mips){
				peList.add(new Pe(0, new PeProvisionerSimple(mip)));
			}
			hostList.add(
					new Host(
							hostId++,
							new RamProvisionerSimple(hosts.get(i).getRam()),
							new BwProvisionerSimple(hosts.get(i).getBw()),
							hosts.get(i).getStorage(),
							peList,
							new VmSchedulerSpaceShared(peList)
					)
			);

		}
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.001;	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				datacenterModel.getArch(), datacenterModel.getOs(), datacenterModel.getVmm(), hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
		LinkedList<Storage> storageList = new LinkedList<Storage>();
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static List<CloudletInfo> printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;
		List<CloudletInfo> cloudletInfos = new ArrayList<>();
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			CloudletInfo cloudletInfo = new CloudletInfo();
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");
				cloudletInfo.setCloudletId(cloudlet.getCloudletId());
				cloudletInfo.setStatus("SUCCESS");
				cloudletInfo.setVmId(cloudlet.getVmId());
				cloudletInfo.setDataCenterId(cloudlet.getResourceId());
				cloudletInfo.setTime(cloudlet.getActualCPUTime());
				cloudletInfo.setStartTime(cloudlet.getExecStartTime());
				cloudletInfo.setFinishTime(cloudlet.getFinishTime());
				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime()));
			}
			cloudletInfos.add(cloudletInfo);
		}
		return cloudletInfos;

	}
}
