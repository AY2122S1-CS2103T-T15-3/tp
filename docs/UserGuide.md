
# User Guide

## Overview
Academy Directory (AD) is a **desktop app for CS1101S tutors to manage students’ contact, grades, tutorial attendance, and assignment completion**. It is optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). AD is developed as an evolution of the Address Book 3 application, primarily for usage in teaching CS1101S - Programming Methodology I at the National University of Singapore.

### Table of Contents
* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

--------------------------------------------------------------------------------------------------------------------

## Quick Start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the file _(link to the latest JAR will be available upon completion of v1.3)_ to the folder you want to use as the _home folder_ for your Academy Directory.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * **`list`** : Lists all contacts.

   * **`attendance`** : Mark a student attendance.

   * **`exit`** : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

</div>

### Adding a student: `add`

Avengers will be able to add their students.

Format: `add n/NAME e/EMAIL t/TELE_HANDLE [p/PHONE_NUMBER]`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A student can have no phone number due to privacy concern.
</div>

* `PHONE_NUMBER` is an optional field. The default value is `NA` which stands for "Not Applicable".
* If Avengers do not wish to enter a student's phone number, 
  * `p` tag can be omitted from the command.
  * OR Avengers can supply the value `NA` to `p` tag. Note that it must be `NA` not `N.A` or `NAb`
* If newly inputted students have `NAME` matching exactly with an already existing entry, the program will output a warning message and show the existing entry.\
`This student already exists in the academy directory`

Examples:
* `add n/Aaron Tan t/@sausage e/e0123456@u.nus.edu p/90312311`
* `add n/Charles Ng t/@charles e/e0123434@u.nus.edu p/NA`
* `add n/Betsy Lim t/@unislave e/e0123456@u.nus.edu`

### Clearing all entries : `clear`

Clears all entries from the academy directory.

Format: `clear`

### Deleting a student: `delete`

Avengers will be able to delete their students.

Format: `delete INDEX`

* `INDEX` is an unique id assigned to each student in the system.
* Deletes the student at the specified `INDEX`
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `delete 2` deletes the 2nd student in the list.

### Editing a student : `edit`

Avengers will be able to edit their students.

Format: `edit INDEX [n/NAME] [e/EMAIL] [t/TELE_HANDLE] [p/PHONE_NUMBER]`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
* `edit 1 p/91234567 e/e0425205@u.nus.com`  Edits the phone number and email address of the 1st student to be `91234567` and `e0425205@u.nus.edu` respectively.
* `edit 2 n/Aaron Tan`  Edits the name of the 2nd student to be Aaron Tan.

### Tagging a student : `tag`

Avengers will be able to tag their students.

Format: `tag INDEX t/TAG [t/TAG]...`

* Assigns a `Tag` to the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, ...
* At least one tag must be provided
* Existing tags will be replaced by the new tags

Examples:
* `tag 1 t/mission` Adds a `mission` tag to the student specified at index 1.
* `tag 2 t/streams t/envmodel` Adds `streams` and `envmodel` tags to the student specified at index 2.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Students matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Listing all students : `list`

Shows a list of all students in the academy directory.

Format: `list`

### Retrieving additional information of students': `retrieve`

Retrieves additional information of students. Currently supported information includes:
- Telegram Handle
- Email address
- Telephone number, if any

Format: `retrieve [p/PHONE_NUMBER] [e/EMAIL] [MORE INFORMATION] [n/NAME]`

### Sorting the list : `sort` 

Sorts the list of students in the academy directory.

Format: `sort attr/ATTRIBUTE ord/ORDER`

* The sort can be done on some `Attribute`  in either ascending order or descending order.
* `Attribute` can only be `name`, `participation`, `RA1`, `Midterm`, `RA2`, `Final` and `Average`.
* `Attribute` is case-insensitve. e.g `name` and `NAME` will both sort the list by `Name`.
* `Order` can only be `asc` and `desc` which indicate either ascending or descending sort.
* `Order` is case-insensitive. e.g `ASC` and `Asc` will both sort the list in ascending order
* Both `Attribute` and `Order` are required for the sorting to work.

Examples:
* `sort attr/RA1 ord/desc` sorts the list in descending order based on their `RA1` grades.
* `sort attr/participation ord/asc` sorts the list in ascending order based on their participation score.

### Editing a student's Studio attendance: `attendance`
Avengers will be able to edit the attendance status of their students.

Format: `attendance INDEX ses/STUDIO_SESSION att/ATTENDANCE_STATUS`

* Edits the attendance of a student or multiple students based on their `INDEX`.
* Modifies the student(s) at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* Multiple `INDEX` can be parsed in at once as long as they are all valid.
* The `STUDIO_SESSION` field is a positive integer from 1 to 12 inclusive which refers to the Studio Session to be modified.
* The `ATTENDANCE_STATUS` field can only be a 1 or 0 to indicate whether the student attended the session or not where 1 marks a student as having attended while 0 marks a student as unattended.
* Existing values will be updated to the input values.

Examples:

* `attendance 1 ses/1 att/1`
* `attendance 1, 2, 3 ses/12 k/Chan a/1 i/7`

### Editing a student’s Studio participation: `part`

Avengers will be able to track the participation of their students during the relevant studios. We will be keeping track of Studio participation with a counter system which starts at 0 by default.

