---
title: Developer Guide
---

### Table of Contents
* [Acknowledgements](#acknowledgements)
* [Setting Up, Getting Started](#setting-up-getting-started)
* [Design](#design)
  * [Architecture](#architecture)
  * [UI Component](#ui-component)
  * [Logic Component](#logic-component)
  * [VersionedModel Component](#versionedmodel-component)
  * [Storage Component](#storage-component)
  * [Common Classes](#common-classes)
* [Implementation](#implementation)
  * [Managing Student's Personal Details](#managing-students-personal-details)
    * [AddCommand](#addcommand)
    * [DeleteCommand](#deletecommand)
    * [TagCommand](#tagcommand)
    * [GetCommand](#getcommand)
    * [EditCommand](#editcommand)
  * [Track Students' Grades, Studio Attendance, and Participation](#track-students-grades-studio-attendance-and-participation)
    * [GradeCommand](#gradecommand)
    * [AttendanceCommand](#attendancecommand)
    * [ParticipationCommand](#participationcommand)
  * [Visualization Tools](#visualization-tools)
    * [ViewCommand](#viewcommand)
    * [ShowCommand](#showcommand)
    * [VisualizeCommand](#visualizecommand)
    * [FilterCommand](#filtercommand)
    * [SortCommand](#sortcommand)
  * [Others](#others)
    * [ListCommand](#listcommand)
    * [ClearCommand](#clearcommand)
    * [UndoCommand](#undocommand)
    * [RedoCommand](#redocommand)
    * [HelpCommand](#helpcommand)
* [Guides](#guides)
* [Appendix](#appendix-a-requirements)
  * [Requirements](#appendix-requirements)
    * [Product Scope](#product-scope)
    * [User Stories](#user-stories)
    * [Use cases](#use-cases)
    * [Non-Functional Requirements](#non-functional-requirements)
    * [Glossary](#glossary)
  * [Manual Testing](#appendix-instructions-for-manual-testing)
    * [Feature Testing](#feature-testing)
    * [UI Testing](#ui-testing)
    
--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
- This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org/).
- The formatting and content of this User Guide is referenced from [AY2122S1-CS2103T-w17-1/tp](https://ay2122s1-cs2103t-w17-1.github.io/tp/).
- Design of the internal version control system is heavily inspired by [Git](https://github.com/git/git).

--------------------------------------------------------------------------------------------------------------------

## **Setting Up, Getting Started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

![Architecture Diagram](images/dg/architecture/ArchitectureDiagram.png)

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four main components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`VersionedModel`**](#versionedmodel-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

On top of the above four main components, there is also an additional [`VersionControl`](#versioncontrol-component) which 
provides common API for `VersionedModel` and `Storage` to implement version control. Because of this, the `VersionControl` component is usually left out in the diagrams in this 
developer guide. For more information regarding the `VersionControl` component, read [here](#versioncontrol-component)

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

![Architecture Sequence Diagram](images/dg/architecture/ArchitectureSequenceDiagram.png)

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

![Component Managers](images/dg/architecture/ComponentManagers.png)

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/ui/Ui.java)

![Structure of the UI Component](images/dg/architecture/ui/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ChartDisplay`, `StudentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `VersionedModel` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `VersionedModel` component, as it displays `Student` object residing in the `VersionedModel` and requires grades statistics from `Student` object in the `VersionedModel`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:
![Logic Class Diagram](images/dg/architecture/logic/LogicClassDiagram.png)

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AcademyDirectoryParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `VersionedModel` when it is executed (e.g. to add a student).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

![Parser Classes](images/dg/logic/parsers/ParserClasses.png)

How the parsing works:
* When called upon to parse a user command, the `AcademyDirectoryParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AcademyDirectoryParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### VersionedModel component
**API** : [`VersionedModel.java`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/model/VersionedModel.java)
![Versioned Model Class Diagram](images/dg/architecture/model/ModelClassDiagram.png)

The `VersionedModel` component,

* stores the academy directory data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `VersionedModel` represents data entities of the domain, they should make sense on their own without depending on other components)
* interfaces with `VersionControl` via the `VersionControlController`, which implements the `Version` API
and thus gives the `VersionedModel` component the ability to interface with version control entities such as `Commit`.

The above implementation is chosen because it makes _turning off_ version control relatively simple; a stub `VersionControlController`
can be used instead.

For more information regarding `VersionControl`, read [here](#versioncontrol-component).

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AcademyDirectory`, which `Student` references. This allows `AcademyDirectory` to only require one `Tag` object per unique tag, instead of each `Student` needing their own `Tag` objects.<br>

![Better Model Class Diagram](images/dg/architecture/model/BetterModelClassDiagram.png)

</div>

### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/storage/Storage.java)

![Storage Class Diagram](images/dg/architecture/storage/StorageClassDiagram.png)

The `Storage` component,
* can save both academy directory data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AcademyDirectoryStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `VersionedModel` component (because the `Storage` component's job is to save/retrieve objects that belong to the `VersionedModel`)
* can write version control objects to disk

### Common classes

Classes used by multiple components are in the `seedu.academydirectory.commons` package.

### VersionControl Component
The internal version control system in AcademyDirectory is inspired by Git. As such, `VersionControl` 
keeps track of AcademyDirectory data state by using `VcObject` which corresponds to Git objects.

#### VersionControl Objects
Shown below is the corresponding class diagram:

![VCObjectClassDiagram](images/dg/architecture/version/VcObjectClassDiagram.png)

Explanation of the objects shown above:
- `Tree`: 
  - represents a snapshot of `data` directory of AcademyDirectory.  It maintains a mapping between version-controlled
filenames and actual filename of tracked blobs.
  - Version controlled name is by default `SHA-1` hash of the blob. However, it is possible 
  to change this to `SHA-256` or even `MD5`. 
  - Supported hash functions are present in `HashMethod`
- `Commit`:
  - saves information regarding the changes to AcademyDirectory data:
    - who makes the change
    - when was the change made
    - what change was made exactly. 
  - points to a `Tree` which represents the snapshot of data _after_ the change is made
  - points to a `Commit` object which represents the parent of the current `Commit` object
- `Label`:
  - labels a `Commit`
  - relevant labels are `HEAD` which represents the current commit, `CURRENT` which represents
current branch, and `OLD` which represents the most recent branch before the current branch.

Note that in the actual implementation, a `VcObject` does not actually hold a reference to 
another `VcObject`; rather it has a `Supplier` of the `VcObject` that it's supposed to have
a reference to. This is to defer read operations to avoid memory overhead. 

#### Interfacing with other components
Both the `Storage` component and `VersionedModel` component interfaces with `VersionControl`
for different reasons:
- `Storage`: needs to be able to write `VcObjects` to disk
- `VersionedModel`:
  - needs to be able to read `VcObjects` from disk.
  - needs to be able to create new `VcObjects` from disk. However, this requires computing 
  file hash i.e. `VersionedModel` needs minimum write access to disk to be able to compute hash

As such, `VersionControl` provides three facade classes, each represents one of the above requirements. The following
class diagram shows this:
![VersionControlClassDiagram](images/dg/architecture/version/VersionControlClassDiagram.png)

The facade classes are: 
- `VersionControlGeneralReader`: to read `VcObject` from disk
- `VersionControlGeneralWriter`: to write `VcObject` to disk
- `HashComputer`: to compute hash of a file or hash of a `VcObject`

To modify disk representation of a particular instance of `VcObject`, modify both its `Reader`
and `Writer`.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Managing Students' Personal Details
### AddCommand

This command adds a new `Student` to `AcademyDirectory`.

#### Implementation
`AddCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

`AddCommand` adds students to the `AcademyDirectory`, with prevention of duplicates by ensuring that each `Student` has a unique `NAME`. 
`NAME` is a sufficiently unique field to identify a unique instance of a student as for the purposes of `AcademyDirectory` is scoped to 
service CS1101S Avengers, each `Studio` has approximately 10 `Students`, with the probability of a `NAME` collision being sufficiently minimised.

<div markdown="span" class="alert alert-info">:information_source: **Note:** The responsibility of ensuring that `Student` 
does not have unnecessary duplicate information (e.g same `PHONE`) is left to an Avenger.
</div>

### DeleteCommand

This command deletes a `Student` from `AcademyDirectory`.

#### Implementation
`DeleteCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

`DeleteCommand` deletes `Student` based on the relative `INDEX` in the `ObservableList` which is the list of `Student` viewed by the `Avenger`. To do this, `DeleteCommand` makes a call to `VersionedModel#deleteStudent()`.

### TagCommand

{Add description}

#### Implementation

{Add implementation}

### GetCommand
This command serves to retrieve a specific `PersonalDetail` of students or a student.

#### Implementation
`GetCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

All fields of `Student` class which implements the `PersonalDetail` interface and whose prefix is present in
the `GetCommand` class can be queried by `GetCommand`.

A `GetCommand` is initialized with a list of `Prefix` objects which represents the prefix of the `PersonalDetail`
to be retrieved, and a list of `String` which represents the keywords that will be matched with 
the names of students in Academy Directory. List of `Prefix` cannot be empty, but list of keywords can be.
An empty list of keywords is interpreted as retrieving personal details of all students in the model.
The specifics are shown in the sequence diagram below:

A `PersonalDetailRetriever` object can be constructed by passing a `Prefix` and a `Name
![GetCommandSequenceDiagram](images/dg/logic/commands/getcommand/GetCommandSequenceDiagram.png)

### EditCommand

This command edits a `Student`'s personal details such as their `NAME`, `PHONE`, `TELEGRAM` and `EMAIL`.

#### Implementation
`EditCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

Similar to `AddCommand`, `EditCommand` supports duplicate prevention by checking that the `NAME` being edited is unique in the list
unless the `NAME` is the same  as the `Student` being edited.

<div markdown="span" class="alert alert-info">:information_source: **Note:** The responsibility of ensuring that `Student` 
does not have unnecessary duplicate information (e.g same `PHONE`) is left to an Avenger.
</div>

### Track Students' Grades, Studio Attendance, and Participation

### GradeCommand

This command serves to update the `Grade` of various `Assessment` that the students will undergo in CS1101S. The assessments include RA1, Midterm, RA2, Practical Exam (PE), and Final.

#### Implementation

`GradeCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

The recording of grade is facilitated by adding an `Assessment` parameter to the `Student`.
The `Assessment` is implemented with a HashMap that stores the String representation of the assessments as the keys, and the integer `Grade` as the values.

The following sequence diagram describes what happens when `GradeCommand` is executed:

![GradeCommandSequenceDiagram](images/dg/logic/commands/gradecommand/GradeCommandSequenceDiagram.png)

### AttendanceCommand

This command serves to update the attendance status of students. A student's `Attendance` can be either attended or unattended.

#### Implementation

`AttendanceCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

The attendance mechanism is facilitated by adding a `StudioRecord` parameter to the `Student`. This `StudioRecord` has an `Attendance` object which we can use to track and update the `Attendance` of the `Student`. `Attendance` implements `Information` and the actual storing of the attendance status is done with a `boolean array`.

The following sequence diagram describes what happens when `AttendanceCommand` is executed:

![AttendanceCommandSequenceDiagram](images/dg/logic/commands/attendancecommand/AttendanceCommandSequenceDiagram.png)

For `UpdateModelAttendanceSequenceDiagram`, the sequential diagrams can be found below:

![UpdateModelAttendanceSequenceDiagram](images/dg/logic/commands/attendancecommand/UpdateModelAttendanceSequenceDiagram.png)

### ParticipationCommand

This command serves to update the `Participation` score of students. Following the XP system for CS1101S, each student is awarded between 0 and 500 XP (inclusive) per Studio session.

#### Implementation

`ParticipationCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

The implementation is similar to `AttendanceCommand`, with the same sequence diagram being applicable for Participation given that the proper refactoring to `Participation` is done.

`ParticipationCommand` has an additional section in the sequence diagram located above the loop in `AttendanceCommand`. The purpose of the logic below is to update a student's `Attendance` to be marked as present if the `participationUpdate` is greater than 0. This is because a student that has a positive `Participation` score would also count as having attended the `Studio`.

![ParticipationCommandMarkAttendanceSequenceDiagram](images/dg/logic/commands/participationcommand/ParticipationCommandMarkAttendanceSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The logic above is to update the `Attendance` and is only executed in the event that `participationUpdate` is more than 0. Otherwise, it will not run.

 </div>

### Visualization Tools

### ViewCommand

This command serves to display the summarised details of a single `Student` in the `AcademyDirectory`.

`ViewCommand` displays the `Student` based on the relative `INDEX` in the `ObservableList` which is the list of `Student` viewed by the `Avenger`.
Once the index and the student associated with the index is retrieved, it is set on the Additional View Model - with its associated type and info,
to send to the UI for display.

It extends the abstract class `Command` and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

![ViewCommandSequenceDiagram](images/dg/logic/commands/viewcommand/ViewCommandSequenceDiagram.png)

#### Implementation

`ViewCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

### ShowCommand

This command serves to display the collated score of all students in the Academy Directory for a specific `Assessment`. The assessments that can be queried are: RA1, Midterm, RA2, Practical Exam (PE), and Final.

#### Implementation

`ShowCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

The grades are collated by iterating through all the students and extracting the score from the `Assessment` HashMap using the input `Assessment` as the key.
The information is formatted into a String and displayed in the AdditionalView. The success message is parsed into `CommandResult` to be returned by `ShowCommand`.

The following sequence diagram describes what happens when `ShowCommand` is executed:

![ShowCommandSequenceDiagram](images/dg/logic/commands/showcommand/ShowCommandSequenceDiagram.png)

### VisualizeCommand

This command provides a Box Plot of the performance of all `Student` in `AcademyDirectory` in all `Assessment`.

#### Implementation

`VisualizeCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

{Improve on explanation and add a possible UML Diagram}

### FilterCommand

This command filters the `ObservableList` by `NAME` or `TAG`.  

#### Implementation

`FilterCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

{Improve on explanation and add a possible UML Diagram}

### SortCommand

This command sorts the `AcademyDirectory` student list based on their `Participation`, `Assessment` and `Name`. When sorting by `Assessment`, users have the option of sorting by individual `Assessment` or by the average grade among. Users can also choose if they want to sort by ascending or descending.

#### Implementation

`SortCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose and is Version Controllable.

The sorting mechanism is based on the `List` interface as it sorts the various `FilteredList` instances using `Comparator`. Based on the `attribute` of the `SortCommand` being executed, the `Comparator` differs as shown by the sequential diagram below:

![SortCommandSequenceDiagram](images/dg/logic/commands/sortcommand/SortCommandSequenceDiagram.png)

The reference frame for GetComparator can be found below. It details the selection process based on the `attribute` of the `SortCommand`.

![GetComparatorSequenceDiagram](images/dg/logic/commands/sortcommand/GetComparatorSequenceDiagram.png)

### Others

### ExitCommand

This command allows user to exit the application after saving all operations and data
It extends the `Command` class and will consequently `@Override` the `Command#execute()` method to serve this purpose.

#### Implementation

![GetComparatorSequenceDiagram](images/dg/logic/commands/exitcommand/ExitCommandSequenceDiagram.png)

### ListCommand

This command shows all students on the class, ordered by when the student is added to the Academy.

#### Implementation

`ListCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
`ListCommand` retrieves the student list and display it on the student list panel on the left side. All students will be displayed.

![GetComparatorSequenceDiagram](images/dg/logic/commands/listcommand/ListCommandSequenceDiagram.png)

### ClearCommand

This command clears all `Student` entries from `AcademyDirectory`.

#### Implementation

`ClearCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose 
and is Version Controllable. A new Academy Directory is created to replace the current one, meaning that the student list is set to empty.

![GetComparatorSequenceDiagram](images/dg/logic/commands/clearcommand/ClearCommandSequenceDiagram.png)


### HistoryCommand
This command shows the commit history. Each commit will be shown with its five character hash, 
author, date, and commit message. Only commands that are _version controllable_ will result in a commit
being created and thus shown by `HistoryCommand`. Commands which can be undone are referred to as _version controllable_ (read [here](#glossary)
for details on what this means).

This command is meant for:
* reminding users what changes were made to the underlying AcademyDirectory Data
* revealing (five character) hash of commits that can be used with `RevertCommand`.

#### Implementation
`HistoryCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose.

The `HistoryCommand` makes use of the following set of invariance:
- The most recent commit that belongs to the current branch is always labelled as `MAIN`
- The most recent commit that belongs to the second-most-recent branch is always labelled
as `OLD`

These guarantees are assured by the `VersionedModel#commit` method. Note that `HEAD` and `MAIN` need not
refer to the same commit e.g. if the user reverts to a previous commit then `MAIN` and `HEAD` will refer to 
different commits. 

Because this set of invariance are respected, thus `HistoryCommand` can show commit history by doing the following: 
- fetch commit labelled as `MAIN` and `OLD` from disk (methods to do this exposed by `VersionedModel`)
- find the lowest common ancestor between `MAIN` and `OLD` (`Commit#LCA` method used here)
- show all commits from initial commit until the lowest common ancestor found above normally
- show all commits from the lowest common ancestor until `MAIN` and `OLD` as per the desired
formatting

The following sequence diagram shows the above implementation:

{Add IMAGE}

#### Limitation
The current implementation can only show two commit branches: `MAIN` and `OLD`. While this is
sufficient in most cases, the ability to show arbitrary number of commit branches to give users
the ability to revert to any previous commits easily without having to look for the commit's hash
manually in disk. However, due to the implementer's inability to figure out how best to show
arbitrary number of commit branches, the current `HistoryCommand` thus can only show two branches. 

### RevertCommand
This command reverts the underlying `AcademyDirectory` data to a previous commit, as identified by the commit's hash.
Commands which can be undone are referred to as _version controllable_ (read [here](#glossary) 
for details on what this means). The following is true regarding `RevertCommand`:

- If any of the following occurs which leads to a failure in parsing commit file in disk, no changes
are made to the underlying disk: 
  - provided hash cannot be found on disk
  - commit file with the given hash exists, but is corrupted
  - commit file with the given hash exists, but no read access is given to AcademyDirectory
  - other reasons which leads to failure in reading commit file
- Otherwise, the `AcademyDirectory` storage data will be restored according to the target commit
to be reverted to. 

#### Implementation
`RevertCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose.

`RevertCommand` reverts state of AcademyDirectory data by doing the following:
- fetch commit identified by the given hash
- shift `VersionedModel`'s `HEAD` pointer to the commit
- restores the academy directory data according to the tree that the fetched commit points to

The following sequence diagram shows the above implementation:

{Add IMAGE}

#### Limitation
Because `RevertCommand` has to restore academy directory data which is the responsibility of the
`Storage` component, `RevertCommand` reinitialize a `Storage` and `VersionedModel` and changes the 
current `VersionedModel` reference to the newly created `VersionedModel`. This is (highly) not 
ideal, but the implementer has no idea how to do this properly without the ungodly reinitialization
of `Storage` and `VersionedModel`...

### UndoCommand
This command undoes a change done to the underlying `AcademyDirectory` data. Note that this is
different from the _view_ of the data e.g. when the `ViewCommand` is used, the _view_ visible to 
the user changes, but the underlying data does not change. Commands which can be undone are referred to 
as _version controllable_ (read [here](#glossary) for details on what this means).

#### Implementation
`UndoCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose. Internally, the `UndoCommand` makes use of the `RevertCommand`. 
Hence `UndoCommand` serves as _syntactic sugar_ for the `RevertCommand`.

### RedoCommand
This command redoes a change done to the underlying `AcademyDirectory` data. Note that this is
different from the _view_ of the data e.g. when the `ViewCommand` is used, the _view_ visible to
the user changes, but the underlying data does not change. Commands which can be redone are referred to
as _version controllable_ (read [here](#glossary) for details on what this means).

#### Implementation
`RedoCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose. Internally, the `RedoCommand` makes use of the `RevertCommand`.
Hence `RedoCommand` serves as _syntactic sugar_ for the `RevertCommand`.

### HelpCommand

This command serves to guide new users on using the application, which syntax to use and when to use them. Users can view a summary of all commands' syntax,
or a specific guide on how to use a particular command.

#### Implementation

`HelpCommand` will extend the `Command` class, and consequently `@Override` the `Command#execute()` method to serve its initial purposes.

The mechanism of the command is done by retrieving a `HELP_MESSAGE` field in each of the other command classes (other than HelpCommand itself). This help command will
be displayed to the user on a separate window later on.

![HelpCommandSequenceDiagram](images/dg/logic/commands/helpcommand/HelpCommandSequenceDiagram.png)

As seen from the diagram, the `HelpCommand` involves the use of conditional branches. If the optional condition is met, a `CommandException` is thrown to let
users know that the input is invalid.

Otherwise, the HelpCommand will use conditional branch to guide users to two different scenarios, as shown above. If it is a general help, a general help command
will be created. If it is a specific help, then a specific help command associated with a command will be created.

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Guides**

The following links to guides on: Documentation, Logging, Testing, Configuration, Dev-Ops.

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------


## Appendix A: Requirements


### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* is an Avenger for CS1101S (a tutor in CS1101S is known as an Avenger)

**Value proposition**:

Currently, Avengers are able to utilise Source Academy as a platform to aid in their students'
learning. However, there is no proper feature that allows Avengers to
maintain a nominal roll for their class, or to track their students' performance.

Academy Directory is designed to fill that gap! It is specifically tailored to help Avengers
better manage their students. The app provides ease of communication, and allows Avengers to
track their students' performance, in terms of attendance, participation and assessments.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                | I want to …​                                                    | So that I can…​                                                     |
| -------- | --------------------------| ------------------------------------------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | CS1101S Avenger           | add, delete and edit student's information                         | update my class list                                                   |
| `* * *`  | CS1101S Avenger           | retrieve specific details of a student                             | view and analyse the data                                              |
| `* * *`  | CS1101S Avenger           | keep track of my students' tutorial performance                    | accurately reward class participation marks                            |
| `* * *`  | CS1101S Avenger           | record my students' assessment results                             | monitor my students' progress and provide timely assistance to them    |
| `* * *`  | first time user           | access the relevant commands with a "help" command                 | learn how to use the app more easily                                   |
| `* *`    | CS1101S Avenger           | view the average scores of my students for specific assessments    | focus on the aspects to improve on during tutorial                     |
| `* *`    | CS1101S Avenger           | visualize the class scores for specific assessments                | gauge how well my students are doing in assessments                    |
| `* *`    | CS1101S Avenger           | add tags to certain students to take note of their weaker topics   | focus on topics that they are struggling with                          |
| `* * `   | CS1101S Avenger           | see history of changes to Academy Directory data                   | easily revert accidental changes to data                               |
| `* * `   | CS1101S Avenger           | undo changes made to Academy Directory data                        | easily revert accidental changes to data                               |
| `* * `   | CS1101S Avenger           | redo changes made to Academy Directory data                        | easily revert accidental undos to data                                 |

### Use cases

(For all use cases below, the **System** is the `AcademyDirectory` and the **Actor** is the `User`, unless specified otherwise)

**Use case: Seeking help**

**MSS**

1. User requests for assistance
2. Academy Directory shows a list of all commands available
3. User requests for help in a specific command
4. Academy Directory shows the User Guide instructions for that command
   
   Use case ends

**Extensions**
* 3a. User requests for the syntax of all commands
  *  3a1. Academy Directory shows a list of all the commands and their associated syntax.
    
     Use case ends
* 3b. User requests for help in a command that does not exist
  * 3b1. Academy Directory shows an error message
    
    Use case resumes at step 2.

**Use case: Delete a student**

**MSS**

1.  User requests to list students
2.  Academy Directory shows a list of students
3.  User requests to delete a specific student in the list
4.  Academy Directory deletes the student

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. Academy Directory shows an error message.

      Use case resumes at step 2.

**Use case: Edit a student**

**MSS**

1.  User requests to list students
2.  Academy Directory shows a list of students
3.  User requests to edit a specific student in the list
4.  Academy Directory edit the student

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. Academy Directory shows an error message.

      Use case resumes at step 2.

**Use case: Update Studio attendance of student**

**MSS**

1.  User requests to list students attending the relevant Studio session
2.  Academy Directory shows a list of students attending the relevant Studio session
3.  User requests to update the attendance of the student in the relevant Studio session
4.  Academy Directory updates the student's attendance for the relevant Studio session

    Use case ends.

**Extensions**

* 1a. The given index for the Studio group is invalid.

  * 1a1. Academy Directory shows an error message.
      Use case resumes at step 1.

* 1b. The given index for the Studio group's Studio session is invalid.

  * 1b1. Academy Directory shows an error message.
      Use case resumes at step 1.

* 2a. The list is empty.

  Use case ends.

* 3a. The given keyword to search for the student is gives no result.

    * 3a1. Academy Directory shows an error message.

      Use case resumes at step 2.

**Use case: Update Studio participation of student**

**MSS**

1. User requests to list students attending the relevant Studio session
2. Academy Directory shows a list of students attending the relevant Studio session
3. User requests to update the Studio participation of the student in the relevant Studio session with the appropriate Studio participation score
4. Academy Directory updates the student's Studio participation for the relevant Studio session

    Use case ends.

**Extensions**

* 1a. The given index for the Studio group is invalid.

    * 1a1. Academy Directory shows an error message.

      Use case resumes at step 1.

* 1b. The given index for the Studio group's Studio session is invalid.

    * 1b1. Academy Directory shows an error message.

      Use case resumes at step 1.

* 2a. The list is empty.

  Use case ends.

* 3a. The given keyword to search for the student is gives no result.

    * 3a1. Academy Directory shows an error message.

      Use case resumes at step 2.

* 3b. The given Studio participation score is invalid (non-integer).

    * 3b1. Academy Directory shows an error message.

      Use case resumes at step 2.

**Use case: Add student's grade for an assessment**

**MSS**

1. User enters a command to add the grade for an assessment to a student.
2. Academy Directory checks for existing instance of the assessment.
3. Academy Directory records the input grade for the assessment.
   
    Use case ends.

**Extensions**

* 1a. The student's name does not match any of the names in the directory.

    * 1a1. Academy Directory shows an error message.

      Use case resumes at step 1.
    
* 1b. The input grade is not a positive integer.

    * 1b1. Academy Directory requests for user to enter a positive integer.

      Use case resumes at step 1.

* 2a. The assessment already exists.

    * 2a1. Academy Directory edits the grade for the existing assessment.

      Use case ends.

* 2b. The assessment is new.

    * 2b1. Academy Directory adds a new assessment with the input grade.

      Use case ends.

**Use case: Show grades for an assessment**

**MSS**

1. User enters a command to display the grades for an assessment.
2. Academy Directory parses through the students to obtain the grades.
3. Academy Directory displays a list of students with the grades.

   Use case ends.

**Extensions**

* 1a. The assessment does not exist.

    * 1a1. Academy Directory shows an error message.
    * 1a2. Academy Directory requests for the user to try another assessment.

      Use case resumes at step 1.

**Use case: Retrieve information**

**MSS**

1. User enters a command to retrieve information
2. Academy Directory obtain the queried information from the students.
3. Academy Directory displays the list of information queried.

   Use case ends.

**Extensions**

* 1a. User specifies exact student name.

    * 1a1. Academy Directory displays the queried information associated with the queried student.
  
         Use case ends.
  
* 1a. User's queried information is not supported 
    * 1a1. Academy Directory shows an error message.
    * 1a2. Academy Directory requests for the user to try another information.
  
        Use case ends.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to _3000_ student without a noticeable sluggishness in performance for typical usage.
3. A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should work in computer with `32-bit` or `64-bit` processor.
5. Users data should be in human editable file and stored locally.
6. Should run on user computer with double-click - no installer or additional libraries required.
7. Should not require internet connection for any of its feature.
8. All user and app data should be stored locally, not through an online database solution.
9. Logs and previous commits stored should be based on the _Principle of Least-Privilege_.
10. Logs and previous commits should be recoverable even when `AcademyDirectory` itself is deleted.
11. Logs and previous commits should be transferable and functional after transfer onto other computers.
12. Users should be able to undo up to at least _100_ commands. 


### Glossary
Term | Definition | Comments
-----| ----------- | ---------- 
Operating System (OS) | Software that manages computer hardware and other computer software. |
Mainstream OS | Examples of mainstream OS includes: Windows, Linux, Unix, OS-X, MacOS, etc. |
Personal Detail | A contact detail of a student | Phone number, Telegram handle, and email address
CS1101S | An introductory Computer Science module for year 1 students in the the National University of Singapore. |
Studios | Tutorials held in CS1101S and are essential in aiding the students to improve their grasp on the concepts taught during the lecture. |
Avengers | A special term to call a CS1101S tutor. An avenger organizes a Studio session to improve on CS1101S concepts taught in lecture, recording attendance and grades.
Principle of Least-Privilege | Minimum levels of access – or permissions – needed to perform function.
Version Controllable `Command` | a `Command` that logs a `Commit` object upon execution with the logging relying on the `Optional` field in the `CommandResult` returned from `Command#execute()`.
Command Line Interface (CLI) | A text-based user interface, where users type commands to instruct the computer to do something.
Graphical User Interface (GUI) | A graphics-based user interface, where users click buttons to instruct the computer to do something.
Java | A program that allows running other programs written in Java programming language.
Command | An instruction typed by a user to Academy Directory.
Command Box | A part of the Academy Directory's GUI which can be used by users to type commands.
Field | Additional information that can be provided to a command for correct command execution. | May or may not have an associated prefix
Parameter | Part of the command which provides additional information provided by the user. | Actual values for the fields
Prefix | An abbreviation of a field. | Always ends with a backslash ('/')

--------------------------------------------------------------------------------------------------------------------

## **Appendix B: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Feature Testing

***

#### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases to come …​ }_

***

#### Add Student

1. Adding a student while all students are being shown
2. Adding a student while only one student is being shown, with there being more than one student in the list

1. _{ more test cases to come …​ }_

***

#### Delete Student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. Multiple students in the list.

   2. Test case: `delete 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student shown in the status message. Result display remains the same.

   3. Test case: `delete 0`<br>
      Expected: No student is deleted. Error details shown in the status message. Result display remains the same.

   4. Other incorrect delete commands to try: 
      1. `delete`
      2. `delete x` (where x is larger than the list size)<br>
         Expected: Similar to previous.


1. _{ more test cases to come …​ }_

***

#### Tag Student

1. Tag a student while all students are being shown
2. Tag a student while only one student is being shown, with there being more than one student in the list 

1. _{ more test cases to come …​ }_

***

#### Get Personal Detail

1. _{ more test cases to come …​ }_

***

#### Edit Personal Detail

1. _{ more test cases to come …​ }_

***

#### Add Grade

1. _{ more test cases to come …​ }_

***

#### Edit Attendance

1. Marking a single student's attendance while all students are being shown in list and no student is being viewed
   1. Prerequisites: List all students using the `list` command. Multiple students in the list.
   2. Test case: `attendance 1 ses/1 att/1` <br>
      Expected: First student's first Studio session is now marked as having attended
   3. Test case: `attendance 1 att/1 ses/1` <br>
      Expected: First student's first Studio session is now marked as having attended
   4. Test case: `attendance 1 ses/0 att/1` <br>
      Expected: No student's attendance is modified. Error details are shown in the status message indicating invalid session. Result display remains the same.
   5. Test case: `attendance 1 ses/0 att/-1` <br>
      Expected: No student's attendance is modified. Error details are shown in the status message indicating invalid attendance. Result display remains the same.
   6. Test case: `attendance 0 ses/1 att/1` <br>
      Expected: No student's attendance is modified. Error details are shown in the status message indicating invalid index. Result display remains the same.
   7. Other incorrect attendance commands to try:
      1. `attendance` (Missing index, session and attendance)
      2. `attendance 1` (Missing session and attendance)
      3. `attendance 1 att/1` (Missing session)
      4. `attendance 1 ses/1` (Missing attendance)
      5. `attendance x ses/y att/z` (Any case where x is an integer greater than the size of the student list, y is an integer out of the range of 0 and 12 inclusive or z is not 0 or 1)


2. Marking a single student's attendance while all students are shown in list and a student is being viewed with no tabs open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display still being viewed in the same state with no tabs open after execution of the command regardless if the execution of the command is successful or not.


3. Marking a single student's attendance while all students are shown in list and a student is being viewed with the "View Participation" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Participation" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Participation" tab open.


4. Marking a single student's attendance while all students are shown in list and a student is being viewed with the "View Test Score" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Test Score" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Test Score" tab open.
   
***

#### Edit Participation

1. Updating a single student's participation score while all students are being shown in list and no student is being viewed
   1. Prerequisites: List all students using the `list` command. Multiple students in the list.
   2. Test case: `participation 1 ses/1 add/1` <br>
      Expected: First student's first Studio session's participation score is 1 greater than it was previously. If it was 500 previously, it would still be 500.
   3. Test case: `participation 1 add/1 ses/1` <br>
      Expected: First student's first Studio session's participation score is 1 greater than it was previously. If it was 500 previously, it would still be 500.
   4. Test case: `participation 1 ses/0 add/1` <br>
      Expected: No student's participation is modified. Error details are shown in the status message indicating invalid session. Result display remains the same.
   5. Test case: `participation 1 ses/1 add/-501` <br>
      Expected: No student's participation is modified. Error details are shown in the status message indicating invalid participation. Result display remains the same.
   6. Test case: `participation 0 ses/1 add/1` <br>
      Expected: No student's participation is modified. Error details are shown in the status message indicating invalid index. Result display remains the same.
   7. Other incorrect participation commands to try:
       1. `participation` (Missing index, session and attendance)
       2. `participation 1` (Missing session and attendance)
       3. `participation 1 add/1` (Missing session)
       4. `participation 1 ses/1` (Missing participation)
       5. `participation x ses/y add/z` (Any case where x is an integer greater than the size of the student list, y is an integer out of the range of 0 and 12 inclusive or z is either greater than 500, or smaller than -501)


2. Updating a single student's participation while all students are shown in list and a student is being viewed with no tabs open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display still being viewed in the same state with no tabs open after execution of the command regardless if the execution of the command is successful or not.


3. Updating a single student's participation while all students are shown in list and a student is being viewed with the "View Participation" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Participation" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Participation" tab open.


4. Updating a single student's participation while all students are shown in list and a student is being viewed with the "View Test Score" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Test Score" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Test Score" tab open.


***

#### View student information

1. View all related information of a student
  1. Prerequisite: List all student using the `list` command. Multiple students are shown in the list. **List should have exactly 6 students** (can using sample data provided as default)
  2. Test case: `view 1`
     Expected: All related information of the first student is shown on the result display visualizer on the right side. This includes: Student name, current tags, all academic-related information (assessment score, studio participation, studio attendance), and personal contact information (phone, email, telegram)
     Expected: No information is modified, Academy Directory runs as normal
     Expected: Status message is that users are viewing student at position 1 of the list
  3. Test case: `view 6`
     Expected: All related information of the last student is shown on the result display visualizer on the right side. This includes: Student name, current tags, all academic-related information (assessment score, studio participation, studio attendance), and personal contact information (phone, email, telegram)
     Expected: No information is modified, Academy Directory runs as normal
     Expected: Status message is that users are viewing student at position 6 of the list
  4. Test case: `view 7`
     Expected: No view is shown on the result display. An error message is shown stating that index number is invalid
  5. Test case: `view 0`, `view add`, `view myself in front of the mirror as a failure of society`
     Expected: No view is shown on the result display. An error message is shown stating that index number is invalid (in a sense that it must be a positive integer)

2. View students when list is altered
  1. Prerequisite: Using filter to reduce the list view to 1 only. List should only have one student filtered.
  2. Test case: `view 1`
     Expected: All related information are shown about the student
     Expected: Status message is that users are viewing student at position 1 of the list
  3. Test case: `view 2`
     Expected: No view is shown on the result display. An error message is shown stating that index number is invalid
     Significance: View works for the current index number shown on the student list only.
    
***

#### Show Grade

1. _{ more test cases to come …​ }_

***

#### Visualize all Grades

1. _{ more test cases to come …​ }_

***

#### Filter Academy Directory

1. _{ more test cases to come …​ }_

***

#### Sort Student List

1. Sorting the student list while all students are shown in and a student is being viewed with no tabs open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list.
   2. Test case: `sort attr/name ord/asc` <br>
      Expected: Student list is now sorted by name in ascending order.
   3. Test case: `sort ord/asc attr/name` <br>
      Expected: Student list is now sorted by name in ascending order.
   4. Test case: `sort attr/name ord/as` <br>
      Expected: Student list's sorting state remains unchanged. Error details are shown in the status message indicating invalid order. Result display remains the same.
   5. Test case: `sort attr/nam ord/asc` <br>
      Expected: Student list's sorting state remains unchanged. Error details are shown in the status message indicating invalid attribute. Result display remains the same.
   6. Other incorrect sort commands to try:
      1. `sort` (Missing attribute and order)
      2. `sort attr/name` (Missing order)
      3. `sort ord/asc`
      4. `sort attr/[ATTRIBUTE] ord/[ORDER]` (Any case where [ATTRIBUTE] is not any of the valid attributes, case-insensitive or [ORDER] is not 'asc' or 'desc', case-insensitive)


2. Sorting the student list while all students are shown in list and a student is being viewed with no tabs open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display still being viewed in the same state with no tabs open after execution of the command regardless if the execution of the command is successful or not.


3. Sorting the student list while all students are shown in list and a student is being viewed with the "View Participation" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Participation" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Participation" tab open.


4. Sorting the student list while all students are shown in list and a student is being viewed with the "View Test Score" open
   1. Prerequisites: List all students using the `list` command. Multiple students in the list. View a single student using `view 1`. Click on "View Test Score" in the result display.
   2. The test cases mirror `Test 1`, with the student being viewed in the result display in the state with no tabs open after successful execution of the command. If the execution of the command fails, the result display still shows the student with the "View Test Score" tab open.


***

#### List All Students

1. _{ more test cases to come …​ }_

***

#### Clear Student List

1. _{ more test cases to come …​ }_

***

#### Undo Changes

1. _{ more test cases to come …​ }_

***

#### Redo Changes

1. _{ more test cases to come …​ }_

***

#### Help

1. Test general help
   1. Prerequisite: Application is started
   2. Test case: `help`
      Expected: A pop-up window is shown summarizing the format of all commands for users, as well as a link to the web User Guide of Academy Directory
   3. Test case: `help    `
      Expected: A pop-up window is shown summarizing the format of all commands for users, as well as a link to the web User Guide of Academy Directory
2. Test specific help
   1. Prerequisite: Application is started
   2. Test case: `help edit`
      Expected: A pop-up window is shown with a customized help message (based on the User Guide of `edit` command) on how to use `edit`, including significance, format, and example.
   3. Test case: `help visualize`
      Expected: A pop-up window is shown with a customized help message (based on the User Guide of `visualize` command) on how to use `visualize`, including significance, format, and example.
   4. Test case: `help ad`, `help addd`
      Expected: No pop-up window is shown, and an error message is shown as status message explaining that there exists no instruction for command `ad`. Significance of the test case is that specific `help` can only be useful when the command is typed in full rather than in partial
   5. Test case: `help r230thg4b0p2nnbtpbgetbi03`
      Expected: No pop-up window is shown, and an error message is shown as status message explaining that there exists no instruction for the command.
3. Test functionality of pop-up window
   1. Prerequisite: Help window is already opened before by any mean, and is kept opened for testing
   2. Test case: Focus on the Main Window, do not close Help Window, and type in `help add` or any other equivalent command
      Expected: The help window is refocused with its content change to the new `help` instead.
***

#### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases to come …​ }_

***

### Graphic User Interface Testing

#### Menus
   1. Test the 5 menu items on the bar
      1. Prerequisite: Application is started
      2. Test case: Click on the second menu item
         Expected: Menu item is expanded to show 5 entries: `show RA1`, `show RA2`, `show MIDTERM`, `show PE`, `show FINAL`
      3. Test case: Click on the third menu-item
         Expected: Menu item is expanded to show 1 entry: `visualize`
   2. Test a random menu item
      1. Prerequisite: The second menu is opened
      2. Test case: Click on entry `Show RA1`
         Expected: The command `show RA1` is executed as an equivalent command-button
         
#### User Interface
   1. Test application user interface
      1. Prerequisite: Application is started
      2. Test case: Expand Academy Directory to full screen
         Expected: Background image also expanded, alongside other components of the internal controls (result display, student list, and status message)
         Expected: No other visual misbehavior of the User Interface (image is cropped or lacking in any visual design)
      3. Test case: Shrink Academy Directory to the smallest possible size
         Expected: Academy Directory is not minimized completely as there is a minimal size for users to still see the data
      4. Test case: Enter keywords to the command bar.
         
#### User Experience

### System testing

#### Performance testing
#### Load testing
#### Compatibility testing
#### Usability testing
#### Portability testing
