package CritterPicker.Critters.Managers;

//import CritterPicker.Critters.CustomInterfaces.FishInterfaceCustom;
import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.User.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FishManager{

    private final FishInterface fi;

    public String addFish(FishDTO fish, MultipartFile file){
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
        List<String> listN = fish.getMonthListN();
        for(int i = 0; i < listN.size() - 1; i++){
            mlistN += listN.get(i) + ", ";
        }
        mlistN += listN.get(listN.size() - 1);

        fishToSave.setMonthListN(mlistN);

        String mlistS = "";
        List<String> listS = fish.getMonthListS();
        for(int i=0; i < listS.size() - 1; i++){
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() - 1);

        fishToSave.setMonthListS(mlistS);

        List<Integer> list = fish.getHourList();
        int x = list.get(0);
        int y = x;
        String hlist = "";

        for(int i = 0; i<list.size(); i++){
            int temp = list.get(i);
            if(i == list.size() - 1){
                if(temp == y + 1){
                    hlist += x + ":00 - " + (temp + 1) + ":00";
                }
                else{
                    if(list.size() == 1){
                        hlist += x + ":00 - " + (y + 1) + ":00";
                    }
                    hlist += x + ":00 - " + (y + 1) + ":00, " + temp + ":00 - " + (temp + 1) + ":00";
                }
            }
            else{
                if(temp != y + 1 && i != 0){
                    hlist += x + ":00 - " + (y + 1) + ":00, ";
                    x = temp;
                }
                y = temp;
            }
        }
        fishToSave.setHourList(hlist);

        fishToSave.setFilename(file.getOriginalFilename());

        fi.save(fishToSave);
        return "saved";
    }

    public List<Fish> findAll(){ return fi.findAll(); }

    public Fish findById(int id){ return fi.findById(id).get(); }

    public void deleteById(int id){ fi.deleteById(id); }

    public List<Fish> findOtherFish(AppUser user) {
        List<Fish> listToReturn = new ArrayList<>();
        for(Fish fish : fi.findAll()){
            Boolean flag = true;
            for(Fish f : user.getFishSet()){
                if(f == fish){
                    flag = false;
                }
            }
            if(flag){
                listToReturn.add(fish);
            }
        }
        return listToReturn;
    }

}
