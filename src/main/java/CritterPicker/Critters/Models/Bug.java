package CritterPicker.Critters.Models;

import CritterPicker.Enums.Rarity;
import CritterPicker.User.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Bug")
@Table(name = "bug")
@Getter
@Setter
public class Bug {
    @Id
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String spawningCondition;

    private boolean availableInRain;

    private int price;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    private String monthListN;

    private String monthListS;

    private String hourList;

    private String filename;

    @ManyToMany(mappedBy = "bugSet", fetch = FetchType.LAZY)
    Set<AppUser> users = new HashSet<>();
}
