/*
package CritterPicker.Registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationManager rm;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "register";
    }

    @PostMapping
    public String processRegister(@Valid RegistrationRequest request, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "register";
        }
        String flag = rm.register(request);
        if (flag.equals("Username")) {
            model.addAttribute("usernameTaken", true);
            return "register";
        }
        if (flag.equals("Email")) {
            model.addAttribute("emailTaken", true);
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("confirm")
    public String confirm(@RequestParam("token") String token) {
        return rm.confirmToken(token);
    }
}



 */