
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");
        Object exception = request.getAttribute("javax.servlet.error.exception");
        
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        if (exception != null) {
            model.addAttribute("exception", exception.toString());
        }
        
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
