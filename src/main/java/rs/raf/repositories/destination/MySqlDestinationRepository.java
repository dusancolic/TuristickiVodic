package rs.raf.repositories.destination;

import rs.raf.entities.Destination;
import rs.raf.repositories.MySqlAbstractRepository;

import java.util.List;

public class MySqlDestinationRepository extends MySqlAbstractRepository implements DestinationRepository {
    @Override
    public Destination addDestination(Destination destination) {
        return null;
    }

    @Override
    public Destination findDestinationWithId(Integer id) {
        return null;
    }

    @Override
    public List<Destination> allDestinations() {
        return null;
    }
}
