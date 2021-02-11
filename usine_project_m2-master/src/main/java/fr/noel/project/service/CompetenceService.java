package fr.noel.project.service;

import fr.noel.project.dto.CompetenceDto;
import fr.noel.project.dto.ResponseDto;

public interface CompetenceService {

    ResponseDto addNewCompetence(CompetenceDto dto);

    ResponseDto deleteCompetence(Long id);

    ResponseDto allCompetence(int page, int max);

    ResponseDto updateCompetence(CompetenceDto dto);

    ResponseDto findOneById(Long id);

}
