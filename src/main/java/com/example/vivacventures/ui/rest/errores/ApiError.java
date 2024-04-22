package com.example.vivacventures.ui.rest.errores;


import java.time.LocalDateTime;

public record ApiError(String message, LocalDateTime timestamp) {

}
