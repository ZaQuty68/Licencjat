package CritterPicker.User;

import CritterPicker.Critters.Models.Bug;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Critters.Models.SeaCreature;
import CritterPicker.Enums.Hemisphere;
import CritterPicker.Enums.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity(name = "AppUser")
@Table(name = "appUser")
@Getter
@Setter
@NoArgsConstructor
public class AppUser implements UserDetails {

    @Id
    private int id;
    @Column(length = 256)
    private String username;
    @Column(length = 256)
    private String password;
    @Column(length = 256)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private Hemisphere hemisphere;

    private Boolean enabled = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_fish",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "fish_id")
    )
    Set<Fish> fishSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_bug",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "bug_id")
    )
    Set<Bug> bugSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_sea_creature",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "sea_creature_id")
    )
    Set<SeaCreature> seaCreatureSet = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
