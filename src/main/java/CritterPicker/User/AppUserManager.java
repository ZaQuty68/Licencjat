package CritterPicker.User;

import CritterPicker.User.AppUserInterface;
import CritterPicker.User.AppUser;
import CritterPicker.Registration.Token.ConfirmationToken;
import CritterPicker.Registration.Token.ConfirmationTokenManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserManager implements UserDetailsService {

    private final AppUserInterface aui;
    private final ConfirmationTokenManager ctm;
    private final static String UserNotFoundMsg = "Username %s not found!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return aui.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(UserNotFoundMsg, username)));
    }

    public String singUpUser(AppUser appUser){
        boolean userExists = aui.findByUsername(appUser.getUsername()).isPresent();
        if(userExists){
            return "Username";
        }
        userExists = aui.findByEmail(appUser.getEmail()).isPresent();
        if(userExists){
            return "Email";
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        aui.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        ctm.saveConfirmationToken(confirmationToken);


        return token;
    }

    public String sendTokenAgain(String email){
        String token = UUID.randomUUID().toString();

        if(!aui.findByEmail(email).isPresent()){
            return "exist";
    }

        AppUser user = aui.findByEmail(email).get();

        if(user.isEnabled()){
            return "email";
        }

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        ctm.saveConfirmationToken(confirmationToken);

        return token;
    }

    public String getUsername(String email){
        if(aui.findByEmail(email).isPresent()){
            return aui.findByEmail(email).get().getUsername();
        }
        else return "User not found";
    }

    public int enableAppUser(String email){
        return  aui.enableAppUser(email);
    }

    public int getNewId(){
        int id;
        if(aui.findAll().isEmpty()){
            id = 1;
        }
        else{
            id = aui.findAll().get(0).getId();
            for (AppUser userId : aui.findAll()){
                if(userId.getId() > id){
                    id = userId.getId();
                }
            }
            id++;
        }
        return id;
    }
}
