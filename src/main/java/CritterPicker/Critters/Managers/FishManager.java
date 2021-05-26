package CritterPicker.Critters.Managers;

import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.Enums.Months;
import CritterPicker.User.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



        List<Integer> intListS = new ArrayList<>();
        for(String n : listN){
            int i = Months.valueOf(n).getOrder() + 6;
            if(i > 12){
                i -= 12;
            }
            intListS.add(i);
        }
        Collections.sort(intListS);

        List<String> listS = new ArrayList<>();

        for(int i : intListS){
            listS.add(Months.values()[i-1].name());
        }


        String mlistS = "";
        for(int i=0; i < listS.size() - 1; i++){
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() - 1);

        fishToSave.setMonthListS(mlistS);



        List<Integer> list = fish.getHourList();
        int x = list.get(0);
        int y = x;
        String hlist = "";

        if(list.size() == 1){
            hlist += x + ":00 - " + (y+1) + ":00";
        }
        else{
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
        }
        fishToSave.setHourList(hlist);

        fishToSave.setFilename(file.getOriginalFilename());

        fi.save(fishToSave);
        return "saved";
    }

    public List<Fish> findAll(){ return fi.findAll(); }

    public Fish findById(int id){ return fi.findById(id); }

    public void deleteById(int id){ fi.deleteById(id); }

    public List<Fish> findOtherFish(AppUser user) {
        List<Fish> listToReturn = new ArrayList<>();
        for(Fish fish : fi.findAll()){
            boolean flag = true;
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

    public FishDTO toDTO(Fish fish, boolean flag){
        FishDTO fishDTO = new FishDTO();

        fishDTO.setName(fish.getName());
        fishDTO.setLocation(fish.getLocation());
        fishDTO.setOnlyInRain(fish.isOnlyInRain());
        fishDTO.setPrice(fish.getPrice());
        fishDTO.setRarity(fish.getRarity());
        fishDTO.setShadowSize(fish.getShadowSize());

        String mlistN = "";
        if(flag){
            mlistN = fish.getMonthListS();
        }
        else{
            mlistN = fish.getMonthListN();
        }
        List<String> listN = new ArrayList<>();
        for(String n : mlistN.split(", ")){
            listN.add(n);
        }
        fishDTO.setMonthListN(listN);

        String hList = fish.getHourList();
        List<Integer> hListInt = new ArrayList<>();
        for(String s : hList.split(", ")){
            List<Integer> helper = new ArrayList<>();
            Matcher m = Pattern.compile("[0-9]{1,2}:").matcher(s);
            while (m.find()){
                helper.add(Integer.parseInt(m.group().substring(0, m.group().length() -1)));
            }
            int x = helper.get(0);
            int y = helper.get(1);
            for(int i = x; i < y; i ++){
                hListInt.add(i);
            }
        }
        fishDTO.setHourList(hListInt);


        return fishDTO;
    }

    public List<FishDTO> toDTOList(List<Fish> fishList, boolean flag){
        List<FishDTO> DTOList = new ArrayList<>();
        for(Fish fish : fishList){
            DTOList.add(toDTO(fish, flag));
        }
        return DTOList;
    }

    public String editFish(FishDTO fish, int id, MultipartFile file){
        boolean fishExists = fi.findByName(fish.getName()).isPresent();
        if(fishExists && !(fish.getName().equals(fi.findById(id).getName()))){
            return "exists";
        }

        Fish fishToSave = fi.findById(id);

        fishToSave.setName(fish.getName());
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



        List<Integer> intListS = new ArrayList<>();
        for(String n : listN){
            int i = Months.valueOf(n).getOrder() + 6;
            if(i > 12){
                i -= 12;
            }
            intListS.add(i);
        }
        Collections.sort(intListS);

        List<String> listS = new ArrayList<>();

        for(int i : intListS){
            listS.add(Months.values()[i-1].name());
        }


        String mlistS = "";
        for(int i=0; i < listS.size() - 1; i++){
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() - 1);

        fishToSave.setMonthListS(mlistS);



        List<Integer> list = fish.getHourList();
        int x = list.get(0);
        int y = x;
        String hlist = "";
        if(list.size() == 1){
            hlist += x + ":00 - " + (y+1) + ":00";
        }
        else{
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
        }
        fishToSave.setHourList(hlist);

        fishToSave.setFilename(file.getOriginalFilename());

        fi.save(fishToSave);
        return "saved";
    }

}
