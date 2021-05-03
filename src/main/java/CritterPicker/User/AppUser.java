package CritterPicker.User;

import CritterPicker.Critters.Models.Bug;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Critters.Models.SeaCreature;
import CritterPicker.Enums.Hemisphere;
import CritterPicker.Enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity(name = "AppUser")
@Table(name = "appUser")
@Data
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

    @ManyToMany
    Set<Fish> fishSet;

    @ManyToMany
    Set<Bug> bugSet;

    @ManyToMany
    Set<SeaCreature> seaCreatureSet;

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
