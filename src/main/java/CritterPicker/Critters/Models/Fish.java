package CritterPicker.Critters.Models;

import CritterPicker.Enums.LocationFish;
import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import CritterPicker.User.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "Fish")
@Table(name = "fish")
@Data
public class Fish {
    @Id
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String quote;

    @Enumerated(EnumType.STRING)
    private LocationFish location;

    private boolean onlyInRain;

    private int price;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Enumerated(EnumType.STRING)
    private ShadowSize shadowSize;

    private String monthListN;

    private String monthListS;

    private String hourList;

    private String filename;

    @ManyToMany
    Set<AppUser> users;
}