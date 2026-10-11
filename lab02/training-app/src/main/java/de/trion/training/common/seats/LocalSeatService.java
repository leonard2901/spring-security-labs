package de.trion.training.common.seats;

import de.trion.training.common.training.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Random;

@ConditionalOnProperty(matchIfMissing = true, name = "localSeatService")
@Service
public class LocalSeatService implements SeatService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Random random = new Random();

    @Override
    public Integer seatsFor(Training t) {
        Integer[] seats = new Integer[]{(random.nextInt(20))};
        logger.info("Seats: {} for training {}", seats, t);

        return seats[0];
    }
}
