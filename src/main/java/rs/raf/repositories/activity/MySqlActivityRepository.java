package rs.raf.repositories.activity;

import rs.raf.entities.Activity;
import rs.raf.repositories.MySqlAbstractRepository;

import java.util.List;

public class MySqlActivityRepository extends MySqlAbstractRepository implements ActivityRepository {
    @Override
    public Activity addActivity(Activity activity) {
        return null;
    }

    @Override
    public Activity findActivityWithId(Integer id) {
        return null;
    }

    @Override
    public List<Activity> AllActivities() {
        return null;
    }
}
