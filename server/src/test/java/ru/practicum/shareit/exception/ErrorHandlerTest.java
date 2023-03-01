package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handleNullException() {
        ErrorResponse errorResponse = errorHandler.handleNullException(new NullPointerException("NullPointerException"));

        assertEquals(errorResponse.getMessage(), "NullPointerException");
    }

    @Test
    void handleException() {
        ErrorResponse errorResponse = errorHandler.handleException(new ValidationException("ValidationException"));

        assertEquals(errorResponse.getMessage(), "ValidationException");
    }

    @Test
    void handleDuplicateException() {
        ErrorResponse errorResponse = errorHandler.handleDuplicateException(new DuplicateException("DuplicateException"));

        assertEquals(errorResponse.getMessage(), "DuplicateException");
    }

}