package seedu.academydirectory.model.student;

import static seedu.academydirectory.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.academydirectory.model.tag.Tag;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private Attendance attendance;
    private Assessment assessment;

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Telegram telegram, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, telegram, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.address = address;
        this.tags.addAll(tags);
        this.attendance = new Attendance(10); // for now, we just assume its 10.
        this.assessment = new Assessment();
    }

    /**
     * Constructor for Student with Attendance and Assessment.
     */
    public Student(Name name, Phone phone, Email email, Telegram telegram, Address address, Attendance attendance,
                   Assessment assessment, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.address = address;
        this.tags.addAll(tags);
        this.attendance = attendance;
        this.assessment = assessment;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public Address getAddress() {
        return address;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both students have the same name.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
                && otherStudent.getName().equals(getName());
    }

    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Student)) {
            return false;
        }
        Student otherStudent = (Student) other;
        return otherStudent.getName().equals(getName())
                && otherStudent.getPhone().equals(getPhone())
                && otherStudent.getEmail().equals(getEmail())
                && otherStudent.getTelegram().equals(getTelegram())
                && otherStudent.getAddress().equals(getAddress())
                && otherStudent.getTags().equals(getTags())
                && otherStudent.getAttendance().equals(getAttendance());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, telegram, address, tags, attendance);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Telegram: ")
                .append(getTelegram())
                .append("; Address: ")
                .append(getAddress())
                .append("; Attendance: ")
                .append(getAttendance());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

}
