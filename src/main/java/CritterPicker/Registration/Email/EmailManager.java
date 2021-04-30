package CritterPicker.Registration.Email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailManager implements EmailSender{

    private final JavaMailSender mailSender;
    private final static Logger logger = LoggerFactory.getLogger(EmailManager.class);

    @Override
    @Async
    public void send(String to, String email){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("critter@picker.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            logger.error("Failed to send email!", e);
            throw new IllegalStateException("Failed to send email!");
        }
    }
}
