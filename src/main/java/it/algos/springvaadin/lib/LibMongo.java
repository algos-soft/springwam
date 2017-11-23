package it.algos.springvaadin.lib;

import com.mongodb.MongoClient;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 06:32
 */
public class LibMongo {

    private static MongoClient mongo;
    private static MongoOperations mongoOps;


    static {
        mongo = new MongoClient("localhost", 27017);
        mongoOps = new MongoTemplate(mongo, "springvaadin");
    }// end of static method


    /**
     * Recupera tutte le entities di una collezione
     */
    public static List findAll(final Class<? extends AEntity> entityClazz) {
        return mongoOps.findAll(entityClazz);
    }// end of static method

    /**
     * Controlla l'esistenza di un record per un campo che dovrebbe essere unico
     */
    public static boolean esiste(final Class<? extends AEntity> entityClazz, String codeCompanyUnico) {
        Query query = new Query();
        query.addCriteria(Criteria.where("codeCompanyUnico").is(codeCompanyUnico));

        return mongoOps.findOne(query, entityClazz) != null;
    }// end of static method

}// end of class

