package de.trion.training.web.training;

import de.trion.training.common.training.TrainingRepository;
import de.trion.training.common.training.UnknownTrainingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class TrainingControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;


    public TrainingControllerAdvice(TrainingRepository trainingRepository, TrainingMapper trainingMapper) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
    }

    @ExceptionHandler
    public String unknownTraining(UnknownTrainingException ex) {
        logger.info("UnknownTrainingException behandelt");
        return "/trainings/unknown";
    }

    @ExceptionHandler
    public ModelAndView trainingEditConcurrent(ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
        var mav = new ModelAndView("/trainings/concurrent");
        mav.addObject("training", request.getAttribute("training"));
        mav.addObject("id", ex.getIdentifier());

        var current = trainingRepository.getReferenceById((String) ex.getIdentifier());
        mav.addObject("current", trainingMapper.toDto(current));
        return mav;
    }

}
