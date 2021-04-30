package CritterPicker.Critters.Managers;

import CritterPicker.Critters.CustomInterfaces.BugInterfaceCustom;
import CritterPicker.Critters.Interfaces.BugInterface;
import CritterPicker.Critters.Models.Bug;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugManager implements BugInterfaceCustom {

    private BugInterface bi;

    public BugManager(BugInterface bi){ this.bi = bi; }

    @Override
    public void addBug(Bug bug){
        Bug bugToSave = new Bug();
        int id;
        if(bi.findAll().isEmpty()){
            id = 1;
        }
        else {
            id = bi.findAll().get(0).getId();
            for(Bug bugId : bi.findAll()){
                if(bugId.getId() > id){
                    id = bugId.getId();
                }
            }
            id++;
        }

        bugToSave.setId(id);
        bugToSave.setName(bug.getName());
        bugToSave.setQuote(bug.getQuote());
        bugToSave.setLocation(bug.getLocation());
        bugToSave.setPrice(bug.getPrice());
        bugToSave.setRarity(bug.getRarity());
        bugToSave.setAvailableInRain(bug.isAvailableInRain());
        bugToSave.setMonthListN(bug.getMonthListN());
        bugToSave.setMonthListS(bug.getMonthListS());
        bugToSave.setHourList(bug.getHourList());

        bi.save(bugToSave);
    }

    @Override
    public List<Bug> findAll(){ return bi.findAll(); }

    @Override
    public Bug findById(int id){ return bi.findById(id); }

    @Override
    public void deleteById(int id){ bi.deleteById(id); }
}
