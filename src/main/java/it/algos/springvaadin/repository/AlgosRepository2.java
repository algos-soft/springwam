package it.algos.springvaadin.repository;

import com.mongodb.Mongo;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * Created by gac on 02/07/17
 * <p>
 * Interfaccia generica di collegamento o 'contratto' tra il Service ed il DataBase
 * Si chiama Repository per comodità, anche se le classi Repository
 *  sono una delle due modalità di utilizzo: Repository e Template
 * La logica di selezione, controllo e manipolazione dei dati risiede nel Service
 * Nelle Repository c'è la metodologia specifica di un collegamento al DB (ce ne sono diverse)
 * <p>
 * Estende una annotation specifica esistente, MongoRepository, con un pacchetto base di metodi
 * Comprende le interfacce: PagingAndSortingRepository e QueryByExampleExecutor
 * Possono essere aggiunte altre interfacce esistenti (JpaRepository)
 * Qui vengono inseriti ed aggiunti i 'contratti' specifici di questa applicazione SpringVaadin
 * <p>
 * Le metodologie concrete specifiche, possono essere di due tipi:
 * - MongoTemplate (basso livello, uso di Criteria, uso più macchinoso)
 * - MongoRepository (alto livello, architettura automatica molto più complessa, uso facile ed elegante)
 * Possono essere scambiate facilmente lasciando inalterati i Service
 *
 * @see https://spring.io/guides/gs/relational-data-access/
 * @see https://spring.io/guides/gs/accessing-data-jpa/
 * @see https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-data-jpa
 * @see http://www.concretepage.com/spring/spring-auto-detection-with-component-service-repository-and-controller-stereotype-annotation-example-using-componentscan-and-component-scan
 * @see http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html/jpa.repositories.html
 * @see https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-one-configuration/
 * @see http://docs.spring.io/spring-data/jpa/docs/1.8.x/reference/html/#repositories.create-instances
 * @see https://springframework.guru/spring-boot-web-application-part-3-spring-data-jpa/
 */
public interface AlgosRepository2  {
}// end of interface
