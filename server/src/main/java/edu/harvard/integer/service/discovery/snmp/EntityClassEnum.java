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
package edu.harvard.integer.service.discovery.snmp;


/**
 * The Enum associates with entity physical MIB table class column.
 *
 * @author dchan
 */
public enum EntityClassEnum {
		
		/** The other. */
		other(1),
		
		/** The unknown. */
		unknown(2),
		
		/** The chassis. */
		chassis(3),
		
		/** The backplane. */
		backplane(4),
		
		/** The container. */
		container(5),
		
		/** The power supply. */
		powerSupply(6),
		
		/** The fan. */
		fan(7),
		
		/** The sensor. */
		sensor(8),
		
		/** The module. */
		module(9),
		
		/** The port. */
		port(10),
		
		/** The stack. */
		stack(11),
		
		/** The cpu. */
		cpu(12);
		
		/** The value. */
		private final int value;

	    /**
    	 * Instantiates a new entity class enum.
    	 *
    	 * @param value the value
    	 */
    	private EntityClassEnum(int value) {
	        this.value = value;
	    }

	    /**
    	 * Gets the value.
    	 *
    	 * @return the value
    	 */
    	public int getValue() {
	        return value;
	    }
	    
	    /**
    	 * Value of.
    	 *
    	 * @param classi the classi
    	 * @return the entity class enum
    	 */
    	public static EntityClassEnum valueOf(int classi) {
	        for (EntityClassEnum ec : EntityClassEnum.values()) {
	            if (ec.value == classi) return ec;
	        } 
	        throw new IllegalArgumentException("Entity Class not found.");
	    }
	
}
