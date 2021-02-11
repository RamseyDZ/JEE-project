package fr.noel.project.service.impl;


import fr.noel.project.dto.ContentResponseDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.dto.TacheDto;
import fr.noel.project.entities.AppUser;
import fr.noel.project.entities.Competence;
import fr.noel.project.entities.Jeux;
import fr.noel.project.entities.Tache;
import fr.noel.project.repositories.AppUserRepository;
import fr.noel.project.repositories.CommandeRepository;
import fr.noel.project.repositories.JeuxRepository;
import fr.noel.project.repositories.TacheRepository;
import fr.noel.project.service.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TacheServiceImpl implements TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private JeuxRepository jeuxRepository;

    @Override
    public ResponseDto addNewTache(TacheDto dto,Long idCommande) {
        try {
            Tache tache = new Tache();
            tache.setDateAffectation(Instant.now());
            tache.setCommande(this.commandeRepository.findById(idCommande).get());
            tache.setLutin(this.userRepository.findById(dto.getLutin().getId()).get());
            tache.setJeux(this.jeuxRepository.findById(dto.getJeux().getId()).get());
            tache = this.tacheRepository.save(tache);
            return new ContentResponseDto(true, "OK", TacheDto.toTacheDto(tache));
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }

    }

    @Override
    public Tache addNewTacheEntity(TacheDto dto, Long idCommande) {
        final AppUser appUser = this.userRepository.findById(dto.getLutin().getId()).get();
        final Jeux jeux = this.jeuxRepository.findById(dto.getJeux().getId()).get();
        final Instant now = Instant.now();
        Tache tache = new Tache();
        tache.setDateAffectation(now);
        tache.setLutin(appUser);
        tache.setJeux(jeux);
        tache.setCommande(this.commandeRepository.findById(idCommande).get());
        final int duree = jeux.getDuree();
        final int nbrComptManq = hasCompetances2(appUser, jeux);
        if (nbrComptManq == 0) {
            Duration duration = Duration.ofMinutes(duree);
            final Instant plus = now.plus(duration);
            tache.setDateFin(plus);
        } else {
            Duration duration = Duration.ofMinutes(duree + Math.round(0.1 * nbrComptManq * duree));
            final Instant plus = now.plus(duration);
            tache.setDateFin(plus);
        }
        return this.tacheRepository.save(tache);
    }

    private boolean hasCompetances(AppUser appUser, Jeux jeux) {
        final List<Competence> gameCompetences = jeux.getCompetences();
        final List<Competence> userCompetences = appUser.getCompetences();
        if (userCompetences == null || userCompetences.size() == 0) {
            return false;
        }
        if (userCompetences.size() > jeux.getCompetences().size()) {
            return gameCompetences.stream().allMatch(r -> userCompetences.contains(r));
        } else {
            return userCompetences.stream().allMatch(r -> gameCompetences.contains(r));
        }
    }

    private int hasCompetances2(AppUser appUser, Jeux jeux) {
        final List<Competence> gameCompetences = jeux.getCompetences();
        final List<Competence> userCompetences = appUser.getCompetences();
        int nbrCompManquant = 0;
        if (userCompetences == null || userCompetences.size() == 0) {
            return gameCompetences.size();
        }
        for (Competence g : gameCompetences) {
            if (!userCompetences.contains(g)) {
                nbrCompManquant++;
            }
        }
        return nbrCompManquant;
    }



    @Override
    public ResponseDto deleteTache(Long id) {
        try {
            this.tacheRepository.deleteById(id);
            return new ResponseDto(true, "OK");
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto allTaches(int page, int max, long idCommande) {
        try {
            final PageRequest pageRequest = PageRequest.of(page, max);
            final Page<Tache> taches = this.tacheRepository.findByCommande_Id(idCommande, pageRequest);
            final List<TacheDto> tacheDtoList = taches.stream().map(TacheDto::toTacheDto).collect(Collectors.toList());
            return new ContentResponseDto(true, "OK", tacheDtoList);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto updateTache(TacheDto dto) {

        final Optional<Tache> byId = this.tacheRepository.findById(dto.getId());
        if (byId.isPresent()) {
            Tache tache = byId.get();
            AppUser appUser=null;
            Jeux jeux=null;
            if (dto.getLutin() != null && dto.getLutin().getId() != null) {
                appUser = userRepository.findById(dto.getLutin().getId()).get();
                tache.setLutin(appUser);
            }
            if (dto.getJeux() != null && dto.getJeux().getId() != null) {
                jeux = jeuxRepository.findById(dto.getJeux().getId()).get();
                tache.setJeux(jeux);
            }
            final int duree = jeux.getDuree();
            final Instant now = Instant.now();
            tache.setDateAffectation(now);
            final int nbrComptManq = hasCompetances2(appUser, jeux);
            if (nbrComptManq == 0) {
                Duration duration = Duration.ofMinutes(duree);
                final Instant plus = now.plus(duration);
                tache.setDateFin(plus);
            } else {
                Duration duration = Duration.ofMinutes(duree + Math.round(0.1 * nbrComptManq * duree));
                final Instant plus = now.plus(duration);
                tache.setDateFin(plus);
            }
            tache = this.tacheRepository.save(tache);
            return new ContentResponseDto(true,"OK",TacheDto.toTacheDto(tache));
        } else {
            return new ResponseDto(false, "ID INCORRECT");
        }
    }

    @Override
    public int maxDureeInTache(long idCommande) {
        return this.tacheRepository.findMaxDuree(idCommande);
    }

    @Override
    public List<Tache> allTachesByUser(long idUser) {
        return this.tacheRepository.findByLutin_Id(idUser);
    }
}
