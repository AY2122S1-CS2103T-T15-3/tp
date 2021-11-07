---
layout: page
title: Amadeus Aristo Winarto's Project Portfolio Page
---

### Project: Academy Directory

Academy Directory is a desktop address book application used for CS1101S tutors to keep track of information related to their students. The user interacts with it using a CLI and mouse, and it has a GUI created with JavaFX. It is written in Java, and has about 14 kLoC.

Given below are my contributions to the project.

#### Major Contribution:

* **New Feature**: Add the ability to get personal details of students (GetCommand)
  * What it does: Allows user to get personal details of student/s easily. Personal details refer to email address, telegram handle, or phone number (if any) 
  * Justification: This feature allows the user to easily retrieve students contact details, thus facilitating easy broadcasts to students e.g. 
  when trying to send email to everyone, no need to wait for Outlook to retrieve email address and risk getting a person with 
  a similar name's email address. Instead, the user can use the command to get all emails and copy-paste into their email client. Output of GetCommand
  is formatted with this in mind. 
  * Highlights: This feature can handle retrieval of personal detail of a specific student or specific students who are matched by provided keywords with student names in the AcademyDirectory.
  * Credits: None
  
* **New Feature**: Add internal version control system
  * What it does: This feature allows AcademyDirectory to keep track of state changes to the underlying data. Much like how Git 
  keeps track of changes to files, so does the version control system in AcademyDirectory. This version control is the basis of the 
  following commands: 
    * HistoryCommand: to view commit history i.e. similar to `git log`
    * RevertCommand: to revert state of AcademyDirectory to a previous commit i.e. similar to `git checkout`
    * UndoCommand: to undo a change made to AcademyDirectory data. Syntactic sugar for RevertCommand 
    * RedoCommand: to redo a change made to AcademyDirectory data. Syntactic sugar for RevertCommand
  * Justification: This feature may seem like an overkill for implementing an undo/redo system. However, there are several 
  benefits for using this approach: 
    * Although current iteration of AcademyDirectory only keeps track of one file i.e. `academydirectory.json`, considering 
    the intention of the program to be extensible to other modules than CS1101S which may require tracking state changes to 
    additional files, an internal VCS seems to be the most future-proof solution.
    * HistoryCommand and RevertCommand, and thus UndoCommand and RedoCommand by extension, becomes essentially trivial 
    in implementation because of this
    * Because our current target audience is CS1101S tutors who are highly likely to be well-versed with Git, a similar
    feature reduces context-switching for the users. For less technical users, the syntactic sugars are provided.
    * It is a good learning opportunity i.e. to learn how Git works, how to (re)design model and software architecture etc.
  * Highlights: At risk of infinite recursion, this feature is itself the highlight.
  * Credits: As this feature is essentially a dumbed-down version of Git, thus credit on design is given fully to Git e.g.
  the use of Commit, Tree and how these interact, behavior of `git log` and `git checkout` and their presentations etc.
  
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s1.github.io/tp-dashboard/?search=Amadeus&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2021-09-17&tabOpen=true&zFR=false&tabAuthor=Amadeus-Winarto&tabRepo=AY2122S1-CS2103T-T15-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&tabType=authorship)

* **Project management**:
  * Proposed and enforced the use of Github Projects for better weekly sprints and code reviews to maintain code quality
  * Wrote several meeting notes to keep track of meeting outcomes and weekly deliverables

* **Documentation**:
  * User Guide:
    * Heavily refactored User Guide from original AB3 format to its current form
    * Added documentation details of the `get`, `revert`, `history`, `undo`, and `redo`
  * Developer Guide:
    * Added implementation details of the `get`, `revert`, `history`, `undo`, and `redo`
    * Added architectural implementation of Model component, which is now the VersionedModel component

* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#55](https://github.com/AY2122S1-CS2103T-T15-3/tp/pull/55), [\#87](https://github.com/AY2122S1-CS2103T-T15-3/tp/pull/87), [\#124](https://github.com/AY2122S1-CS2103T-T15-3/tp/pull/124), etc.
  * Contributed to forum discussions
  * Reported bugs and suggestions for other teams in the class i.e. PED, by personal chat etc.
  * Participated in all team meeting and discussion throughout the duration of the project.
