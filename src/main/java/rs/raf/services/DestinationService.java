package rs.raf.services;


import rs.raf.entities.Destination;
import rs.raf.repositories.destination.DestinationRepository;

import javax.inject.Inject;
import java.util.List;

public class DestinationService {

       @Inject
        private DestinationRepository destinationRepository;

        public List<Destination> allDestinations(int page, int size) {
            return this.destinationRepository.allDestinations(page,size);
        }

        public long countDestinations() {
            return this.destinationRepository.countDestinations();
        }

        public Destination findDestination(String name) {
            return this.destinationRepository.findDestination(name);
        }

        public Destination findDestinationById(Long id) {
            return this.destinationRepository.findDestinationById(id);
        }
        public Destination addDestination(Destination destination) {
            return this.destinationRepository.addDestination(destination);
        }

        public String removeDestination(Destination destination) {
            return this.destinationRepository.removeDestination(destination);
        }

        public Destination updateDestination(Destination destination) {
            return this.destinationRepository.updateDestination(destination);
        }

}
