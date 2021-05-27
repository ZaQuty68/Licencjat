package CritterPicker.Algorithm;

import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.Critters.DTO.SeaCreatureDTO;
import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import CritterPicker.Enums.Hemisphere;
import CritterPicker.Enums.LocationFish;
import CritterPicker.User.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AlgorithmManager {
    private final FishManager fm;
    private final BugManager bm;
    private final SeaCreatureManager scm;

    private int rare = 5;
    private int scarce = 10;
    private int uncommon = 15;
    private int fairlyCommon = 25;
    private int common = 45;

    public List<AlgorithmResponse> Money(AppUser user, AlgorithmRequest request) {
        List<AlgorithmResponse> responses = new ArrayList<>();
        boolean southHemisphere = false;
        if (user.getHemisphere().equals(Hemisphere.SOUTH)) {
            southHemisphere = true;
        }
        List<FishDTO> allFish = fm.toDTOList(fm.findAll(), southHemisphere);
        List<SeaCreatureDTO> allSeaCreature = scm.toDTOList(scm.findAll(), southHemisphere);
        String today = LocalDateTime.now().getMonth().toString();
        String tomorrow = LocalDateTime.now().plusDays(1).getMonth().toString();


        for (int i : request.getHoursToday()) {
            List<FishDTO> fishRiver = getFishList(allFish, i, LocationFish.River, today, request.isRaining());
            List<FishDTO> fishPond = getFishList(allFish, i, LocationFish.Pond, today, request.isRaining());
            List<FishDTO> fishClifftop = getFishList(allFish, i, LocationFish.Clifftop, today, request.isRaining());
            for (FishDTO fish : fishRiver) {
                fishClifftop.add(fish);
            }
            List<FishDTO> fishMouth = getFishList(allFish, i, LocationFish.Mouth, today, request.isRaining());
            for (FishDTO fish : fishRiver) {
                fishMouth.add(fish);
            }
            List<FishDTO> fishSea = getFishList(allFish, i, LocationFish.Sea, today, request.isRaining());
            List<FishDTO> fishPier = getFishList(allFish, i, LocationFish.Pier, today, request.isRaining());
            for (FishDTO fish : fishSea) {
                fishPier.add(fish);
            }

            float averageRiver = getAverageFish(fishRiver, getPoolFish(fishRiver), request.isCJOnIsland());
            float averagePond = getAverageFish(fishPond, getPoolFish(fishPond), request.isCJOnIsland());
            float averageClifftop = getAverageFish(fishClifftop, getPoolFish(fishClifftop), request.isCJOnIsland());
            float averageMouth = getAverageFish(fishMouth, getPoolFish(fishMouth), request.isCJOnIsland());
            float averageSea = getAverageFish(fishSea, getPoolFish(fishSea), request.isCJOnIsland());
            float averagePier = getAverageFish(fishPier, getPoolFish(fishPier), request.isCJOnIsland());

            List<SeaCreatureDTO> seaCreatureList = getSeaCreatureList(allSeaCreature, i, today);
            float averageSeaCreature = getAverageSeaCreature(seaCreatureList, getPoolSeaCreature(seaCreatureList));


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
                max = averageSeaCreature;
                maxLocation = "SeaCreature";
            }
            if (averageSeaCreature > maxNoBait) {
                maxNoBait = averageSeaCreature;
                maxLocationNoBait = "SeaCreature";
            }

            responses.add(makeResponse(maxLocation, maxLocationNoBait, i, true));
        }

        for (int i : request.getHoursTomorrow()) {
            List<FishDTO> fishRiver = getFishList(allFish, i, LocationFish.River, tomorrow, request.isRaining());
            List<FishDTO> fishPond = getFishList(allFish, i, LocationFish.Pond, tomorrow, request.isRaining());
            List<FishDTO> fishClifftop = getFishList(allFish, i, LocationFish.Clifftop, tomorrow, request.isRaining());
            for (FishDTO fish : fishRiver) {
                fishClifftop.add(fish);
            }
            List<FishDTO> fishMouth = getFishList(allFish, i, LocationFish.Mouth, tomorrow, request.isRaining());
            for (FishDTO fish : fishRiver) {
                fishMouth.add(fish);
            }
            List<FishDTO> fishSea = getFishList(allFish, i, LocationFish.Sea, tomorrow, request.isRaining());
            List<FishDTO> fishPier = getFishList(allFish, i, LocationFish.Pier, tomorrow, request.isRaining());
            for (FishDTO fish : fishSea) {
                fishPier.add(fish);
            }

            float averageRiver = getAverageFish(fishRiver, getPoolFish(fishRiver), false);
            float averagePond = getAverageFish(fishPond, getPoolFish(fishPond), false);
            float averageClifftop = getAverageFish(fishClifftop, getPoolFish(fishClifftop), false);
            float averageMouth = getAverageFish(fishMouth, getPoolFish(fishMouth), false);
            float averageSea = getAverageFish(fishSea, getPoolFish(fishSea), false);
            float averagePier = getAverageFish(fishPier, getPoolFish(fishPier), false);

            List<SeaCreatureDTO> seaCreatureList = getSeaCreatureList(allSeaCreature, i, tomorrow);
            float averageSeaCreature = getAverageSeaCreature(seaCreatureList, getPoolSeaCreature(seaCreatureList));


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
                max = averageSeaCreature;
                maxLocation = "SeaCreature";
            }
            if (averageSeaCreature > maxNoBait) {
                maxNoBait = averageSeaCreature;
                maxLocationNoBait = "SeaCreature";
            }

            responses.add(makeResponse(maxLocation, maxLocationNoBait, i, false));
        }

        return responses;
    }

    private List<FishDTO> getFishList(List<FishDTO> listFish, int i, LocationFish location, String month, boolean isRaining) {
        List<FishDTO> fishToReturn = new ArrayList<>();
        for (FishDTO fish : listFish) {
            if (fish.getLocation().equals(location) && (!fish.isOnlyInRain() || (fish.isOnlyInRain() && isRaining))) {
                boolean flagMonth = false;
                for (String s : fish.getMonthListN()) {
                    if (s.equals(month)) {
                        flagMonth = true;
                    }
                }
                if (flagMonth) {
                    boolean flagHour = false;
                    for (int j : fish.getHourList()) {
                        if (j == i) {
                            flagHour = true;
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
                if (s.equals(month)) {
                    flagMonth = true;
                }
            }
            if (flagMonth) {
                boolean flagHour = false;
                for (int j : seaCreature.getHourList()) {
                    if (j == 1) {
                        flagHour = true;
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

    private float getAverageFish(List<FishDTO> listFish, float pool, boolean isCJ) {
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

    private float getAverageSeaCreature(List<SeaCreatureDTO> listSeaCreature, float pool) {
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
                s += "Fishing in ponds. Note that fishing in ponds without fish bait isn't really effective, so if You don't have any next best thing is ";
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
                s += "Fishing in rivers on clifftop. Note that fishing in river on clifftop without fish bait isn't really effective, so if You don't have any next best thing is ";
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
                s += "Fishing in river mouths. Note that fishing in river mouths without fish bait isn't really effective, so if You don't have any next best thing is ";
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
                s += "Fishing on a sea pier. Note that fishing on a sea pier without fish bait isn't really effective, so if You don't have any next best thing is ";
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
        int y = x;
        if(responses.size() == 1){
            AlgorithmResponsePrepared response = new AlgorithmResponsePrepared();
            response.setHours(x + ":00 - " + (y+1) + ":00");
            response.setText(responses.get(0).getText());
            response.setToday(responses.get(0).isToday());
        }
        for(int i = 1; i < responses.size(); i++){
            AlgorithmResponse temp = responses.get(i);
            AlgorithmResponse previous = responses.get(i-1);
            if(temp.isToday() == previous.isToday()){

            }

        }
    }
}
