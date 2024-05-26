package rs.raf.repositories.destination;

import rs.raf.entities.Destination;

import java.util.List;

public interface DestinationRepository {


    Destination addDestination(Destination destination);
    Destination findDestination(String name);

    List<Destination> allDestinations();

    String removeDestination(Destination destination);
    Destination updateDestination(Destination destination);
}
