package it.algos.springwam.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories({"it.algos.springvaadin.entity", "it.algos.springwam.entity"})
@ComponentScan({"it.algos.springvaadin", "it.algos.springwam"})
@EntityScan({"it.algos.springvaadin.entity", "it.algos.springwam.entity"})
public class SpringwamApplication {

    /**
     * Constructor
     *
     * @param args eventuali parametri in ingresso
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringwamApplication.class, args);
    }// end of constructor

}// end of main class
