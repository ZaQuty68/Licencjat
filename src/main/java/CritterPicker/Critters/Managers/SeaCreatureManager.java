package CritterPicker.Critters.Managers;

import CritterPicker.Critters.CustomInterfaces.SeaCreatureInterfaceCustom;
import CritterPicker.Critters.Interfaces.SeaCreatureInterface;
import CritterPicker.Critters.Models.SeaCreature;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeaCreatureManager implements SeaCreatureInterfaceCustom {

    private SeaCreatureInterface si;

    public SeaCreatureManager(SeaCreatureInterface si){ this.si = si; }

    @Override
    public void addSeaCreature(SeaCreature seaCreature){
        SeaCreature seaCreatureToSave = new SeaCreature();
        int id;
        if(si.findAll().isEmpty()){
            id = 1;
        }
        else{
            id = si.findAll().get(0).getId();
            for(SeaCreature seaCreatureId : si.findAll()){
                if(seaCreatureId.getId() > id){
                    id = seaCreatureId.getId();
                }
            }
            id++;
        }

        seaCreatureToSave.setId(id);
        seaCreatureToSave.setName(seaCreature.getName());
        seaCreatureToSave.setQuote(seaCreature.getQuote());
        seaCreatureToSave.setSwimmingPattern(seaCreature.getSwimmingPattern());
        seaCreatureToSave.setPrice(seaCreature.getPrice());
        seaCreatureToSave.setRarity(seaCreature.getRarity());
        seaCreatureToSave.setShadowSize(seaCreature.getShadowSize());
        seaCreatureToSave.setMonthListN(seaCreature.getMonthListN());
        seaCreatureToSave.setMonthListS(seaCreature.getMonthListS());
        seaCreatureToSave.setHourList(seaCreature.getHourList());

        si.save(seaCreatureToSave);
    }

    @Override
    public List<SeaCreature> findAll(){ return si.findAll(); }

    @Override
    public SeaCreature findById(int id){ return si.findById(id); }

    @Override
    public void deleteById(int id){ si.deleteById(id); }
}
