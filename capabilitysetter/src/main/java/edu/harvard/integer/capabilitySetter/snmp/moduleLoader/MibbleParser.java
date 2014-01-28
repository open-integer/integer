package edu.harvard.integer.capabilitySetter.snmp.moduleLoader;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import edu.harvard.integer.capabilitySetter.CapabilitySetterException;
import edu.harvard.integer.capabilitySetter.CapabilitySetterMain;
import edu.harvard.integer.capabilitySetter.ModuleLoadingLog;
import edu.harvard.integer.capabilitySetter.ModuleLoadingLog.ErrorTypeE;
import edu.harvard.integer.capabilitySetter.SNMPModuleCache;
import edu.harvard.integer.capabilitySetter.snmp.MibParser;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.MaxAccess;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPTable;



public class MibbleParser implements MibParser{
	
	public static String MibResourceDir = "mibs/ietf/";
	private HashMap<String, SNMPModuleCache>  snmpCache = new HashMap<>();
	
	
	private File mibLocation;	
	private static MibbleParser source = null;
	
	private MibbleParser() throws CapabilitySetterException {
		
		System.out.println("In MibSources constructor");		
		mibLocation =  new File(System.getProperty(CapabilitySetterMain.MIBFILELOCATON));
		if ( !mibLocation.isDirectory() ) {
			throw new CapabilitySetterException("No MIB Directory defined." );
		}
		
		init();
	}
	
