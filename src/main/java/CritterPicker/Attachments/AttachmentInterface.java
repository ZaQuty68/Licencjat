package CritterPicker.Attachments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentInterface extends JpaRepository<Attachment, Integer> {

    Optional<Attachment> findById(int id);

    void deleteById(int id);
}
