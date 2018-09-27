
# Ribrest Framework
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.andtankian/Ribrest/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)

Ribrest Framework is a simple Java Restful framework that truly improve your productivity when developing restful based webservices.

It works by automatically creating restful webservice endpoints based on model classes defined on your project.

## Introduction
Any Java project has classes called model classes, or domain classes. These classes are commonly real world representation and used to store data.

For example, in a library application, the `Book` class is a nice example of a model class. In a school application, `Student` is a great example of model class.

In most cases, model classes have at least four basic operations associated to it:
|Operations|
|--|
|**C**reate new model|
|**R**ead one or more models|
|**U**pdate an existent model|
|**D**elete an existent model|

In a restful webservice application, **CRUD** operations can be represented as the following *http verbs*:
|Verbs |
|--|
|`POST`  for **C**|
|`GET` for **R**|
|`PUT` for **U**|
|`DELETE` for **D**|

Of course model classes could have more than four operations, in fact it's quite rare that model classes have ONLY four basic operations. It can have as many you want to.

*Ok, knowing that, what can Ribrest do for me?*

Ribrest can scan your model classes and automatically generate valid endpoints! 
You don't need to worry about webservices architectures,  design patterns, data representation and other things anymore. Ribrest does all these things for you. Now you can focus on what is important to your project like domain classes, data validation and business logic.

For example, in a library application, just to creating a `Book` class, Ribrest would generate the following endpoints:
*http://localhost:2007/library/books/ - POST (To create a new Book)
http://localhost:2007/library/books/ - GET (To read books)
http://localhost:2007/library/books/{id} - PUT (To update an existent book)
http://localhost:2007/library/books/{id} - DELETE (To delete an existent book)*

These are valid endpoints! If you open your browser and type http://localhost:2007/library/books/, it will return a JSON representing all books stored in the database.

Yeah, you just create ONE CLASS called `Book`, and THAT'S IT! Everything is working. You will not hard code restful webservice resources anymore.

It's awesome! Don't you think?

Ribrest automatically scans your model class using [Classgraph](https://github.com/classgraph/classgraph), creates programmatically endpoints using [Jersey](https://jersey.github.io/), setup and run a http server using [Grizzly](https://javaee.github.io/grizzly/), manages dependency injection using [HK2](https://javaee.github.io/hk2/) and manages database ORM using [JPA](https://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html).

Thank you guys for all these fantastic projects.

## Ok, let's use it

Import the Ribrest as a maven dependency

     <dependency>
          <groupId>com.github.andtankian</groupId>
          <artifactId>Ribrest</artifactId>
          <version>1.19.0</version>
      </dependency>


Create your new class following this basic setup:
	
	@RibrestModel
	@Entity
	public class MyModelObject extends AbstractModel {
		//some attributes and methods...	
	}

Create your main file to initialize the framework

    public class MyMain {
    	public static void main (String args[]){
	    	Ribrest.getInstance()
			    	.appBaseUrl("http://localhost:2007")
			    	.appName("myapp")
			    	.persistenceUnitName("myunit")
			    	.init();
    	}
    }


That's it. The minimal requirement to run Ribrest based projects :)

**DON'T FORGET THE FOLLOWING THINGS**

 - You should have a persistence unit like `resources/META-INF/persistence.xml` named by your choice. If you like, you can copy, paste and modify the Ribrest test persistence unit. [You can find it here](https://github.com/andtankian/ribrest/blob/master/src/test/resources/META-INF/persistence.xml).
 - No programs or servers should be running in the port you defined in your code. It's very common, in development environment, to have others servers running  in the port 3000, 5000, 8000, 8080. Make sure the port specified in your code be unique.

Ok, making sure of above tenets, **you can now run** your Ribrest project and watch the following output log.

> Ribrest INFO: *** INITIALIZING RIBREST FRAMEWORK ***
Ribrest INFO: Application is up and running at: http://localhost:2007/myapp/
Ribrest INFO: Static file server has been created at: http://localhost:2007/static
Ribrest INFO: To shutdown Ribrest, type CTRL+C...

Try to request to `MyClass` endpoint that should be: http://localhost:2007/myapp/mymodelobjects

;)

## Default operations supported

|Verb|Request Headers|Expected Results|
|--|--|--|
|GET|-|**Status**:200, **Content-Type**: *application/json* (when there are objects registered)<br/>**Status**:204 (when there aren't objects registered)|
|POST|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:201, **Content-Type**: *application/json* |
|PUT|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:200, **Content-Type**: *application/json* |
|DELETE|**Content-Type**: *application/x-www-form-urlencoded*|**Status**:200, **Content-Type**: *application/json* |
