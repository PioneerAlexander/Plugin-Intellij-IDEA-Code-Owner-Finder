package org.intellij.sdk.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.AbstractVcs
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vcs.actions.VcsContextFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class CodeOwnerFinderAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: throw Exception("No file found")
        val vcs: AbstractVcs? = ProjectLevelVcsManager.getInstance(event.project!!).getVcsFor(file)
        val filePath = VcsContextFactory.SERVICE.getInstance().createFilePathOn(file)
        val session = vcs?.vcsHistoryProvider?.createSessionFor(filePath)
        val revisionList = session?.revisionList ?: throw Exception("No commits")
        var currPath: FilePath? = filePath
        val committersDatabase: MutableList<String> = mutableListOf()
        for (revision in revisionList) {
            val currentCommit = runBlocking(Dispatchers.IO) {
                vcs.loadRevisions(file, revision.revisionNumber)
            } ?: throw Exception("Commit not found")
            val ourFileChange = currentCommit.changes.find { it.afterRevision?.file == currPath }
            if (ourFileChange != null) {
                currPath = ourFileChange.beforeRevision?.file
            }
            val name = currentCommit.committerName
            committersDatabase.add(name)

            println(name)
        }
        val mappedCommittersCount = committersDatabase.groupBy { it }.mapValues { it.value.size }
        val informationCommits = StringBuilder()
        var bestCommitterByCount = ""
        var maximumCommits = 0
        for (committer in mappedCommittersCount) {
            informationCommits.append("${committer.key}has ${committer.value} commits\n")
            if (committer.value > maximumCommits) {
                bestCommitterByCount = committer.key
                maximumCommits = committer.value
            }
        }
        informationCommits.append("The best committer was ${bestCommitterByCount}with $maximumCommits commits\n")
        Messages.showMessageDialog(informationCommits.toString(), "Action", Messages.getInformationIcon())
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = project != null && file != null && !file.isDirectory
    }
}
