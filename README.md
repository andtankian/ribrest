## Ribrest Framework.  
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.andtankian/Ribrest/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)

Ribrest Framework is a simple Java Restful framework that truly improve your productivity when developing restful based webservices.

## How to use it

Import the maven dependency.

     <dependency>
          <groupId>com.github.andtankian</groupId>
          <artifactId>Ribrest</artifactId>
          <version>1.10.3</version>
      </dependency>


Create your new class following this basic setup:
	
	@RibrestModel
	@Entity
	public class MyModelObject extends AbstractModel {
		//some attributes and methods...	
	}

Create your main file to initialize the framework.

    public class MyMain {
    	public static void main (String args[]){
	    	Ribrest.getInstance()
			    	.appBaseUrl("http://localhost:8080")
			    	.appName("myapp")
			    	.persistenceUnitName("myunit")
			    	.init();
    	}
    }

That's it.
You should get some log about Ribrest initialization

> Ribrest INFO: ***INITIALIZING RIBREST FRAMEWORK***
	Ribrest INFO: Application is up and running at: http://localhost:8080/myapp/
	Ribrest INFO: To shutdown Ribrest, type CTRL+C...

Try to send some http request to `MyClass` endpoint that should be: http://localhost:8080/myapp/mymodelobjects

;)

## Default operations supported

|Verb|Request Headers|Expected Results|
|--|--|--|
|GET|-|**Status**:200, **Content-Type**: *application/json* (when there are objects registered)<br/>**Status**:204 (when there aren't objects registered)|
|POST|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:201, **Content-Type**: *application/json* |
|PUT|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:200, **Content-Type**: *application/json* |
|DELETE|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:200, **Content-Type**: *application/json* |


