package kz.iitu.springlab;

import kz.iitu.springlab.aspect.SlowMethodAspect;
import kz.iitu.springlab.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Lab4AspectTests {

    @Autowired CatalogService catalogService;
    @Autowired SlowMethodAspect slowMethodAspect;

    @Test
    void serviceIsCglibProxy() {
        assertTrue(AopUtils.isCglibProxy(catalogService));
    }

    @Test
    void exceptionIsNotSuppressed() {
        assertThrows(IllegalArgumentException.class, () -> catalogService.remove(0));
    }

    @Test
    void slowCallIsCollectedFastIsNot() {
        int before = slowMethodAspect.getSlowCalls().size();
        catalogService.findById(1);                 // ~50 ms, below the threshold
        assertEquals(before, slowMethodAspect.getSlowCalls().size());
        catalogService.findAll(3);                  // ~300 ms, above 200 ms
        assertEquals(before + 1, slowMethodAspect.getSlowCalls().size());
        assertEquals("CatalogService.findAll(..)",
                slowMethodAspect.getSlowCalls().getLast().method());
    }
}
