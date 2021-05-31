package CritterPicker.Critters.Managers;

import CritterPicker.Critters.DTO.SeaCreatureDTO;
import CritterPicker.Critters.Interfaces.SeaCreatureInterface;
import CritterPicker.Critters.Models.SeaCreature;
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
public class SeaCreatureManager {

    private final SeaCreatureInterface sci;

    public String addSeaCreature(SeaCreatureDTO seaCreature, MultipartFile file) {
        boolean scExists = sci.findByName(seaCreature.getName()).isPresent();
        if (scExists) {
            return "exists";
        }

        SeaCreature seaCreatureToSave = new SeaCreature();
        int id;
        if (sci.findAll().isEmpty()) {
            id = 1;
        } else {
            id = sci.findAll().get(0).getId();
            for (SeaCreature seaCreatureId : sci.findAll()) {
                if (seaCreatureId.getId() > id) {
                    id = seaCreatureId.getId();
                }
            }
            id++;
        }

        seaCreatureToSave.setId(id);
        seaCreatureToSave.setName(seaCreature.getName());
        seaCreatureToSave.setPrice(seaCreature.getPrice());
        seaCreatureToSave.setRarity(seaCreature.getRarity());
        seaCreatureToSave.setShadowSize(seaCreature.getShadowSize());

        String mlistN = "";
        List<String> listN = seaCreature.getMonthListN();
        for (int i = 0; i < listN.size() - 1; i++) {
            mlistN += listN.get(i) + ", ";
        }
        mlistN += listN.get(listN.size() - 1);

        seaCreatureToSave.setMonthListN(mlistN);


        List<Integer> intListS = new ArrayList<>();
        for (String n : listN) {
            int i = Months.valueOf(n).getOrder() + 6;
            if (i > 12) {
                i -= 12;
            }
            intListS.add(i);
        }
        Collections.sort(intListS);

        List<String> listS = new ArrayList<>();

        for (int i : intListS) {
            listS.add(Months.values()[i - 1].name());
        }


        String mlistS = "";
        for (int i = 0; i < listS.size() - 1; i++) {
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() - 1);

        seaCreatureToSave.setMonthListS(mlistS);


        List<Integer> list = seaCreature.getHourList();
        int x = list.get(0);
        int y = x;
        String hlist = "";

        if (list.size() == 1) {
            hlist += x + ":00 - " + (y + 1) + ":00";
        } else {
            for (int i = 0; i < list.size(); i++) {
                int temp = list.get(i);
                if (i == list.size() - 1) {
                    if (temp == y + 1) {
                        hlist += x + ":00 - " + (temp + 1) + ":00";
                    } else {
                        if (list.size() == 1) {
                            hlist += x + ":00 - " + (y + 1) + ":00";
                        }
                        hlist += x + ":00 - " + (y + 1) + ":00, " + temp + ":00 - " + (temp + 1) + ":00";
                    }
                } else {
                    if (temp != y + 1 && i != 0) {
                        hlist += x + ":00 - " + (y + 1) + ":00, ";
                        x = temp;
                    }
                    y = temp;
                }
            }
        }
        seaCreatureToSave.setHourList(hlist);

        seaCreatureToSave.setFilename(file.getOriginalFilename());

        sci.save(seaCreatureToSave);
        return "saved";
    }

    public List<SeaCreature> findAll() {
        return sci.findAll();
    }

    public SeaCreature findById(int id) {
        return sci.findById(id);
    }

    public void deleteById(int id) {
        sci.deleteById(id);
    }

    public List<SeaCreature> findOtherSeaCreatures(AppUser user) {
        List<SeaCreature> listToReturn = new ArrayList<>();
        for (SeaCreature seaCreature : sci.findAll()) {
            boolean flag = true;
            for (SeaCreature sc : user.getSeaCreatureSet()) {
                if (sc == seaCreature) {
                    flag = false;
                }
            }
            if (flag) {
                listToReturn.add(seaCreature);
            }
        }
        return listToReturn;
    }

