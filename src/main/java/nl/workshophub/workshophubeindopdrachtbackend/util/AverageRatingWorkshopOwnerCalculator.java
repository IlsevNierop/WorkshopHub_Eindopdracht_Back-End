package nl.workshophub.workshophubeindopdrachtbackend.util;

import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.springframework.stereotype.Component;

@Component
public class AverageRatingWorkshopOwnerCalculator {

    public Double calculateAverageRatingWorkshopOwner(User workshopOwner) {
        if (workshopOwner.getWorkshopOwnerReviews().isEmpty()) {
            return null;
        }
        double sumRatings = 0;
        for (Review r : workshopOwner.getWorkshopOwnerReviews()) {
            sumRatings += r.getRating();
        }
        return sumRatings/workshopOwner.getWorkshopOwnerReviews().size();

    }

}
