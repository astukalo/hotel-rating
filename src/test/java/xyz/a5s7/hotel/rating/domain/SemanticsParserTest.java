package xyz.a5s7.hotel.rating.domain;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class SemanticsParserTest {

    private SemanticsParser semanticsParser = new SemanticsParser();

    @Test
    public void fromJson() throws Exception {
        File file = new File(this.getClass().getResource("/semantics.json").getFile());
        Semantics semantics = semanticsParser.fromJson(new FileInputStream(file));
        assertEquals(2,semantics.getPositive().size());
        assertEquals(2,semantics.getNegative().size());
        assertEquals(3,semantics.getIntensifier().size());
    }

}