package fr.noel.project.service;

import fr.noel.project.dto.CommandeDto;
import fr.noel.project.dto.ResponseDto;

import java.util.Map;

public interface CommandeService {
    ResponseDto addNewCommande(CommandeDto commandeDto);

    ResponseDto deleteCommande(Long id);

    ResponseDto allCommande(int page, int max);

    ResponseDto updateCommande(CommandeDto commandeDto);

    boolean isCommandeFinished(Long idCommande);

    ResponseDto allNotFinishedCommande(int page, int max);

    Map<String,Long> statesCommande();

    ResponseDto findOneById(Long id);

    ResponseDto findAllByUser(Long id);


}
