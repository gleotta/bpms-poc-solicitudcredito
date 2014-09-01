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

##Requisitos
1. Instalado RedHat BPM Suite 6.0.1
2. Tener usuario configurado con permiso `admin`
3. Instalado Oracle JDK 1.7 
4. Instalado Maven 3.0.4 o superior
4. Instalado git 1.8.x

##Instalacion
### 1. Clonar repositorio GIT

```
>cd $HOME/git
>git clone https://github.com/gleotta/bpms-poc-solicitudcredito.git
```
> $HOME representa el directorio base del usuario, en Linux está en /home/**usuario** y en Winows en c:\\Users\\**usuario**

###2. Importar proyecto `POC` a BPM Suite





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



