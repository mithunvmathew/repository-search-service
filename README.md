#### Repository-Search-Service

This service provide endpoint to search Github repositories with sorted as per the stars of repository. It provided the most popular repositories based on stars.  
Maximum number of repositories listed in a request is 100

##### Requirements   
Java 17   
Docker


##### Build Jar
From the project root location, run the below commands  
./gradlew clean build

###### Run
From the project root location, run the below commands  
docker-compose up  

OR   
./gradlew bootRun

App running in the port 8085 (http://localhost:8085/)  
Swagger Api Documentation available here  
http://localhost:8085/swagger-ui.html

##### Running automated tests  
./gradlew integrationTest  
Html Test report : <ProjectRoot>/build/reports/tests/integrationTest/index.html
 
