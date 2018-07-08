package phuong.jsp.chatGroup.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.exceptions.TemplateInputException;
import phuong.jsp.chatGroup.configuration.Layout;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @Layout(value = "default", title = "403")
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        ex.printStackTrace ();
        log.error (ex.toString ());
        model.addAttribute ("error", ex.getStackTrace ());
        return "403";
    }

    @Layout(value = "default", title = "AccessDeniedException")
    @ExceptionHandler(AccessDeniedException.class)
    public String AccessDeniedException(Exception ex, Model model) {
        ex.printStackTrace ();
        model.addAttribute ("error", "Access is denied");
        return "403";
    }

    @Layout(value = "default", title = "An error happened during template parsing")
    @ExceptionHandler(TemplateInputException.class)
    public String TemplateInputException(Exception ex, Model model) {
        ex.printStackTrace ();
        model.addAttribute ("error", "Access is denied");
        return "403";
    }

    @RequestMapping(value = "/404", produces = "text/html")
    public String render404(Model model) {
        // Add model attributes
        return "403";
    }
}
