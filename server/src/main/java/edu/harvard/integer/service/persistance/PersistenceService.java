/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.service.persistance;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.persistence.DataPreLoadFile;
import edu.harvard.integer.common.persistence.PersistenceStepStatusEnum;
import edu.harvard.integer.server.IntegerApplication;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.persistance.dao.persistance.DataPreLoadFileDAO;

/**
 * The persistence service is started when the server starts. This will load any
 * data that needs to be loaded. The data loaded is listed in the
 * DataPreloadData table.
 * 
 * @author David Taylor
 * 
 */
@Singleton
@Startup
@Path("/Database")
@DependsOn(value = { "DistributionService" })
public class PersistenceService extends BaseService implements
		PersistenceServiceInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface persistanceManager;

	@Inject
	DataLoaderInterface dataLoader;

	DataPreLoadFile[] preloads = null;

	/**
	 * All PersistenceService initialization occurs here.
	 */
	@PostConstruct
	public void init() {

		logger.warn("PersistenceServices is starting");

		// Register the application for RESTfull interface
		IntegerApplication.register(this);

		loadPreloads();

	}
	
	private void loadPreloads() {
		logger.info("Loading preload data files");

		DataPreLoadFileDAO dao = persistanceManager.getDataPreLoadFileDAO();

		try {
			preloads = dao.findAll();

			logger.info(showPreloads());

			for (DataPreLoadFile dataPreLoadFile : preloads) {
				if (dataPreLoadFile.getStatus() == null
						|| !PersistenceStepStatusEnum.Loaded
								.equals(dataPreLoadFile.getStatus())) {

					long startTime = System.currentTimeMillis();

					dataLoader.loadDataFile(dataPreLoadFile);

					if (PersistenceStepStatusEnum.Loaded.equals(dataPreLoadFile
							.getStatus())) {
						logger.info("Loaded " + dataPreLoadFile.getDataFile());

						dataPreLoadFile.setTimeToLoad(System
								.currentTimeMillis() - startTime);
						dataPreLoadFile.setErrorMessage(null);
					}

					dao.update(dataPreLoadFile);

				} else
					logger.info("Preload already loaded!" + dataPreLoadFile);
			}

			logger.info(showPreloads());

		} catch (IntegerException e) {
			logger.error("Error loading preload table! " + e.toString());

			e.printStackTrace();
		}
	}

	@Override
	public String showPreloads() {
		StringBuffer b = new StringBuffer();

		b.append("Number of preloads ").append(preloads.length);

		for (DataPreLoadFile dataPreLoadFile : preloads) {
			b.append("\nData Preload: ").append(dataPreLoadFile.getName());
			b.append(" Status ").append(dataPreLoadFile.getStatus());
			b.append(" Time loaded ").append(dataPreLoadFile.getTimeToLoad());
		}

		return b.toString();
	}
}
