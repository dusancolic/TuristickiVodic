package rs.raf.services;

import rs.raf.entities.Activity;
import rs.raf.repositories.activity.ActivityRepository;

import javax.inject.Inject;
import java.util.List;

public class ActivityService {

    @Inject
    private ActivityRepository activityRepository;

    public Activity addActivity(Activity activity) {
        System.out.println("addActivity");
        return this.activityRepository.addActivity(activity);
    }

    public Activity findActivity(String name) {
        System.out.println("findActivity");
        return this.activityRepository.findActivity(name);
    }
    public List<Activity> allActivities() {
        System.out.println("allActivities");
        return this.activityRepository.allActivities();
    }
}
