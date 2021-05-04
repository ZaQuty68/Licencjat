package CritterPicker.Critters.Managers;

//import CritterPicker.Critters.CustomInterfaces.FishInterfaceCustom;
import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.DTO.FishDTO;
import CritterPicker.Enums.Months;
import CritterPicker.Storage.Attachments.Attachment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FishManager{

    private final FishInterface fi;

    public String addFish(FishDTO fish, Attachment attachment){
        boolean fishExists = fi.findByName(fish.getName()).isPresent();
        if(fishExists){
            return "exists";
        }

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

        String mlistN = "";
        for(String s : fish.getMonthListN()){
            mlistN += s + ",";
        }
        fishToSave.setMonthListN(mlistN);

        String mlistS = "";
        for(String s : fish.getMonthListS()){
            mlistS += s + ",";
        }
        fishToSave.setMonthListS(mlistS);

        String hlist = "";
        for(int i : fish.getHourList()){
            hlist += i + ",";
        }
        fishToSave.setHourList(hlist);

        fishToSave.setAttachment(attachment);

        fi.save(fishToSave);
        return "saved";
    }

    public List<Fish> findAll(){ return fi.findAll(); }

    public Fish findById(int id){ return fi.findById(id).get(); }

    public void deleteById(int id){ fi.deleteById(id); }
}
