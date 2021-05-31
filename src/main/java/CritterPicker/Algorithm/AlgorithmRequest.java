package CritterPicker.Algorithm;

import CritterPicker.Enums.AlgorithmOption;
import lombok.Data;

@Data
public class AlgorithmRequest {
    private boolean raining;
    private boolean CJOnIsland;
    private AlgorithmOption option;
}
