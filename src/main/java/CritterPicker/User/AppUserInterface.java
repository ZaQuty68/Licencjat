package CritterPicker.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserInterface extends JpaRepository<AppUser, Integer> {

    List<AppUser> findAll();

    AppUser findById(int id);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

}
