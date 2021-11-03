package ch.baloise.example.contact.mapper;

import org.apache.camel.main.Main;

/**
 * Main App for reading and converting json files with partner objects
 * from the folder src/data/partner.
 */
public class ContactMapperMainApp {
    
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new TransformPartnerRoute());
        main.run(args);
    }
}
