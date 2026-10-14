package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

// Individual assignment, variant 3: prepend an ISO-8601 timestamp
@Component("timestamped")
@Order(3)
public class TimestampedNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(TimestampedNotifier.class);

    @PostConstruct
    void init() { log.info("TIMESTAMPED >> notifier initialised"); }

    @Override
    public String send(String message) {
        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return timestamp + " " + message;
    }

    @Override
    public String channel() { return "timestamped"; }
}
