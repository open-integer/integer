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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementFields;
import edu.harvard.integer.service.persistance.dao.BaseDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ManagementObjectValueDAO;

/**
 * The DAO is responsible for persisting the ServiceElement. All queries will be
 * done in this class.
 * 
 * @author David Taylor
 * 
 */
public class ServiceElementFieldsDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public ServiceElementFieldsDAO(EntityManager entityManger, Logger logger, Class<? extends BaseEntity> classType) {
		super(entityManger, logger, classType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard
	 * .integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {

		ServiceElementFields serviceElement = (ServiceElementFields) entity;

		if (serviceElement.getValues() != null) {
			ServiceElementProtocolInstanceIdentifierDAO seiDAO = new ServiceElementProtocolInstanceIdentifierDAO(
					getEntityManager(), getLogger());
			
			serviceElement.setValues(seiDAO.update(serviceElement.getValues()));

		}

		if (serviceElement.getAttributeValues() != null) {
			ManagementObjectValueDAO valueDao = new ManagementObjectValueDAO(
					getEntityManager(), getLogger());
			serviceElement.setAttributeValues(valueDao.update(serviceElement
					.getAttributeValues()));
		}

		if (serviceElement.getParentIds() != null
				&& serviceElement.getParentIds().size() > 0) {
			ServiceElement parent = findById(serviceElement.getParentIds().get(
					0));
			if (parent != null)
				parent.setHasChildren(true);
			update(parent);
		}

		if (serviceElement.getAssociations() != null) {
			ServiceElementAssociationDAO dao = new ServiceElementAssociationDAO(
					getEntityManager(), getLogger());
			serviceElement.setAssociations(dao.update(serviceElement
					.getAssociations()));
		}
		super.preSave(entity);
	}

	/**
	 * Find the top level service elements. The service elements that do not
	 * have parents. Ex. Device
	 * 
	 * @param object
	 * @return
	 */
	public ServiceElementFields[] findTopLevelServiceElements() {

		StringBuffer b = new StringBuffer();

		String tableName = getTableName();
		
		b.append("select se.* ").append('\n');
		b.append("from ").append(tableName).append(" se ").append('\n');
		b.append(
				" where not exists (select * from  ").append(tableName).append("_parentIds sep ")
				.append('\n');
		b.append("where sep.").append(tableName).append("_identifier = se.identifier)");

		Query query = getEntityManager().createNativeQuery(b.toString(),
				getPersistentClass());

		@SuppressWarnings("unchecked")
		List<ServiceElementFields> resultList = query.getResultList();

		return (ServiceElementFields[]) resultList
				.toArray(new ServiceElementFields[resultList.size()]);

	}

	/**
	 * Get the list of Service elements that are children of the parent service
	 * element ID.
	 * 
	 * @param ID
	 *            . ID of the parent service element.
	 * @return ServiceElement[] of child service elements for the given parent
	 *         id.
	 */
	public ServiceElementFields[] findByParentId(ID parent) {

		StringBuffer b = new StringBuffer();
		String tableName = getTableName();
		
		b.append("select se.* ").append('\n');
		b.append("from ").append(tableName).append(" se ").append('\n');
		b.append(
				"   join ").append(tableName).append("_parentIds sep on (sep.").append(tableName).append("_identifier = se.identifier) ")
				.append('\n');
		b.append("where sep.identifier = :parent");

		Query query = getEntityManager().createNativeQuery(b.toString(),
				getPersistentClass());
		query.setParameter("parent", parent.getIdentifier());

		@SuppressWarnings("unchecked")
		List<ServiceElementFields> resultList = query.getResultList();

		return (ServiceElementFields[]) resultList
				.toArray(new ServiceElementFields[resultList.size()]);
	}

	
	/**
	 * Get the list of Service elements that are children of the parent service
	 * element ID.
	 * 
	 * @param ID
	 *            . ID of the parent service element.
	 * @return ServiceElement[] of child service elements for the given parent
	 *         id.
	 */
	public ServiceElementFields[] findByParentIdAndName(ID parent, String name) {

		String tableName = getTableName();
		StringBuffer b = new StringBuffer();

		b.append("select se.* ").append('\n');
		b.append("from ").append(tableName).append(" se ").append('\n');
		b.append(
				"   join ").append(tableName).append("_parentIds sep on (sep.").append(tableName).append("_identifier = se.identifier) ")
				.append('\n');
		b.append("where sep.identifier = :parent");
		b.append("  and se.name = :name");

		Query query = getEntityManager().createNativeQuery(b.toString(),
				getPersistentClass());
		
		query.setParameter("parent", parent.getIdentifier());
		query.setParameter("name", name);
		
		@SuppressWarnings("unchecked")
		List<ServiceElementFields> resultList = query.getResultList();

		return (ServiceElementFields[]) resultList
				.toArray(new ServiceElementFields[resultList.size()]);
	}
	
	/**
	 * @param parentId
	 * @param value
	 * @return
	 */
	public ServiceElementFields findByIdAndValue(ID parentId,
			ID serviceElementTypeId,
			@SuppressWarnings("rawtypes") ManagementObjectValue value) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<ServiceElementFields> query = criteriaBuilder
				.createQuery(ServiceElementFields.class);

		Root<ServiceElementFields> from = query.from(ServiceElementFields.class);

//		Join<ServiceElement, ManagementObjectStringValue> join = from
//				.join("valueId");

		// if (value instanceof ManagementObjectStringValue)
		// from.join(ManagementObjectStringValue.class);

		ParameterExpression<Long> idParam = criteriaBuilder
				.parameter(Long.class);

		ParameterExpression<ID> serviceElementTypeParam = criteriaBuilder
				.parameter(ID.class);

		query.select(from).where(
				criteriaBuilder.and(criteriaBuilder.equal(
						from.get("identifier"), idParam), criteriaBuilder
						.equal(from.get("serviceElementTypeId"),
								serviceElementTypeParam)));

		TypedQuery<ServiceElementFields> typeQuery = getEntityManager().createQuery(
				query);
		typeQuery.setParameter(idParam, parentId.getIdentifier());
		typeQuery.setParameter(serviceElementTypeParam, serviceElementTypeId);

		@SuppressWarnings("unused")
		List<ServiceElementFields> resultList = typeQuery.getResultList();

		return null;
	}