Format: `part INDEX ses/STUDIO_SESSION add/PARTICIPATION_TO_ADD`

* Edits the Studio participation of a student or multiple students based on their `INDEX`.
* Modifies the student(s) at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* The `STUDIO_SESSION` field is a positive integer from 1 to 12 inclusive which refers to the Studio Session to be modified.
* The `PARTICIPATION_TO_ADD` field is an integer from -500 to 500 inclusive which indicates the Participation score of the student.
* `PARTICIPATION_TO_ADD` will be added to the student's current Participation score
* A student’s Studio Participation score cannot be reduced below 0.
* If a student's `Attendance` is `false` and the Participation score to be added is greater than 0, the student will also be marked as having attended the Studio.

Examples:
* `part 1, 2, 3 ses/12 add/500`
* `part 4, 6 ses/2 add/-300`
* `part 4 ses/9 add/1`

### Adding grades for an assessment:  `grade`
Avengers will be able to add a student’s grade for a particular assessment.

Format: `grade INDEX a/ASSESSMENT g/GRADE`

* Record the student’s `GRADE` for the `ASSESSMENT`.
* The input `GRADE` must be a positive integer.

Example:
* `grade 1 a/RA1 g/15`

### Displaying the grades for an assessment:  `show`
Avengers will be able to view the grades and the average score of all the students 
for a particular assessment.

Format: `show ASSESSMENT`

* Display the list of grades that the students achieved for a particular `ASSESSMENT`, as well
as the average score.

Examples:
* `show RA1`

### Visualizing assessments' grades of the entire class: `vis`
Avengers will be able to view the grades and the average score of all the students
for a particular assessment.

Format: `vis`

* Visualize assessment grades of the class with Box & Whisker plot
* Any grade of any student that is not yet entered will be counted as `0`

Examples:
* `vis`

### Seeking help : `help`

![help message](images/helpMessage.png)

First-time avengers can seek in-depth assistance using AD with a `help` command, either
generally or specifically.

#### Viewing general help
Format: `help`

* The singular command `help` displays a menu of all commands available to use, as a list, and how
  to navigate each command, alongside the syntax for each command.

#### Viewing specific help

Format: `help COMMAND_NAME`

* Display all information related to the command, which includes
    * What the command is about.
    * Why the command is needed.
    * What the syntax for the command is.
    * Example of usage and expected results.

Examples:
* `help add`: Displays the exact same content as what is shown on this User Guide for the `add` command
  or [here](https://github.com/nus-cs2103-AY2122S1/tp/blob/master/docs/UserGuide.md#adding-a-student-add).
* `help list`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AcademyDirectory data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AcademyDirectory data are saved as a JSON file `[JAR file location]/data/academydirectory.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AcademyDirectory will discard all data and start with an empty data file at the next run.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AcademyDirectory home folder.

**Q**: What are "Studios" in this application?<br>
**A**: Studios are tutorials held in CS1101S and are essential in aiding the students to improve their grasp on the concepts taught during the lecture.

**Q**: What are "Avengers" in this application?<br>
**A**: ‘Avenger’ is a special term to call a CS1101S tutor. They are the people who organize a Studio session to improve on CS1101S concepts taught in lecture, recording attendance and grades.
The two terms are used throughout the UG in substitute of ‘tutorial’ and ‘tutor’, in consideration of the targeted audience of our application as CS1101S teaching assistants.

--------------------------------------------------------------------------------------------------------------------

## Command Summary

Action | Format, Examples
--------|------------------
[**Add**](#adding-a-student-add) | `add n/NAME p/PHONE_NUMBER e/EMAIL [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
[**Clear**](#clearing-all-entries--clear) | `clear`
[**Delete**](#deleting-a-student-delete) | `delete INDEX`<br> e.g., `delete 3`
[**Edit**](#editing-a-student--edit) | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
[**Tag**](#tagging-a-student--tag)| `tag INDEX t/TAG [t/TAG]...` <br> e.g., `tag 1 t/mission t/streams`
[**Find**](#locating-students-by-name-find) | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
[**List**](#listing-all-students--list) | `list`
[**Retrieve**](#retrieving-additional-information-of-students-retrieve) | `retrieve [p/PHONE_NUMBER] [e/EMAIL] [te/TELEGRAM] [n/NAME]` <br> e.g., `retrieve p/ e/ n/Alex Yeoh`
[**Sort**](#sorting-the-list--sort) | `sort attr/ATTRIBUTE ord/ORDER`<br> e.g., `sort attr/average ord/asc`
[**Attendance**](#editing-a-students-studio-attendance-attendance) | `attendance INDEX ses/STUDIO_SESSION att/ATTENDANCE_STATUS` <br> e.g., `attendance 1, 2 ses/1 att/1`
[**Studio Participation**](#editing-a-students-studio-participation-part) | `part INDEX ses/STUDIO_SESSION add/PARTICIPATION_TO_ADD`<br> e.g., `part 1 ses/12 att/0`
[**Grade**](#adding-grades-for-an-assessment--grade) | `grade INDEX as/ASSESSMENT g/GRADE` <br> e.g., `grade INDEX as/RA1 g/15`
[**Show Grades**](#displaying-the-grades-for-an-assessment--show) | `show ASSESSMENT` <br> e.g., `show RA1`
[**Help**](#seeking-help--help) | `help`
[**Exit**](#exiting-the-program--exit) | `exit`
