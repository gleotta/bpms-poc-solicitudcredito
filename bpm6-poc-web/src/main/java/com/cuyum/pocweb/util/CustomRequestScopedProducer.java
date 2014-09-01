/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cuyum.pocweb.util;


import java.io.IOException;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

@RequestScoped
public class CustomRequestScopedProducer {
    
	@Produces
	@PersistenceContext
	private EntityManager em;
	
    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @Produces
    @Singleton
    public Properties getPropiedades() {
    
    	Properties ret =  new Properties();
    	try {
    		System.out.println("Cargando propiedades del bpms");
			ret.load(this.getClass().getClassLoader().getResourceAsStream("bpms.properties"));
			ret.list(System.out);
			return ret;
    	} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	
    }

}
