package DAY37.Task003;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemoTest09Mockito {

    @Mock
    BookRepository bookRepository;

    @Spy
    @InjectMocks
    BookService bookServiceSpy;

    @Test
    void shouldReturnInStockWhenCountIsNormal() {
        // Mock repo response
        when(bookRepository.getBookCount()).thenReturn(20);

        // Call real method on spy
        String status = bookServiceSpy.getBookServiceStatus();

        // Verify interaction with spy
        verify(bookServiceSpy, times(1)).isBookCountLow();

        // Assertion
        assertEquals("IN_STOCK", status);
    }

    @Test
    void shouldReturnLowStockWhenIsBookCountLowIsStubbed() {
        // Stub spy method
        doReturn(true).when(bookServiceSpy).isBookCountLow();

        // Call method
        String status = bookServiceSpy.getBookServiceStatus();

        // Verify spy interaction
        verify(bookServiceSpy, times(1)).isBookCountLow();

        // Assertion
        assertEquals("LOW_STOCK", status);

        // BookRepository should never be called because spy method was stubbed
        verify(bookRepository, never()).getBookCount();
    }
}
