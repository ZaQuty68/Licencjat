package CritterPicker.Critters.Managers;

import CritterPicker.Critters.DTO.BugDTO;
import CritterPicker.Critters.Interfaces.BugInterface;
import CritterPicker.Critters.Models.Bug;
import CritterPicker.Enums.Months;
import CritterPicker.User.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class BugManager{

    private final BugInterface bi;

    public String addBug(BugDTO bug, MultipartFile file){
        boolean bugExists = bi.findByName(bug.getName()).isPresent();
        if(bugExists){
            return "exists";
        }

        Bug bugToSave = new Bug();
        int id;
        if(bi.findAll().isEmpty()){
            id = 1;
        }
        else {
            id = bi.findAll().get(0).getId();
            for(Bug bugId : bi.findAll()){
                if(bugId.getId() > id){
                    id = bugId.getId();
                }
            }
            id++;
        }

        bugToSave.setId(id);
        bugToSave.setName(bug.getName());
        bugToSave.setSpawningCondition(bug.getSpawningCondition());
        bugToSave.setPrice(bug.getPrice());
        bugToSave.setRarity(bug.getRarity());
        bugToSave.setAvailableInRain(bug.isAvailableInRain());

        String mlistN = "";
        List<String> listN = bug.getMonthListN();
        for(int i = 0; i < listN.size() - 1; i++){
            mlistN += listN.get(i) + ", ";
        }
        mlistN += listN.get(listN.size() - 1);

        bugToSave.setMonthListN(mlistN);



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
        for(int i = 0; i < listS.size() - 1; i++){
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() -1);

        bugToSave.setMonthListS(mlistS);

        List<Integer> list = bug.getHourList();
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
        bugToSave.setHourList(hlist);

        bugToSave.setFilename(file.getOriginalFilename());

        bi.save(bugToSave);
        return "saved";
    }

    public List<Bug> findAll(){ return bi.findAll(); }

    public Bug findById(int id){ return bi.findById(id); }

    public void deleteById(int id){ bi.deleteById(id); }

    public List<Bug> findOtherBugs(AppUser user){
        List<Bug> listToReturn = new ArrayList<>();
        for(Bug bug : bi.findAll()){
            boolean flag = true;
            for(Bug b : user.getBugSet()){
                if(b == bug){
                    flag = false;
                    break;
                }
            }
            if(flag){
                listToReturn.add(bug);
            }
        }
        return listToReturn;
    }

    public BugDTO toDTO(Bug bug, boolean flag){
        BugDTO bugDTO = new BugDTO();

        bugDTO.setName(bug.getName());
        bugDTO.setSpawningCondition(bug.getSpawningCondition());
        bugDTO.setPrice(bug.getPrice());
        bugDTO.setRarity(bug.getRarity());
        bugDTO.setAvailableInRain(bug.isAvailableInRain());

        String mlistN = "";
        if(flag) {
            mlistN = bug.getMonthListS();
        }
        else {
            mlistN = bug.getMonthListN();
        }
        List<String> listN = new ArrayList<>();
        for(String n : mlistN.split(", ")){
            listN.add(n);
        }
        bugDTO.setMonthListN(listN);

        String hList = bug.getHourList();
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
        bugDTO.setHourList(hListInt);

        return bugDTO;
    }

    public List<BugDTO> toDTOList(List<Bug> bugList, boolean flag){
        List<BugDTO> DTOList = new ArrayList<>();
        for(Bug bug : bugList){
            DTOList.add(toDTO(bug, flag));
        }
        return DTOList;
    }

    public String editBug(BugDTO bug, int id, MultipartFile file){
        boolean bugExists = bi.findByName(bug.getName()).isPresent();
        if(bugExists && !(bug.getName().equals(bi.findById(id).getName()))){
            return "exists";
        }

        Bug bugToSave = bi.findById(id);

        bugToSave.setName(bug.getName());
        bugToSave.setSpawningCondition(bug.getSpawningCondition());
        bugToSave.setPrice(bug.getPrice());
        bugToSave.setRarity(bug.getRarity());
        bugToSave.setAvailableInRain(bug.isAvailableInRain());

        String mlistN = "";
        List<String> listN = bug.getMonthListN();
        for(int i = 0; i < listN.size() - 1; i++){
            mlistN += listN.get(i) + ", ";
        }
        mlistN += listN.get(listN.size() - 1);

        bugToSave.setMonthListN(mlistN);



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
        for(int i = 0; i < listS.size() - 1; i++){
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() -1);

        bugToSave.setMonthListS(mlistS);

        List<Integer> list = bug.getHourList();
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
        bugToSave.setHourList(hlist);

        bugToSave.setFilename(file.getOriginalFilename());

        bi.save(bugToSave);
        return "saved";
    }
}
