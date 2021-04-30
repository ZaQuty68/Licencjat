package CritterPicker.Attachments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentManager{

    public AttachmentInterface ai;

    public  void addAttachment(Attachment attachment){
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
        attachmentToSave.setFilename(attachment.getFilename());
        ai.save(attachmentToSave);

    }
}
