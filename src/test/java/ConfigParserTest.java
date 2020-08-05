import static org.junit.jupiter.api.Assertions.*;

class ConfigParserTest {
    ConfigParser config;
    ConfigParser stagingConfig;
    ConfigParser devConfig;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        config = new ConfigParser("config.txt");
        stagingConfig = new ConfigParser("config-staging.txt");
        devConfig = new ConfigParser("config-dev.txt");
    }

    @org.junit.jupiter.api.Test
    void testingGet() {
        assertAll(
                () -> assertEquals("fintek", config.get("application.name")),
                () -> assertEquals("8080", config.get("application.port")),
                () -> assertEquals("/api/v1", config.get("application.context-url")),
                () -> assertEquals("sq04_db", config.get("dbname")),
                () -> assertEquals("sq04_db-development", devConfig.get("dbname")),
                () -> assertEquals("fintek-development", devConfig.get("application.name")),
                () -> assertEquals("fintek-staging", stagingConfig.get("application.name")),
                () -> assertNull(stagingConfig.get("application.nme")),
                () -> assertNull(stagingConfig.get("dbnam")),
                () -> assertNull(stagingConfig.get("name")),
                () -> assertEquals("fast-staging", stagingConfig.get("pipeline"))
        );
    }
}

