package com.cuyum;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.command.RemoteConfiguration;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

public class BpmService {

    private static Logger log = Logger.getLogger(BpmService.class);

	/* POC */

	//URL de servidor bpm6 de cuyum
	private String deploymentUrlStr;

	private String deploymentId;
    private String processDefinitionId;

    private String userId;
    private String password;
    
    private RuntimeEngine engine;
    private KieSession session;
    private TaskService taskService;
    
    
    public static BpmService createInstance(String url, String deploymentUnitId, String processDefinitionId, String user, String pass) {
    	BpmService ret = new BpmService(url, deploymentUnitId, processDefinitionId, user, pass);
    	ret.init();
    	return ret;
    }
    
	private BpmService(String deploymentUrlStr, String deploymentId,
			String processDefinitionId, String userId, String password) {
		super();
		this.deploymentUrlStr = deploymentUrlStr;
		this.deploymentId = deploymentId;
		this.processDefinitionId = processDefinitionId;
		this.userId = userId;
		this.password = password;
	}
        
    private void init(){
    	if(deploymentUrlStr!=null){
    		
    		URL deploymentUrl;
			try {
				deploymentUrl = new URL(deploymentUrlStr);
				
				// Creamos la sesion REST
				//creamos factoria con timeout
				RemoteConfiguration configuration = new RemoteConfiguration(deploymentId, deploymentUrl, userId, password,30);
				engine = new RemoteRuntimeEngine(configuration);
				session = engine.getKieSession();
				taskService = engine.getTaskService();
				
			} catch (MalformedURLException e) {
					throw new RuntimeException(e);
			}
    	}
    }
    
   

    
    public ProcessInstance crearInstancia() {
    	//connect();
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
		ProcessInstance processInstance = session.startProcess(processDefinitionId, params);
		log.info("Started process instance: " + processInstance + " "
				+ (processInstance == null ? "" : processInstance.getId()));
		
		return processInstance;

    }
    
    public List<TaskSummary> obtenerTareasForUser(String userTask, long processInstance){
    	
    	//obtener una capa de servicios de tareas (TASKs)
		
		//List<Status> estados = new ArrayList();
		//estados.add(Status.Ready);
		//buscamos las tareas asignadas al grupo del usuario referenciado por "userId"
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(userTask, "en-UK");
	
		//List<TaskSummary> tasks = taskService.getTasksAssignedAsBusinessAdministrator(userId, "en-UK");
		
		
		//filtramos todas las tareas de la instancia recien creada
		List<TaskSummary> thisProcTasks = findTaskId(processInstance, tasks);
		
		return thisProcTasks;
    }
    
    public void verificarSolicitudPorRecepcion(TaskSummary taskSummary){
    
		//se trata de obtener la tarea
		Task task = taskService.getTaskById(taskSummary.getId());
		
		log.info("Ubico la task " + task.getId() + ": " + taskSummary.getName() +" ("+taskSummary.getStatus()+")");
		
		
		//creo una bolsa de variables que permitiran el completado correcto de la tarea
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put("observaciones", "estaTodoOk");
		
		//reclamo la tarea
		taskService.claim(task.getId(), userId);
		
		//establecer que la tarea esta en progreso
		taskService.start(task.getId(), userId);
				
		//completo la tarea
		taskService.complete(task.getId(), userId, taskParams);
		
    }
    
    public void aprobarSolictudPorAnalista(TaskSummary taskSummary){
    	//se trata de obtener la tarea
    			Task task = taskService.getTaskById(taskSummary.getId());
    			
    			log.info("Ubico la task " + task.getId() + ": " + taskSummary.getName() +" ("+taskSummary.getStatus()+")");
    			
    			
    			//creo una bolsa de variables que permitiran el completado correcto de la tarea
    			Map<String, Object> taskParams = new HashMap<String, Object>();
    			taskParams.put("aprobada", "true");
    			
    			//reclamo la tarea
    			taskService.claim(task.getId(), userId);
    			
    			//establecer que la tarea esta en progreso
    			taskService.start(task.getId(), userId);
    					
    			//completo la tarea
    			taskService.complete(task.getId(), userId, taskParams);
		
    }
    
     
    
    /* HELPER METHOD */
    private List<TaskSummary> findTaskId(long procInstId, List<TaskSummary> taskSumList) {
        List<TaskSummary> taskList = new ArrayList<TaskSummary>();
        for (TaskSummary task : taskSumList) {
        	log.info("Task "+task.getName()+" (" + task.getId() +") for process instance "+procInstId);
            if (task.getProcessInstanceId() == procInstId) {
            	taskList.add(task); 
            }
        }
        return taskList;
    }
    
    
//  public List<ProcessInstance> obtenerInstanciasActivas(){
//  	//connect();
//  	
//  	List<ProcessInstance> instances = new ArrayList<ProcessInstance>();
//  	instances.addAll(session.getProcessInstances());
//  	//session.getQueryResults(query, arguments)
//  	
//  	
//  	if(instances.isEmpty()){
//  		log.info("No active instances");
//  		return instances;
//  	}
//  	for (ProcessInstance processInstance : instances) {
//  		log.info("================================");
//  		log.info("Process: "+processInstance.getProcessId());
//  		log.info("Instance: "+processInstance.getId());
//  		log.info("State:"+processInstance.getState());
//  		log.info("================================");
//		}
//  	return instances;
//  }
    
}