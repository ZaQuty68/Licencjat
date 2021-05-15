package CritterPicker.Algorithm;

import CritterPicker.Critters.Interfaces.FishInterface;
import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public class Algorithm {
    private final FishManager fm;
    private final BugManager bm;
    private final SeaCreatureManager scm;


    public Algorithm(FishManager fm, BugManager bm, SeaCreatureManager scm){
        this.fm = fm;
        this.bm = bm;
        this.scm = scm;
    }

    /////////////////PLAN
    /*
    0.wszystkie ponieższe kroki wykonywane dla każdej kolejnej podanej
    przez użytkownika godziny

    1. Algorytm bierze wszystkie crittery AKTUALNIE DOSTĘPNE! crittery
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
