package it.algos.springvaadin.repository;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 22-ott-2017
 * Time: 20:43
 */
@EnableMongoRepositories(basePackages = "it.algos.springvaadin")
public class SpringMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(new MongoClient("localhost"), "findall");
    }// end of method

}// end of class
