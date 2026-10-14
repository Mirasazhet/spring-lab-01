package kz.iitu.springlab;

import kz.iitu.springlab.config.AppProperties;
import kz.iitu.springlab.config.EnvironmentBanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class Lab3DevProfileTests {

    @Autowired AppProperties props;
    @Autowired EnvironmentBanner banner;

    @Test
    void devProfileOverridesSharedValues() {
        assertEquals("IT1-2411", props.group());
        assertEquals(1, props.mail().retryCount());
        assertFalse(props.mail().enabled());
        assertEquals(Duration.ofHours(8), props.security().tokenTtl());
        assertEquals(8, props.security().minPasswordLength());
        assertEquals("DEVELOPMENT: the data is test data", banner.describe());
    }
}
