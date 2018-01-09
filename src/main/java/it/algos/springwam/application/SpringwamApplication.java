package it.algos.springwam.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by gac on 30/05/17.
 * <p>
 * Questa classe contiene il metodo 'main' che è il punto di ingresso dell'applicazione Java
 * In fase di sviluppo si possono avere diverse configurazioni, ognuna delle quali punta un ''main' diverso
 * Nel JAR finale (runtime) si può avere una sola classe col metodo 'main'.
 * Nel WAR finale (runtime) occorre (credo) inserire dei servlet di context diversi
 * Senza @ComponentScan, SpringBoot non 'vede' le classi con @SpringView
 * che sono in una directory diversa da questo package
 * <p>
 * Questa classe non fa praticamente niente se non avere le Annotation riportate qui
 */
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
