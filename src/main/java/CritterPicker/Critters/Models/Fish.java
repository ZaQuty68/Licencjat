package CritterPicker.Critters.Models;

import CritterPicker.Enums.LocationFish;
import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import CritterPicker.User.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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