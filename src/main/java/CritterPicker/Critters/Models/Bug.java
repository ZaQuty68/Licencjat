package CritterPicker.Critters.Models;

import CritterPicker.Enums.Rarity;
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