	private MibLoader mibLoader = new MibLoader();
	
	
	@SuppressWarnings("unchecked")
	public void init() throws CapabilitySetterException {
		
		mibLoader.removeAllDirs();		
		/**
		 * MIBS is hold on under two directories.  One is called ietf which holds common MIBS
		 * another one is called vendor which holds vendor MIBs.
		 */
		File[] files = mibLocation.listFiles();
		try {
			
            for ( File f : files ) {
		    	
				if ( f.getName().equals(CapabilitySetterMain.IETFMIB) 
						                || f.getName().equals(CapabilitySetterMain.IANA)) {
					mibLoader.addDir(f);
				}	    	
		    }
			for ( File f : files ) {
		    	
				if ( f.getName().equals(CapabilitySetterMain.IETFMIB)) {
					loadDirMIBs(f);
				}	    	
		    }
	        for ( File f : files ) {
		    	
				if ( f.getName().equals(CapabilitySetterMain.VENDORMIB)) {
					
					mibLoader.addDir(f);
					loadDirMIBs(f);
				}	   	
		    }
		}
		catch ( Exception e ) {
			
			e.printStackTrace();
			throw new CapabilitySetterException(e.getClass().getName(), e.getMessage());
		}
	    Mib[] mibs = mibLoader.getAllMibs();
	    for ( Mib mib : mibs ) {
	    	
	    	List<SNMPTable>  tblList = new ArrayList<>();
	    	List<SNMP>  scaleList = new ArrayList<>();
	    	
	    	Collection<MibSymbol> ss = mib.getAllSymbols();
	    	for (MibSymbol s : ss)
	    	{
	    		if (s instanceof MibValueSymbol)
	    		{
	    			MibValueSymbol vs = (MibValueSymbol) s;
	    			/**
	    			 * Search for node which is mib "entry" and create an table on
	    			 * the repository
	    			 * 
	    			 */
	    			if (vs.isTableRow())
	    			{
	    				SNMPTable snmpTbl = new SNMPTable();
	    				snmpTbl.setIndex(new ArrayList<SNMP>());
	    					
	    				snmpTbl.setName(vs.getName());
	    				snmpTbl.oid = vs.getValue().toString();
	    				
	    				MibValueSymbol[] avss = vs.getChildren();
	    				for (MibValueSymbol avs : avss)
	    				{
	    					/**
	    					 * Only card if it is accessable.
	    					 */
	    					SnmpObjectType snmpType = (SnmpObjectType) avs.getType();
	    					if (snmpType.getAccess().canRead() || snmpType.getAccess().canRead() )
	    					{
	    						ObjectIdentifierValue obj = (ObjectIdentifierValue) vs.getValue();
	    						SNMP snmp = createSNMP( obj, snmpType );
	    					    snmpTbl.getIndex().add(snmp);
	    					}
	    				}
	    				tblList.add(snmpTbl);
	    			} 
	    			else if (vs.isScalar())
	    			{
	    				SnmpObjectType snmpType = (SnmpObjectType) vs.getType();
	    				if ( snmpType.getAccess().canRead() || snmpType.getAccess().canWrite() ) 
	    				{
	    					ObjectIdentifierValue obj = (ObjectIdentifierValue) vs.getValue();
	    					SNMP snmp = createSNMP( obj, snmpType );
	    					
	    					scaleList.add(snmp);				    	
	    				}
	    			}
	    		}
	    	}
	    	if ( tblList.size() > 0 || scaleList.size() > 0 ) {
	    		
	    		SNMPModuleCache moduleCache = snmpCache.get(mib.getName());
		    	if ( moduleCache == null ) {
		    		
		    		SNMPModule snmpModule = new SNMPModule();
		    		
		    		MibValue ident = null;
		    		snmpModule.setName(mib.getName());
		    		if ( mib.getRootSymbol() != null ) {
		    			ident = mib.getRootSymbol().getValue();
		    		}
		    		else {	    		
		    			continue;
		    		}
		    		
		    		String oid = ident.toObject().toString();
		    		snmpModule.setOid(oid);
		    		
		    		moduleCache = new SNMPModuleCache(snmpModule);
		    		snmpCache.put(mib.getName(), moduleCache);
		    	}
		    	
		    	for ( SNMPTable tbl : tblList ) {
		    		moduleCache.getTbllist().add(tbl);
		    	}
		    	for ( SNMP snmp : scaleList ) {
		    		moduleCache.getScalelist().add(snmp);
		    	}
	    	}
	    }
	    
	    Collection<SNMPModuleCache> modules =  snmpCache.values();
	    for ( SNMPModuleCache c : modules ) 
	    {
	    	System.out.println("Add module " + c.getName() + " scale size " + c.getScalelist().size() + " table size " + c.getTbllist().size() );
	    }
	    
	    System.out.println("Module count " + mibs.length );	    
	}
	
	
	private SNMP createSNMP( ObjectIdentifierValue obj, SnmpObjectType snmpType ) {
		
		MibType oType = (MibType) obj.getSymbol().getType();
		
	    SNMP snmp = new SNMP();
	    snmp.displayName = obj.getName();
	    snmp.oid = obj.toObject().toString();
	    snmp.description = snmpType.getDescription();
	     					    
	    MaxAccess access = null;
	    if ( snmpType.getAccess() == SnmpAccess.READ_ONLY ) {
	    	access = MaxAccess.ReadOnly;
	    }
	    else if ( snmpType.getAccess() == SnmpAccess.READ_CREATE ) {
	    	access = MaxAccess.ReadWrite;
	    }
	    else if ( snmpType.getAccess() == SnmpAccess.READ_WRITE ) {
	    	access = MaxAccess.ReadWrite;
	    }
	    else if ( snmpType.getAccess() == SnmpAccess.WRITE_ONLY ) {
	    	access = MaxAccess.WriteOnly;
	    }
	    snmp.maxAccess = access;
	    
	    if ( oType instanceof SnmpObjectType ) {
	    	snmp.units = ((SnmpObjectType) oType).getSyntax().getName();
	    }
	    else {
	    	System.out.println("Not an snmpObjectType .... ");
	    }
	    
	    return snmp;
	}
	
	
	
	
	private void loadDirMIBs( File dirf ) throws Exception {
		
		for ( File f : dirf.listFiles() ) {
			
			if ( f.isHidden() ) {
				continue;
			}
			System.out.println("Load file " + f.getName());
 	    	try {
				mibLoader.load(f);
			} 
 	    	catch (IOException e) {
				throw e;
			} 
 	    	catch (MibLoaderException e) {
 	    		
 	    		e.getLog().printTo(System.out);
			}
 	    }
	}
	
	
	public static MibbleParser getInstance() {
		
		synchronized (MibbleParser.class) {
			if ( source == null ) {
				try {
					source = new MibbleParser();
				} 
				catch (CapabilitySetterException e) {
					
					/*
					 * Log it and move on.
					 */			
					e.printStackTrace();
				}
			}
		}
		return source;		
	}
	
	
	
