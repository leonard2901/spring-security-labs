package de.trion.training.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
public class InfoController {

   private final Logger logger = LoggerFactory.getLogger(getClass());

   @RequestMapping("/info")
   public ModelAndView info(Principal p, @AuthenticationPrincipal UserDetails principal,
     Authentication authentication, @CurrentSecurityContext SecurityContext context) {

      if (p != null) {
         logger.info("Principal name {}", p.getName());
      }

      //angemeldet und passender type-cast
      if (principal != null) {
         logger.info("Authentication principal username {}", principal.getUsername());
      }


      //angemeldet, sonst null
      if (authentication != null) {
        logger.info("{}:, principal: {}, authorities: {}", authentication.getName(), p, authentication.getAuthorities());
         var details = authentication.getDetails();
         if (details instanceof WebAuthenticationDetails w) {
            logger.info("ip: {}, session: {}", w.getRemoteAddress(), w.getSessionId());
         }
      }

      final Authentication a = context.getAuthentication();
      if (a != null) {
         final Object principalObject = a.getPrincipal();
         logger.info("{}: principal: {}, authorities: {}", a.getName(), principalObject, a.getAuthorities());
      }

      return new ModelAndView("info", Map.of("heading", "Info!"));
   }
}
