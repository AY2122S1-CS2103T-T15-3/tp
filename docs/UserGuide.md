---
layout: page
title: User Guide
---

Academy Directory (AD) is a **desktop app for CS1101S tutors to manage students’ contact, grades, tutorial attendance, and assignment completion**. It is optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). AD is developed as an evolution of the Address Book 3 application, primarily for usage in teaching CS1101S - Programming Methodology I at the National University of Singapore.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `addressbook.jar` from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * **`list`** : Lists all contacts.

   * **`add`**`n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * **`delete`**`3` : Deletes the 3rd contact shown in the current list.

   * **`clear`** : Deletes all contacts.

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

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Tutors will be able to add their tutees.

Format: `add n/NAME e/EMAIL t/TELE_HANDLE [p/PHONE_NUMBER]`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A student can have no phone number due to privacy concern.
</div>

* `PHONE_NUMBER` is an optional field.
* `EMAIL` entry **must end with @u.nus.edu**. Else, a warning message will be shown.\
`Students outside of NUS cannot be added`
* If newly inputted students have `NAME`, `EMAIL`, `TELE_HANDLE` matching exactly
with an already existing entry, the program will output a warning message and show the existing entry.\
`This student already exists and thus cannot be added again`\
`Existing student: Name: NAME, Email: EMAIL , Telegram: TELE_HANDLE`

Examples:
* `add n/Aaron Tan t/@sausage e/e0123456@u.nus.edu p/90312311`
* `add n/Betsy Lim t/@unislave e/e0123456@u.nus.edu`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a student : `edit`

Tutors will be able to edit their tutees.

Format: `edit INDEX [n/NAME] [e/EMAIL] [t/TELE_HANDLE] [p/PHONE_NUMBER]`

* Edits the tutee at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
* `edit 1 p/91234567 e/e0425205@u.nus.com`  Edits the phone number and email address of the 1st student to be `91234567` and `e0425205@u.nus.edu` respectively.
* `edit 2 n/Aaron Tan`  Edits the name of the 2nd student to be Aaron Tan.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a student: `delete`

Tutors will be able to delete their tutees.

Format: `delete INDEX`

* `INDEX` is an unique id assigned to each student in the system.
* Deletes the student at the specified `INDEX`
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `delete 2` deletes the 2nd student in the list.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

### Editing a student's Studio attendance: `attendance`
Avengers will be able to edit the attendance status of their students.

Format: `attendance s/STUDIO_GROUP k/KEYWORD a/ATTENDANCE_STATUS [i/INDEX]`

* Edits the attendance of a student or multiple students who have the matching `KEYWORD` in their names and in Studio group as defined by `STUDIO_GROUP`.
* Only full words will be matched e.g. `Han` will not match `Hans`.
* The search is case-insensitive. e.g `hans` will match `Hans`.
* If `INDEX` is not supplied, the command will edit the attendance of the student is in the last created Studio session. Otherwise, it will execute the edit in the specified Studio session if `INDEX` is valid.
* The `ATTENDANCE_STATUS` field can only be a 1 or 0 to indicate whether the student attended the session or not.
* Existing values will be updated to the input values.

Examples:

* `attendance s/1 k/Aaron a/0`
* `attendance s/33 k/Chan a/1 i/7`

### Editing a student’s Studio participation: `studiopart`

Avengers will be able to track the participation of their tutees during the relevant studios. We will be keeping track of Studio participation with a counter system which starts at 0 by default. If a student participates, their counter will increment by 1.

Format: `studiopart s/STUDIO_GROUP k/KEYWORD [a/AMOUNT] [i/INDEX]`

* Edits the Studio participation of a student or multiple students who have the matching KEYWORD in their names and in Studio group as defined by `STUDIO_GROUP`.
Only full words will be matched e.g. `Han` will not match `Hans`.
The search is case-insensitive. e.g `hans` will match `Hans`.
If `AMOUNT` is not supplied, the command will add 1 to the Studio. Otherwise it will edit the tutee’s Studio participation counter by the specified `AMOUNT`.
A tutee’s Studio participation counter cannot be reduced below 0.
If `INDEX` is not supplied, the command will edit the Studio participation of the tutee in the last created Studio session. Otherwise, it will execute the edit in the specified Studio session if `INDEX` is valid.

Examples:
* `studiopart s/12 k/Benny a/3` 
* `studiopart s/2 k/Keng i/7`
* `studiopart s/32 k/Ting a/-1 i/8`






--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
