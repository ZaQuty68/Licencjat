package CritterPicker.Critters.Interfaces;

import CritterPicker.Critters.Models.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FishInterface extends JpaRepository<Fish, Integer> {

    List<Fish> findAll();

    Optional<Fish> findById(int id);

    void deleteById(int id);

    Optional<Fish> findByName(String name);
}
