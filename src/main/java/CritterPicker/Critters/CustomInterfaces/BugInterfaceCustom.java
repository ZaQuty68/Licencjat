package CritterPicker.Critters.CustomInterfaces;

import CritterPicker.Critters.Models.Bug;

import java.util.List;

public interface BugInterfaceCustom {

    void addBug(Bug bug);

    List<Bug> findAll();

    Bug findById(int id);

    void deleteById(int id);
}
