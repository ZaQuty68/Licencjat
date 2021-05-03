package CritterPicker.Critters.Models;

import CritterPicker.Attachments.Attachment;
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

@Entity(name = "SeaCreature")
@Table(name = "seaCreature")
@Data
public class SeaCreature {
    @Id
    private int id;
    @NotNull(message = "This field is required")
    @Size(min = 3, max = 50, message = "Sea Creature name should be between 3 to 50 characters long")
    @Column(length = 50)
    private String name;

    @NotNull(message = "This field is required")
    @Size(min = 10, max = 500, message = "Quote should be between 10 to 500 characters long")
    @Column(length = 500)
    private String quote;

    @NotNull(message = "This field is required")
    @Size(min = 3, max = 500, message = "Swimming pattern should be between 3 and 500 characters long")
    @Column(length = 500)
    private String swimmingPattern;

    @NotNull(message = "This field is required")
    @Min(value = 10, message = "Minimal price is 10")
    @Max(value = 20000, message = "Maximal price is 20000")
    private int price;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Enumerated(EnumType.STRING)
    private ShadowSize shadowSize;

    @NotNull(message = "This field is required")
    private String monthListN;

    @NotNull(message = "This field is required")
    private String monthListS;

    @NotNull(message = "This field is required")
    private String hourList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment", referencedColumnName = "id")
    private Attachment attachment;

    @ManyToMany
    Set<AppUser> users;
}
