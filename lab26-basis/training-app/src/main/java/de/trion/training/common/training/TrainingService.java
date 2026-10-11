package de.trion.training.common.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Page<Training> findAll(Pageable pageable) {
        return trainingRepository.findAll(pageable);
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    public Optional<Training> findById(String id) {
        return trainingRepository.findById(id);
    }

    public List<String> findLocations() {
        return trainingRepository.findLocations();
    }
}
