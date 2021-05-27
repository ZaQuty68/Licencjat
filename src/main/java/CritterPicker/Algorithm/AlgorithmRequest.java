package CritterPicker.Algorithm;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmRequest {
    private List<Integer> hoursToday;
    private List<Integer> hoursTomorrow;
    private boolean Raining;
    private boolean CJOnIsland;
}
