package fr.noel.project.service;

import fr.noel.project.dto.JeuxDto;
import fr.noel.project.dto.ResponseDto;

public interface JeuxService {

    ResponseDto addNewJeux(JeuxDto dto);

    ResponseDto deleteJeux(Long id);

    ResponseDto allJeux(int page, int max);

    ResponseDto updateJeux(JeuxDto dto);

    Boolean isGameCreationFinished(JeuxDto jeuxDto);

    Boolean isGameCreationFinished(Long id);

    ResponseDto findOneById(Long id);

}

