package CritterPicker.Algorithm;

import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import CritterPicker.Enums.Hemisphere;
import CritterPicker.User.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AlgorithmManager {
    private final FishManager fm;
    private final BugManager bm;
    private final SeaCreatureManager scm;

    public List<String> Money(AppUser user){
        boolean flag = false;
        if(user.getHemisphere().equals(Hemisphere.SOUTH)){
            flag = true;
        }

        List<FishDTO> allFish = fm.toDTOList(fm.findAll(), flag);

        List<FishDTO> fishRiver = new ArrayList<>();
        for(FishDTO fish : allFish){

        }

        return null;
    }

    /////////////////PLAN
    /*
    0.wszystkie ponieższe kroki wykonywane dla każdej kolejnej podanej
    przez użytkownika godziny

    1. Algorytm bierze wszystkie AKTUALNIE DOSTĘPNE! crittery
    i rozdziela je na lokacje (bug i seacreature jako pojedyńcze lokacje)
    z robakami jest problem, nie pojawiają się one z jakąkolwiek konsystencją,
    aby niektóre się pojawiły trzeba spełnić specjalne warunki, dlatego raczej
    wykluczę je z algorytmu

    2. Obliczenie średniej wartości zarabianych pieniędzy dla każdej rzadkości
    w każdej lokacji

    3. Uwzględnienie prawdopodobieństwa konkretnych rzadkości (jeszcze nie wiem
    jak byłoby najlepiej)

    4. Obliczenie ile razy gracz dałby radę złapać crittera w danej lokacji
    w przeciągu godziny oraz pomnorzenie przez otrzymany w pkt 3 wynik
     */
}