	/**
	 * @param selection
	 * @return
	 */
	public ServiceElementFields[] findBySelection(Selection selection) {

		if (selection.getFilters() == null)
			return new ServiceElementFields[0];

		String tableName = getTableName();
		StringBuffer b = new StringBuffer();

		b.append("select se.* ").append('\n');
		b.append("from ").append(tableName).append(" se ").append('\n');
		b.append(
				"    join ").append(tableName).append("_ManagementObjectValue seav on (seav.").append(tableName).append("_identifier = se.identifier) ")
				.append('\n');
		b.append(
				"    join ManagementObjectValue mov on (mov.identifier = seav.attributeValues_identifier) ")
				.append('\n');
		b.append(
				"    join ServiceElementManagementObject mo on (mo.identifier = mov.managementObjectId) ")
				.append('\n');
		b.append("    join Capability c on (c.identifier = mo.capabilityId) ")
				.append('\n');
		b.append(
				"    join Mechanism_capabilities mc on (mc.identifier = c.identifier) ")
				.append('\n');
		b.append(
				"    join Mechanism m on (m.identifier = mc.Mechanism_identifier) ")
				.append('\n');
		b.append(
				"    join Technology_mechanisims tm on (tm.identifier = m.identifier) ")
				.append('\n');
		b.append(
				"    join Technology t on (t.identifier = tm.Technology_identifier) ")
				.append('\n');
		b.append("where ").append('\n');

		boolean addedOne = false;

		for (Filter filter : selection.getFilters()) {
			if (filter.getTechnologies() != null)
				addedOne = addFilterNodeRestriction("t.name",
						filter.getTechnologies(), addedOne, b, false);

			if (filter.getLinkTechnologies() != null)
				addedOne = addFilterNodeRestriction("t.name",
						filter.getLinkTechnologies(), addedOne, b, false);

			if (addedOne)
				b.append(")").append('\n');
			else
				b.append(" 1 = 1 ").append('\n');

			if (filter.getCategories() != null
					&& filter.getCategories().size() > 0) {

				if (filter.getTechnologies() != null
						|| filter.getLinkTechnologies() != null)
					b.append('\n').append(" union ").append('\n');

				b.append("select se.* ").append('\n');
				b.append("from ServiceElement se ").append('\n');
				b.append(
						"  join ServiceElementType selt on (selt.identifier = se.serviceElementTypeId) ")
						.append('\n');
				b.append(
						"  join Category c on (c.identifier = selt.category_identifier) ")
						.append('\n');
				b.append("where ").append('\n');

				addedOne = false;
				addFilterNodeRestriction("c.name", filter.getCategories(),
						addedOne, b, false);
				if (!addedOne)
					b.append(" 1 = 1 ").append('\n');

				b.append('\n').append(" union ").append('\n');

				b.append("select se.* ").append('\n');
				b.append("from ServiceElement se ").append('\n');
				b.append(
						"   join ServiceElementType selt on (selt.identifier = se.serviceElementTypeId) ")
						.append('\n');
				b.append(
						"   join Category c on (c.identifier = selt.category_identifier) ")
						.append('\n');
				b.append(
						"   join Category_childIds cc on (cc.Category_identifier = c.identifier) ")
						.append('\n');
				b.append("where ").append('\n');
				addedOne = false;
				addFilterNodeRestriction("cc.name", filter.getCategories(),
						addedOne, b, false);
				if (!addedOne)
					b.append(" 1 = 1 ").append('\n');

			}

//			if (filter.getLocations() != null
//					&& filter.getLocations().size() > 0) {
//				b.append('\n').append(" union ").append('\n');
//
//				b.append("select se.* ").append('\n');
//				b.append("from ServiceElement se ").append('\n');
//
//				addedOne = false;
//				for (ID locationId : filter.getLocations()) {
//					if (addedOne)
//						b.append(" and ");
//					else
//						addedOne = true;
//
//					b.append(" se.primaryLocationId == ")
//							.append(locationId.getIdentifier()).append('\n');
//				}
//			}
		}

		b.append(" group by se.identifier");

		Query createQuery = getEntityManager().createNativeQuery(b.toString(),
				ServiceElement.class);

		@SuppressWarnings("unchecked")
		List<ServiceElementFields> resultList = createQuery.getResultList();

		return (ServiceElementFields[]) resultList
				.toArray(new ServiceElementFields[resultList.size()]);
	}

