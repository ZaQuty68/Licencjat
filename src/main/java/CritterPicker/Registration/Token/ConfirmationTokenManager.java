package CritterPicker.Registration.Token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenManager {

    private final ConfirmationTokenInterface ci;

    public void saveConfirmationToken(ConfirmationToken token){
        ConfirmationToken tokenToSave = new ConfirmationToken();
        tokenToSave.setId(getNewId());
        tokenToSave.setToken(token.getToken());
        tokenToSave.setCreatedAt(token.getCreatedAt());
        tokenToSave.setExpiresAt(token.getExpiresAt());
        tokenToSave.setAppUser(token.getAppUser());
        
        ci.save(tokenToSave);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return ci.findByToken(token);
    }

    public int setConfirmedAt(String token){
        return ci.updateConfirmedAt(token, LocalDateTime.now());
    }

    public int getNewId(){
        int id;
        if(ci.findAll().isEmpty()){
            id = 1;
        }
        else{
            id = ci.findAll().get(0).getId();
            for (ConfirmationToken tokenId : ci.findAll()){
                if(tokenId.getId() > id){
                    id = tokenId.getId();
                }
            }
            id++;
        }
        return id;
    }
}
