# bytebuddy-example

To run:

```
mvn package assembly:single && java -jar target/bytebuddy-example-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Branches

- main - powerful stuff, replace class with proxy, without agent
- subclass - dynamically create sub-class proxy
- buildtime-plugin - use buildtime plugin (needs to add 'package' into maven lifecycle)