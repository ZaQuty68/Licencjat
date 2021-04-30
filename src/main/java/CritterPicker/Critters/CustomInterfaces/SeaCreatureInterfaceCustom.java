package CritterPicker.Critters.CustomInterfaces;

import CritterPicker.Critters.Models.SeaCreature;

import java.util.List;

public interface SeaCreatureInterfaceCustom {

    void addSeaCreature(SeaCreature fish);

    List<SeaCreature> findAll();

    SeaCreature findById(int id);

    void deleteById(int id);
}
