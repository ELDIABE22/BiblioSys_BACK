package com.example.bibliosys.Services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.User;
import com.example.bibliosys.Models.request.AuthCreateUserRequest;
import com.example.bibliosys.Models.request.AuthLoginRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.AuthResponse;
import com.example.bibliosys.Repository.UserRepository;
import com.example.bibliosys.utils.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsuario(), userEntity.getContraseña(), authorityList);
    }

    public ApiResponse<AuthResponse> registerService(AuthCreateUserRequest authRegisterRequest) {
        if (userRepository.findByUsuario(authRegisterRequest.usuario()).isPresent()) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message("El nombre de usuario ya está registrado")
                    .build();
        }

        if (userRepository.findByCorreo(authRegisterRequest.correo()).isPresent()) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message("El correo electrónico ya está registrado")
                    .build();
        }

        User userEntity = User.builder()
                .nombres(authRegisterRequest.nombres())
                .apellidos(authRegisterRequest.apellidos())
                .usuario(authRegisterRequest.usuario())
                .correo(authRegisterRequest.correo())
                .contraseña(passwordEncoder.encode(authRegisterRequest.contraseña()))
                .estado("Activo")
                .rol("Asistente")
                .build();

        User userSaved = userRepository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        String accessToken = jwtUtils.createToken(authentication);

        User userWithoutPassword = User.builder()
                .id(userSaved.getId())
                .nombres(userSaved.getNombres())
                .apellidos(userSaved.getApellidos())
                .usuario(userSaved.getUsuario())
                .correo(userSaved.getCorreo())
                .rol(userSaved.getRol())
                .estado(userSaved.getEstado())
                .build();

        AuthResponse authResponse = new AuthResponse(userWithoutPassword, accessToken);

        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .message("Usuario creado")
                .build();
    }

    public ApiResponse<AuthResponse> loginService(AuthLoginRequest authLoginRequest) {
        try {
            String username = authLoginRequest.usuario();
            String password = authLoginRequest.contraseña();

            Authentication authentication = this.authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsuario(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if (!"Activo".equalsIgnoreCase(user.getEstado())) {
                return ApiResponse.<AuthResponse>builder()
                        .data(null)
                        .message("La cuenta no esta activa")
                        .build();
            }

            // Objeto usuario sin contraseña
            User userWithoutPassword = User.builder()
                    .id(user.getId())
                    .nombres(user.getNombres())
                    .apellidos(user.getApellidos())
                    .usuario(user.getUsuario())
                    .correo(user.getCorreo())
                    .rol(user.getRol())
                    .estado(user.getEstado())
                    .build();

            String accessToken = jwtUtils.createToken(authentication);
            AuthResponse authResponse = new AuthResponse(userWithoutPassword, accessToken);

            ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                    .data(authResponse)
                    .message("Login exitoso")
                    .build();

            return apiResponse;
        } catch (BadCredentialsException e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message("Usuario o contraseña incorrectos")
                    .build();
        } catch (IllegalStateException e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ApiResponse.<AuthResponse>builder()
                    .data(null)
                    .message(e.getMessage())
                    .build();
        }
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    public boolean updatePassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByCorreo(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setContraseña(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
