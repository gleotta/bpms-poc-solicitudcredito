/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cuyum.pocweb.controller;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.kie.api.runtime.process.ProcessInstance;

import com.cuyum.pocweb.service.BpmService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class BpmController {

    @Inject
    private BpmService bpm;
    
    @Inject
    private FacesContext facesContext;
    
    @Inject Logger log;

    public void crearInstancia() throws Exception {
        try {
            ProcessInstance instance = bpm.crearInstancia();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Instancia Creada! (Nº "+instance.getId()+")", "Se ha creado la instancia "+instance.getId()+" con éxito.");
            facesContext.addMessage(null, m);
        } catch (Exception e) {
        	log.error("Error tratando de crear instancia desde controller ", e);
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "No se ha podido crear la instancia nueva del proceso");
            facesContext.addMessage(null, m);
        }
    }
    
    @Produces
    @Named
    public List<ProcessInstance> getProcesos(){
    	return bpm.obtener_instancias_activas();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
