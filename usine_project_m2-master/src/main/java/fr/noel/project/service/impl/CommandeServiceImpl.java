package fr.noel.project.service.impl;


import fr.noel.project.dto.*;
import fr.noel.project.entities.Commande;
import fr.noel.project.entities.Tache;
import fr.noel.project.repositories.CommandeRepository;
import fr.noel.project.service.CommandeService;
import fr.noel.project.service.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private TacheService tacheService;

    @Override
    public ResponseDto addNewCommande(CommandeDto commandeDto) {
        Commande commande = new Commande();
        commande.setComment(commandeDto.getComment());
        commande.setDateCreation(Instant.now());
        commande = this.commandeRepository.save(commande);
        try {
            final List<TacheDto> taches = commandeDto.getTaches();
            final List<Tache> entityTaches = new ArrayList<>();
            if (taches != null && taches.size() > 0) {
                for (TacheDto t : taches) {
                    final Tache tache = tacheService.addNewTacheEntity(t, commande.getId());
                    entityTaches.add(tache);
                }
            }
            commande.setTaches(entityTaches);
            final Commande save = this.commandeRepository.save(commande);
            return new ContentResponseDto(true, "OK", CommandeDto.toCommandeDto(save));
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }

    }

    @Override
    public ResponseDto deleteCommande(Long id) {
        try {
            this.commandeRepository.deleteById(id);
            return new ResponseDto(true, "OK");
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto allCommande(int page, int max) {
        try {
            final PageRequest of = PageRequest.of(page, max);
            final Page<Commande> all = this.commandeRepository.findAll(of);
            final List<CommandeDto> collect = all.getContent().stream().map(CommandeDto::toCommandeDto).collect(Collectors.toList());
            collect.forEach(c -> {
                if (this.isCommandeFinished(c.getId())) {
                    c.setStatut(StatutCommande.TERMINE.name());
                } else {
                    c.setStatut(StatutCommande.EN_COURS.name());
                }
            });
            return new PageableResponse(true, "OK", collect, all.getTotalElements(), all.getTotalPages());
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto updateCommande(CommandeDto commandeDto) {
        final Optional<Commande> byId = this.commandeRepository.findById(commandeDto.getId());
        if (byId.isPresent()) {
            Commande commande = byId.get();
            if (commandeDto.getComment() != null && !commandeDto.equals("")) {
                commande.setComment(commandeDto.getComment());
            }
            if (commandeDto.getDateCreation() != null) {
                commande.setDateCreation(commandeDto.getDateCreation());
            }
            final List<TacheDto> taches = commandeDto.getTaches();
            if(taches!=null && taches.size()>0){
                if (taches != null && taches.size() > 0) {
                    for (TacheDto t : taches) {
                        this.tacheService.updateTache(t);
                    }
                }
            }
            commande = this.commandeRepository.save(commande);
            return new ContentResponseDto(true, "OK", CommandeDto.toCommandeDto(commande));
        } else {
            return new ResponseDto(false, "ID_INTROUVABLE");
        }
    }

    @Override
    public boolean isCommandeFinished(Long idCommande) {
        final Optional<Commande> byId = this.commandeRepository.findById(idCommande);
        if (byId.isPresent()) {
            final Commande commande = byId.get();
            final int i = this.tacheService.maxDureeInTache(commande.getId());
            Duration duration = Duration.ofMinutes(i);
            Instant dateFinCommande = commande.getDateCreation().plus(duration);
            if (dateFinCommande.isBefore(Instant.now())) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ResponseDto allNotFinishedCommande(int page, int max) {
        try {
            Map<String, List<CommandeDto>> myResponseMap = new HashMap<>();
            final PageRequest of = PageRequest.of(page, max);
            final Page<Commande> all = this.commandeRepository.findAll(of);
            final List<CommandeDto> commandeDtos = all.getContent().stream().map(CommandeDto::toCommandeDto).collect(Collectors.toList());
            final Map<Boolean, List<CommandeDto>> filterCommandes = commandeDtos.stream().collect(Collectors.partitioningBy(c -> this.isCommandeFinished(c.getId())));
            myResponseMap.put("finishedCommande", filterCommandes.get(true));
            myResponseMap.put("nonFinishedCommande", filterCommandes.get(false));
            return new ContentResponseDto(true, "OK", myResponseMap);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public Map<String, Long> statesCommande() {
        Map<String, Long> nbrCommandes = new HashMap<>();
        final List<Commande> all = this.commandeRepository.findAll();
        final int activeCommande = all.stream().filter(c -> !this.isCommandeFinished(c.getId())).collect(Collectors.toList()).size();
        final Long nbrActivesCommande = Long.valueOf(activeCommande);
        final Long totalCommandes = Long.valueOf(all.size());
        nbrCommandes.put("nbrActiveCommande", nbrActivesCommande);
        nbrCommandes.put("totalCommandes", totalCommandes);
        return nbrCommandes;
    }

    @Override
    public ResponseDto findOneById(Long id) {
        final Optional<Commande> byId = this.commandeRepository.findById(id);
        if (byId.isPresent()) {
            final Commande commande = byId.get();
            final CommandeDto commandeDto = CommandeDto.toCommandeDto(commande);
            if (this.isCommandeFinished(commandeDto.getId())) {
                commandeDto.setStatut(StatutCommande.TERMINE.name());
            } else {
                commandeDto.setStatut(StatutCommande.EN_COURS.name());
            }
            return new ContentResponseDto(true, "OK", commandeDto);
        } else {
            return new ResponseDto(false, "NOT FOUND");
        }

    }

    @Override
    public ResponseDto findAllByUser(Long id) {
        try {
            final List<Tache> taches = this.tacheService.allTachesByUser(id);
            Function<Tache, CommandeDto> mappeToDTO = tache -> CommandeDto.toCommandeDto(tache.getCommande());
            final List<CommandeDto> content = taches.stream().map(mappeToDTO).collect(Collectors.toList());
            return new ContentResponseDto(true,"OK",content);
        } catch (Exception e) {
            return new ResponseDto(false,e.getMessage());
        }

    }
}
