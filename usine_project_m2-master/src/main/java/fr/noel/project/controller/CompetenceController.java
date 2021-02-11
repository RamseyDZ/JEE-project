package fr.noel.project.controller;

import fr.noel.project.dto.CompetenceDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.service.CompetenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/competence")
@Slf4j
public class CompetenceController {

    @Autowired
    private CompetenceService competenceService;

    @GetMapping
    public ResponseDto getAllCompetences(@RequestParam(name = "page") int page,
                                         @RequestParam(name = "max") int max) {
        log.info("GET ALL COMPETENCE PAGE {} MAX {} ", page, max);
        return this.competenceService.allCompetence(page, max);
    }

    @PostMapping
    public ResponseDto addOrUpdateCompetence(@RequestBody CompetenceDto dto) {
        if (dto.getId() == null) {
            log.info("ADD NEW COMPETENCE {} ", dto);
            return this.competenceService.addNewCompetence(dto);
        } else {
            log.info("UPDATE COMPETENCE ID {} ", dto.getId());
            return this.competenceService.updateCompetence(dto);
        }
    }

    @PostMapping("/{id}")
    public ResponseDto deleteCompetence(@PathVariable Long id) {
        log.info("DELETE COMPETENCE ID {} ", id);
        return this.competenceService.deleteCompetence(id);

    }

    @GetMapping("/{id}")
    public ResponseDto findOne(@PathVariable Long id) {
        log.info("FIND ONE COMPETENCE BY ID {} ", id);
        return this.competenceService.findOneById(id);
    }
}
