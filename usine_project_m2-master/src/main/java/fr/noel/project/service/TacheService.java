package fr.noel.project.service;

import fr.noel.project.dto.ResponseDto;
import fr.noel.project.dto.TacheDto;
import fr.noel.project.entities.Tache;

import java.util.List;

public interface TacheService {

    ResponseDto addNewTache(TacheDto dto,Long idCommande);

    Tache addNewTacheEntity(TacheDto dto,Long idCommande);

    ResponseDto deleteTache(Long id);

    ResponseDto allTaches(int page, int max,long idCommande);

    ResponseDto updateTache(TacheDto dto);

    int maxDureeInTache(long idCommande);

    List<Tache> allTachesByUser(long idUser);


}