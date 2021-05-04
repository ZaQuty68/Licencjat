/*
package CritterPicker.Controller;

import CritterPicker.Critters.Managers.BugManager;
import CritterPicker.Critters.Managers.FishManager;
import CritterPicker.Critters.Managers.SeaCreatureManager;
//import CritterPicker.Managers.UserManager;
import CritterPicker.Critters.Models.Bug;
import CritterPicker.Critters.Models.Fish;
import CritterPicker.Critters.Models.SeaCreature;
//import CritterPicker.Models.User;
import CritterPicker.DTO.FishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WebControllerApi {

    @Autowired
    FishManager fm;
    @Autowired
    BugManager bm;
    @Autowired
    SeaCreatureManager sm;

    @GetMapping("/api/hello")
    public  String sayHello(){ return "Hello World!"; }

    ///////////////////////////////////////////////////////      FISH
    @GetMapping("/api/fish")
    public List<Fish> getFish(){ return fm.findAll(); }

    @GetMapping("/api/fish/{id}")
    public Fish getFishById(@PathVariable int id){ return fm.findById(id); }

    @PostMapping("/api/fish")
    public List<Fish> addFish(@RequestBody FishDTO fish){
        fm.addFish(fish);
        return fm.findAll();
    }

    @DeleteMapping("/api/fish/delete/{id}")
    public void deleteFishById(@PathVariable int id){
        fm.deleteById(id);
        getFish();
    }


    ///////////////////////////////////////////////////////      BUG
    @GetMapping("/api/bug")
    public List<Bug> getBug(){ return bm.findAll(); }

    @GetMapping("/api/bug/{id}")
    public Bug getBugById(@PathVariable int id){ return bm.findById(id); }

    @PostMapping("/api/bug")
    public List<Bug> addBug(@RequestBody Bug bug){
        bm.addBug(bug);
        return bm.findAll();
    }

    @DeleteMapping("/api/bug/delete/{id}")
    public void deleteBugById(@PathVariable int id){
        bm.deleteById(id);
        getBug();
    }


    ///////////////////////////////////////////////////////      SEA CREATURE
    @GetMapping("/api/seaCreature")
    public List<SeaCreature> getSeaCreature(){ return sm.findAll(); }

    @GetMapping("/api/seaCreature/{id}")
    public SeaCreature getSeaCreatureById(@PathVariable int id){ return sm.findById(id); }

    @PostMapping("/api/seaCreature")
    public List<SeaCreature> addSeaCreature(@RequestBody SeaCreature seaCreature){
        sm.addSeaCreature(seaCreature);
        return sm.findAll();
    }

    @DeleteMapping("/api/seaCreature/delete/{id}")
    public void deleteSeaCreatureById(@PathVariable int id){
        sm.deleteById(id);
        getSeaCreature();
    }


    ////////////////////////////////////////////////       USER
    /*@PostMapping("api/user")
    public  List<User> addUser(@RequestBody User user){
        um.addUser(user);
        return um.findAll();
    }
}

 */