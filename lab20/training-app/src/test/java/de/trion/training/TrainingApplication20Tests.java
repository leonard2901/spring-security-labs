package de.trion.training;

import de.trion.training.common.training.Training;
import de.trion.training.common.training.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TrainingApplication20Tests
{

   @Test
   void contextLoads() {
   }

   @Autowired
   TrainingRepository trainingRepository;

   @Test
   void locations() {
      trainingRepository.save(Training.withLocation("Test").withTopic("Test").withInstructor("Thomas").build());
      assertThat(trainingRepository.findLocations()).isNotEmpty();
   }

}
