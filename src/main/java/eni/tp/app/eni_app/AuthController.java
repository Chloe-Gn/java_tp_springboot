package eni.tp.app.eni_app;

import eni.tp.app.eni_app.bll.AuthManager;
import eni.tp.app.eni_app.bll.EniManagerResponse;
import eni.tp.app.eni_app.bo.Member;
import eni.tp.app.eni_app.ihm.EniFlashMessage;
import eni.tp.app.eni_app.ihm.EniIHMHelpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SessionAttributes({"loggedUser"})
@Controller
public class AuthController {

    @Autowired
    AuthManager authManager;

    @GetMapping("login")
    public String showLogin(Model model, RedirectAttributes redirectAttributes) {

              // Tester si deja connecté (le user "loggedUser" en session n'est pas null) retourner page d'erreur
        Member loggedUser = (Member) model.getAttribute("loggedUser");

        if (loggedUser != null) {

            // Ajouter un message temporaire (flash message)
            EniIHMHelpers.sendCommonFlashMessage(redirectAttributes, EniFlashMessage.TYPE_FLASH_ERROR, "Erreur, vous êtes déjà connecté(e)");

            return "redirect:/";
        }

        // Instancier un User vide (email et password vide)
        Member user = new Member();

        // Envoyer le user dans le Model
        model.addAttribute("user", user);

        // Afficher la page qui contient le formulaire
        return "auth/login-page";
    }

    @PostMapping("login")
    public String processLogin(@Valid @ModelAttribute (name = "user") Member user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // Mettre l'user dans la session
        model.addAttribute("loggedUser", user);

        // 1 : controle de surface
        if(bindingResult.hasErrors()){

            return "auth/login-page";
        }

        // erreur : si controle de



        // retourner la page avec les erreurs de validation (le format)





        EniManagerResponse<Member> response = authManager.authenticate(user.email, user.password);

        // 2 : controle metier (le manager)
        // erreur code 756 retourner la page avec l'erreur métier


    if(response.code.equals("7(8)6")) {
        return "/login";
    }


        // 3 : connecter l'user en session
        // Mettre l'user retrouvé en base dans la session

        model.addAttribute("loggedUser",response.data);



        //appeler la fonction ici
        EniIHMHelpers.sendCommonFlashMessage(redirectAttributes, EniFlashMessage.TYPE_FLASH_SUCCESS, "Vous etes deja connecté");


        // rediriger sur ta page d'accueil
        return "redirect:/";
    }


    @GetMapping("logout")
    public String logout(SessionStatus status, RedirectAttributes redirectAttributes) {

        //nettoyer la session (se déconnecter)
        status.setComplete();

        // appeler la fonction: EniIHMHelpers
        EniIHMHelpers.sendCommonFlashMessage(redirectAttributes, EniFlashMessage.TYPE_FLASH_SUCCESS, "Vous êtes déconnecté(e)");

        //rediriger à la page d'accueil
        return "redirect:/";


    }

};




