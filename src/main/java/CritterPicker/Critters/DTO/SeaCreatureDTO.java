package CritterPicker.Critters.DTO;

import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import CritterPicker.User.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SeaCreatureDTO {
    @NotNull(message = "This field is required")
    @Size(min = 3, max = 50, message = "Sea Creature name should be between 3 to 50 characters long")
    private String name;

    @NotNull(message = "This field is required")
    @Min(value = 10, message = "Minimal price is 10")
    @Max(value = 20000, message = "Maximal price is 20000")
    private int price;

    private Rarity rarity;

    private ShadowSize shadowSize;

    private List<String> monthListN;

    private List<Integer> hourList;
}
