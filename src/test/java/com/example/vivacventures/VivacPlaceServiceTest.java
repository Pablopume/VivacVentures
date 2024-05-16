package com.example.vivacventures;

import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.exceptions.BadPriceException;
import com.example.vivacventures.domain.servicios.MapperService;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VivacPlaceServiceTest {

    @Mock
    private VivacPlaceRepository vivacPlaceRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private VivacPlaceService vivacPlaceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSaveVivacPlace_Correct() {
        // Arrange
        VivacPlace vivacPlace = new VivacPlace();
        vivacPlace.setPrice(100);
        VivacPlaceEntity vivacPlaceEntity = new VivacPlaceEntity();
        when(mapperService.toVivacPlaceEntity(any(VivacPlace.class))).thenReturn(vivacPlaceEntity);

        // Act
        VivacPlace result = vivacPlaceService.saveVivacPlace(vivacPlace);

        // Assert
        assertEquals(vivacPlace, result);
        verify(vivacPlaceRepository).save(vivacPlaceEntity);
    }

    @Test
     void testSaveVivacPlace_BadPrice() {
        // Arrange
        VivacPlace vivacPlace = new VivacPlace();
        vivacPlace.setPrice(-10);

        // Act & Assert
        assertThrows(BadPriceException.class, () -> vivacPlaceService.saveVivacPlace(vivacPlace));
    }
}
