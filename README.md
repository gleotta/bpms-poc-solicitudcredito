#bpms-poc-solicitudcredito

POC usando KIE remotas de JBPM6 en Red Hat BPM Suite 6.0.1 

## Proceso de Aprobación de Credito Dummy
Imagen en poc/src/main/resources/altacredito.altacredito-image.png
![Proceso alta crédito](bpm6-poc/src/main/resources/altacredito.altacredito.png "Proceso alta credito")

Archivo BPMN2 poc/src/main/resources/altacredito.bpmn2
[Proceso bpmn2](poc/src/main/resources/altacredito.bpmn2 "Proceso bpmn2")

Posee dos Tareas Humanas:
* Verificar Solicitud Credito -> Grupo: recepcion, admim
* Aprobar Solicitud Credito -> Grupo: analista, admin


##Contenido repositorio
1. Proyecto **poc**: Kmodule conteniendo proceso altacredito.altacredito
2. Proyecto **bpm6-poc**: Cliente java standalone que conume el proceso altacredito.altacredito a través de Kie Remotas
3. Proyecto **bpm6-poc-web**: Aplicación WEB que crea nuevas instancias de altacredito.altacredito a través de Kie remotas


##Requisitos
1. Instalado RedHat BPM Suite 6.0.1
2. Tener usuario configurado con permiso `admin`
3. Instalado Oracle JDK 1.7 
4. Instalado Maven 3.0.4 o superior
5. Instalado git 1.8.x


##Instalación
###1. Clonar repositorio GIT

```
>cd $HOME/git
>git clone https://github.com/gleotta/bpms-poc-solicitudcredito.git
```
> $HOME representa el directorio base del usuario, en Linux está en /home/**usuario** y en Winows en c:\\Users\\**usuario**

###2. Importar proyecto **poc** a BPM Suite
1. Ingresar a BPM Suite (Ej. *http://localhost:8080/business-central*)
2. Ir a *Autoria->Administración->Repositorios->Clonar Repositorio*
3. Ingresar:
```  
Repository Name: bpms-poc-solicitudcredito
Organizacional Unit: example
Git URL: file://[$HOME]/git/bpms-poc-solicitudcredito/.git
Username:
Password:
```
> Reemplazar [$HOME] or directorio base de usuario

4. Ir a *Autoria-> Autoría de Proyectos -> example / bpms-poc-solicitudcredito / poc* 
5. Ir a *Herramientas -> Editor de Proyectos -> Construcción & Implementación*
6. Ir a *Implementar -> Implementaciones*  
7. Verificar que exista implementado `com.cuyum.jbpm:poc:1.1`

###3. Construir y ejecutar **bpm6-poc**
1. Construyo proyecto
```
>cd $HOME/git/bpms-poc-solicitudcredito/bpm6-poc
>mvn clean install
```

2. Ejecuto proyecto
```
>java -jar target/bpm6-poc.jar [user_bpms] [password]
```

> [user_bpms] es el usuarios de BPM Suite con privilegios de **admin**

Resultado
```
[main] INFO  com.cuyum.BpmService  - Started process instance: ProcessInstance 1 [processId=altacredito.altacredito,state=1] 1
[main] INFO  com.cuyum.MainClass  - Estado de la instancia del proceso: 1
[main] INFO  com.cuyum.BpmService  - Task Verificar Datos Credito (1) for process instance 1
[main] INFO  com.cuyum.BpmService  - Ubico la task 1: Verificar Datos Credito (Ready)
[main] INFO  com.cuyum.BpmService  - Task Aprobar Credito (2) for process instance 1
[main] INFO  com.cuyum.BpmService  - Ubico la task 2: Aprobar Credito (Ready)

```


2) Clases
com.cuyum.BpmService: Facade de acceso a las KIE de jBPM
com.cuyum.MainClass: Clase principal que se conecta al server de jBPM, crea una instancia 
y ejecuta las tareas humanas a traves de BpmService.

3) Propiedades de conexión
Dentro de com.cuyum.MainClass estan las propiedades para conectarse al servidor jBPM

url = "http://[SERVER]:[PORT]/business-central/"; url del servidor de jbpm
deploymentId = "pe.com.bcp:altacredito:1.0"; identificación de la unidad de deploy
processDefinitionId = "altacredito.altacredito"; identifiación del proceso de alta de credito
user = "user"; //usuario con roles: "recepcion", "analista" pass="a123456$";
password = "pass"

4) Se puede usar contra el server 162.243.12.101:8080 que quedara disponible algunos dias
Esta instancia tiene los siguientes usuarios:
juan: recepcion
jose: analista
admibcrea: recepcion, analista

5) Se puede instalar un server propio de BPM Suite 6 y agregar los usuarios y roles rqueridos.
Luego se debe importar el procesos bpmn2 y crera la unidad de deply que será utilizada por
este POC

Contacto: German Leotta <gleotta@servicios-redhat.com>



