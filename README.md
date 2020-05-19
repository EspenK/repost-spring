# Repost Spring
This is the Spring Boot implementation of the Repost API.

## Installation
A version of the Java JDK must be installed. 
[OpenJDK 11](https://openjdk.java.net/projects/jdk/11/) was used when creating the
project and is therefore recommended. Below are the steps for building and deploying
the API. These instructions are only for a Linux build.

1. Clone the repository
```bash
git clone https://github.com/EspenK/repost-spring.git
```

2. Navigate to the `repost-spring` directory. Make the `mvnw` file executable. This
is an instanced version of the [Apache Maven](https://maven.apache.org/) build tool
```bash
cd repost-spring
chmod +x mvnw
```

3. Package a JAR file using the Maven tool. This will also install the required
dependencies
```bash
./mvnw package
```

4. Copy the default configuration file to the root folder. See 
[Configurations](#configurations) for more information
```bash
cp src/main/resources/application.properties user.properties
```

## Configurations
Configurations are set using a properties file. See step 4 above for copying this
file to the root directory of the project for further use. 

- **server.port** - Change this to use a different port for the API
- **repost.signing-key** - The secret key used for [JSON Web Tokens](https://jwt.io/)
- **repost.client_id** - The OAuth2 client_id
- **repost.origins[0]** - A 
[CORS](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing) URL. Use 
`repost.origins[1]` etc for multiple allowed origins

### Using PostgreSQL as a database
First add a `#` symbol before the properties `spring.datasource.driverClassName`
and `spring.jpa.database-platform` as they are not needed for PostgreSQL. Set the
appropriate database username and password using the `spring.datasource.username` 
and `spring.datasource.password` properties. Finally set the database connection
URL with the `spring.datasource.url` property. Example:
```
#spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/DATABASE_NAME
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
#spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

## Running the API
The API can now be executed using a java runtime. Make sure to replace **ROOT_PATH** 
below with an absolute path to your cloned project directory.
```bash
java -jar target/repost-0.0.1-SNAPSHOT.jar --spring.config.location=file:///ROOT_PATH/user.properties
```

## Documentation
Documentation for the API is available after deployment at the `/api/swagger` and 
`/api/docs` endpoints.
