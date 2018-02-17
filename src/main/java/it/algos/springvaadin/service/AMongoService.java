package it.algos.springvaadin.service;

import com.mongodb.MongoClient;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 06:32
 */
@SpringComponent
@Service
@Scope("singleton")
public class AMongoService {


//    @Autowired
//    private AppProperties properties;

    private MongoClient mongo;
    private MongoOperations mongoOps;


    /**
     * Metodo invocato subito DOPO il costruttore (chiamato da Spring)
     * (si può usare qualsiasi firma)
     * Regola il modello-dati specifico nel Service
     */
    @PostConstruct
    private void inizia() {
        mongo = new MongoClient("localhost", 27017);
        mongoOps = new MongoTemplate(mongo, "springvaadintest");//@todo rimettere properties
    }// end of method


    /**
     * Recupera tutte le entities di una collezione
     */
    public  List findAll(final Class<? extends AEntity> entityClazz) {
        return mongoOps.findAll(entityClazz);
    }// end of static method


    /**
     * Controlla l'esistenza di un record per un campo che dovrebbe essere unico
     */
    public  boolean esiste(final Class<? extends AEntity> entityClazz, String codeCompanyUnico) {
        Query query = new Query();
        query.addCriteria(Criteria.where("codeCompanyUnico").is(codeCompanyUnico));

        return mongoOps.findOne(query, entityClazz) != null;
    }// end of static method


}// end of class

