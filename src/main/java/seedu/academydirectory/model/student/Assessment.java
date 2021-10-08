package seedu.academydirectory.model.student;

import java.util.HashMap;

/**
 * Represents a Student's assessment in the address book.
 */
public class Assessment implements Information {

    public static final String MESSAGE_CONSTRAINTS =
            "Grades can only be recorded for the following assessments: RA1, MIDTERM, RA2, PE, FINAL.";

    private HashMap<String, Integer> assessment;

    /**
     * Constructs a HashMap containing the Assessment and respective grades.
     */
    public Assessment() {
        assessment = new HashMap<>();
        assessment.put("RA1", -1);
        assessment.put("MIDTERM", -1);
        assessment.put("RA2", -1);
        assessment.put("PE", -1);
        assessment.put("FINAL", -1);
    }

    /**
     * Returns true if a given string is a valid assessment.
     */
    public static boolean isValidAssessment(String test) {
        return test.equalsIgnoreCase("RA1") || test.equalsIgnoreCase("MIDTERM") || test.equalsIgnoreCase("RA2")
                || test.equalsIgnoreCase("PE") || test.equalsIgnoreCase("FINAL");
    }

    public HashMap<String, Integer> getAssessment() {
        return assessment;
    }

    public void updateAssessmentGrade(String assessmentName, Integer grade) {
        this.assessment.replace(assessmentName, grade);
    }

    private String getAssessmentGrade(String assessmentName) {
        Integer grade = this.assessment.get(assessmentName);
        return grade == -1 ? "NA" : grade.toString();
    }

    @Override
    public String toString() {
        return "RA1: " + getAssessmentGrade("RA1") + ", "
                + "MIDTERM: " + getAssessmentGrade("MIDTERM") + ", "
                + "RA2: " + getAssessmentGrade("RA2") + ", "
                + "PE: " + getAssessmentGrade("PE") + ", "
                + "FINAL: " + getAssessmentGrade("FINAL");
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Assessment // instanceof handles nulls
                && assessment.equals(((Assessment) other).assessment)); // state check
    }

    @Override
    public int hashCode() {
        return assessment.hashCode();
    }
}
