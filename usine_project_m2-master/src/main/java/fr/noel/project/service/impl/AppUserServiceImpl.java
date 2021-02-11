package fr.noel.project.service.impl;



import fr.noel.project.dto.*;
import fr.noel.project.entities.*;
import fr.noel.project.repositories.AppUserRepository;
import fr.noel.project.repositories.CategoriesRepository;
import fr.noel.project.repositories.CompetenceRepository;
import fr.noel.project.repositories.RoleRepository;
import fr.noel.project.service.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompetenceRepository competenceRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private TacheService tacheService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        final AppUser user = this.userRepository.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("email : " + s + " is not found");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new UserPrincipal(user, authorities);
    }

    public AppUser findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public AppUser saveUser(UserDto dto) throws Exception {
        if (this.findByEmail(dto.getEmail()) != null) {
            throw new Exception("EMAIL ALREADY EXISTS");
        }
        AppUser appUser = new AppUser();
        appUser.setEmail(dto.getEmail());
        appUser.setName(dto.getName());
        appUser.setCreatedBy(dto.getCreatedBy());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        appUser.setPassword(encoder.encode(dto.getPassword()));
        appUser.setRole(this.roleRepository.findByName(dto.getRole()));
        appUser.setCreationDate(Instant.now());

        final List<CompetenceDto> userComp = dto.getCompetences();
        if (userComp != null && userComp.size() > 0) {
            final List<Long> ids = userComp.stream().map(CompetenceDto::getId).collect(Collectors.toList());
            final List<Competence> listEntityComp = this.competenceRepository.findByIdIn(ids);
            appUser.setCompetences(listEntityComp);
        }

        return this.userRepository.save(appUser);
    }

    public Long findIdByEmail(String email) {
        return this.userRepository.findIdByEmail(email);
    }



    public boolean isUserAvailable(AppUser user) {
        final List<Tache> taches = this.tacheService.allTachesByUser(user.getId());
        if (taches != null && taches.size() > 0) {
            BinaryOperator<Tache> maxTacheAccumulator = (t1, t2) -> {
                final long duration1 = t1.getDateAffectation().until(t1.getDateFin(), ChronoUnit.MINUTES);
                final long duration2 = t2.getDateAffectation().until(t2.getDateFin(), ChronoUnit.MINUTES);
                if (duration1 > duration2) {
                    return t1;
                }else {
                    return t2;
                }
            };
            final Tache maxDuree = taches.stream().reduce(taches.get(0), maxTacheAccumulator);
            if (maxDuree.getDateFin().isBefore(Instant.now())) {
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }


    public ResponseDto allUsers(Long adminId) {
        Map<String, List<UserDto>> content = new HashMap<>();
        try {
            final List<AppUser> byCreatedBy = this.userRepository.findByCreatedBy(adminId);
            final Map<Boolean, List<AppUser>> allUsers = byCreatedBy.stream().collect(Collectors.partitioningBy(this::isUserAvailable));
            final List<UserDto> availableUsers = allUsers.get(true).stream().map(UserDto::toUserDto).collect(Collectors.toList());
            final List<UserDto> notAvailableUsers = allUsers.get(false).stream().map(UserDto::toUserDto).collect(Collectors.toList());

            content.put("availaible", availableUsers);
            content.put("notAvailaible", notAvailableUsers);
            return new ContentResponseDto(true, "OK", content);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    public ResponseDto allFormatedUsers(Long adminId) {
        try {
            final List<AppUser> byCreatedBy = this.userRepository.findByCreatedBy(adminId);
            final List<UserDto> content = byCreatedBy.stream().map(UserDto::toUserDto).collect(Collectors.toList());
            return new ContentResponseDto(true, "OK", content);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    public Map<String, Long> stateUsers(Long adminId) {
        Map<String, Long> content = new HashMap<>();
        final List<AppUser> byCreatedBy = this.userRepository.findByCreatedBy(adminId);
        final Map<Boolean, List<AppUser>> allUsers = byCreatedBy.stream().collect(Collectors.partitioningBy(this::isUserAvailable));
        final List<UserDto> availableUsers = allUsers.get(true).stream().map(UserDto::toUserDto).collect(Collectors.toList());
        final List<UserDto> notAvailableUsers = allUsers.get(false).stream().map(UserDto::toUserDto).collect(Collectors.toList());
        content.put("availaibleUsers", Long.valueOf(availableUsers.size()));
        content.put("notavailaibleUsers", Long.valueOf(notAvailableUsers.size()));
        return content;
    }

    public ResponseDto findOneById(Long id){
        final Optional<AppUser> byId = this.userRepository.findById(id);
        if(byId.isPresent()){
            final AppUser appUser = byId.get();
            return new ContentResponseDto(true, "OK", UserDto.toUserDto(appUser));
        } else {
            return new ResponseDto(false, "NOT FOUND");
        }
    }

    public ResponseDto updateUser(UserDto userDto) {
        final Optional<AppUser> byId = this.userRepository.findById(userDto.getId());
        if (byId.isPresent()) {
            AppUser appUser = byId.get();

            if (userDto.getPassword() != null && !userDto.getPassword().equals("")) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                appUser.setPassword(encoder.encode(userDto.getPassword()));
            }
            if (userDto.getEmail() != null && !userDto.getEmail().equals("") && this.findByEmail(userDto.getEmail()) == null) {
                appUser.setEmail(userDto.getEmail());
            }
            if (userDto.getName() != null && !userDto.getName().equals("")) {
                appUser.setName(userDto.getName());
            }
            final List<CompetenceDto> userComp = userDto.getCompetences();
            if (userComp != null && userComp.size() > 0) {
                final List<Long> ids = userComp.stream().map(CompetenceDto::getId).collect(Collectors.toList());
                final List<Competence> listEntityComp = this.competenceRepository.findByIdIn(ids);
                appUser.setCompetences(listEntityComp);
            }
            appUser = this.userRepository.save(appUser);
            return new ContentResponseDto(true, "OK", UserDto.toUserDto(appUser));
        } else {
            return new ResponseDto(false, "NOT FOUND");
        }
    }

    public ResponseDto deleteUser(Long id) {
        try {
            this.userRepository.deleteById(id);
            return new ResponseDto(true, "OK");
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }


}
