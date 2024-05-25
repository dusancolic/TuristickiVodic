package rs.raf.services;


import rs.raf.entities.Destination;
import rs.raf.repositories.destination.DestinationRepository;

import javax.inject.Inject;
import java.util.List;

public class DestinationService {

       @Inject
        private DestinationRepository destinationRepository;

        public List<Destination> allDestinations() {
            return this.destinationRepository.allDestinations();
        }

        public Destination findDestination(String name) {
            return this.destinationRepository.findDestination(name);
        }

        public Destination addDestination(Destination destination) {
            return this.destinationRepository.addDestination(destination);
        }

}
