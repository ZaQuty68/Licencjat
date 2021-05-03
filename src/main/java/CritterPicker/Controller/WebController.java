package CritterPicker.Controller;

import CritterPicker.Attachments.AttachmentManager;
import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.DTO.FishDTO;
import CritterPicker.Enums.Months;
import CritterPicker.Registration.RegistrationManager;
import CritterPicker.Registration.RegistrationRequest;
import CritterPicker.User.AppUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class WebController {
    @Autowired
    AttachmentManager am;
    @Autowired
    BugManager bm;
    @Autowired
    FishManager fm;
    @Autowired
    SeaCreatureManager sc;
    @Autowired
    RegistrationManager rm;
    @Autowired
    AppUserManager aum;

    //////////////////////////////////////////////////////////   LOGIN
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String redirectToLogin(){ return "redirect:/login"; }

    //////////////////////////////////////////////////////////   REGISTRATION
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("req", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("req") RegistrationRequest req, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        String flag = rm.register(req);
        if (flag.equals("Username")) {
            model.addAttribute("usernameTaken", true);
            return "register";
        }
        if (flag.equals("Email")) {
            model.addAttribute("emailTaken", true);
            return "register";
        }
        return "redirect:/register/waitForConfirm";
    }

    @GetMapping("/register/waitForConfirm")
    public String register(){
        return "waitForConfirm";
    }

    @GetMapping("/register/confirm")
    public String confirm(@RequestParam("token") String token, Model model){
        String flag = rm.confirmToken(token);
        if(flag.equals("email")){
            model.addAttribute("email", true);
            return "confirm";
        }
        if(flag.equals("expired")){
            model.addAttribute("expired", true);
            return "confirm";
        }
        model.addAttribute("confirmed", true);
        return "confirm";
    }

    @GetMapping("/register/sendAgain")
    public String sendAgain(){
        return "sendAgain";
    }

    @PostMapping("/register/sendAgain")
    public String processSendAgain(String email, Model model){
        String token = aum.sendTokenAgain(email);
        if(token.equals("exist")){
            model.addAttribute("exist", true);
            return "sendAgain";
        }
        if(token.equals("email")){
            model.addAttribute("email", true);
            return "sendAgain";
        }
        rm.sendConfirmationAgain(token,email,aum.getUsername(email));
        model.addAttribute("send", true);
        return "sendAgain";
    }




    //////////////////////////////////////////////////////////   HOME PAGE
    @GetMapping("/homePage")
    public String homePage(){
        return "homePage";
    }
    //////////////////////////////////////////////////////////   ADMIN
    @GetMapping("/admin/newFish")
    public String newFish(Model model){
        model.addAttribute("fish", new Fish());
        return "newFish";
    }

    @PostMapping("/admin/newFish")
    public String processNewFish(@Valid @ModelAttribute("fish") Fish fish, Model model, BindingResult result){
        if(result.hasErrors()) {
            return "newFish";
        }
        System.out.println(fish.getName());
        //for(Months m : fish.getMonthsListN()){
        //    System.out.println(m.toString());
        //}
        return null;
    }

    //////////////////////////////////////////////////////////   LIST
    @GetMapping("/allList")
    public String allList(Model model){
        model.addAttribute("fishes", fm.findAll());
        model.addAttribute("bugs", bm.findAll());
        model.addAttribute("seaCreatures", sc.findAll());
        return "allList";
    }
}
