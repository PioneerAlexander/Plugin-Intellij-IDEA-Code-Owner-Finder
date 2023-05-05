# Jetbrains Bootcamp Test Assignment

When working with large codebases, it is often convenient to know who knows best about the contents
of a given file. On the one hand, it may be the person who wrote the most code in that file. On the
other hand, especially if the file has existed a long time ago, it may sometimes be that the
original author hasn't looked into it for a long time
and may have forgotten about it. Surely someone who has worked with this code recently, but who has
made a significant contribution to this code may be able to provide more up-to-date information.
Let's call such a person the **code owner**.

This is an IntelliJ IDEA plugin that uses data from the
version control data, evaluate each committer's contribution to the selected file, and make a
recommendation, who to approach with questions. There is a template for the plugin - it is an
action (`AnAction`) in the `org.intellij.sdk.action.CodeOwnerFinderAction` class. It can be called
from the dropdown menu for any file in the Project View. The plugin can return information in a
dialog box created with `Messages.showMessageDialog`.

The central component of the job is an algorithm for calculating the contribution of individual
committers to a given file using data from the version control system and identifying the owner of
the code.

When implementing the plugin, I used the following IntelliJ Platform classes, properties and
methods:

* `ProjectLevelVcsManager.getInstance` to access version control system data
* `VcsContextFactory.SERVICE.getInstance()` to work with files and paths that are managed by a
  version control system
* `AbstractVcs.vcsHistoryProvider` to get a list of revisions (including for a given file)
* `VcsFileRevision

## The references I studied for developing plugins for IntelliJ IDEA

* [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)
* [Running a Simple Gradle-Based IntelliJ Platform Plugin](https://plugins.jetbrains.com/docs/intellij/gradle-prerequisites.html#executing-the-plugin)
* [Creating Actions](https://plugins.jetbrains.com/docs/intellij/working-with-custom-actions.html)
* [Virtual Files](https://plugins.jetbrains.com/docs/intellij/virtual-file.html)
* [Version Control Systems](https://plugins.jetbrains.com/docs/intellij/vcs-integration-for-plugins.html)