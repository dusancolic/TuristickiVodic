package rs.raf;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import rs.raf.filters.AuthFilter;
import rs.raf.repositories.activity.ActivityRepository;
import rs.raf.repositories.activity.MySqlActivityRepository;
import rs.raf.repositories.article.ArticleRepository;
import rs.raf.repositories.article.MySqlArticleRepository;
import rs.raf.repositories.comment.CommentRepository;
import rs.raf.repositories.comment.MySqlCommentRepository;
import rs.raf.repositories.destination.DestinationRepository;
import rs.raf.repositories.destination.MySqlDestinationRepository;
import rs.raf.repositories.user.MySqlUserRepository;
import rs.raf.repositories.user.UserRepository;
import rs.raf.services.*;

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
                this.bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                this.bind(MySqlActivityRepository.class).to(ActivityRepository.class).in(Singleton.class);
                this.bind(MySqlCommentRepository.class).to(CommentRepository.class).in(Singleton.class);
                this.bind(MySqlDestinationRepository.class).to(DestinationRepository.class).in(Singleton.class);
                this.bind(MySqlArticleRepository.class).to(ArticleRepository.class).in(Singleton.class);

                this.bindAsContract(UserService.class);
                this.bindAsContract(ActivityService.class);
                this.bindAsContract(CommentService.class);
                this.bindAsContract(DestinationService.class);
                this.bindAsContract(ArticleService.class);

            }
        };
        register(binder);
        register(AuthFilter.class);
        // Ucitavamo resurse
        packages("rs.raf.resources");


    }
}