    public SeaCreatureDTO toDTO(SeaCreature seaCreature, boolean flag) {
        SeaCreatureDTO seaCreatureDTO = new SeaCreatureDTO();

        seaCreatureDTO.setName(seaCreature.getName());
        seaCreatureDTO.setPrice(seaCreature.getPrice());
        seaCreatureDTO.setRarity(seaCreature.getRarity());
        seaCreatureDTO.setShadowSize(seaCreature.getShadowSize());

        String mlistN = "";
        if (flag) {
            mlistN = seaCreature.getMonthListS();
        } else {
            mlistN = seaCreature.getMonthListN();
        }
        List<String> listN = new ArrayList<>();
        for (String n : mlistN.split(", ")) {
            listN.add(n);
        }
        seaCreatureDTO.setMonthListN(listN);

        String hList = seaCreature.getHourList();
        List<Integer> hListInt = new ArrayList<>();
        for (String s : hList.split(", ")) {
            List<Integer> helper = new ArrayList<>();
            Matcher m = Pattern.compile("[0-9]{1,2}:").matcher(s);
            while (m.find()) {
                helper.add(Integer.parseInt(m.group().substring(0, m.group().length() - 1)));
            }
            int x = helper.get(0);
            int y = helper.get(1);
            for (int i = x; i < y; i++) {
                hListInt.add(i);
            }
        }
        seaCreatureDTO.setHourList(hListInt);

        return seaCreatureDTO;
    }

    public List<SeaCreatureDTO> toDTOList(List<SeaCreature> seaCreatureList, boolean flag) {
        List<SeaCreatureDTO> DTOList = new ArrayList<>();
        for (SeaCreature seaCreature : seaCreatureList) {
            DTOList.add(toDTO(seaCreature, flag));
        }
        return DTOList;
    }

    public String editSeaCreature(SeaCreatureDTO seaCreature, int id, MultipartFile file) {
        boolean seaCreatureExists = sci.findByName(seaCreature.getName()).isPresent();
        if (seaCreatureExists && !(seaCreature.getName().equals(sci.findById(id).getName()))) {
            return "exists";
        }

        SeaCreature seaCreatureToSave = sci.findById(id);

        seaCreatureToSave.setName(seaCreature.getName());
        seaCreatureToSave.setPrice(seaCreature.getPrice());
        seaCreatureToSave.setRarity(seaCreature.getRarity());
        seaCreatureToSave.setShadowSize(seaCreature.getShadowSize());

        String mlistN = "";
        List<String> listN = seaCreature.getMonthListN();
        for (int i = 0; i < listN.size() - 1; i++) {
            mlistN += listN.get(i) + ", ";
        }
        mlistN += listN.get(listN.size() - 1);

        seaCreatureToSave.setMonthListN(mlistN);


        List<Integer> intListS = new ArrayList<>();
        for (String n : listN) {
            int i = Months.valueOf(n).getOrder() + 6;
            if (i > 12) {
                i -= 12;
            }
            intListS.add(i);
        }
        Collections.sort(intListS);

        List<String> listS = new ArrayList<>();

        for (int i : intListS) {
            listS.add(Months.values()[i - 1].name());
        }


        String mlistS = "";
        for (int i = 0; i < listS.size() - 1; i++) {
            mlistS += listS.get(i) + ", ";
        }
        mlistS += listS.get(listS.size() - 1);

        seaCreatureToSave.setMonthListS(mlistS);


        List<Integer> list = seaCreature.getHourList();
        int x = list.get(0);
        int y = x;
        String hlist = "";

        if (list.size() == 1) {
            hlist += x + ":00 - " + (y + 1) + ":00";
        } else {
            for (int i = 0; i < list.size(); i++) {
                int temp = list.get(i);
                if (i == list.size() - 1) {
                    if (temp == y + 1) {
                        hlist += x + ":00 - " + (temp + 1) + ":00";
                    } else {
                        if (list.size() == 1) {
                            hlist += x + ":00 - " + (y + 1) + ":00";
                        }
                        hlist += x + ":00 - " + (y + 1) + ":00, " + temp + ":00 - " + (temp + 1) + ":00";
                    }
                } else {
                    if (temp != y + 1 && i != 0) {
                        hlist += x + ":00 - " + (y + 1) + ":00, ";
                        x = temp;
                    }
                    y = temp;
                }
            }
        }
        seaCreatureToSave.setHourList(hlist);

        seaCreatureToSave.setFilename(file.getOriginalFilename());

        sci.save(seaCreatureToSave);
        return "saved";
    }
}
