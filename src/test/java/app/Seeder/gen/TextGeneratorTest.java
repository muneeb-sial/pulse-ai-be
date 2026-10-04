package app.seeder.gen;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextGeneratorTest {

    @Test
    void noteReachesMinimumWordCount() {
        TextGenerator gen = new TextGenerator(new Random(1));
        for (int i = 0; i < 50; i++) {
            String note = gen.note(1000, 1400);
            assertTrue(TextGenerator.wordCount(note) >= 1000, "note too short: " + TextGenerator.wordCount(note));
            assertFalse(note.contains("{"), "unfilled slot in note");
        }
    }

    @Test
    void sameSeedGivesSameText() {
        assertEquals(new TextGenerator(new Random(7)).note(1000, 1400), new TextGenerator(new Random(7)).note(1000, 1400));
    }

    @Test
    void detailHasNoHeadings() {
        String text = new TextGenerator(new Random(3)).detail(120, 250);
        assertTrue(TextGenerator.wordCount(text) >= 120);
        assertFalse(text.contains("Presenting Complaint"));
    }

    @Test
    void uniqueValuesDoNotCollide() {
        java.util.Set<String> phones = new java.util.HashSet<>();
        java.util.Set<String> cards = new java.util.HashSet<>();
        for (long i = 1; i <= 100_000; i++) {
            assertTrue(phones.add(UniqueValues.phone(i)));
            assertTrue(cards.add(UniqueValues.idCard(i)));
        }
        assertTrue(UniqueValues.phone(1).length() <= 20);
        assertTrue(UniqueValues.idCard(1).length() <= 30);
    }
}
