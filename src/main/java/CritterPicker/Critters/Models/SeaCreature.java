package CritterPicker.Critters.Models;

import CritterPicker.Enums.Rarity;
import CritterPicker.Enums.ShadowSize;
import CritterPicker.User.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "SeaCreature")
@Table(name = "seaCreature")
@Getter
@Setter
public class SeaCreature {
    @Id
    private int id;

    @Column(length = 50)
    private String name;

    private int price;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Enumerated(EnumType.STRING)
    private ShadowSize shadowSize;

    private String monthListN;

    private String monthListS;

    private String hourList;

    private String filename;

    @ManyToMany(mappedBy = "seaCreatureSet", fetch = FetchType.LAZY)
    Set<AppUser> users = new HashSet<>();
}
