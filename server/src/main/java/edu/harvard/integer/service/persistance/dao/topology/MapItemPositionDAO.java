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

package edu.harvard.integer.service.persistance.dao.topology;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.MapItemPosition;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 */
public class MapItemPositionDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public MapItemPositionDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, MapItemPosition.class);

	}

	/**
	 * @param mapId
	 * @return
	 */
	public MapItemPosition[] findByMapId(ID mapId) {
		
		return findByIDField(mapId, "mapId", MapItemPosition.class);
	}

	/**
	 * @param mapId
	 * @param itemId
	 * @return
	 */
	public MapItemPosition findByMapIdAndItemId(ID mapId, ID itemId) {
		if (mapId == null || itemId == null) {
			getLogger().error("MapId ItemId are required fields!! Unable to lookup MapItemPostiong for map " + mapId + " item " + itemId);
			return null;
		}
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<MapItemPosition> query = criteriaBuilder.createQuery(MapItemPosition.class);

		Root<MapItemPosition> from = query.from(MapItemPosition.class);
		query.select(from);

		ParameterExpression<ID> mapIdField = criteriaBuilder.parameter(ID.class);
		ParameterExpression<ID> itemIdField = criteriaBuilder.parameter(ID.class);
		query.select(from).where(criteriaBuilder.and(
				criteriaBuilder.equal(from.get("mapId"), mapIdField),
				criteriaBuilder.equal(from.get("itemId"), itemIdField)));

		TypedQuery<MapItemPosition> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(mapIdField, mapId);
		typeQuery.setParameter(itemIdField, itemId);

		List<MapItemPosition> resultList = typeQuery.getResultList();

		return returnFirst(resultList);
	}

	
}
