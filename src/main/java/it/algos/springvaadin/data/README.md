#Spring Data
[http://www.baeldung.com/spring-data-mongodb-tutorial]

Spring Data’s mission is to provide a familiar and consistent, Spring-based programming model for data access
while still retaining the special traits of the underlying data store.

```
    Spring Data Commons
    Spring Data JPA
    Spring Data KeyValue
    Spring Data LDAP
    Spring Data MongoDB
    Spring Data Gemfire
    Spring Data REST
    Spring Data Redis
    Spring Data for Apache Cassandra
    Spring Data for Apache Solr
    Spring Data Couchbase (community module)
    Spring Data Elasticsearch (community module)
    Spring Data Neo4j (community module)
```

##Working with NoSQL technologies
(https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-nosql.html)

Spring Data provides additional projects that help you access a variety of NoSQL technologies including:
MongoDB, Neo4J, Elasticsearch, Solr, Redis, Gemfire, Cassandra, Couchbase and LDAP



##Introduction to Spring Data MongoDB
[http://www.baeldung.com/spring-data-mongodb-tutorial]

We’ll go over the basics using both the **MongoTemplate** as well as **MongoRepository**
using practical tests to illustrate each operation.

MongoTemplate è il livello più basso che usa i Criteria
MongoRepository è il livello più alto che costruisce le query dai nomi dei campi


##MongoRepository
###Inserire l'annotation
```
@EnableMongoRepositories(basePackages = "it.algos.springvaadin")
```
nella classe **SpringvaadinApplication**, che contiene il metodo **main** di ingresso

###Create the Repository
Now, after the configuration, we need to create a repository – extending the existing MongoRepository interface:

```
public interface VersioneRepository extends MongoRepository<Versione, String> {
    // 
}
```
Now we can auto-wire this VersioneRepository and use operations from MongoRepository or add custom operations.

###Using MongoRepository
Insert
```
Versione vers = new Versione();
vers.setSigla("Prova");
versioneRepository.save(vers);
```
Here’s the end state of the database:
```
{
    "_id" : ObjectId("55b4fda5830b550a8c2ca25a"),
    "_class" : "it.algos.springvaadin.entity.versione",
    "sigla" : "Prova"
}```
