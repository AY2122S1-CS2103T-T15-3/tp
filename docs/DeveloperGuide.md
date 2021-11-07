---
title: Developer Guide
---

### Table of Contents
* [Information About this Developer Guide](#information-about-this-developer-guide)
  * [Purpose of Developer Guide](#purpose)
  * [Target Audience of Developer Guide](#target-audience)
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
    * [ExitCommand](#exitcommand)
    * [ListCommand](#listcommand)
    * [ClearCommand](#clearcommand)
    * [HistoryCommand](#historycommand)
    * [RevertCommand](#revertcommand)
    * [UndoCommand](#undocommand)
    * [RedoCommand](#redocommand)
    * [HelpCommand](#helpcommand)
* [Guides](#guides)
* [Appendix](#appendix)
  * [A: Requirements](#appendix-a-requirements)
    * [Product Scope](#product-scope)
    * [User Stories](#user-stories)
    * [Use cases](#use-cases)
    * [Non-Functional Requirements](#non-functional-requirements)
    * [Glossary](#glossary)
  * [B: Manual Testing](#appendix-b-instructions-for-manual-testing)
  * [C: Version Controlled Commands](#appendix-c-version-controlled-commands)
    
--------------------------------------------------------------------------------------------------------------------
## Information about this Developer Guide
### Purpose
This developer guide aims to provide information regarding design and implementation of Academy Directory. 
To do this effectively, the following details can be found: 
- Design considerations of certain features
- Implementation details of certain features
- Possible future extensions

### Target Audience
The current version of Academy Directory is specifically designed for
**CS1101S Avengers**. Therefore, the main Target Audience of this developer guide are Java developers who 
are or formerly were CS1101S Avengers. As such, the following assumptions are made regarding the Target Audience:
- Is familiar with the common terms relating to Computer Science.
- Is familiar with CS1101S module structure and teaching pedagogy.

Technical background is assumed. We also provide the definitions for
certain technical terms commonly used in this developer guide [here](#glossary).

## **Acknowledgements**
- This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org/).
- The formatting and content of the User Guide and Developer Guide is referenced from [AY2122S1-CS2103T-w17-1/tp](https://ay2122s1-cs2103t-w17-1.github.io/tp/).
- Design of the internal version control system is heavily inspired by [Git](https://github.com/git/git).
- Certain code implementations may have been inspired by [Baeldung tutorials](https://www.baeldung.com/) or [StackOverflow answers](https://stackoverflow.com)
- Application logo were designed with the assistance of [Tailor Brand](https://studio.tailorbrands.com/brands/6276554147/downloads) and [Adobe Spark](https://spark.adobe.com/express-apps/logo-maker/preview)
- Libraries used:
  - [JavaFX](https://openjfx.io/) for the amazing GUI
  - [Jackson](https://github.com/FasterXML/jackson) to save your data
  - [JUnit5](https://github.com/junit-team/junit5) so that we can deliver to you bug-free!
  - [MDFX](https://github.com/JPro-one/markdown-javafx-renderer) so that you can see User Guide in help without internet
- Background and icons used:
  - Source Academy classroom front page, taken from the [Asset page in Source Academy front-end](https://github.com/source-academy/frontend/tree/master/src/assets)
    License for reuse and distribution [here](https://github.com/source-academy/frontend/blob/master/LICENSE)
  - Slides Carnival background [here](https://www.slidescarnival.com/iris-free-presentation-template/3923)
  - External icons used on the UI
    - [Student icon](https://icons8.com/icon/iZBBn0SF22gW/programmer) taken from IconS8
    - [Grade icon](https://pngtree.com/freepng/test-papers-stationery-illustration_4652639.html) taken from PNGTree
    - [Statistic icon](https://pngtree.com/freepng/vector-statistics-icon_3782961.html) taken from PNGTree


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

The part labeled "VersionControl" occurs because `delete` is a version controlled command. For non-version controlled
commands the sequence diagram stays largely the same; the difference is the lack of the "VersionControl" part. 
For a list of version controlled command, refer [here](#appendix-c-version-controlled-commands).

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

![Component Managers](images/dg/architecture/ComponentManagers.png)

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/ui/Ui.java)

![Structure of the UI Component](images/dg/architecture/ui/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts smaller UI-part components, including `CommandBox`, `ResultDisplay`, 
`VisualizerDisplay`, `StudentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract 
`UiPart` class which captures the commonalities between classes that represent smaller parts of the visible GUI. Of these, all components are always presented,
except for the Help Window which can be shown or hide depending on the results of user command.

Some classes of the UI, notably `CommandBox` and `AppMenu`, keeps a reference of a functional interface called `CommandExecutor` that
executes a Command from the Logic `component`.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in their respective matching `.fxml` files stored in the `src/main/resources/view` folder. For example, the layout of the 
[`MainWindow`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/academydirectory/ui/MainWindow.java) is 
specified in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103T-T15-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component, through the MainWindow object.
* listens for changes in `VersionedModel` data so that the UI can be updated with the modified data.
* keeps a reference (for Main Window) or depends (for AppMenu) on the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* keeps a reference of some classes in the `VersionedModel` component, particularly the Student class, as it displays `Student` object residing in 
  the `VersionedModel` and requires grades statistics from `Student` object in the `VersionedModel`.
  
![Creator Class Diagram](images/dg/architecture/ui/CreatorClassDiagram.png)

One important component of the UI is the specialized Creator class which extends the abstract class UiPart - for the purpose of reusing the Visualizer Display
to show users the result of a command execution. After information is sent from Main Window to the Visualizer Display, the Visualizer Display then send the Additional
Info Object to the Creator classes, which will be used to convert it for user view in the Visualizer Display itself.

**Design Considerations**

CommandExecutor Functional Interface
   * Initial design of AB3: Keeping the CommandExecutor functional Interface inside CommandBox
     - Cons: This design initially is supposed to encapsulate the CommandBox UI object, that the command box is able to execute user's command on behalf of the 
       Logic component. However, the disadvantage of this design is that if there are future extensions requiring more components that can execute commands, like another
       input box or GUI functionalities, then another interface must be used.
   * Current design: Separate CommandExecutor from CommandBox
     - Pros: Allows other components of the UI to use the CommandExecutor interface for command execution, and better abstract out the design of Academy Directory, hence making CommandExecutor a medium
       of communication between the Logic and the UI component. In addition, it serves as a basis for any future extension of Academy Directory.
     - Cons: It makes the implementation of the UI segment more complicated in return.

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
* stores a `AddtionalViewModel` that stores additional information required by the UI's `VisualizerDisplay` such as number statistic for the `Visualize` command.

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

**Design Considerations:**

**Aspect: How to save state changes**
- **Alternative 1 (current choice):** Saves state changes to disk ala Git
  - Pros:
    - future-proof; even when AcademyDirectory supports adding and tracking many files e.g. 
    profile pictures of students, code screenshots etc. 
    - provides backbone for additional features e.g. regeneration of data even when data becomes 
    corrupted, etc.
    - Undo, redo, and history operations can be implemented easily
    - Undo and redo operations can still be done even when application is closed and opened again
  - Cons: Complex implementation
    
- **Alternative 2:** Saves state changes in memory: 
  - Pros: Easy to implement
  - Cons: Lacks future-proof*ness* of the first alternative


#### VersionControl Objects
Shown below is the corresponding class diagram:

![VCObjectClassDiagram](images/dg/architecture/version/VcObjectClassDiagram.png)

**Explanation of the objects shown above:**
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
  - relevant labels are `HEAD` which represents the current commit, `MAIN` which represents
current branch, and `OLD` which represents the most recent branch before the current branch.

**A few implementation details:**
- A `VcObject` does not actually hold a reference to 
another `VcObject`; rather it has a `Supplier` of the `VcObject` that it's supposed to have
a reference to. This is to defer read operations to avoid memory overhead.
- Equality of `VcObject` is checked using its _hash_. This follows the idea that a _hash_ is 
intended to be a reliable fingerprint, and the chosen hash function (SHA1) has a (very) low probability 
of collision. 
- All classes which extends `VcObject` class should have a singleton NULL private instance. This reduces headache in terms of 
handling edge cases e.g. root of commit tree, exception handling, etc. The NULL singleton is used to indicate failure
for whatever reason, and allows the application to remain operational.
- Class-specific implementation details: 
  - `Tree` supports an additional method `Tree#regenerateBlobs()`, which regenerate the files
  maintained by the said `Tree`.
  - `Commit` supports a few additional methods which reflect the underlying commit tree: 
    - `Commit#findLca(Commit)` finds the lowest common ancestor of two commits, returning `Commit.emptyCommit()` 
    if no common ancestor can be found.
    - `Commit#getHistory()` returns all commits from given commit to the root of the commit tree.
    - `Commit#getHighestAncestor()` returns the commit ancestor that is the furthest away,
    limited by the given end Commit or by the root of the commit tree.

**Design Considerations:**

**Aspect: Which change to track**
- **Alternative 1 (current choice):** Tracks changes at the file level
  - Pros: Easy to implement
  - Cons: May have performance issues in terms of disk space and memory usage

- **Alternative 2:** Tracks changes at the line level of the file:
  - Pros: Lower disk space requirements
  - Cons: Difficult to implement
  
**Possible improvements in the future:** 
- Explore implementing `VcObject` as a proper monad with the bind operator, considering the clear relationship
between the different `VcObject`.

#### Interfacing with other components
Both the `Storage` component and `VersionedModel` component interfaces with `VersionControl`
for different reasons:
- `Storage`: needs to be able to write `VcObject`s to disk
- `VersionedModel`:
  - needs to be able to read `VcObject`s from disk.
  - needs to be able to create new `VcObject`s. However, this requires computing the `VcObject`
  file representation's hash i.e. `VersionedModel` needs minimum write access to disk to be able to compute hash

As such, `VersionControl` provides three facade classes, each representing one of the above requirements. The following
class diagram shows this:
![VersionControlClassDiagram](images/dg/architecture/version/VersionControlClassDiagram.png)

The facade classes are: 
- `VersionControlGeneralReader`: to read `VcObject` from disk
- `VersionControlGeneralWriter`: to write `VcObject` to disk
- `HashComputer`: to compute hash of a file or hash of a `VcObject`. MUST NOT expose actual
write methods as this would allow `VersionedModel` to write to disk, breaking abstractions.

To modify disk representation of a particular class which extends from `VcObject`, modify both its `Reader`
  and `Writer`.

**Design Considerations:**

**Aspect: How to represent `VcObject` in disk**
- **Alternative 1 (current choice)**: Custom representation
  - Pros: Smaller disk space utilisation
  - Cons: Added layer of complexity
- **Alternative 2**: Standard representation e.g. JSON
  - Pros: Parsing is simple i.e. use Jackson library
  - Cons: Larger disk space utilisation

Considering that `VcObject`s are not very complicated, the added layer of complexity does not outweigh 
the benefits of smaller disk space utilisation in our opinion. Hence, we go for alternative 1.

**Possible improvements in the future:**
- Make version control folder hidden from user
- Make version control files read-only. This is best done if future iterations implement a login system
- Use delta encoding to keep track of `VcObject`

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Managing Students' Personal Details
### AddCommand

This command adds a new `Student` to `AcademyDirectory`.

#### Implementation
`AddCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose. 
The `AddCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

`AddCommand` adds students to the `AcademyDirectory`. This command prevents addition of duplicate students by ensuring that each `Student` has a unique `NAME`. 
The `NAME` field is a sufficiently unique field to identify a unique instance of a student because `AcademyDirectory` is scoped to 
service CS1101S Avengers. In the CS110S module each _Studio_ has at most 10 `Students`. Thus, the probability of a `NAME` collision being sufficiently minimised.

<div markdown="span" class="alert alert-info">:information_source: **Note:** The responsibility of ensuring that `Student` 
does not have unnecessary duplicate personal detail (e.g same `PHONE`) is left to the Avenger.
</div>

The specifics are shown in the sequence diagram below:
![AddCommandSequenceDiagram](images/dg/logic/commands/addcommand/AddCommandSequenceDiagram.png)

### DeleteCommand

This command deletes a `Student` from `AcademyDirectory`.

#### Implementation
`DeleteCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `DeleteCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands).

`DeleteCommand` deletes `Student` based on the relative `INDEX` in the `ObservableList` which is the list of `Student` viewed by the `Avenger`. To do this, `DeleteCommand` makes a call to `VersionedModel#deleteStudent()`.

The specifics are shown in the sequence diagram below:
![DeleteCommandSequenceDiagram](images/dg/logic/commands/deletecommand/DeleteCommandSequenceDiagram.png)

### TagCommand
This command assigns tags to a `Student`.

#### Implementation
`TagCommand` will extend the `Command` class and consequently `@Override` the `Command#execute()` method
to serve the aforementioned purpose. The `TagCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands).

`TagCommand` tags a `Student` based on the relative `INDEX` in the `ObservableList`. The `Tag` is implemented with a HashSet
that stores the `Tag`. When `TagCommand` is executed, the `Student`'s `Tag` attribute will be replaced by a new HashSet
containing the input `Tag` object(s). In the event the input `Tag` is empty, the `Student`'s `Tag` attribute will be replaced by an
empty HashSet.

The following sequence diagram describes what happens when `TagCommand` is executed:

![TagCommandSequenceDiagram](images/dg/logic/commands/tagcommand/TagCommandSequenceDiagram.png)

### GetCommand
This command serves to retrieve a specific `PersonalDetail` of students or a student.

#### Implementation
`GetCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

All fields of `Student` class which implements the `PersonalDetail` interface and whose prefix is present in
`GetCommand#SUPPORTED_PREFIX` can be queried by `GetCommand`.

A `GetCommand` is initialized with a list of `Prefix` objects which represents the prefix of the `PersonalDetail`
to be retrieved, and a list of `String` which represents the keywords that will be matched with 
the names of students in Academy Directory. List of `Prefix` cannot be empty, but list of keywords can be.
An empty list of keywords is interpreted as retrieving personal details of all students in the model. If 
list of keywords is not empty, then the pattern-matching behavior for name in `GetCommand` is similar to that
of the [`FilterCommand`](#filtercommand).

The specifics are shown in the sequence diagram below:

![GetCommandSequenceDiagram](images/dg/logic/commands/getcommand/GetCommandSequenceDiagram.png)

Because the output of `GetCommand` can be long, for readability reasons the result is displayed
in the `AdditionalView` part of the UI. 

### EditCommand
This command edits a `Student`'s personal details such as their `NAME`, `PHONE`, `TELEGRAM` and `EMAIL`.

#### Implementation
`EditCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `EditCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

Similar to `AddCommand`, `EditCommand` supports duplicate prevention by checking that the `NAME` being edited is unique in the list
unless the `NAME` is the same  as the `Student` being edited.

<div markdown="span" class="alert alert-info">:information_source: **Note:** The responsibility of ensuring that `Student` 
does not have unnecessary duplicate information (e.g same `PHONE`) is left to an Avenger.
</div>

The following sequence diagram describes what happens when `GradeCommand` is executed:

![EditCommandSequenceDiagram](images/dg/logic/commands/editcommand/EditCommandSequenceDiagram.png)

### Track Students' Grades, Studio Attendance, and Participation

### GradeCommand

This command serves to update the `Grade` of various `Assessment` that the students will undergo in CS1101S. The assessments include RA1, Midterm, RA2, Practical Exam (PE), and Final.

#### Implementation

`GradeCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `GradeCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

The recording of grade is facilitated by adding an `Assessment` parameter to the `Student`.
The `Assessment` is implemented with a HashMap that stores the String representation of the assessments as the keys, and the integer `Grade` as the values.

The following sequence diagram describes what happens when `GradeCommand` is executed:

![GradeCommandSequenceDiagram](images/dg/logic/commands/gradecommand/GradeCommandSequenceDiagram.png)

### AttendanceCommand

This command serves to update the attendance status of students. A student's `Attendance` can be either attended or unattended.

#### Implementation

`AttendanceCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `AttendanceCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

The attendance mechanism is facilitated by adding a `StudioRecord` parameter to the `Student`. This `StudioRecord` has an `Attendance` object which we can use to track and update the `Attendance` of the `Student`. `Attendance` implements `Information` and the actual storing of the attendance status is done with a `boolean array`.

The following sequence diagram describes what happens when `AttendanceCommand` is executed:

![AttendanceCommandSequenceDiagram](images/dg/logic/commands/attendancecommand/AttendanceCommandSequenceDiagram.png)

For `UpdateModelAttendanceSequenceDiagram`, the sequential diagrams can be found below:

![UpdateModelAttendanceSequenceDiagram](images/dg/logic/commands/attendancecommand/UpdateModelAttendanceSequenceDiagram.png)

### ParticipationCommand

This command serves to update the `Participation` score of students. Following the XP system for CS1101S, each student is awarded between 0 and 500 XP (inclusive) per Studio session.

#### Implementation

`ParticipationCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `ParticipationCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

The implementation is similar to `AttendanceCommand`, with the same sequence diagram being applicable for Participation given that the proper refactoring to `Participation` is done.

`ParticipationCommand` has an additional section in the sequence diagram located above the loop in `AttendanceCommand`. The purpose of the logic below is to update a student's `Attendance` to be marked as present if the `participationUpdate` is greater than 0. This is because a student that has a positive `Participation` score would also count as having attended the `Studio`.

![ParticipationCommandMarkAttendanceSequenceDiagram](images/dg/logic/commands/participationcommand/ParticipationCommandMarkAttendanceSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The logic above is to update the `Attendance` and is only executed in the event that `participationUpdate` is more than 0. Otherwise, it will not run.

 </div>

### Visualization Tools

### ViewCommand

This command serves to display the summarised details of a single `Student` in the `AcademyDirectory`.

![ViewCommandSequenceDiagram](images/dg/logic/commands/viewcommand/ViewCommandSequenceDiagram.png)

#### Implementation

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

The grades are collated by iterating through all the students and extracting the grades from the `Assessment` HashMap using the input `Assessment` as the key.

The information is returned as a HashMap with key being the `Assessment` name and value being a list containing the class' grade in that assessment.
The information will be displayed in the AdditionalView. The success message is parsed into `CommandResult` to be returned by `VisualizeCommand`.

The following sequence diagram describes what happens when `VisualizeCommand` is executed:

![VisualizeCommandSequenceDiagram](images/dg/logic/commands/visualizecommand/VisualizeCommandSequenceDiagram.png)

### FilterCommand

This command filters the `ObservableList` by `Name` or `Tag`.  

#### Implementation

`FilterCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.

The `FilterCommand` searches the `AcademyDirectory` for students with `Name` or `Tag` matching the keyword and displays
the student list with the filtered students.

The following sequence diagram describes what happens when `FilterCommand` is executed:

![FilterCommandSequenceDiagram](images/dg/logic/commands/filtercommand/FilterCommandSequenceDiagram.png)

### SortCommand

This command sorts the `AcademyDirectory` student list based on their `Participation`, `Assessment` and `Name`. When sorting by `Assessment`, users have the option of sorting by individual `Assessment` or by the average grade among. Users can also choose if they want to sort by ascending or descending.

#### Implementation

`SortCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
The `SortCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

The sorting mechanism is based on the `List` interface as it sorts the various `FilteredList` instances using `Comparator`. Based on the `attribute` of the `SortCommand` being executed, the `Comparator` differs as shown by the sequential diagram below:

![SortCommandSequenceDiagram](images/dg/logic/commands/sortcommand/SortCommandSequenceDiagram.png)

The reference frame for GetComparator can be found below. It details the selection process based on the `attribute` of the `SortCommand`.

![GetComparatorSequenceDiagram](images/dg/logic/commands/sortcommand/GetComparatorSequenceDiagram.png)

### Others

### ExitCommand

This command allows user to exit the application after saving all operations and data
It extends the `Command` class and will consequently `@Override` the `Command#execute()` method to serve this purpose.

#### Implementation

![ExitCommandSequenceDiagram](images/dg/logic/commands/exitcommand/ExitCommandSequenceDiagram.png)

### ListCommand

This command shows all students on the class, ordered by when the student is added to the Academy.

#### Implementation

`ListCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose.
`ListCommand` retrieves the student list and display it on the student list panel on the left side. All students will be displayed.

![ListCommandSequenceDiagram](images/dg/logic/commands/listcommand/ListCommandSequenceDiagram.png)

### ClearCommand

This command clears all `Student` entries from `AcademyDirectory`.

#### Implementation

`ClearCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to serve the aforementioned purpose 
and is Version Controllable. A new Academy Directory is created to replace the current one, meaning that the student list is set to empty.
The `ClearCommand` is a version controlled command. For the list of version controlled command, refer [here](#appendix-c-version-controlled-commands)

![ClearCommandSequenceDiagram](images/dg/logic/commands/clearcommand/ClearCommandSequenceDiagram.png)

### HistoryCommand
This command shows the commit history. Each commit will be shown with its five character hash, 
author, date, and commit message. Only commands that are _version controlled_ will result in a commit
being created and thus shown by `HistoryCommand`. Commands which can be undone are referred to as _version controlled commands_ (read [here](#glossary)
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
- fetch the commits labelled as `MAIN` and `OLD` from disk (methods to do this exposed by `VersionedModel`)
- find the lowest common ancestor between `MAIN` and `OLD` (`Commit#findLca` method used here)
- show all commits from initial commit until the lowest common ancestor found above normally
- show all commits from the lowest common ancestor until `MAIN` and `OLD` as per the desired
formatting

The following sequence diagram shows the above implementation:

![HistoryCommandSequenceDiagram](images/dg/logic/commands/historycommand/HistoryCommandSequenceDiagram.png)

Note that the above diagram is omits several details but should be sufficient to grasp how `HistoryCommand` works.

#### Limitation
The current implementation can only show two commit branches: `MAIN` and `OLD`. While this is
sufficient in most cases, the ability to show arbitrary number of commit branches to give users
the ability to revert to any previous commits easily without having to look for the commit's hash
manually in disk. However, due to the implementer's inability to figure out how best to show
arbitrary number of commit branches, the current `HistoryCommand` thus can only show two branches. 

### RevertCommand
This command reverts the underlying `AcademyDirectory` data to a previous commit, as identified by the commit's hash.
Commands which can be undone are referred to as _version controlled commands_ (read [here](#glossary) 
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

![RevertCommandSequenceDiagram](images/dg/logic/commands/revertcommand/RevertCommandSequenceDiagram.png)

Note that the above diagram is incomplete; it is approximately accurate but is sufficient to help explain the limitation
of `RevertCommand`, as in below. 

#### Limitation
Because `RevertCommand` has to restore academy directory data which is the responsibility of the
`Storage` component, `RevertCommand` creates a `StorageManager` and uses it to load data from disk 
and set the current `VersionedModel` internal data to the newly reloaded data. This is indicated in the sequence diagram
as the sudden use of `Storage` component directly from `RevertCommand`. This is (highly) not 
ideal, but the implementer has no idea how to do this properly i.e. without sudden creation of a new `StorageManager`
... Right now the solution is to ensure that this newly created `StorageManager` is destroyed immediately to prevent
access from elsewhere, and to put this limitation in this documentation to be solved one day. 

### UndoCommand
This command undoes a change done to the underlying `AcademyDirectory` data. Note that this is
different from the _view_ of the data e.g. when the `ViewCommand` is used, the _view_ visible to 
the user changes, but the underlying data does not change. Commands which can be undone are referred to 
as _version controlled commands_ (read [here](#glossary) for details on what this means).

#### Implementation
`UndoCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose. Internally, the `UndoCommand` makes use of the `RevertCommand`. 
Hence `UndoCommand` serves as _syntactic sugar_ for the `RevertCommand`.

### RedoCommand
This command redoes a change done to the underlying `AcademyDirectory` data. Note that this is
different from the _view_ of the data e.g. when the `ViewCommand` is used, the _view_ visible to
the user changes, but the underlying data does not change. Commands which can be redone are referred to
as _version controlled commands_ (read [here](#glossary) for details on what this means).

#### Implementation
`RedoCommand` will extend the `Command` class and will consequently `@Override` the `Command#execute()` method to
serve the aforementioned purpose. Internally, the `RedoCommand` makes use of the `RevertCommand`.
Hence `RedoCommand` serves as _syntactic sugar_ for the `RevertCommand`.

### HelpCommand

This command serves to guide new users on using the application, which syntax to use and when to use them. Users can view a summary of all commands' syntax,
or a specific guide on how to use a particular command.

#### Design considerations

In implementing the Help Command, two alternative options were proposed in taking what the help message should be:
1. To parse the help message directly from the User Guide
   * Pros: This is a more logical way to get the help message, relying on a parser that identifies the common starting section of each command on the User Guide
   * Cons: Unfortunately, in doing so, there was a bug exception FileNotFoundException, of which HelpCommand cannot take the help message directly from the User Guide
   because it cannot find the file. Keeping the help message on the /resources folder was a solution, but the risk of file cannot be read is still there. In addition,
   this creates the risk of the parser working incorrectly if the header section of the User Guide changes.
2. To store a HELP_MESSAGE field in each command class
   * Pros: Does not rely on external file, and is not prone to FileNotFoundException. Also, it encapsulates the object better, with each command having an associated
   static field of HELP_MESSAGE inside it (except for HelpCommand itself), and could be considered a good design pattern.
   * Cons: This requires the help message to be constantly updated manually by the developer, each time the User Guide changes. In addition, the length of the help
   message makes the Command classes harder to read.
     
Ultimately, Solution 2 was adopted to reduce the risk of bugs on Help Command being vulnerable to FileNotFoundException error, and to better encapsulate the Command
classes. In addition, Academy Directory will always require maintenance from the developers, and it is not a bad thing to keep the HELP_MESSAGE updated to the User Guide changes continuously.

#### Implementation

`HelpCommand` will extend the `Command` class, and consequently `@Override` the `Command#execute()` method to serve its initial purposes.

The mechanism of the command is done by retrieving a `HELP_MESSAGE` field in each of the other command classes (other than HelpCommand itself). This help command will
be displayed to the user on a separate window later on.

![HelpCommandSequenceDiagram](images/dg/logic/commands/helpcommand/HelpCommandSequenceDiagram.png)

As seen from the diagram, the `HelpCommand` involves the use of conditional branches. If the optional condition is met, a `CommandException` is thrown to let
users know that the input is invalid.

Otherwise, the HelpCommand will use conditional branch to guide users to two different scenarios, as shown above. If it is a general help, a general help command
will be created. If it is a specific help, then a specific help command associated with a command will be created.

--------------------------------------------------------------------------------------------------------------------

## **Guides**

The following links to guides on: Documentation, Logging, Testing, Configuration, Dev-Ops.

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------
## **Appendix**

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
| `* * *`  | First time user           | access the relevant commands with a "help" command                 | learn how to use the app more easily                                   |
| `* *`    | CS1101S Avenger           | view the average scores of my students for specific assessments    | focus on the aspects to improve on during tutorial                     |
| `* *`    | CS1101S Avenger           | visualize the class scores for specific assessments                | gauge how well my students are doing in assessments                    |
| `* *`    | CS1101S Avenger           | add tags to certain students to take note of their weaker topics   | focus on topics that they are struggling with                          |
| `* *`    | CS1101S Avenger           | view all information relating to one student in one go             | have an overview of my student status                                  |
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
* 3b. User requests for help in a command that does not exist
  * 3b1. Academy Directory shows an error message
    
    Use case resumes at step 3.

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
    
* 1b. The input grade is not a non-negative integer.

    * 1b1. Academy Directory requests for user to enter a non-negative integer.

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
  
**Use case: View student information**

**MSS**

1. User enter a command to view all information of one student by index
2. Academy Directory fetches all related data of one student
3. Academy Directory displays all information to users.

**Extensions**
* 1a. Users request information of a student whose index exceeds the student list size
    * 1a1. Academy Directory informs users that the index is invalid via an error message
      
      Use case resumes at step 1
  
* 1a. The index number is invalid (not a positive integer)
    * 1a1. Academy Directory informs users that the index is invalid and must be a positive integer for the command to be executed
     
      Use case resumes at step 1

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to _3000_ student without a noticeable sluggishness in performance for typical usage.
3. A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should work in computer with `32-bit` or `64-bit` processor.
5. User data should be in human editable file and stored locally.
6. Should run on user computer with double-click - no installer or additional libraries required.
7. Should not require internet connection for any of its feature.
8. All user and app data should be stored locally, not through an online database solution.
9. Logs and previous commits stored should be based on the _Principle of Least-Privilege_.
10. Logs and previous commits should be recoverable even when `AcademyDirectory` itself is deleted.
11. Logs and previous commits should be transferable and functional after transfer onto other computers.
12. Users should be able to undo up to at least _100_ commands.
13. Software default file size should not exceed _50_ Megabytes.

### Glossary

| Term | Definition | Comments |
|-----| ----------- | ---------- |
| Operating System (OS) | Software that manages computer hardware and other computer software. | |
| Mainstream OS | Examples of mainstream OS includes: Windows, Linux, Unix, OS-X, MacOS, etc. | |
| Personal Detail | A contact detail of a student | Phone number, Telegram handle, and email address |
| CS1101S | An introductory Computer Science module for year 1 students in the the National University of Singapore. | |
| Studios | Tutorials held in CS1101S and are essential in aiding the students to improve their grasp on the concepts taught during the lecture. | < 10 students in a studio |
| Avengers | A special term to call a CS1101S tutor. An avenger organizes a Studio session to improve on CS1101S concepts taught in lecture, recording attendance and grades. | An avenger holds at most one class. |
| Principle of Least-Privilege | Minimum levels of access – or permissions – needed to perform function. | |
| Command Line Interface (CLI) | A text-based user interface, where users type commands to instruct the computer to do something. | |
| Graphical User Interface (GUI) | A graphics-based user interface, where users click buttons to instruct the computer to do something. | |
| Java | A program that allows running other programs written in Java programming language. | |
| `Command` | An interface representing an instruction typed by a user to Academy Directory. | |
| Version controlled `Command` | a `Command` that logs a commit message, and thus stages at least one `VcObject` object upon execution. | Refer to the list of such commands [here](#appendix-c-version-controlled-commands) |
| Command Box | A part of the Academy Directory's GUI which can be used by users to type commands. | |
| Field | Additional information that can be provided to a command for correct command execution. | May or may not have an associated prefix |
| Parameter | Part of the command which provides additional information provided by the user. | Actual values for the fields |
| Prefix | An abbreviation of a field. | Always ends with a backslash ('/') |

--------------------------------------------------------------------------------------------------------------------

## **Appendix B: Instructions for manual testing**


Manual testing was conducted internally by the team as of the time of writing.


The details of the testing procedure can be found here: [Manual Testing](ManualTesting.md)


<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are encouraged to do more *exploratory* testing.


In the future, we aim to expand testing to be done by external testers to improve the testing rigour.
</div>

## **Appendix C: Version Controlled Commands**
The following list is a list of commands that are version controlled i.e. they can be undone and
redone using the `UndoCommand` and the `RedoCommand` command. Furthermore, the use of these commands
will be reflected in the commit history, using the `HistoryCommand`. More concretely, these commands _stages_
`VcObject`/s that will be saved to disk, thus facilitating `VersionControl`
- [`AddCommand`](#addcommand)
- [`DeleteCommand`](#deletecommand)
- [`TagCommand`](#tagcommand)
- [`EditCommand`](#editcommand)
- [`GradeCommand`](#gradecommand)
- [`AttendanceCommand`](#attendancecommand)
- [`ParticipationCommand`](#participationcommand)
- [`SortCommand`](#sortcommand)
- [`ClearCommand`](#clearcommand)

Commands not shown in the above list _will not_ appear in the commit history, and thus
cannot be reverted to and / or be undone or redone.

## **Appendix D: Effort**
If the effort required to create **AB3** is 10, we would place the effort level required to implement the current version
of **AcademyDirectory** at 15.

Our team has put in a significant amount of effort to get AcademyDirectory to the current version. Below, we list some notable changes overall and notable features implemented by us.

### Notable Changes in General

1. **Morphed existing AB3 to align with our design for AcademyDirectory**

   We have put in a significant amount of effort morphing the existing code base, AB3 to support the need of our application, which is designed for CS1101S avenger to be more effective and efficient.

   Firstly, we had to create new classes for components related to our application, such as <TO BE ADDED>. Each of these classes has different input format requirements and is related to different command.

   Secondly, we had to remove all the irrelevant classes and update the existing test cases to fit our need.

   Lastly, we had to integrate all these new classes with the existing code to save applications data to a file
   in AcademyDirectory. This required major refactoring of existing classes to support multiple new fields and commands.


2. **Redesigned GUI**

   Compared to AB3, AcademyDirectory <TO BE ADDED> 

### Notable Features

Notable features we implemented from scratch include <TO BE ADDED: SOME USEFUL COMMANDS E.G. FILTER SORT ETC.>
We came up with these features as they fit well in helping CS1101S avengers be more effective and efficient.

The implementation details and design considerations for these features could be found in [Implementation](#implementation) section
and the [Design](#design) section respectively.

## **Appendix 4: Limitations and Future improvements**

We acknowledge the fact that our current product is not perfect, and it still has rooms for improvement.
Below are some limitations and future improvements of our product.

### Limitations
1. **Commands not accepting multiple fields**

   Currently, our product does not support the functionality of accepting multiple fields. For example:
  - `find c/DBS p/programmer`
  - `soon d/7 i/20`

    The above commands will produce an error message, stating that the commands are invalid. This is because our initial implementation would only take one field (e.g c/) and ignore the rest. Thus, the workaround for this issue is to not allow the users to enter multiple fields.

2. **Invalid prefix resulting in an unexpected error message**

   As pointed out in PE-D, our current product is not able to check for a typo in the prefixes. For example:
  - `edit 1 c/Grab zp/Engineer`

    The example above will produce an error message, stating that the company name should contain alphanumeric characters. This is because our current implementation will take `zp/Engineer` as a part of the company name.