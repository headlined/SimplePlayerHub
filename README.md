# SimplePlayerHub
Legit am only making this because I need this for ONE THING and I'm too lazy to find a completed app. Guess y'all can have it too

## Build / Gson

This project now includes a `pom.xml` (Maven) that adds Google Gson as a dependency.

- To build and run with Maven:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.nutshell.java.Main"
```

- To use Gson in your Java code add the import:

```java
import com.google.gson.Gson;
```

And use it like:

```java
Gson gson = new Gson();
String json = gson.toJson(myObject);
MyClass obj = gson.fromJson(jsonString, MyClass.class);
```
