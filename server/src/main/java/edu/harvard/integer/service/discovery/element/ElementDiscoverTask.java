/*
 *  Copyright (c) 2014 Harvard University and the persons
 *  identified as authors of the code.  All rights reserved. 
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 * 	.    Redistributions of source code must retain the above copyright
 * 		 notice, this list of conditions and the following disclaimer.
 * 
 * 	.    Redistributions in binary form must reproduce the above copyright
 * 		 notice, this list of conditions and the following disclaimer in the
 * 		 documentation and/or other materials provided with the distribution.
 * 
 * 	.    Neither the name of Harvard University, nor the names of specific
 * 		 contributors, may be used to endorse or promote products derived from
 * 		 this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *      
 */
package edu.harvard.integer.service.discovery.element;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.element.ElementAccessTask;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.access.snmp.SnmpSysInfo;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryParseElementTypeEnum;
import edu.harvard.integer.common.discovery.DiscoveryParseString;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentType;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover;
import edu.harvard.integer.service.discovery.snmp.containment.ContainmentGenerator;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;



/**
 * The Class ElementDiscoverTask is used to discover IP network node.
 *
 * @author dchan
 */
public class ElementDiscoverTask <E extends ElementAccess> extends ElementAccessTask<DiscoverNode> {

	 /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);

	
	private NetworkDiscovery  netDiscover;
	private DiscoverNode discoverNode;
	private SnmpSysInfo sysInfo;
	
	private ServiceElementDiscoveryManagerInterface discMgr;
	private ManagementObjectCapabilityManagerInterface capMgr;
	private ServiceElementAccessManagerInterface accessMgr;
	
	
    public ElementDiscoverTask( NetworkDiscovery netDisc, DiscoverNode node ) throws IntegerException {
		
		super(node.getElementEndPoint());
		
		init( netDisc, node, null );
	}
	
	
    public ElementDiscoverTask( NetworkDiscovery netDisc, 
    		                    DiscoverNode node, SnmpSysInfo sysInfo ) throws IntegerException {
		
		super(node.getElementEndPoint());
		
		init( netDisc, node, sysInfo );
	}
    
    
    private void init( NetworkDiscovery netDisc, 
                       DiscoverNode node, SnmpSysInfo sysInfo ) throws IntegerException {
    	
    	this.netDiscover = netDisc;
		this.discoverNode = node;
		this.sysInfo = sysInfo;	
    }
	
	
	/* 
	 * Discover IP service element in detail.  If "sysInfo" is empty, it needs to get the system group
	 * from the device.
	 * 
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public DiscoverNode call()  {
		
		try {
			discMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
	        capMgr = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
	        accessMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
		    if ( sysInfo == null ) {
		    	
		    	PDU pdu = new PDU();
				pdu.addAll(netDiscover.getTopLevelVBs());
				
				PDU rpdu = SnmpService.instance().getPdu(discoverNode.getElementEndPoint(), pdu);
				sysInfo = new SnmpSysInfo(rpdu);					
		    }
		    OID sysId = new OID(sysInfo.getSysObjectID());
		    SnmpVendorDiscoveryTemplate template = null;
		    
		    try {
		    	 VendorIdentifier identify = discMgr.getVendorIdentifier(sysId.toString());
		    	 if ( identify != null ) {
		    		 template = discMgr.getSnmpVendorDiscoveryTemplateByVendor(identify.getID());
		    	 }
		    }
		    catch (Exception e ) {
		    	
		    	/**
		    	 * That is OK not finding vendor id.
		    	 */
		    	logger.info("********************************* Cannot find vendor information " + sysId.getUnsigned(CommonSnmpOids.vendorSysIdIndex));
		    }
		    
		    logger.info( "Call check template ");
		    /**
		     * It is possible that no template being create for that SysId yet.
		     */
		    if ( template == null ) {
		    	template = new SnmpVendorDiscoveryTemplate();
				VendorIdentifier vendorIdentifier = discMgr.getVendorIdentifier(sysId.toDottedString());
				if (vendorIdentifier != null)
					template.setVendorId(vendorIdentifier.getID());
				else {
					/**
					 * No such vendorIdentifier exist. Create one.  Later on this vendor identifier should be configured to 
					 * associated with the real vendor name.
					 */
					vendorIdentifier = new VendorIdentifier();
					
					int[] oida = sysId.getValue();
					vendorIdentifier.setName(sysId.toString());
					vendorIdentifier.setVendorSubtypeId(sysId.toString());
					vendorIdentifier.setVendorSubtypeName(sysId.toString());
					
					if ( oida.length < CommonSnmpOids.vendorSysIdIndex ) {
						
						vendorIdentifier.setVendorOid(sysId.toString());
						vendorIdentifier.setIdentifier(100000L);
					}
					else {
						
						vendorIdentifier.setIdentifier(sysId.getUnsigned(CommonSnmpOids.vendorSysIdIndex));
						int[] sysOida = new int[CommonSnmpOids.vendorSysIdIndex]; 
						
						for ( int i=0; i<CommonSnmpOids.vendorSysIdIndex; i++ ) {
							sysOida[i] = oida[i];
						}
						OID o = new OID(sysOida);
						vendorIdentifier.setVendorOid(o.toString());
						vendorIdentifier = discMgr.updateVendorIdentifier(vendorIdentifier);
					}
					
					template.setVendorId(vendorIdentifier.getID());
					logger.error("Unable to find vendor identifier for " + sysId.toDottedString());
					
					
				}
				
		    	template.setDescription(sysInfo.getSysDescr());
		    	template = discMgr.updateSnmpVendorDiscoveryTemplate(template);
		    }
		    
		    String firmwareVer = null;
		    String model = null;
		    String softwareVer = null;
		    	    
		    logger.info( "Call check check firmware. ");
		    
		    DiscoveryParseString dps = template.getParseString();
		    if ( dps != null ) {
		    	firmwareVer = dps.parseElement(DiscoveryParseElementTypeEnum.FirmwareVersion, sysInfo.getSysDescr());
		    	model = dps.parseElement(DiscoveryParseElementTypeEnum.Model, sysInfo.getSysDescr());
		    	softwareVer = dps.parseElement(DiscoveryParseElementTypeEnum.SoftwareVersion, sysInfo.getSysDescr());
		    }
		    List<VariableBinding> vbs = new ArrayList<>();
		    if ( firmwareVer == null && template.getFirmware() != null) {
		    	
		    	VariableBinding vb = null;
		    	if ( template.getFirmware().getScalarVB() ) {
		    		vb = new VariableBinding(new OID(template.getFirmware().getOid() + ".0"));
					
		    	}
		    	else {
		    		vb = new VariableBinding(new OID(template.getFirmware().getOid()));
		    	}
		    	vbs.add(vb);
		    }
		    if ( model == null && template.getModel() != null ) {
		    	VariableBinding vb = null;
		    	if ( template.getModel().getScalarVB() ) {
		    		vb = new VariableBinding(new OID(template.getModel().getOid() + ".0"));
					
		    	}
		    	else {
		    		vb = new VariableBinding(new OID(template.getModel().getOid()));
		    	}
		    	vbs.add(vb);
		    }
		    
		    if ( softwareVer == null && template.getSoftwareRevision() != null ) {
		    	VariableBinding vb = null;
		    	if ( template.getSoftwareRevision().getScalarVB() ) {
		    		vb = new VariableBinding(new OID(template.getSoftwareRevision().getOid() + ".0"));
					
		    	}
		    	else {
		    		vb = new VariableBinding(new OID(template.getSoftwareRevision().getOid()));
		    	}
		    	vbs.add(vb);
		    }
		    if ( vbs.size() > 0 ) {
		    	PDU pdu = new PDU();
				pdu.addAll(vbs);
				
				PDU rpdu = SnmpService.instance().getPdu(discoverNode.getElementEndPoint(), pdu);
				for ( VariableBinding vb : rpdu.getVariableBindings() ) {
					
					if ( template.getFirmware() != null && vb.getOid().startsWith(new OID(template.getFirmware().getOid()))) {
					
						firmwareVer = vb.toString();
					}
					else if ( template.getSoftwareRevision() != null && vb.getOid().startsWith(new OID(template.getSoftwareRevision().getOid())) ) {
						
						softwareVer = vb.toString();
					}
					else if ( template.getModel() != null && vb.getOid().startsWith(new OID(template.getModel().getOid()))  ) {
						model = vb.toString();
					}
				}
		    }
		    
		    if ( model == null ) {
		    	if ( sysId.size() >=  CommonSnmpOids.vendorSysIdIndex ) {
		    		model = defineUnknownVendor(sysId.toString()); 
		    	}
		    	else {
		    		model = defineUnknownVendor(sysId.toString());
		    	}
		    }
		    
		    VendorContainmentSelector vs = new VendorContainmentSelector();
		    vs.setFirmware(firmwareVer);
		    vs.setModel(model);
		    vs.setSoftwareVersion(softwareVer);
		    vs.setVendor(defineUnknownVendor(sysId.toString()));
		    
		    ServiceElementType set = null;
		    SnmpContainment sc = null;		    
		    sc = discMgr.getSnmpContainment(vs);
		    
		    if ( sc != null && sc instanceof SnmpServiceElementTypeContainment) {
			    set =  discMgr.getServiceElementTypeById(((SnmpServiceElementTypeContainment) sc).getServiceElementTypeId());
			    discoverNode.setTopServiceElementType(set);
		    }
		    
		    if ( sc == null ) {
		    	
				SnmpContainmentType containmentType = checkContainmentType(discoverNode.getElementEndPoint());  
				
		    	set = new ServiceElementType();
		    	set.addSignatureValue(SignatureTypeEnum.Vendor, defineUnknownVendor(sysId.toString()));
		    	set.addSignatureValue(SignatureTypeEnum.Model , checkContainmentType(discoverNode.getElementEndPoint()).name());
				set.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);
				
				ContainmentGenerator.setUpTopServiceElementProperty(discoverNode.getElementEndPoint(), set, containmentType);
				
				set = capMgr.updateServiceElementType(set);
				discoverNode.setTopServiceElementType(set);
				
				sc = ContainmentGenerator.generator(set, containmentType);
				
				SnmpContainment updateSnmpContainment = capMgr.updateSnmpContainment(sc);
				logger.info("Created SnmpContainment " + updateSnmpContainment.getID());
				
		    }
		    
		    List<ID> cids = set.getUniqueIdentifierCapabilities();
		    List<String>  identifyDefs = new ArrayList<>();
		    if ( cids != null ) {
		    	
		    	for ( ID cid : cids ) {
		    		
		    		List<ServiceElementManagementObject> serviceElmentObjs = capMgr.getManagemntObjectsForCapability(cid);
		    		for ( ServiceElementManagementObject s : serviceElmentObjs ) {
		    			identifyDefs.add(s.getName());
		    		}
		    	}
		    }
		    else {
		    	/**
		    	 * If it is empty, use IP address for now.
		    	 */
		    	identifyDefs.add(NetworkDiscovery.IPIDENTIFY);	    	
		    }
		    discoverNode.setIdentifyDefs(identifyDefs);
		    SnmpServiceElementDiscover discover = DiscoverWorkerFactory.getSnmpServiceElementWorker(sc.getContainmentType());
		    
		    /**
		     * Get uniqueIdentifier to determine if the service element being discovered.
		     */
		    ServiceElement se = new ServiceElement();
		    se.setServiceElementTypeId(set.getID());
		    
		    se.setDescription(sysInfo.getSysDescr());
		    se.setName(sysInfo.getSysName());
		    se.setUpdated(new Date());
		    
		    logger.info("call update service element " + se.getName());
		 
		     if ( discover != null ) {
		 	    	
		 	     discover.findUIDForServiceElement(set, se, discoverNode.getElementEndPoint());
		 	     discover.discoverServiceElementAttribute(discoverNode.getElementEndPoint(), se, set, "0");
		 	}
		  
		    logger.info("call update service element " + se.getName());
		    se = accessMgr.updateServiceElement(se);
		    
		    discoverNode.setAccessElement(se);
		    if ( discover != null ) {
		    	
		    	discover.discover(sc, discoverNode);
		    }
		    return discoverNode;
		}
		catch ( IntegerException e ) {
		
			boolean skipErrorReport = false;
			logger.info("Exception during discovery.  "+ e.getMessage() );
			if ( e.getErrorCode() instanceof NetworkErrorCodes ) {
				
				NetworkErrorCodes nec = (NetworkErrorCodes) e.getErrorCode();
				if ( nec == NetworkErrorCodes.StopByRequest ) {
					skipErrorReport = true;
				}
			}
			if ( !skipErrorReport ) {
				netDiscover.discoverErrorOccur(e.getErrorCode(), e.getMessage() + " Discover Node:" + discoverNode.getIpAddress() );
				
			}
			
		}
		finally {
			netDiscover.discoveredElement(discoverNode, discoverNode.getSubnetId());
		}
		return null;
	}


	
	/**
	 * Check containment type of the device by make SNMP getNext to check
	 * if certain MIB tables are supported.   
	 * 
	 * @param ept
	 * @return
	 * @throws IntegerException
	 */
	public SnmpContainmentType checkContainmentType( ElementEndPoint ept ) throws IntegerException {
		
		SnmpContainmentType sct = SnmpContainmentType.SnmpContainmintList;
		/**
		 * Check MIB on the device for containment information.
		 */
		
		/**
		 * Check if entity MIB exists or not.
		 */
		OID checkOid = new OID(CommonSnmpOids.entPhysicalClass);
		
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(checkOid));
		
		PDU rpdu = SnmpService.instance().getNextPdu(ept, pdu);
		OID returnOid = rpdu.get(0).getOid();
		
		if ( returnOid.startsWith(checkOid) ) {
			return SnmpContainmentType.EntityMib;
		}
		
		/**
		 * Check if host resource MIB exists or not.
		 */
		pdu.clear();
		checkOid = new OID(CommonSnmpOids.hrDeviceEntry);
		pdu.add(new VariableBinding(checkOid));
		
		rpdu = SnmpService.instance().getNextPdu(ept, pdu);
	    returnOid = rpdu.get(0).getOid();
		
		if ( returnOid.startsWith(checkOid) ) {
			return SnmpContainmentType.HostResourcesMib;
		}
		return sct;
	}
	
	
	public String defineUnknownVendor( String vendorOid ) throws IntegerException {
		
		VendorIdentifier vendorIdent = discMgr.getVendorIdentifier(vendorOid);
		if ( vendorIdent != null ) {
			return vendorIdent.getName();
		}
		
		return "Undefine:" + vendorOid;
	}
	
}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    