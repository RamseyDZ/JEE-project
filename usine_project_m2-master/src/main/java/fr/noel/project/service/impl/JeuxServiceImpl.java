package fr.noel.project.service.impl;


import fr.noel.project.dto.*;
import fr.noel.project.entities.Categorie;
import fr.noel.project.entities.Commande;
import fr.noel.project.entities.Competence;
import fr.noel.project.entities.Jeux;
import fr.noel.project.repositories.CategoriesRepository;
import fr.noel.project.repositories.CommandeRepository;
import fr.noel.project.repositories.CompetenceRepository;
import fr.noel.project.repositories.JeuxRepository;
import fr.noel.project.service.JeuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class JeuxServiceImpl implements JeuxService {

    @Autowired
    private JeuxRepository jeuxRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private CompetenceRepository competenceRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    @Override
    public ResponseDto addNewJeux(JeuxDto dto) {
        try {
            Jeux jeux = new Jeux();
            jeux.setName(dto.getName());
            jeux.setDuree(dto.getDuree());
            jeux.setArchived(false);
            jeux.setCategorie(categoriesRepository.findById(dto.getCategorieId()).orElse(null));
            final List<CompetenceDto> competences = dto.getCompetences();
            if (competences != null && competences.size() > 0) {
                final List<Long> ids = competences.stream().map(CompetenceDto::getId).collect(Collectors.toList());
                final List<Competence> compList = this.competenceRepository.findByIdIn(ids);
                jeux.setCompetences(compList);
            }
            jeux = this.jeuxRepository.save(jeux);
            return new ContentResponseDto(true, "OK", JeuxDto.toJeuxDto(jeux));
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto deleteJeux(Long id) {
        try {
            final Optional<Jeux> byId = this.jeuxRepository.findById(id);
            final Jeux jeux = byId.get();
            jeux.setArchived(true);
            this.jeuxRepository.save(jeux);
            return new ResponseDto(true, "OK");
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto allJeux(int page, int max) {
        final PageRequest of = PageRequest.of(page, max);
        final Page<Jeux> all = this.jeuxRepository.findAll(of);
        final List<JeuxDto> collect = all.getContent().stream().map(JeuxDto::toJeuxDto).collect(Collectors.toList());
        return new PageableResponse(true, "OK", collect, all.getTotalElements(), all.getTotalPages());
    }

    @Override
    public ResponseDto updateJeux(JeuxDto dto) {
        final Optional<Jeux> byId = this.jeuxRepository.findById(dto.getId());
        if (byId.isPresent()) {
            Jeux jeux = byId.get();
            if (dto.getName() != null && !dto.getName().equals("")) {
                jeux.setName(dto.getName());
            }
            if (dto.getDuree() != null) {
                jeux.setDuree(dto.getDuree());
            }
            if (dto.isArchived() != jeux.isArchived()) {
                jeux.setArchived(dto.isArchived());
            }
            if (dto.getCategorieId() != null) {
                final Optional<Categorie> catById = this.categoriesRepository.findById(dto.getCategorieId());
                final Categorie cat = catById.get();
                jeux.setCategorie(cat);
            }
            final List<CompetenceDto> compList = dto.getCompetences();
            if (compList != null && compList.size() > 0) {
                final List<Long> ids = compList.stream().map(CompetenceDto::getId).collect(Collectors.toList());
                final List<Competence> newListComp = this.competenceRepository.findByIdIn(ids);
                jeux.setCompetences(newListComp);
            }
            jeux = this.jeuxRepository.save(jeux);
            return new ContentResponseDto(true, "OK", JeuxDto.toJeuxDto(jeux));
        } else {
            return new ResponseDto(false, "ID_INTROUVABLE");
        }
    }

    @Override
    public Boolean isGameCreationFinished(JeuxDto jeuxDto) {
        return null;
    }

    @Override
    public Boolean isGameCreationFinished(Long id) {
        return null;
    }

    @Override
    public ResponseDto findOneById(Long id) {
        final Optional<Jeux> byId = this.jeuxRepository.findById(id);
        if (byId.isPresent()) {
            final Jeux jeux = byId.get();
            return new ContentResponseDto(true,"OK",JeuxDto.toJeuxDto(jeux));
        } else {
            return new ResponseDto(false,"NOT FOUND");
        }
    }

/*    @Override
    public Boolean isGameCreationFinished(JeuxDto jeuxDto) {
        final Instant dateFin = Instant.parse(jeuxDto.getDateFin());
        return dateFin.isBefore(Instant.now());
    }

    @Override
    public Boolean isGameCreationFinished(Long id) {
        final Optional<Jeux> byId = this.jeuxRepository.findById(id);
        if(byId.isPresent()){
            final Jeux tempGame = byId.get();
            return tempGame.getDateFin().isBefore(Instant.now());
        }
        return false;
    }*/
}
