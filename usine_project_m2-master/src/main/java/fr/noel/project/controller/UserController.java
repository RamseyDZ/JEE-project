package fr.noel.project.controller;

import fr.noel.project.dto.ContentResponseDto;
import fr.noel.project.dto.JwtResponse;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.dto.UserDto;
import fr.noel.project.entities.AppUser;
import fr.noel.project.entities.UserPrincipal;
import fr.noel.project.security.JwtTokenUtil;
import fr.noel.project.service.impl.AppUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/app")
@Slf4j
public class UserController {


    @Autowired
    private AppUserServiceImpl userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseDto newUser(@RequestBody UserDto user, Authentication authentication) {
        log.info("REGISTER WEB SERVICE");
        log.info("User  " + user);
        try {
            final Long idByEmail = this.userService.findIdByEmail(authentication.getName());
            user.setCreatedBy(idByEmail);
            AppUser tempUser = this.userService.saveUser(user);
            return new ContentResponseDto(true, "OK", UserDto.toUserDto(tempUser));
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto user) {
        try {
            authenticate(user.getEmail(), user.getPassword());
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(false, e.getMessage());
            return ResponseEntity.ok(responseDto);
        }
        final UserPrincipal userDetails = (UserPrincipal) userService.loadUserByUsername(user.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(true, "OK", UserDto.toUserDto(userDetails.getUser()), token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/availableUsers", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto availableUsers(Authentication authentication) {
        try {
            final Long idByEmail = this.userService.findIdByEmail(authentication.getName());
            return this.userService.allUsers(idByEmail);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseDto findOneById(@PathVariable Long id) {
        return this.userService.findOneById(id);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto findAll(Authentication authentication) {
        try {
            final Long idByEmail = this.userService.findIdByEmail(authentication.getName());
            return this.userService.allFormatedUsers(idByEmail);
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    @PostMapping(value = "/user")
    public ResponseDto updateUser(@RequestBody UserDto dto) {
        return this.userService.updateUser(dto);
    }

    @PostMapping(value = "/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto deleteById(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }


}
