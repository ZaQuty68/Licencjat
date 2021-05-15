package CritterPicker.Critters.Models;

import CritterPicker.Enums.LocationFish;
import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import CritterPicker.User.AppUser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Fish")
@Table(name = "fish")
@Getter
@Setter
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

    @ManyToMany(mappedBy = "fishSet", fetch = FetchType.LAZY)
    Set<AppUser> users = new HashSet<>();
}