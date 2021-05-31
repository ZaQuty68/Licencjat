package CritterPicker.Algorithm;

import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.Critters.DTO.SeaCreatureDTO;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import CritterPicker.Enums.AlgorithmOption;
import CritterPicker.Enums.Hemisphere;
import CritterPicker.Enums.LocationFish;
import CritterPicker.Enums.Months;
import CritterPicker.User.AppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlgorithmManager {
    private final FishManager fm;
    private final SeaCreatureManager scm;

    private final int rare = 1;
    private final int scarce = 4;
    private final int uncommon = 5;
    private final int fairlyCommon = 8;
    private final int common = 17;
    private final int raritySum = rare + scarce + uncommon + fairlyCommon + common;

    private final int plusDays = 1;

    public AlgorithmManager(FishManager fm, SeaCreatureManager scm){
        this.fm = fm;
        this.scm = scm;
    }

    public  List<AlgorithmResponsePrepared> Calculate(AppUser user, AlgorithmRequest request){
        List<AlgorithmResponse> responses = new ArrayList<>();
        boolean southHemisphere = user.getHemisphere().equals(Hemisphere.SOUTH);

        List<FishDTO> fishes;
        List<SeaCreatureDTO> seaCreatures;
        List<FishDTO> allFish = new ArrayList<>();
        List<SeaCreatureDTO> allSeaCreature = new ArrayList<>();
        if(request.getOption().equals(AlgorithmOption.Money)){
            fishes = fm.toDTOList(fm.findAll(), southHemisphere);
            seaCreatures = scm.toDTOList(scm.findAll(), southHemisphere);
        }
        else{
            allFish = fm.toDTOList(fm.findAll(), southHemisphere);
            allSeaCreature = scm.toDTOList(scm.findAll(), southHemisphere);
            fishes = fm.toDTOList(fm.findOtherFish(user), southHemisphere);
            seaCreatures = scm.toDTOList(scm.findOtherSeaCreatures(user), southHemisphere);
        }

        String today = LocalDateTime.now().getMonth().toString();
        String tomorrow = LocalDateTime.now().plusDays(plusDays).getMonth().toString();
        int startingHour = LocalDateTime.now().getHour();

        for (int i = startingHour; i < 24; i++) {
            List<FishDTO> fishRiver = getFishList(fishes, i, LocationFish.River, today, request.isRaining());
            List<FishDTO> fishPond = getFishList(fishes, i, LocationFish.Pond, today, request.isRaining());
            List<FishDTO> fishClifftop = getFishList(fishes, i, LocationFish.Clifftop, today, request.isRaining());
            fishClifftop.addAll(fishRiver);
            List<FishDTO> fishMouth = getFishList(fishes, i, LocationFish.Mouth, today, request.isRaining());
            fishMouth.addAll(fishRiver);
            List<FishDTO> fishSea = getFishList(fishes, i, LocationFish.Sea, today, request.isRaining());
            List<FishDTO> fishPier = getFishList(fishes, i, LocationFish.Pier, today, request.isRaining());
            fishPier.addAll(fishSea);

            List<SeaCreatureDTO> seaCreatureList = getSeaCreatureList(seaCreatures, i, today);

            float averageRiver, averagePond, averageClifftop, averageMouth, averageSea, averagePier, averageSeaCreature;

            if(request.getOption().equals(AlgorithmOption.Money)){
                averageRiver = getAveragePriceFish(fishRiver, request.isCJOnIsland());
                averagePond = getAveragePriceFish(fishPond, request.isCJOnIsland());
                averageClifftop = getAveragePriceFish(fishClifftop, request.isCJOnIsland());
                averageMouth = getAveragePriceFish(fishMouth, request.isCJOnIsland());
                averageSea = getAveragePriceFish(fishSea, request.isCJOnIsland());
                averagePier = getAveragePriceFish(fishPier, request.isCJOnIsland());

                averageSeaCreature = getAveragePriceSeaCreature(seaCreatureList);
            }
            else{
                List<FishDTO> allRiver = getFishList(allFish, i, LocationFish.River, today, request.isRaining());
                List<FishDTO> allPond = getFishList(allFish, i, LocationFish.Pond, today, request.isRaining());
                List<FishDTO> allClifftop = getFishList(allFish, i, LocationFish.Clifftop, today, request.isRaining());
                allClifftop.addAll(allRiver);
                List<FishDTO> allMouth = getFishList(allFish, i, LocationFish.Mouth, today, request.isRaining());
                allMouth.addAll(allRiver);
                List<FishDTO> allSea = getFishList(allFish, i, LocationFish.Sea, today, request.isRaining());
                List<FishDTO> allPier = getFishList(allFish, i, LocationFish.Pier, today, request.isRaining());
                allPier.addAll(allSea);

                List<SeaCreatureDTO> seaCreatureAllList = getSeaCreatureList(allSeaCreature, i, today);


                averageRiver = getAveragePriorityFish(fishRiver, allRiver, today);
                averagePond = getAveragePriorityFish(fishPond, allPond, today);
                averageClifftop = getAveragePriorityFish(fishClifftop, allClifftop, today);
                averageMouth = getAveragePriorityFish(fishMouth, allMouth, today);
                averageSea = getAveragePriorityFish(fishSea, allSea, today);
                averagePier = getAveragePriorityFish(fishPier, allPier, today);
                averageSeaCreature = getAveragePrioritySeaCreature(seaCreatureList, seaCreatureAllList, today);
            }

            float max = averageRiver;
            float maxNoBait = averageRiver;
            String maxLocation = "River";
            String maxLocationNoBait = "River";
            if (averagePond > max) {
                max = averagePond;
                maxLocation = "Pond";
            }
            if (averageClifftop > max) {
                max = averageClifftop;
                maxLocation = "Clifftop";
            }
            if (averageMouth > max) {
                max = averageMouth;
                maxLocation = "Mouth";
            }
            if (averageSea > max) {
                max = averageSea;
                maxLocation = "Sea";
            }
            if (averageSea > maxNoBait) {
                maxNoBait = averageSea;
                maxLocationNoBait = "Sea";
            }
            if (averagePier > max) {
                max = averagePier;
                maxLocation = "Pier";
            }
            if (averageSeaCreature > max) {
                maxLocation = "SeaCreature";
            }
            if (averageSeaCreature > maxNoBait) {
                maxLocationNoBait = "SeaCreature";
            }

            responses.add(makeResponse(maxLocation, maxLocationNoBait, i, true));
        }

        for (int i = 0; i < 24; i++) {
            List<FishDTO> fishRiver = getFishList(fishes, i, LocationFish.River, tomorrow, false);
            List<FishDTO> fishPond = getFishList(fishes, i, LocationFish.Pond, tomorrow, false);
            List<FishDTO> fishClifftop = getFishList(fishes, i, LocationFish.Clifftop, tomorrow, false);
            fishClifftop.addAll(fishRiver);
            List<FishDTO> fishMouth = getFishList(fishes, i, LocationFish.Mouth, tomorrow, false);
            fishMouth.addAll(fishRiver);
            List<FishDTO> fishSea = getFishList(fishes, i, LocationFish.Sea, tomorrow, false);
            List<FishDTO> fishPier = getFishList(fishes, i, LocationFish.Pier, tomorrow, false);
            fishPier.addAll(fishSea);

            List<SeaCreatureDTO> seaCreatureList = getSeaCreatureList(seaCreatures, i, tomorrow);

            float averageRiver, averagePond, averageClifftop, averageMouth, averageSea, averagePier, averageSeaCreature;

            if(request.getOption().equals(AlgorithmOption.Money)){
                averageRiver = getAveragePriceFish(fishRiver, false);
                averagePond = getAveragePriceFish(fishPond, false);
                averageClifftop = getAveragePriceFish(fishClifftop, false);
                averageMouth = getAveragePriceFish(fishMouth, false);
                averageSea = getAveragePriceFish(fishSea, false);
                averagePier = getAveragePriceFish(fishPier, false);

                averageSeaCreature = getAveragePriceSeaCreature(seaCreatureList);
            }
            else{
                List<FishDTO> allRiver = getFishList(allFish, i, LocationFish.River, tomorrow, false);
                List<FishDTO> allPond = getFishList(allFish, i, LocationFish.Pond, tomorrow, false);
                List<FishDTO> allClifftop = getFishList(allFish, i, LocationFish.Clifftop, tomorrow, false);
                allClifftop.addAll(allRiver);
                List<FishDTO> allMouth = getFishList(allFish, i, LocationFish.Mouth, tomorrow, false);
                allMouth.addAll(allRiver);
                List<FishDTO> allSea = getFishList(allFish, i, LocationFish.Sea, tomorrow, false);
                List<FishDTO> allPier = getFishList(allFish, i, LocationFish.Pier, tomorrow, false);
                allPier.addAll(allSea);

                List<SeaCreatureDTO> seaCreatureAllList = getSeaCreatureList(allSeaCreature, i, tomorrow);



                averageRiver = getAveragePriorityFish(fishRiver, allRiver, tomorrow);
                averagePond = getAveragePriorityFish(fishPond, allPond, tomorrow);
                averageClifftop = getAveragePriorityFish(fishClifftop, allClifftop, tomorrow);
                averageMouth = getAveragePriorityFish(fishMouth, allMouth, tomorrow);
                averageSea = getAveragePriorityFish(fishSea, allSea, tomorrow);
                averagePier = getAveragePriorityFish(fishPier, allPier, tomorrow);
                averageSeaCreature = getAveragePrioritySeaCreature(seaCreatureList, seaCreatureAllList, tomorrow);
            }


            float max = averageRiver;
            float maxNoBait = averageRiver;
            String maxLocation = "River";
            String maxLocationNoBait = "River";
            if (averagePond > max) {
                max = averagePond;
                maxLocation = "Pond";
            }
            if (averageClifftop > max) {
                max = averageClifftop;
                maxLocation = "Clifftop";
            }
            if (averageMouth > max) {
                max = averageMouth;
                maxLocation = "Mouth";
            }
            if (averageSea > max) {
                max = averageSea;
                maxLocation = "Sea";
            }
            if (averageSea > maxNoBait) {
                maxNoBait = averageSea;
                maxLocationNoBait = "Sea";
            }
            if (averagePier > max) {
                max = averagePier;
                maxLocation = "Pier";
            }
            if (averageSeaCreature > max) {
                maxLocation = "SeaCreature";
            }
            if (averageSeaCreature > maxNoBait) {
                maxLocationNoBait = "SeaCreature";
            }

            responses.add(makeResponse(maxLocation, maxLocationNoBait, i, false));
        }

        return prepareResponses(responses);

    }

    private List<FishDTO> getFishList(List<FishDTO> listFish, int i, LocationFish location, String month, boolean isRaining) {
        List<FishDTO> fishToReturn = new ArrayList<>();
        for (FishDTO fish : listFish) {
            if (fish.getLocation().equals(location) && (!fish.isOnlyInRain() || (fish.isOnlyInRain() && isRaining))) {
                boolean flagMonth = false;
                for (String s : fish.getMonthListN()) {
                    if (s.equalsIgnoreCase(month)) {
                        flagMonth = true;
                        break;
                    }
                }
                if (flagMonth) {
                    boolean flagHour = false;
                    for (int j : fish.getHourList()) {
                        if (j == i) {
                            flagHour = true;
                            break;
                        }
                    }
                    if (flagHour) {
                        fishToReturn.add(fish);
                    }
                }
            }
        }
        return fishToReturn;
    }

    private List<SeaCreatureDTO> getSeaCreatureList(List<SeaCreatureDTO> listSeaCreature, int i, String month) {
        List<SeaCreatureDTO> seaCreatureToReturn = new ArrayList<>();
        for (SeaCreatureDTO seaCreature : listSeaCreature) {
            boolean flagMonth = false;
            for (String s : seaCreature.getMonthListN()) {
                if (s.equalsIgnoreCase(month)) {
                    flagMonth = true;
                    break;
                }
            }
            if (flagMonth) {
                boolean flagHour = false;
                for (int j : seaCreature.getHourList()) {
                    if (j == i) {
                        flagHour = true;
                        break;
                    }
                }
                if (flagHour) {
                    seaCreatureToReturn.add(seaCreature);
                }
            }
        }
        return seaCreatureToReturn;
    }

    private float getPoolFish(List<FishDTO> listFish) {
        float pool = 0;
        for (FishDTO fish : listFish) {
            switch (fish.getRarity()) {
                case Rare:
                    pool += rare;
                    break;
                case Scarce:
                    pool += scarce;
                    break;
                case Uncommon:
                    pool += uncommon;
                    break;
                case FairlyCommon:
                    pool += fairlyCommon;
                    break;
                case Common:
                    pool += common;
                    break;
            }
        }
        return pool;
    }

    private float getPoolSeaCreature(List<SeaCreatureDTO> listSeaCreature) {
        float pool = 0;
        for (SeaCreatureDTO seaCreature : listSeaCreature) {
            switch (seaCreature.getRarity()) {
                case Rare:
                    pool += rare;
                    break;
                case Scarce:
                    pool += scarce;
                    break;
                case Uncommon:
                    pool += uncommon;
                    break;
                case FairlyCommon:
                    pool += fairlyCommon;
                    break;
                case Common:
                    pool += common;
                    break;
            }
        }
        return pool;
    }

    private float getAveragePriceFish(List<FishDTO> listFish, boolean isCJ) {
        float pool = getPoolFish(listFish);
        float sum = 0;
        for (FishDTO fish : listFish) {
            int x = 0;
            switch (fish.getRarity()) {
                case Rare:
                    x = rare;
                    break;
                case Scarce:
                    x = scarce;
                    break;
                case Uncommon:
                    x = uncommon;
                    break;
                case FairlyCommon:
                    x = fairlyCommon;
                    break;
                case Common:
                    x = common;
                    break;
            }
            sum += x * fish.getPrice();
        }
        if (isCJ) {
            sum = sum * 1.5f;
        }
        return sum / pool;
    }

    private float getAveragePriceSeaCreature(List<SeaCreatureDTO> listSeaCreature) {
        float pool = getPoolSeaCreature(listSeaCreature);
        float sum = 0;
        for (SeaCreatureDTO seaCreature : listSeaCreature) {
            int x = 0;
            switch (seaCreature.getRarity()) {
                case Rare:
                    x = rare;
                    break;
                case Scarce:
                    x = scarce;
                    break;
                case Uncommon:
                    x = uncommon;
                    break;
                case FairlyCommon:
                    x = fairlyCommon;
                    break;
                case Common:
                    x = common;
                    break;
            }
            sum += x * seaCreature.getPrice();
        }
        return sum / pool;
    }

    private float getAveragePriorityFish(List<FishDTO> listFish, List<FishDTO> listForPool, String month){
        float pool = getPoolFish(listForPool);
        float sum = 0;
        for (FishDTO fish : listFish){
            int monthsLeft = 0;
            int y = -1;
            for(String m : fish.getMonthListN()){
                if(m.equalsIgnoreCase(month)){
                    y = Months.valueOf(m).getOrder();
                }
                if(Months.valueOf(m).getOrder() == y + 1){
                    monthsLeft ++;
                    y ++;
                }
                if(y == 12){
                    y = 0;
                    for (String m2 : fish.getMonthListN()){
                        if(monthsLeft != 11){
                            if(Months.valueOf(m2).getOrder() == y + 1){
                                monthsLeft ++;
                                y ++;
                            }
                        }
                    }
                }
            }

            switch (fish.getRarity()) {
                case Rare:
                    sum += rare * ((raritySum - rare) + (12-monthsLeft) * 10);
                    break;
                case Scarce:
                    sum += scarce * ((raritySum - scarce) + (12-monthsLeft) * 10);
                    break;
                case Uncommon:
                    sum += uncommon * ((raritySum - uncommon) + (12-monthsLeft) * 10);
                    break;
                case FairlyCommon:
                    sum += fairlyCommon * ((raritySum - fairlyCommon) + (12-monthsLeft) * 10);
                    break;
                case Common:
                    sum += common * ((raritySum - common) + (12-monthsLeft) * 10);
                    break;
            }
        }
        return sum / pool;
    }

    private float getAveragePrioritySeaCreature(List<SeaCreatureDTO> listSeaCreature, List<SeaCreatureDTO> listForPool,  String month){
        float pool = getPoolSeaCreature(listForPool);
        float sum = 0;
        for (SeaCreatureDTO seaCreature : listSeaCreature){
            int monthsLeft = 0;
            int y = -1;
            for(String m : seaCreature.getMonthListN()){
                if(m.equalsIgnoreCase(month)){
                    y = Months.valueOf(m).getOrder();
                }
                if(Months.valueOf(m).getOrder() == y + 1){
                    monthsLeft ++;
                    y ++;
                }
                if(y == 12){
                    y = 0;
                    for (String m2 : seaCreature.getMonthListN()){
                        if(monthsLeft != 11){
                            if(Months.valueOf(m2).getOrder() == y + 1){
                                monthsLeft ++;
                                y ++;
                            }
                        }
                    }
                }
            }

            switch (seaCreature.getRarity()) {
                case Rare:
                    sum += rare * ((raritySum - rare) + (12-monthsLeft) * 10);
                    break;
                case Scarce:
                    sum += scarce * ((raritySum - scarce) + (12-monthsLeft) * 10);
                    break;
                case Uncommon:
                    sum += uncommon * ((raritySum - uncommon) + (12-monthsLeft) * 10);
                    break;
                case FairlyCommon:
                    sum += fairlyCommon * ((raritySum - fairlyCommon) + (12-monthsLeft) * 10);
                    break;
                case Common:
                    sum += common * ((raritySum - common) + (12-monthsLeft) * 10);
                    break;
            }

        }
        return sum / pool;
    }

    private AlgorithmResponse makeResponse(String location, String locationNoBait, int hour, boolean today) {
        AlgorithmResponse response = new AlgorithmResponse();
        response.setHour(hour);
        response.setToday(today);
        String s = "";
        switch (location) {
            case "River":
                s += "Fishing in a river.";
                break;
            case "Pond":
                s += "Fishing in ponds. Note that fishing in ponds without fish bait isn't really effective, so if you don't have any, next best thing is ";
                switch (locationNoBait) {
                    case "River":
                        s += "fishing in river.";
                        break;
                    case "Sea":
                        s += "fishing in the sea.";
                        break;
                    case "SeaCreature":
                        s += "diving in the sea for sea creatures.";
                        break;
                }
                break;
            case "Clifftop":
                s += "Fishing in rivers on clifftop. Note that fishing in river on clifftop without fish bait isn't really effective, so if you don't have any, next best thing is ";
                switch (locationNoBait) {
                    case "River":
                        s += "fishing in river.";
                        break;
                    case "Sea":
                        s += "fishing in the sea.";
                        break;
                    case "SeaCreature":
                        s += "diving in the sea for sea creatures.";
                        break;
                }
                break;
            case "Mouth":
                s += "Fishing in river mouths. Note that fishing in river mouths without fish bait isn't really effective, so if you don't have any, next best thing is ";
                switch (locationNoBait) {
                    case "River":
                        s += "fishing in river.";
                        break;
                    case "Sea":
                        s += "fishing in the sea.";
                        break;
                    case "SeaCreature":
                        s += "diving in the sea for sea creatures.";
                        break;
                }
                break;
            case "Sea":
                s += "Fishing in the sea.";
                break;
            case "Pier":
                s += "Fishing on a sea pier. Note that fishing on a sea pier without fish bait isn't really effective, so if you don't have any, next best thing is ";
                switch (locationNoBait) {
                    case "River":
                        s += "fishing in river.";
                        break;
                    case "Sea":
                        s += "fishing in the sea.";
                        break;
                    case "SeaCreature":
                        s += "diving in the sea for sea creatures.";
                        break;
                }
                break;
            case "SeaCreature":
                s += "Diving in the sea for sea creatures.";
                break;
        }
        response.setText(s);
        return response;
    }

    private List<AlgorithmResponsePrepared> prepareResponses(List<AlgorithmResponse> responses){
        List<AlgorithmResponsePrepared> responsesToReturn = new ArrayList<>();
        int x = responses.get(0).getHour();
        for(int i = 1; i < responses.size(); i++){
            AlgorithmResponse temp = responses.get(i);
            AlgorithmResponse previous = responses.get(i-1);
            if(temp.isToday() != previous.isToday() || !temp.getText().equals(previous.getText()) || temp.getHour() != previous.getHour() + 1){
                AlgorithmResponsePrepared prepared = new AlgorithmResponsePrepared();
                prepared.setToday(previous.isToday());
                prepared.setText(previous.getText());
                prepared.setHours(x + ":00 - " + (previous.getHour()+1) + ":00");
                responsesToReturn.add(prepared);
                x = temp.getHour();
            }
            if(i == responses.size()-1){
                AlgorithmResponsePrepared prepared = new AlgorithmResponsePrepared();
                prepared.setToday(temp.isToday());
                prepared.setText(temp.getText());
                prepared.setHours(x + ":00 - " + (temp.getHour()+1) + ":00");
                responsesToReturn.add(prepared);
            }
        }
        return responsesToReturn;
    }
}
