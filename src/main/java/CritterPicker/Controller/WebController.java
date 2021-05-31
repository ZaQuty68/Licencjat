package CritterPicker.Controller;

import CritterPicker.Algorithm.AlgorithmManager;
import CritterPicker.Algorithm.AlgorithmRequest;
import CritterPicker.Critters.DTO.BugDTO;
import CritterPicker.Critters.DTO.SeaCreatureDTO;
import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
import CritterPicker.Critters.DTO.FishDTO;
import CritterPicker.Registration.RegistrationManager;
import CritterPicker.Registration.RegistrationRequest;
import CritterPicker.Storage.StorageManager;
import CritterPicker.User.AppUser;
import CritterPicker.User.AppUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class WebController {
    @Autowired
    BugManager bm;
    @Autowired
    FishManager fm;
    @Autowired
    SeaCreatureManager scm;
    @Autowired
    RegistrationManager rm;
    @Autowired
    AppUserManager aum;
    @Autowired
    StorageManager sm;
    @Autowired
    AlgorithmManager am;

    //////////////////////////////////////////////////////////   LOGIN
    @GetMapping("/login")
    public String login(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/logout";
        }
        return "login";
    }

    @GetMapping("/")
    public String redirect(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "redirect:/homePage";
    }

    //////////////////////////////////////////////////////////   REGISTRATION
    @GetMapping("/register")
    public String register(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/logout";
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/logout";
        }
        return "waitForConfirm";
    }

    @GetMapping("/register/confirm")
    public String confirm(@RequestParam("token") String token, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/logout";
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/logout";
        }
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////   USER   //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/homePage")
    public String homePage(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((AppUser)principal).getRole().name();
        model.addAttribute("role", role);

        return "homePage";
    }

    @GetMapping("/allList")
    public String allList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showFish", true);
        model.addAttribute("showBug", true);
        model.addAttribute("showSeaCreature",true);
        model.addAttribute("fishes", fm.findAll());
        model.addAttribute("bugs", bm.findAll());
        model.addAttribute("seaCreatures", scm.findAll());
        return "allList";
    }

    @GetMapping("/userList")
    public String userList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showFish", true);
        model.addAttribute("showBug", true);
        model.addAttribute("showSeaCreature",true);
        model.addAttribute("fishes", fm.findOtherFish(user));
        model.addAttribute("bugs", bm.findOtherBugs(user));
        model.addAttribute("seaCreatures", scm.findOtherSeaCreatures(user));
        return "userList";
    }

    //////////////////////////////////////////////////////////   SMALL LISTS

    @GetMapping("/fishList")
    public String fishList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showFish", true);
        model.addAttribute("fishes", fm.findAll());
        return "allList";
    }
    @GetMapping("/bugList")
    public String bugList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showBug", true);
        model.addAttribute("bugs", bm.findAll());
        return "allList";
    }
    @GetMapping("/seaCreatureList")
    public String seaCreatureList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showSeaCreature", true);
        model.addAttribute("seaCreatures", scm.findAll());
        return "allList";
    }

    @GetMapping("/userFishList")
    public String userFishList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showFish",true);
        model.addAttribute("fishes", fm.findOtherFish(user));
        return "userList";
    }

    @GetMapping("/userBugList")
    public String userBugList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showBug", true);
        model.addAttribute("bugs", bm.findOtherBugs(user));
        return "userList";
    }

    @GetMapping("/userSeaCreatureList")
    public String userSeaCreatureList(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("showSeaCreature",true);
        model.addAttribute("seaCreatures", scm.findOtherSeaCreatures(user));
        return "userList";
    }

    //////////////////////////////////////////////////////////   ADDING AND REMOVING CRITTERS FROM USER

    @GetMapping("/userFish")
    public String userFish(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.addFish(idU, fm.findById(id));
        return "redirect:/userFishList";
    }
    @GetMapping("/userFishDelete")
    public String userFishDelete(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.removeFish(idU, fm.findById(id));
        return "redirect:/userFishList";
    }

    @GetMapping("/userBug")
    public String userBug(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.addBug(idU, bm.findById(id));
        return "redirect:/userBugList";
    }
    @GetMapping("/userBugDelete")
    public String userBugDelete(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.removeBug(idU, bm.findById(id));
        return "redirect:/userBugList";
    }

    @GetMapping("/userSeaCreature")
    public String userSeaCreature(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.addSeaCreature(idU, scm.findById(id));
        return "redirect:/userSeaCreatureList";
    }
    @GetMapping("/userSeaCreatureDelete")
    public String userSeaCreatureDelete(@RequestParam("id") int id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int idU = ((AppUser)principal).getId();
        aum.removeSeaCreature(idU, scm.findById(id));
        return "redirect:/userSeaCreatureList";
    }

    //////////////////////////////////////////////////////////   ALGORITHM
    @GetMapping("/algorithmInput")
    public String algorithmInput(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("request", new AlgorithmRequest());
        return "algorithmInput";
    }

    @PostMapping("/algorithmInput")
    public String processAlgorithmInput(@ModelAttribute("request") AlgorithmRequest request, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((AppUser)principal).getId();
        AppUser user = aum.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("option", request.getOption());
        model.addAttribute("responses", am.Calculate(user, request));
        return "algorithmOutput";
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////   ADMIN   /////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////   ADDING NEW CRITTERS
    @GetMapping("/admin/newFish")
    public String newFish(Model model){
        model.addAttribute("fish", new FishDTO());
        return "newFish";
    }

    @PostMapping("/admin/newFish")
    public String processNewFish(@Valid @ModelAttribute("fish") FishDTO fish, BindingResult result, @RequestParam("file") MultipartFile file, Model model){
        if(result.hasErrors()) {
            return "newFish";
        }
        if(sm.checkFilename(file.getOriginalFilename())){
            model.addAttribute("filenameTaken", true);
            return "newFish";
        }
        String flag = fm.addFish(fish, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            return "newFish";
        }
        sm.store(file);
        return "redirect:/fishList";
    }

    @GetMapping("/admin/newBug")
    public String newBug(Model model){
        model.addAttribute("bug", new BugDTO());
        return "newBug";
    }

    @PostMapping("/admin/newBug")
    public String processNewBug(@Valid @ModelAttribute("bug") BugDTO bug, BindingResult result, @RequestParam("file") MultipartFile file, Model model){
        if(result.hasErrors()) {
            return "newBug";
        }
        if(sm.checkFilename(file.getOriginalFilename())){
            model.addAttribute("filenameTaken", true);
            return "newBug";
        }
        String flag = bm.addBug(bug, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            return "newBug";
        }
        sm.store(file);
        return "redirect:/bugList";
    }

    @GetMapping("/admin/newSeaCreature")
    public String newSeaCreature(Model model){
        model.addAttribute("seaCreature", new SeaCreatureDTO());
        return "newSeaCreature";
    }

    @PostMapping("/admin/newSeaCreature")
    public String processNewSeaCreature(@Valid @ModelAttribute("seaCreature") SeaCreatureDTO seaCreature, BindingResult result, @RequestParam("file") MultipartFile file, Model model){
        if(result.hasErrors()) {
            return "newSeaCreature";
        }
        if(sm.checkFilename(file.getOriginalFilename())){
            model.addAttribute("filenameTaken", true);
            return "newSeaCreature";
        }
        String flag = scm.addSeaCreature(seaCreature, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            return "newSeaCreature";
        }
        sm.store(file);
        return "redirect:/seaCreatureList";
    }

    //////////////////////////////////////////////////////////   EDITING CRITTERS
    @GetMapping("/admin/editFish")
    public String editFish(Model model, @RequestParam("id") int id){
        model.addAttribute("fish", fm.toDTO(fm.findById(id), false));
        model.addAttribute("id", id);
        return "editFish";
    }

    @PostMapping("/admin/editFish")
    public String processEditFish(@Valid @ModelAttribute("fish") FishDTO fish, BindingResult result, @RequestParam("file") MultipartFile file, Model model, @RequestParam("id") int id){
        if(result.hasErrors()) {
            model.addAttribute("fish", fm.toDTO(fm.findById(id),false));
            model.addAttribute("id", id);
            return "editFish";
        }
        String oldFilename = fm.findById(id).getFilename();
        if(sm.checkFilename(file.getOriginalFilename()) && !(oldFilename.equals(file.getOriginalFilename()))){
            model.addAttribute("filenameTaken", true);
            model.addAttribute("fish", fm.toDTO(fm.findById(id),false));
            model.addAttribute("id", id);
            return "editFish";
        }
        String flag = fm.editFish(fish, id, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            model.addAttribute("fish", fm.toDTO(fm.findById(id),false));
            model.addAttribute("id", id);
            return "editFish";
        }
        sm.deleteFile(oldFilename);
        sm.store(file);
        return "redirect:/fishList";
    }

    @GetMapping("/admin/editBug")
    public String editBug(Model model, @RequestParam("id") int id){
        model.addAttribute("bug", bm.toDTO(bm.findById(id), false));
        model.addAttribute("id", id);
        return "editBug";
    }

    @PostMapping("/admin/editBug")
    public String processEditFish(@Valid @ModelAttribute("bug") BugDTO bug, BindingResult result, @RequestParam("file") MultipartFile file, Model model, @RequestParam("id") int id){
        if(result.hasErrors()) {
            model.addAttribute("bug", bm.toDTO(bm.findById(id), false));
            model.addAttribute("id", id);
            return "editBug";
        }
        String oldFilename = bm.findById(id).getFilename();
        if(sm.checkFilename(file.getOriginalFilename()) && !(oldFilename.equals(file.getOriginalFilename()))){
            model.addAttribute("filenameTaken", true);
            model.addAttribute("bug", bm.toDTO(bm.findById(id), false));
            model.addAttribute("id", id);
            return "editBug";
        }
        String flag = bm.editBug(bug, id, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            model.addAttribute("bug", bm.toDTO(bm.findById(id), false));
            model.addAttribute("id", id);
            return "editBug";
        }
        sm.deleteFile(oldFilename);
        sm.store(file);
        return "redirect:/bugList";
    }

    @GetMapping("/admin/editSeaCreature")
    public String editSeaCreature(Model model, @RequestParam("id") int id){
        model.addAttribute("seaCreature", scm.toDTO(scm.findById(id), false));
        model.addAttribute("id", id);
        return "editSeaCreature";
    }

    @PostMapping("/admin/editSeaCreature")
    public String processEditSeaCreature(@Valid @ModelAttribute("seaCreature") SeaCreatureDTO seaCreature, BindingResult result, @RequestParam("file") MultipartFile file, Model model, @RequestParam("id") int id){
        if(result.hasErrors()) {
            model.addAttribute("seaCreature", scm.toDTO(scm.findById(id), false));
            model.addAttribute("id", id);
            return "editSeaCreature";
        }
        String oldFilename = scm.findById(id).getFilename();
        if(sm.checkFilename(file.getOriginalFilename()) && !(oldFilename.equals(file.getOriginalFilename()))){
            model.addAttribute("filenameTaken", true);
            model.addAttribute("seaCreature", scm.toDTO(scm.findById(id),false));
            model.addAttribute("id", id);
            return "editSeaCreature";
        }
        String flag = scm.editSeaCreature(seaCreature, id, file);
        if(flag.equals("exists")){
            model.addAttribute("nameTaken", true);
            model.addAttribute("seaCreature", scm.toDTO(scm.findById(id),false));
            model.addAttribute("id", id);
            return "editSeaCreature";
        }
        sm.deleteFile(oldFilename);
        sm.store(file);
        return "redirect:/seaCreatureList";
    }


    //////////////////////////////////////////////////////////   DELETING CRITTERS
    @GetMapping("/admin/deleteFishConfirm")
    public String deleteFishConfirm(@RequestParam("id") int id, Model model){
        model.addAttribute("fish", fm.findById(id));
        return "deleteFishConfirm";
    }

    @GetMapping("/admin/deleteFish")
    public String deleteFish(@RequestParam("id") int id){
        sm.deleteFile(fm.findById(id).getFilename());
        aum.deleteFish(fm.findById(id));
        fm.deleteById(id);
        return "redirect:/fishList";
    }

    @GetMapping("/admin/deleteBugConfirm")
    public String deleteBugConfirm(@RequestParam("id") int id, Model model){
        model.addAttribute("bug", bm.findById(id));
        return "deleteBugConfirm";
    }

    @GetMapping("/admin/deleteBug")
    public String deleteBug(@RequestParam("id") int id){
        sm.deleteFile(bm.findById(id).getFilename());
        aum.deleteBug(bm.findById(id));
        bm.deleteById(id);
        return "redirect:/bugList";
    }

    @GetMapping("/admin/deleteSeaCreatureConfirm")
    public String deleteSeaCreatureConfirm(@RequestParam("id") int id, Model model){
        model.addAttribute("seaCreature", scm.findById(id));
        return "deleteSeaCreatureConfirm";
    }

    @GetMapping("/admin/deleteSeaCreature")
    public String deleteSeaCreature(@RequestParam("id") int id){
        sm.deleteFile(scm.findById(id).getFilename());
        aum.deleteSeaCreature(scm.findById(id));
        scm.deleteById(id);
        return "redirect:/seaCreatureList";
    }
}
