package de.trion.training.api.training;

import de.trion.training.api.FieldErrorDto;
import de.trion.training.api.ValidationDto;
import de.trion.training.common.training.Training;
import de.trion.training.common.training.TrainingDto;
import de.trion.training.common.training.TrainingMapper;
import de.trion.training.common.training.TrainingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/trainings")
public class TrainingApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    public TrainingApiController(TrainingService trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    @GetMapping
    public Page<TrainingDto> get(Pageable pageable) {
        final Page<Training> page = trainingService.findAll(pageable);
        final List<TrainingDto> dtoList = trainingMapper.toDto(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    @GetMapping("/{id}")
    public TrainingDto getById(@PathVariable String id) {
        return trainingMapper.toDto(trainingService.findById(id).orElseThrow());
    }

    @PostMapping
    public TrainingDto save(@Valid @RequestBody TrainingDto training) {
        logger.info("Valides Training: {}", training);
        return trainingMapper.toDto(trainingService.save(trainingMapper.fromDto(training)));
    }

    @GetMapping("/locations")
    public List<String> locations() {
        return trainingService.findLocations();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationDto validationHandler(MethodArgumentNotValidException ex) {
        var result = new ValidationDto();
        result.setCode("VALIDATION_ERROR");
        var br = ex.getBindingResult();
        for (final FieldError fr : br.getFieldErrors()) {
            result.addFieldError(new FieldErrorDto(fr.getField(), fr.getDefaultMessage()));
        }
        return result;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleTrainingNotFoundException(NoSuchElementException ex) {
        return new ResponseEntity<>("Object not found", HttpStatus.NOT_FOUND);
    }
}