	private boolean addFilterNodeRestriction(String fieldName,
			List<FilterNode> techNodes, boolean addedOne, StringBuffer b,
			boolean isSelected) {
		for (FilterNode filterNode : techNodes) {
			if (filterNode == null) {
				getLogger().warn(
						"FilterNode is null. Adding nodes for " + fieldName
								+ " SQL to this point " + b.toString());
				continue;
			}

			if (isSelected || Boolean.TRUE.equals(filterNode.getSelected())) {
				if (addedOne)
					b.append(" or ");
				else {
					b.append("( ");
					addedOne = true;
				}

				b.append(" ").append(fieldName).append(" = '")
						.append(filterNode.getItemId().getName()).append("'");

				if (filterNode.getChildren() != null)
					addFilterNodeRestriction(fieldName,
							filterNode.getChildren(), addedOne, b, true);
			}
		}
		return addedOne;
	}

	/**
	 * @param name
	 * @return
	 */
	public ServiceElementFields findByName(String name) {

		return (ServiceElementFields) findByStringField(name, "name", getPersistentClass());
	}

	/**
	 * @param childId
	 * @throws IntegerException 
	 */
	public ServiceElementFields findParent(ID childId) throws IntegerException {
		ServiceElementFields child = findById(childId);
		
		if (child.getParentIds()  != null && child.getParentIds().size() > 0)
			return findById(child.getParentIds().get(0));
	
		return null;
	}


}
