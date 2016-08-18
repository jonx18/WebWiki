# WebWiki

Para ejecutar
Crear base de datos mysql con usuario,contraseña y nombre de base igual a "wikiTest" sin comillas.
Desplegar el WikiWebTest.war o montar el proyecto eclipse y ejecutarlo en tomcat 7.
Puede usarse para una carga mas rapida como archivo de prueba el dump : "eswikiversity-prueba-pages-meta-history.xml"
Configurar el path al archivo history en el history.path del historyPath.properties ubicado en:
	*Desplegado en tomcat: /Tomcat/webapps/WikiWebTest/WEB-INF/clases/historyPath.properties
	*En el proyecto eclipse: /src/main/resources/historyPath.properties
Acceder a http://localhost:8080/WikiWebTest/
