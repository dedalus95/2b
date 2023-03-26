package com.ema.secondbrain.security.controller;

import com.ema.secondbrain.constants.Endpoints;
import com.ema.secondbrain.constants.SecurityConstants;
import com.ema.secondbrain.entity.Role;
import com.ema.secondbrain.entity.User;
import com.ema.secondbrain.repository.RefreshTokenRepository;
import com.ema.secondbrain.repository.RoleRepository;
import com.ema.secondbrain.repository.UserRepository;
import com.ema.secondbrain.security.CustomAuthenticationManager;
import com.ema.secondbrain.security.dto.AuthResponseDTO;
import com.ema.secondbrain.security.dto.LoginDto;
import com.ema.secondbrain.security.dto.TokenRefreshResponseDto;
import com.ema.secondbrain.security.entity.RefreshToken;
import com.ema.secondbrain.utilities.JwtUtility;
import com.ema.secondbrain.utilities.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ema.secondbrain.constants.Endpoints.AUTH;

@RestController
@RequestMapping(Endpoints.API + AUTH)
public class AuthController {


    private final CustomAuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtUtility jwtUtility;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(CustomAuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtUtility jwtUtility,
                          RefreshTokenRepository refreshTokenRepository,
                          RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtility = jwtUtility;
        this.refreshTokenRepository = refreshTokenRepository;
        this.roleRepository = roleRepository;
    }


    @PostMapping("/login")
    ResponseEntity<Object> login(
            @RequestBody @Valid LoginDto loginDto
    ) {
        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            try {
                authenticationManager.authenticate(authenticationToken);
            } catch (Exception e) {
                return ResponseHandler.generateResponse("Invalid username or password!", HttpStatus.FORBIDDEN, null);
            }
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            List<String> roles = new ArrayList<>();
            user.getRoles().forEach(role -> roles.add(role.getName()));
            String token = jwtUtility.generateToken(loginDto.getUsername(), roles);
            RefreshToken refreshToken = jwtUtility.createRefreshToken(user);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO();

            authResponseDTO.setToken(token);
            authResponseDTO.setRoles(roles);
            authResponseDTO.setId(user.getId());
            authResponseDTO.setEmail(user.getUsername());
            authResponseDTO.setRefreshToken(refreshToken.getToken());
            authResponseDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
            authResponseDTO.setDuration(SecurityConstants.TOKEN_EXPIRATION);

            return ResponseHandler.generateResponse("ok", HttpStatus.OK, authResponseDTO);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.FORBIDDEN, null);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshtoken(@RequestBody @Valid String refreshToken) throws Exception {

        // Find the refresh token in the database.
        RefreshToken refreshTokenDb = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new Exception("Refresh Token non trovato"));

        // Check if the refresh token is expired.
        jwtUtility.verifyExpiration(refreshTokenDb);

        // Get the user from the refresh token.
        User userLogged = refreshTokenDb.getUser();

        Collection<String> roles = userLogged.getRoles().stream().map(Role::getName).toList();
        //Generate new Token
        String newToken = jwtUtility.generateToken(userLogged.getUsername(), roles);

        // Generate new refresh token.
        RefreshToken newRefreshToken = jwtUtility.createRefreshToken(userLogged);
        TokenRefreshResponseDto tokenRefreshResponse = new TokenRefreshResponseDto(newToken, newRefreshToken.getToken());


        // Return refresh response.
        return ResponseEntity.ok(tokenRefreshResponse);
    }

    /**
     * Create the first user in the database.
     * @return the created user
     */
    @PostMapping("/createFirstUser")
    public ResponseEntity<?> createFirstUser(HttpServletRequest req) {
        // Get ADMIN and USER role.
        Optional<Role> ruoloAdmin = roleRepository.findByName("ROLE_ADMIN");
        Optional<Role> ruoloUser = roleRepository.findByName("ROLE_USER");
        ArrayList<Role> roles = new ArrayList<>();

        // If the roles are not present, create them.
        if (ruoloAdmin.isEmpty()) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roles.add(roleRepository.save(role));
        } else {
            roles.add(ruoloAdmin.get());
        }
        if (ruoloUser.isEmpty()) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roles.add(roleRepository.save(role));
        } else {
            roles.add(ruoloUser.get());
        }


        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setEmail("ecerilli1@gmail.com");
        user.setUsername("dedalus");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(roles);
        user.setEnable(true);
        // Save the user in the database.
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("User already present!", HttpStatus.FORBIDDEN, null);
        }

        // Return the created user.
        return ResponseHandler.generateResponse("ok", HttpStatus.OK, user);

        }
    }