	@Override
	public List<String>  getSupportModuleNames() throws CapabilitySetterException {
		
	    ClassLoader  loader = mibLoader.getClass().getClassLoader();
	    CodeSource src = mibLoader.getClass().getProtectionDomain().getCodeSource();
	    List<String> list = new ArrayList<String>();

	    try {
	    	if( src != null ) {
		        URL jar = src.getLocation();
		        ZipInputStream zip = new ZipInputStream( jar.openStream());
		        ZipEntry ze = null;

		        while( ( ze = zip.getNextEntry() ) != null ) {
		            String entryName = ze.getName();
		            
		            if( entryName.startsWith(MibResourceDir) && loader.getResource(entryName) != null ) {
		            	
		            	String mibName = entryName.replaceAll(MibResourceDir, "");
		            	if ( mibName.length() > 0 ) {
		            		 list.add(mibName);
		            	}
		            }
		        }
		     }	
	     }
	     catch ( IOException e ) {
	    	 
	     }
		 return list;
	}

	
	@SuppressWarnings("unchecked")
	public String[]  getEntryNames( Mib mib ) {
		
		Collection<MibSymbol> ss = mib.getAllSymbols();
		for (MibSymbol s : ss)
		{
			if (s instanceof MibValueSymbol)
			{
				MibValueSymbol vs = (MibValueSymbol) s;
				/**
				 * Search for node which is mib "entry" and create an table on
				 * the repository
				 * 
				 */
				if (vs.isTableRow())
				{
					String entryName = vs.getName();
				} 
				else if (vs.isScalar())
				{
				}
			}
		}
		return null;
	}
		
	
	private Mib getMib( String moduleName ) throws Exception {
		
		Mib m = mibLoader.getMib(moduleName);
		if ( m == null ) {
			m = mibLoader.load(moduleName);
		}		
		return m;
	}


	@Override
	public void loadModule(File moduleFile) throws CapabilitySetterException {
		
		try {
			mibLoader.load(moduleFile);		    
		} 
		catch (IOException | MibLoaderException e) {
			
			throw new CapabilitySetterException(e.getClass().getName(), e.getMessage());
		}
	}


	@Override
	public HashMap<String, SNMP> getLoadedSNMPObjects() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<String> getSupportCommonModuleNames()
			throws CapabilitySetterException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ModuleLoadingLog> importMIB(List<MIBImportInfo> mibinfos) throws CapabilitySetterException {
	
		File[] files = mibLocation.listFiles();
		mibLoader.removeAllDirs();	
		try {
			
            for ( File f : files ) {
		    	
				if ( f.getName().equals(CapabilitySetterMain.IETFMIB) 
						                || f.getName().equals(CapabilitySetterMain.IANA) 
						                || f.getName().equals(CapabilitySetterMain.VENDORMIB)  ) {
					mibLoader.addDir(f);
				}	    	
		    }
    		
    		List<ModuleLoadingLog>  logs = new ArrayList<>();
    		for ( MIBImportInfo mInfo : mibinfos ) {
    			
    			BufferedReader bufReader = new BufferedReader(new StringReader(mInfo.getMib()));
    			/**
    			 * If the vendor information is null, it is assume it is a common MIB.
    			 */
    			String mibName = null;
    			String line=null;
    			while( (line=bufReader.readLine()) != null )
    			{
                    if ( line.indexOf("DEFINITIONS") >= 0 && line.indexOf("BEGIN") >= 0 && line.indexOf("::") >= 0 ) {
                    	String[] contents = line.split(" ");
                    	
                    	mibName = contents[0];
                    	break;
                    }
    			}
    			if ( mibName == null || mibName.equals("") ) {
    			 
    				ModuleLoadingLog log = new ModuleLoadingLog(mInfo);
    				log.setErr(ErrorTypeE.OtherError);
    				log.setErrDescription("This file seems to be not a MIB file");
    				
    				continue;
    			}   		
    			
    			
    		}
    		return logs;
    		
		}
		catch ( Exception e ) {
			
			// If an exception occurs in here.  Throw the exception before that is one need to take care of before
			// the parser error.
			String message = null;
			if ( e.getMessage() == null ) {
	
			}
			else {				
				message = e.getMessage();
			}
			throw new CapabilitySetterException(e.getClass().getName(), message);
		}
	}
	

}
