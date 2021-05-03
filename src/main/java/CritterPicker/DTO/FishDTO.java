package CritterPicker.DTO;

import CritterPicker.Enums.LocationFish;
import CritterPicker.Enums.Months;
import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FishDTO {
    @NotNull(message = "This field is required")
    @Size(min = 3, max = 50, message = "Fish name should be between 3 to 50 characters long")
    private String name;

    @NotNull(message = "This field is required")
    @Size(min = 10, max = 500, message = "Quote should be between 10 to 500 characters long")
    private String quote;

    private LocationFish location;

    private boolean onlyInRain;

    @NotNull(message = "This field is required")
    @Min(value = 10, message = "Minimal price is 10")
    @Max(value = 20000, message = "Maximal price is 20000")
    private int price;

    private Rarity rarity;

    private ShadowSize shadowSize;

    @NotNull(message = "This field is required")
    private List<Months> monthsListN;

    @NotNull(message = "This field is required")
    private List<Months> monthListS;

    @NotNull(message = "This field is required")
    private List<Integer> hourList;
}
