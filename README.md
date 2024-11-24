## to reuse this project please make sure to follow these steps
### 1. Clone the repository
### 2. make sure you are connected on same network with a Nakivo server
### 3. inside /src/globalVars/GlobalVars.java file change the following variables:
    -public static final String NakivoServiceEndpoint = "https://serverIP+Port/c/router";
    -public static final String NakivoServiceEndpointPrefix = "https://serverIP+Port/t/";
### 4. inside application.properties file change the following variables:
    -nakivo.serverIp= your main server IP
    -nakivo.url=https://your-main-server-ip/c/router
    - spring.datasource.url=jdbc:mysql://mysql_serverIP:3306/db_name
    - spring.datasource.username=db_username
    - spring.datasource.password=db_password
    - spring.mail.username=email_address
    - spring.mail.password=email_password
    - note : email is configured to use gmail smtp server if you wish to use another email server change configurations inside application.properties file
### 5. run the project
### 6. check your database for the created tables and data being backed up , through it use grafana or your custom dashboard to see current state of your Nakivo server
### 7. some crons are running to send emails and backup data every day at 7am and 5pm
# put a star if you like the project :)
note : this project is still under development and will be updated with more features and improvements
```
another note : this project is not yet fully tested and may contain bugs
```
#### another note : this project Nakivo api to automate data gathering -- docs : https://helpcenter.nakivo.com/api-reference/Content/API-Reference-Overview.htm
