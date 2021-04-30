package CritterPicker.Critters.Interfaces;

import CritterPicker.Critters.Models.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugInterface extends JpaRepository<Bug, Integer> {

    List<Bug> findAll();

    Bug findById(int id);

    void deleteById(int id);
}
