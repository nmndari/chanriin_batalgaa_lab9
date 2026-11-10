package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ===== Хэсэг A: happy path =====

    @Test
    void letterGrade_score95_returnsA() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGrade_score85_returnsB() {
        assertEquals("B", calc.letterGrade(85));
    }

    @Test
    void gpa_scores95and85_returns3point5() {
        assertEquals(3.5, calc.gpa(new double[] {95, 85}), 1e-9);
    }

    @Test
    void scholarship_gpa39_credits16_noViolation_returns100() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarship_gpa35_credits12_noViolation_returns70() {
        assertEquals(70, calc.scholarship(3.5, 12, false));
    }

    // ===== Хэсэг B: branch хамралтыг 100% хүргэх тестүүд =====

    // --- letterGrade ---

    @Test
    void letterGrade_negativeScore_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGrade_scoreAbove100_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    @Test
    void letterGrade_score75_returnsC() {
        assertEquals("C", calc.letterGrade(75));
    }

    @Test
    void letterGrade_score65_returnsD() {
        assertEquals("D", calc.letterGrade(65));
    }

    @Test
    void letterGrade_score50_returnsF() {
        assertEquals("F", calc.letterGrade(50));
    }

    // --- gpa ---

    @Test
    void gpa_nullArray_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_emptyArray_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[] {}));
    }

    @Test
    void gpa_allFiveGrades_returns2point0() {
        // A=4, B=3, C=2, D=1, F=0 (switch-ийн default салаа) -> 10 / 5 = 2.0
        assertEquals(2.0, calc.gpa(new double[] {95, 85, 75, 65, 50}), 1e-9);
    }

    // --- scholarship: оролтын шалгалтын || нөхцөл бүр тусдаа ---

    @Test
    void scholarship_negativeGpa_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.1, 16, false));
    }

    @Test
    void scholarship_gpaAbove4_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 16, false));
    }

    @Test
    void scholarship_negativeCredits_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.5, -1, false));
    }

    // --- scholarship: шийдвэрийн салаанууд ---

    @Test
    void scholarship_hasViolation_returns0() {
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    @Test
    void scholarship_gpa39_credits14_returns70() {
        // голч >= 3.8 хангасан ч кредит < 15 -> дараагийн шат (70)
        assertEquals(70, calc.scholarship(3.9, 14, false));
    }

    @Test
    void scholarship_gpa35_credits10_returns50() {
        // голч >= 3.4 хангасан ч кредит < 12 -> дараагийн шат (50)
        assertEquals(50, calc.scholarship(3.5, 10, false));
    }

    @Test
    void scholarship_gpa32_credits16_returns50() {
        assertEquals(50, calc.scholarship(3.2, 16, false));
    }

    @Test
    void scholarship_gpa25_credits16_returns0() {
        assertEquals(0, calc.scholarship(2.5, 16, false));
    }

    // ===== Хэсэг C: gpa() давталтын заагийн тестүүд (0 / 1 / 2+ дүрэм) =====
    // 0 удаа: gpa_nullArray_throws, gpa_emptyArray_throws (дээр, Хэсэг B-д бичигдсэн)

    @Test
    void gpa_loopOnce_singleScore95_returns4point0() {
        assertEquals(4.0, calc.gpa(new double[] {95}), 1e-9);
    }

    @Test
    void gpa_loopManyTimes_fourScores_returns2point5() {
        // A=4, B=3, C=2, D=1 -> 10 / 4 = 2.5
        assertEquals(2.5, calc.gpa(new double[] {95, 85, 75, 65}), 1e-9);
    }

    // ===== Хэсэг D: letterGrade() зааг утгын тестүүд =====

    @Test
    void letterGrade_boundary0_returnsF() {
        assertEquals("F", calc.letterGrade(0));
    }

    @Test
    void letterGrade_boundary59_99_returnsF() {
        assertEquals("F", calc.letterGrade(59.99));
    }

    @Test
    void letterGrade_boundary60_returnsD() {
        assertEquals("D", calc.letterGrade(60));
    }

    @Test
    void letterGrade_boundary69_99_returnsD() {
        assertEquals("D", calc.letterGrade(69.99));
    }

    @Test
    void letterGrade_boundary89_99_returnsB() {
        assertEquals("B", calc.letterGrade(89.99));
    }

    @Test
    void letterGrade_boundary90_returnsA() {
        assertEquals("A", calc.letterGrade(90));
    }

    @Test
    void letterGrade_boundary100_returnsA() {
        assertEquals("A", calc.letterGrade(100));
    }
}
