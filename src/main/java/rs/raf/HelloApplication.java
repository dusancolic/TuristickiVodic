package rs.raf;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import rs.raf.repositories.subject.InMemorySubjectRepository;
import rs.raf.repositories.subject.SubjectRepository;
import rs.raf.services.SubjectService;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication() {
        // Ukljucujemo validaciju
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // Definisemo implementacije u dependency container-u
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(InMemorySubjectRepository.class).to(SubjectRepository.class).in(Singleton.class);

                this.bindAsContract(SubjectService.class);
            }
        };
        register(binder);

        // Ucitavamo resurse
        packages("rs.raf.resources");
    }
}
