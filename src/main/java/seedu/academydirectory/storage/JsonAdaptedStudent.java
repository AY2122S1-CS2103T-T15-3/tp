package seedu.academydirectory.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.academydirectory.commons.exceptions.IllegalValueException;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Attendance;
import seedu.academydirectory.model.student.Email;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.Participation;
import seedu.academydirectory.model.student.Phone;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.model.student.StudioRecord;
import seedu.academydirectory.model.student.Telegram;
import seedu.academydirectory.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Student}.
 */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String telegram;
    private final boolean[] attendance;
    private final int[] participation;
    private final HashMap<String, Integer> assessment;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                              @JsonProperty("email") String email, @JsonProperty("telegram") String telegram,
                              @JsonProperty("attendance") boolean[] attendance,
                              @JsonProperty("participation") int[] participation,
                              @JsonProperty("assessment") HashMap<String, Integer> assessment,
                              @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.attendance = attendance;
        this.participation = participation;
        this.assessment = assessment;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }
    /**
     * Converts a given {@code Student} into this class for Jackson use.
     */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().toString();
        email = source.getEmail().value;
        telegram = source.getTelegram().value;
        attendance = source.getAttendance().getAttendanceArray();
        participation = source.getParticipation().getParticipationArray();
        assessment = source.getAssessment().getAssessmentHashMap();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted student object into the model's {@code Student} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted student.
     */
    public Student toModelType() throws IllegalValueException {
        final List<Tag> studentTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            studentTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (telegram == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Telegram.class.getSimpleName()));
        }
        if (!Telegram.isValidTelegram(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        }
        final Telegram modelTelegram = new Telegram(telegram);

        Attendance tempAttendance = new Attendance(attendance.length);
        tempAttendance.setAttendance(attendance);

        Participation tempParticipation = new Participation(participation.length); // should be same length as attend
        tempParticipation.setParticipation(participation);

        StudioRecord tempStudioRecord = new StudioRecord(tempAttendance, tempParticipation);

        final StudioRecord modelStudioRecord = tempStudioRecord;

        final Assessment modelAssessment = new Assessment(assessment);

        final Set<Tag> modelTags = new HashSet<>(studentTags);

        return new Student(modelName, modelPhone, modelEmail, modelTelegram, modelStudioRecord,
                modelAssessment, modelTags);
    }

}
