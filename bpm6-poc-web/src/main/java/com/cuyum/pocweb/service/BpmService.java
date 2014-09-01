package com.cuyum.pocweb.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.command.RemoteConfiguration;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

@Stateless
public class BpmService {

	@Inject
    private Logger log;
	
	@Inject
	private Properties properties;

	/* POC */

	//URL de servidor bpm6 de cuyum
	private String deploymentUrlStr;

	private String deploymentId;
    private String processDefinitionId;

    private String userId;
    private String password;
    
    private KieSession ksession;
        
    @PostConstruct
    private void init(){
    	deploymentUrlStr = properties.getProperty("bpms.url");

    	deploymentId = properties.getProperty("deployment.id");
        processDefinitionId = "altacredito.altacredito";

        userId = properties.getProperty("user"); //usuario con roles: "revisor", "analista" o "admin"  ;
        password = properties.getProperty("password");
    	
    	if(deploymentUrlStr!=null){
    		
    		URL deploymentUrl;
			try {
				deploymentUrl = new URL(deploymentUrlStr);
				// Creamos la sesion REST
				//creamos factoria con timeout
				RemoteConfiguration configuration = new RemoteConfiguration(deploymentId, deploymentUrl, userId, password,30);
				RuntimeEngine engine = new RemoteRuntimeEngine(configuration);

				//creamos factoria sin timeout
  		//RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(deploymentId, deploymentUrl, userId, password);
  		
  		//engine = restSessionFactory.newRuntimeEngine();
				//engine.en
				
				ksession = engine.getKieSession();
			} catch (MalformedURLException e) {
				log.info("Error de formaci\u00F3n de URL");
			}
    	}
    }
    
        
    public List<ProcessInstance> obtener_instancias_activas(){
   	
    	List<ProcessInstance> instances = new ArrayList<ProcessInstance>();
    	instances.addAll(ksession.getProcessInstances());
    	
    	if(instances.isEmpty()){
    		log.info("No active instances");
    		return instances;
    	}
    	for (ProcessInstance processInstance : instances) {
    		log.info("================================");
    		log.info("Process: "+processInstance.getProcessId());
    		log.info("Instance: "+processInstance.getId());
    		log.info("State:"+processInstance.getState());
    		log.info("================================");
		}
    	return instances;
    }
    
    public ProcessInstance crearInstancia() {
    	//Creamos una bolsa de parametros a enviar para iniciar la instancia
    	//estos parametros son necesarios para que el proceso corra
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idSolicitud", System.currentTimeMillis());
		params.put("nombreCliente", "Pepe Argento");
		params.put("dni", "12345678");
		params.put("fechaNacimiento", "1/1/1975");
		params.put("observaciones", "");
		params.put("aprobada", "false");
		
		
		//iniciamos una instancia del proceso
		ProcessInstance processInstance = ksession.startProcess(processDefinitionId, params);
		log.info("Started process instance: " + processInstance + " "
				+ (processInstance == null ? "" : processInstance.getId()));
		
		return processInstance;

    }
    
    
}