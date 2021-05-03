package CritterPicker.Critters.Managers;

//import CritterPicker.Critters.CustomInterfaces.FishInterfaceCustom;
import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Enums.Months;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FishManager{

    private FishInterface fi;

    public void addFish(Fish fish){
        Fish fishToSave = new Fish();
        int id;
        if(fi.findAll().isEmpty()){
            id = 1;
        }
        else{
            id = fi.findAll().get(0).getId();
            for (Fish fishId : fi.findAll()){
                if(fishId.getId() > id){
                    id = fishId.getId();
                }
            }
            id++;
        }

        fishToSave.setId(id);
        fishToSave.setName(fish.getName());
        fishToSave.setQuote(fish.getQuote());
        fishToSave.setLocation(fish.getLocation());
        fishToSave.setOnlyInRain(fish.isOnlyInRain());
        fishToSave.setPrice(fish.getPrice());
        fishToSave.setRarity(fish.getRarity());
        fishToSave.setShadowSize(fish.getShadowSize());
        fishToSave.setMonthListN(fish.getMonthListN());
        fishToSave.setMonthListS(fish.getMonthListS());
        fishToSave.setHourList(fish.getHourList());

        fi.save(fishToSave);
    }

    public void processAddingFish(Fish fish, List<Months> mlistN, List<Months> mlistS, List<Integer> hlist){
        for(Months month: mlistN){
            System.out.println(month.toString());
        }
    }

    public List<Fish> findAll(){ return fi.findAll(); }

    public Fish findById(int id){ return fi.findById(id); }

    public void deleteById(int id){ fi.deleteById(id); }
}
