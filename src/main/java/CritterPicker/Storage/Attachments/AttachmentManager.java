package CritterPicker.Storage.Attachments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentManager{

    private final AttachmentInterface ai;

    public Attachment addAttachment(String filename){
        Attachment attachmentToSave = new Attachment();
        int id;
        if(ai.findAll().isEmpty()){
            id = 1;
        }
        else{
            id = ai.findAll().get(0).getId();
            for(Attachment attachmentId : ai.findAll()){
                if(attachmentId.getId() > id){
                    id = attachmentId.getId();
                }
            }
            id ++;
        }
        attachmentToSave.setId(id);
        attachmentToSave.setFilename(filename);
        ai.save(attachmentToSave);
        return ai.findById(id).get();
    }

    public void deleteById(int id){ ai.deleteById(id); }
}
