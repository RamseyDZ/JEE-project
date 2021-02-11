package fr.noel.project.service;


import fr.noel.project.dto.CategorieDto;
import fr.noel.project.dto.ResponseDto;

public interface CategorieService {

    ResponseDto addNewCategorie(CategorieDto categorie);
    ResponseDto deleteCategorie(Long id);
    ResponseDto allCategories(int page,int max);
    ResponseDto updateCategorie(CategorieDto categorie);
    ResponseDto findOneById(Long id);

}
