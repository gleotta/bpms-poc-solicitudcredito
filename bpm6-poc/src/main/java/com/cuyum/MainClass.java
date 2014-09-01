package com.cuyum;
/**
 * 
 */


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.model.TaskSummary;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		
		if(args.length < 2 || args.length > 4)
        {
            System.out.println("Usage: java -jar bpm-poc.jar username password [http://localhost:8080/business-central [com.cuyum.jbpm:poc:1.1]]");
            return;
        }
		PropertyConfigurator.configure("log4j.properties");

//		String url = "http://localhost:8080/business-central";
//		String deploymentId = "com.cuyum.jbpm:poc:1.0";
//	    String processDefinitionId = "altacredito.altacredito";
//	    //String user = "juan"; //usuario con rol: "recepcion" pass="j123456$";
//	    //String user = "jose"; //usuario con rol: "analista" pass="j123456$";
//	    String user = "german"; //usuario con roles: "recepcion", "analista" pass="a123456$";
//	    String password = "german123$";
		
        String userId = args[0];
        String password = args[1];
        String applicationContext = null;
        if(args.length > 2)
        	applicationContext = args[2];
        else
            applicationContext = "http://localhost:8080/business-central";
        String deploymentId;
        if(args.length > 3)
            deploymentId = args[3];
        else
            deploymentId = "com.cuyum.jbpm:poc:1.1";
	    String processDefinitionId = "altacredito.altacredito";
		/*--------------------------------------------*/
		//iniciamos la prueba de concepto del proceso "registro-operadores"
		BpmService poc = BpmService.createInstance(applicationContext, deploymentId, processDefinitionId, userId, password);
		
		//creamos la instancia
		ProcessInstance instance = poc.crearInstancia();
		
		
		log.info("Estado de la instancia del proceso: "+instance.getState());
		List<TaskSummary> tareas = new ArrayList<TaskSummary>(); 
		tareas = poc.obtenerTareasForUser(userId,instance.getId());
		poc.verificarSolicitudPorRecepcion(tareas.get(0));
		
		tareas = poc.obtenerTareasForUser(userId, instance.getId());
		poc.aprobarSolictudPorAnalista(tareas.get(0));
		/*--------------------------------------------*/
		
		
	}

}
