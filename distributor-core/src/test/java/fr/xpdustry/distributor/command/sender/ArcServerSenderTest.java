package fr.xpdustry.distributor.command.sender;

import arc.util.*;
import arc.util.Log.*;

import fr.xpdustry.distributor.command.caption.*;
import fr.xpdustry.distributor.string.*;
import fr.xpdustry.distributor.util.*;

import cloud.commandframework.captions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;


public class ArcServerSenderTest{
    private ArcServerSender sender;
    private MockLogHandler logger;

    @BeforeAll
    public static void before(){
        Log.formatter = (text, useColors, args) -> Strings.format(text.replace("@", "&fb&lb@&fr"), args);
        Log.level = LogLevel.debug;
    }

    @BeforeEach
    public void setup(){
        sender = new ArcServerSender(new ArcCaptionRegistry());
        logger = new MockLogHandler();
        Log.logger = logger;
    }

    @ParameterizedTest
    @ValueSource(strings = {"NONE", "DEBUG", "INFO", "ERROR"})
    public void test_send_message(String intent){
        final var message = "Hello @";
        final var expected = "Hello &fb&lbBob&fr";

        sender.send(MessageIntent.valueOf(intent), message, "Bob");
        assertEquals(expected, logger.getLastText());
        checkIntent(intent);
    }

    @ParameterizedTest
    @ValueSource(strings = {"NONE", "DEBUG", "INFO", "ERROR"})
    public void test_send_caption(String intent){
        final var caption = StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_NUMBER;
        final var expected = "'&fb&lb30&fr' is not a valid number in the range &fb&lb10&fr to &fb&lb20&fr";
        final var variables = new CaptionVariable[]{
            CaptionVariable.of("input", "30"),
            CaptionVariable.of("min", "10"),
            CaptionVariable.of("max", "20")
        };

        sender.send(MessageIntent.valueOf(intent), caption, variables);
        assertEquals(expected, logger.getLastText());
        checkIntent(intent);
    }

    public void checkIntent(String intent){
        assertTrue(switch(intent){
            case "DEBUG" -> logger.getLastLevel() == LogLevel.debug;
            case "ERROR" -> logger.getLastLevel() == LogLevel.err;
            default -> logger.getLastLevel() == LogLevel.info;
        });
    }
}