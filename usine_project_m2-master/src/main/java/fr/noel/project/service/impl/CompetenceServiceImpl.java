package fr.noel.project.service.impl;


import fr.noel.project.dto.CompetenceDto;
import fr.noel.project.dto.ContentResponseDto;
import fr.noel.project.dto.PageableResponse;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.entities.Competence;
import fr.noel.project.repositories.CompetenceRepository;
import fr.noel.project.service.CompetenceService;
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
public class CompetenceServiceImpl implements CompetenceService {

    @Autowired
    private CompetenceRepository competenceRepository;

    @Override
    public ResponseDto addNewCompetence(CompetenceDto dto) {
        Competence competence = new Competence();
        competence.setName(dto.getName());
        try {
            final Competence save = competenceRepository.save(competence);
            return new ContentResponseDto(true,"OK",CompetenceDto.toCompetenceDto(save));
        } catch (Exception e) {
            return new ResponseDto(false,e.getMessage());
        }

    }

    @Override
    public ResponseDto deleteCompetence(Long id) {
        try {
            this.competenceRepository.deleteById(id);
            return new ResponseDto(true,"OK");
        } catch (Exception e) {
            return new ResponseDto(false,e.getMessage());
        }
    }

    @Override
    public ResponseDto allCompetence(int page, int max) {
        final PageRequest of = PageRequest.of(page, max);
        final Page<Competence> all = this.competenceRepository.findAll(of);
        final List<CompetenceDto> collect = all.getContent().stream().map(CompetenceDto::toCompetenceDto).collect(Collectors.toList());
        return new PageableResponse(true, "OK", collect, all.getTotalElements(), all.getTotalPages());
    }

    @Override
    public ResponseDto updateCompetence(CompetenceDto dto) {
        final Optional<Competence> byId = this.competenceRepository.findById(dto.getId());
        if (byId.isPresent()) {
            Competence competence = byId.get();
            if (dto.getName() != null && !dto.getName().equals("")) {
                competence.setName(dto.getName());
            }
            competence = this.competenceRepository.save(competence);
            return new ContentResponseDto(true, "OK", CompetenceDto.toCompetenceDto(competence));
        } else {
            return new ResponseDto(false, "ID_INTROUVABLE");
        }
    }

    @Override
    public ResponseDto findOneById(Long id) {
        final Optional<Competence> byId = this.competenceRepository.findById(id);
        if(byId.isPresent()){
            final Competence competence = byId.get();
            return new ContentResponseDto(true,"OK",CompetenceDto.toCompetenceDto(competence));
        }
        else {
            return new ResponseDto(false,"NOT FOUND");
        }
    }
}
