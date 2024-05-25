package rs.raf.repositories.activity;

import rs.raf.entities.Activity;

import java.util.List;

public interface ActivityRepository {

    Activity addActivity(Activity activity);
    Activity findActivity(String name);
    List<Activity> allActivities();
}
