package CritterPicker.Critters.Interfaces;

import CritterPicker.Critters.Models.SeaCreature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeaCreatureInterface extends JpaRepository<SeaCreature, Integer> {

    List<SeaCreature> findAll();

    SeaCreature findById(int id);

    void deleteById(int id);

    Optional<SeaCreature> findByName(String name);
}
