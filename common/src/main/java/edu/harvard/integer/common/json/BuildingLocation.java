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

package edu.harvard.integer.common.json;

/**
 * @author David Taylor
 *
 */
public class BuildingLocation {

	private Integer id = null;
	private String feature_type = null;
	private String bld_num = null;
	private String bld_root = null;
	private String search_string = null;
	private String match_string = null;
	private String lat = null;
	private String lon = null;
	private String sp_x = null;
	private String sp_y = null;
	
	
//	"identifier": "id",
//	"label": "match_string",
//	"items": 
//	[
//		{
//			"id": "2144",
//			"feature_type": "Harvard Buildings",
//			"bld_num": "06204",
//			"bld_root": "06204",
//			"search_string": "",
//			"match_string": "0 Arrow Street",
//			"lat": "42.370897",
//			"lon": "-71.114478",
//			"sp_x": "760344.997",
//			"sp_y": "2960416.656"
//		},
	/**
	 * 
	 */
	public BuildingLocation() {
		
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the feature_type
	 */
	public String getFeature_type() {
		return feature_type;
	}


	/**
	 * @param feature_type the feature_type to set
	 */
	public void setFeature_type(String feature_type) {
		this.feature_type = feature_type;
	}


	/**
	 * @return the bld_num
	 */
	public String getBld_num() {
		return bld_num;
	}


	/**
	 * @param bld_num the bld_num to set
	 */
	public void setBld_num(String bld_num) {
		this.bld_num = bld_num;
	}


	/**
	 * @return the bld_root
	 */
	public String getBld_root() {
		return bld_root;
	}


	/**
	 * @param bld_root the bld_root to set
	 */
	public void setBld_root(String bld_root) {
		this.bld_root = bld_root;
	}


	/**
	 * @return the search_string
	 */
	public String getSearch_string() {
		return search_string;
	}


	/**
	 * @param search_string the search_string to set
	 */
	public void setSearch_string(String search_string) {
		this.search_string = search_string;
	}


	/**
	 * @return the match_string
	 */
	public String getMatch_string() {
		return match_string;
	}


	/**
	 * @param match_string the match_string to set
	 */
	public void setMatch_string(String match_string) {
		this.match_string = match_string;
	}


	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}


	/**
	 * @param lat the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}


	/**
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}


	/**
	 * @param lon the lon to set
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}


	/**
	 * @return the sp_x
	 */
	public String getSp_x() {
		return sp_x;
	}


	/**
	 * @param sp_x the sp_x to set
	 */
	public void setSp_x(String sp_x) {
		this.sp_x = sp_x;
	}


	/**
	 * @return the sp_y
	 */
	public String getSp_y() {
		return sp_y;
	}


	/**
	 * @param sp_y the sp_y to set
	 */
	public void setSp_y(String sp_y) {
		this.sp_y = sp_y;
	}

	
}
