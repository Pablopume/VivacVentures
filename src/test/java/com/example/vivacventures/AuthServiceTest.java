package com.example.vivacventures;

import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.modelo.exceptions.MailIncorrectoException;
import com.example.vivacventures.domain.modelo.exceptions.YaExisteException;
import com.example.vivacventures.domain.servicios.MandarMail;
import com.example.vivacventures.domain.servicios.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MandarMail mandarMail;

    @InjectMocks
    private UserService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testRegister_Success() throws MessagingException {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setUsername("testuser");

        when(userEntityMapper.toUserEntity(userRegisterDTO)).thenReturn(userEntity);
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        boolean result = authService.register(userRegisterDTO);

        // Assert
        assertTrue(result);
        verify(userRepository).save(userEntity);
        verify(mandarMail).generateAndSendEmail(
                eq("test@example.com"),
                anyString(),
                eq("VivacVentures. Autenticación de Usuario")
        );
    }

    @Test
    void testRegister_InvalidEmail() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("invalidEmail");
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setPassword("password");

        // Act & Assert
        assertThrows(MailIncorrectoException.class, () -> authService.register(userRegisterDTO));
    }

    @Test
 void testRegister_NotExistingUsername() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setUsername("existinguser");
        userRegisterDTO.setPassword("password");

        when(userRepository.findByUsername("existinguser")).thenReturn(new UserEntity());

        // Act & Assert
        assertThrows(YaExisteException.class, () -> authService.register(userRegisterDTO));
    }

    @Test
    public void testRegister_ExistingEmail() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("existing@example.com");
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setPassword("password");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(new UserEntity());

        // Act & Assert
        assertThrows(YaExisteException.class, () -> authService.register(userRegisterDTO));
    }
}
