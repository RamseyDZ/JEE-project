package fr.noel.project.controller;

import fr.noel.project.dto.CategorieDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.service.CategorieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/cat")
@Slf4j
public class CategorieController {

    @Autowired
    private CategorieService categorieService;


    @GetMapping
    public ResponseDto getAllCategories(@RequestParam(name = "page") int page,
                                        @RequestParam(name = "max") int max) {
        log.info("GET ALL CATEGORIES PAGE {} MAX {} ", page, max);
        return this.categorieService.allCategories(page, max);
    }
    @PostMapping
    public ResponseDto addOrUpdateCategorie(@RequestBody CategorieDto dto) {
        if(dto.getId()==null){
            log.info("ADD NEW CATEGORIES {} ", dto);
            return this.categorieService.addNewCategorie(dto);
        }
        else {
            log.info("UPDATE CATEGORIES ID {} ", dto.getId());
            return this.categorieService.updateCategorie(dto);
        }
    }

    @PostMapping("/{id}")
    public ResponseDto deleteCategorie(@PathVariable Long id) {
        log.info("DELETE CATEGORIE ID {} ", id);
        return this.categorieService.deleteCategorie(id);

    }

    @GetMapping("/{id}")
    public ResponseDto findOne(@PathVariable Long id) {
        log.info("FIND ONE CATEGORIE BY ID {} ", id);
        return this.categorieService.findOneById(id);
    }




}
