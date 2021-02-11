package fr.noel.project.service;

import java.util.Map;

public interface DashboardService {

    Map<String,Object> getAppState(Long adminId);
}

