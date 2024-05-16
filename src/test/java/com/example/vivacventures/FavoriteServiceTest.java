package com.example.vivacventures;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.repository.FavoritoRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import com.example.vivacventures.domain.servicios.FavoritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FavoriteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VivacPlaceRepository vivacPlaceRepository;

    @Mock
    private FavoritoRepository favoritoRepository;

    @InjectMocks
    private FavoritoService favoritoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFavorito_Success() {
        // Arrange
        String username = "pablo";
        int vivacId = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        VivacPlaceEntity vivacPlaceEntity = new VivacPlaceEntity();
        vivacPlaceEntity.setId(vivacId);

        when(userRepository.findByUsername(username)).thenReturn(userEntity);
        when(vivacPlaceRepository.getVivacPlaceEntitiesById(vivacId)).thenReturn(vivacPlaceEntity);

        // Act
        favoritoService.saveFavorito(username, vivacId);

        // Assert
        verify(favoritoRepository).save(any(FavoritoEntity.class));
    }

    @Test
    void testSaveFavorito_UserNotFound() {
        // Arrange
        String username = "s";
        int vivacId = 1;

        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(NoExisteException.class, () -> favoritoService.saveFavorito(username, vivacId));
    }

    @Test
    public void testSaveFavorito_VivacPlaceNotFound() {
        // Arrange
        String username = "pablo";
        int vivacId = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(userEntity);
        when(vivacPlaceRepository.getVivacPlaceEntitiesById(vivacId)).thenReturn(null);

        // Act & Assert
        assertThrows(NoExisteException.class, () -> favoritoService.saveFavorito(username, vivacId));
    }
}
