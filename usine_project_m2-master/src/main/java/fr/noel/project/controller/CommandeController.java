package fr.noel.project.controller;

import fr.noel.project.dto.CommandeDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.service.CommandeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/app/commande")
@Slf4j
public class CommandeController {

    @Autowired
    private CommandeService commandeService;


    @GetMapping
    public ResponseDto getAllCommandes(@RequestParam(name = "page") int page,
                                       @RequestParam(name = "max") int max) {
        log.info("GET ALL COMMANDE PAGE {} MAX {} ", page, max);
        return this.commandeService.allCommande(page, max);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto addOrUpdateCommande(@RequestBody CommandeDto dto) {
        if (dto.getId() == null) {
            log.info("ADD NEW COMMANDE {} ", dto);
            return this.commandeService.addNewCommande(dto);
        } else {
            log.info("UPDATE COMMANDE ID {} ", dto.getId());
            return this.commandeService.updateCommande(dto);
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto deleteCommande(@PathVariable Long id) {
        log.info("DELETE COMMANDE ID {} ", id);
        return this.commandeService.deleteCommande(id);

    }

    @GetMapping("/{id}")
    public ResponseDto findOne(@PathVariable Long id) {
        log.info("FIND ONE COMMANDE BY ID {} ", id);
        return this.commandeService.findOneById(id);
    }

    @GetMapping("/user/{id}")
    public ResponseDto findByUser(@PathVariable Long id) {
        log.info("FIND ONE COMMANDE BY USER ID {} ", id);
        return this.commandeService.findAllByUser(id);
    }
}
