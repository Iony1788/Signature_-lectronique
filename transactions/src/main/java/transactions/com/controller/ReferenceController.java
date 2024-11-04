package transactions.com.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("pages/saisieReference")  
public class ReferenceController {

    
    


    @GetMapping 
    public ModelAndView showForm() {
        return new ModelAndView("/pages/infoUser");
    }
    
    
    //
    
    
    


    
}
