import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import java.io.File

class SelectCodeJumpAction: PsiElementBaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.isInTypeScriptFile() || element.isDenoJsonFile()
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val denoCachePath = System.getProperty("user.home") + "/Library/Caches/deno"

        for ((key, value) in dictionary()) {
            if (element.text.contains(key)) {
                openFileInEditor(project, denoCachePath + value)
                break // Exit the loop once a match is found
            }
        }
    }

    override fun getText() = "Code jump for unresolved function"
    override fun getFamilyName() = "SelectCodeJumpAction"

    private fun openFileInEditor(project: Project, filePath: String) {
        val file = File(filePath)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        } else {
            // Handle the case where the file does not exist or could not be found
            println("File not found: $filePath")
        }
    }

    private fun dictionary() = mapOf(
        "datasource-rest" to "/npm/registry.npmjs.org/@apollo/datasource-rest/6.0.1/dist/RESTDataSource.d.ts",
        "server" to "/npm/registry.npmjs.org/@apollo/server/4.7.5/dist/esm/externalTypes/plugins.d.ts",
        "fetcher" to "/npm/registry.npmjs.org/@apollo/utils.fetcher/3.0.0/dist/index.d.ts",
        "express" to "/npm/registry.npmjs.org/@types/express/4.17.13/index.d.ts"
    )
}

fun PsiElement.isInTypeScriptFile(): Boolean {
    val fileType = (containingFile?.fileType as? LanguageFileType) ?: return false
    return fileType.language.id.lowercase() == "typescript"
}

fun PsiElement.isDenoJsonFile(): Boolean {
    return containingFile?.name == "deno.json"
}
