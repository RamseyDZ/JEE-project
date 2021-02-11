package fr.noel.project.service.impl;


import fr.noel.project.dto.CategorieDto;
import fr.noel.project.dto.ContentResponseDto;
import fr.noel.project.dto.PageableResponse;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.entities.Categorie;
import fr.noel.project.repositories.CategoriesRepository;
import fr.noel.project.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    @Autowired
    private CategoriesRepository categoriesRepository;


    @Override
    public ResponseDto addNewCategorie(CategorieDto categorie) {
        Categorie cat = new Categorie();
        cat.setName(categorie.getName());
        try {
            cat = this.categoriesRepository.save(cat);
            return new ContentResponseDto(true, "OK", CategorieDto.toCategorieDto(cat));
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }

    }

    @Override
    public ResponseDto deleteCategorie(Long id) {
        try {
            this.categoriesRepository.deleteById(id);
            return new ResponseDto(true, "OK");
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto allCategories(int page, int max) {
        final PageRequest of = PageRequest.of(page, max);
        final Page<Categorie> all = this.categoriesRepository.findAll(of);
        final List<CategorieDto> collect = all.getContent().stream().map(CategorieDto::toCategorieDto).collect(Collectors.toList());
        return new PageableResponse(true, "OK", collect, all.getTotalElements(), all.getTotalPages());
    }

    @Override
    public ResponseDto updateCategorie(CategorieDto catInput) {
        final Optional<Categorie> byId = this.categoriesRepository.findById(catInput.getId());
        if (byId.isPresent()) {
            final Categorie cate = byId.get();
            if (catInput.getName() != null && !catInput.equals("")) {
                cate.setName(catInput.getName());
            }
            this.categoriesRepository.save(cate);
            return new ResponseDto(true, "OK");

        } else {
            return new ResponseDto(false, "ID INCORRECT");
        }
    }

    @Override
    public ResponseDto findOneById(Long id) {
        final Optional<Categorie> byId = this.categoriesRepository.findById(id);
        if (byId.isPresent()){
            final Categorie categorie = byId.get();
            return new ContentResponseDto(true,"OK",CategorieDto.toCategorieDto(categorie));
        }
        else {
            return new ResponseDto(false,"NOT FOUND");
        }

    }
}
