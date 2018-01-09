package it.algos.springvaadin.app;

import com.vaadin.spring.annotation.SpringComponent;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 02-dic-2017
 * Time: 09:13
 */
@SpringComponent
@ConfigurationProperties()
@Data
public class AppProperties {

    private Spring spring;
    private String database;

    private Mail mail;
    private String host;
    private int port;
    private String username;
    private String password;
    private String sender;

    @Data
    public static class Spring {

        private Data data;

        @lombok.Data
        public static class Data {
            private Mongodb mongodb;

            @lombok.Data
            public static class Mongodb {
                private String database;
            }// end of static class
        }// end of static class
    }// end of static class


    @Data
    public static class Mail {

        private Smtp smtp;

        @Data
        public static class Smtp {
            private String host;
            private int port;
            private String username;
            private String password;
            private String sender;
        }// end of static class
    }// end of static class


    public String getHost() {
        return getMail().getSmtp().getHost();
    }// end of method

    public int getPort() {
        return getMail().getSmtp().getPort();
    }// end of method

    public String getUsername() {
        return getMail().getSmtp().getUsername();
    }// end of method

    public String getPassword() {
        return getMail().getSmtp().getPassword();
    }// end of method

    public String getSender() {
        return getMail().getSmtp().getSender();
    }// end of method

    public String getDatabase() {
        return getSpring().getData().getMongodb().getDatabase();
    }// end of method

}// end of class
