package rs.raf.repositories.destination;

import rs.raf.entities.Destination;

import java.util.List;

public interface DestinationRepository {


    Destination addDestination(Destination destination);
    Destination findDestinationWithId(Integer id);

    List<Destination> allDestinations();
}
