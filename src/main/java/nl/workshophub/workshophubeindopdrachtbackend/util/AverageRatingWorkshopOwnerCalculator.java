package nl.workshophub.workshophubeindopdrachtbackend.util;

import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import org.springframework.stereotype.Component;

@Component
public class AverageRatingWorkshopOwnerCalculator {

    public Double calculateAverageRatingWorkshopOwner(User workshopOwner) {
        if (workshopOwner.getWorkshops() == null) {
            return null;
        }
        double sumRatings = 0;
        double numberReviews = 0;
        for (Workshop w: workshopOwner.getWorkshops()){
            for (Review r: w.getWorkshopReviews()){
                sumRatings += r.getRating();
                numberReviews ++;
            }
        }
        if (numberReviews == 0) {
            return null;
        }

        return sumRatings/numberReviews;

    }

}
