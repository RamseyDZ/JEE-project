package fr.noel.project.service.impl;


import fr.noel.project.service.CommandeService;
import fr.noel.project.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class DashboardServiceImpl implements DashboardService {


    @Autowired
    private AppUserServiceImpl userService;

    @Autowired
    private CommandeService commandeService;


    ///- nombre de commandes en cours
    //- nombre de lutins occupés
    //- nombre de lutins libres
    //- nombre total de commandes

    @Override
    public Map<String, Object> getAppState(Long adminId) {
        final Map<String, Long> stateUsers = userService.stateUsers(adminId);
        final Map<String, Long> statesCommande = commandeService.statesCommande();
        return Stream.concat(statesCommande.entrySet().stream(),stateUsers.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
