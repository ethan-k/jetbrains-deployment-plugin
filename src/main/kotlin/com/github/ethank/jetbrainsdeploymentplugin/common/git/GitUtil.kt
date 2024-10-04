package com.github.ethank.jetbrainsdeploymentplugin.common.git

import com.github.ethank.jetbrainsdeploymentplugin.common.git.model.CommitInfo
import com.intellij.openapi.project.Project
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

fun getRecentCommits(project: Project, count: Int): List<CommitInfo> {
    val projectPath = project.basePath ?: return emptyList()
    return try {
        val repo: Repository = FileRepositoryBuilder()
            .setGitDir(File(projectPath, ".git"))
            .build()

        Git(repo).use { git ->
            git.log().setMaxCount(count).call().map {
                CommitInfo(it.id.name.substring(0, 7), it.shortMessage)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

fun getBranch(project: Project): String {
    val projectPath = project.basePath ?: return "unknown branch"
    return try {
        val repo: Repository = FileRepositoryBuilder()
            .setGitDir(File(projectPath, ".git"))
            .build()
        repo.branch ?: "unknown branch"
    } catch (e: Exception) {
        e.printStackTrace()
        "unknown branch"
    }
}