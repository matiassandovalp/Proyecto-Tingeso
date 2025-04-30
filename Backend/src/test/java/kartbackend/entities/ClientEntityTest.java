// src/test/java/kartbackend/entities/ClientEntityTest.java
package kartbackend.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientEntityTest {

    @Test
    public void testSetAndGet() {
        ClientEntity client = new ClientEntity();
        client.setClientId("C001");
        client.setNombreCliente("Juan Pérez");
        client.setVisitasMensuales(5);

        assertEquals("C001", client.getClientId());
        assertEquals("Juan Pérez", client.getNombreCliente());
        assertEquals(5, client.getVisitasMensuales());
    }
}