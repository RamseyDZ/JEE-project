package fr.noel.project.controller;


import fr.noel.project.dto.JeuxDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.service.JeuxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/jeux")
@Slf4j
public class JeuxController {

    @Autowired
    private JeuxService jeuxService;

    @GetMapping
    public ResponseDto getAllJeux(@RequestParam(name = "page") int page,
                                  @RequestParam(name = "max") int max) {
        log.info("GET ALL JEUX PAGE {} MAX {} ", page, max);
        return this.jeuxService.allJeux(page, max);
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseDto addOrUpdateJeux(@RequestBody JeuxDto dto) {
        if (dto.getId() == null) {
            log.info("ADD NEW JEUX {} ", dto);
            return this.jeuxService.addNewJeux(dto);
        } else {
            log.info("UPDATE JEUX ID {} ", dto.getId());
            return this.jeuxService.updateJeux(dto);
        }
    }

    @PostMapping("/{id}")
    public ResponseDto archiveJeux(@PathVariable Long id) {
        log.info("DELETE JEUX ID {} ", id);
        return this.jeuxService.deleteJeux(id);

    }

    @GetMapping("/{id}")
    public ResponseDto findOne(@PathVariable Long id) {
        log.info("FIND ONE JEUX BY ID {} ", id);
        return this.jeuxService.findOneById(id);
    }
}
