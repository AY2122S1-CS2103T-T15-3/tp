package seedu.academydirectory.model.student;

import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import seedu.academydirectory.logic.parser.Prefix;

/**
 * Obtains desired Information among a list of Students
 */
public class InformationWantedFunction implements Function<Student, Optional<Information>> {
    public static final List<Prefix> SUPPORTED_PREFIX = List.of(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM);

    private final Prefix prefix;
    private final Optional<Name> name;

    /**
     * Returns a Predicate that can be used to obtain the desired {@code Information} among a list of Students.
     * Will return {@code Information} tied to the given {@code Prefix} for a given {@code Name} if a Student with
     * the same name is present, or for all students if no {@code Name} is given.
     * @param prefix Prefix of the information that is desired
     * @param name Name of student whose information is desired. Can be not passed in.
     */
    public InformationWantedFunction(Prefix prefix, Name name) {
        this.name = Optional.ofNullable(name);
        this.prefix = prefix;
    }

    public InformationWantedFunction(Prefix prefix) {
        this(prefix, null);
    }

    @Override
    public Optional<Information> apply(Student student) {
        if (name.isPresent() && name.filter(x -> student.getName().fullName.contains(x.fullName)).isEmpty()) {
            return Optional.empty();
        }

        if (PREFIX_EMAIL.equals(prefix)) {
            return Optional.of(student.getEmail());
        } else if (PREFIX_TELEGRAM.equals(prefix)) {
            return Optional.of(student.getTelegram());
        } else if (PREFIX_PHONE.equals(prefix)) {
            return Optional.of(student.getPhone());
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof InformationWantedFunction // instanceof handles nulls
                && prefix.equals(((InformationWantedFunction) other).prefix)); // state check
    }
}
