package CritterPicker.Critters.DTO;

import CritterPicker.Enums.Rarity;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BugDTO {
    @NotNull(message = "This field is required")
    @Size(min = 3, max = 50, message = "Bug name should be between 3 to 50 characters long")
    private String name;

    @NotNull(message = "This field is required")
    @Size(min = 3, max = 500, message = "Bug spawning condition should be between 3 and 500 characters long")
    private String spawningCondition;

    private boolean availableInRain;

    @NotNull(message = "This field is required")
    @Min(value = 10, message = "Minimal price is 10")
    @Max(value = 20000, message = "Maximal price is 20000")
    private int price;

    private Rarity rarity;

    private List<String> monthListN;

    private List<Integer> hourList;
}

