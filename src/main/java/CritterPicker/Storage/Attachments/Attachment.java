package CritterPicker.Storage.Attachments;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Attachment")
@Table(name = "attachment")
@Data
public class Attachment {
    @Id
    private int id;

    private String filename;
}
