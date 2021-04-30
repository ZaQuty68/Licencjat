package CritterPicker.Critters.Interfaces;

import CritterPicker.Critters.Models.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishInterface extends JpaRepository<Fish, Integer> {

    List<Fish> findAll();

    Fish findById(int id);

    void deleteById(int id);
}
