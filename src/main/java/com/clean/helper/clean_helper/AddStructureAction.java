package com.clean.helper.clean_helper;

import com.clean.helper.clean_helper.icons.Icons;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AddStructureAction extends CreateFileFromTemplateAction {


    public AddStructureAction() {
        super(
            "Clean Architecture Structure",
            "It creates 3 roots folders called \"domain\", \"data\" and \"presentation\" and their own subdirectories in line with clean architecture structure.",
             Icons.ActionIcon
        );
    }

    /**
     * @param project
     * @param directory
     * @param builder
     */
    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder
                .setTitle("New Clean Architecture Structure")
                .addKind("Clean Architecture Structure", Icons.ActionIcon, "Clean Architecture Structure");
    }

    /**
     * @param directory
     * @param newName
     * @param templateName
     * @return
     */
    @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return "Clean Architecture Structure";
    }

    @Override
    protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
        dir.createSubdirectory(name);
        var featureRoot = dir.findSubdirectory(name);

        //region Root Section
        featureRoot.createSubdirectory("data");
        featureRoot.createSubdirectory("domain");
        featureRoot.createSubdirectory("presentation");
        //endregion

        //region data Section
        var featureSubRoot = featureRoot.findSubdirectory("data");
        featureSubRoot.createSubdirectory("datasources");
        featureSubRoot.createSubdirectory("repositories");
        featureSubRoot.createSubdirectory("models");
        //endregion

        //region domain Section
        featureSubRoot = featureRoot.findSubdirectory("domain");
        featureSubRoot.createSubdirectory("entities");
        featureSubRoot.createSubdirectory("repositories");
        featureSubRoot.createSubdirectory("usecases");
        //endregion

        //region presentation Section
        featureSubRoot = featureRoot.findSubdirectory("presentation");
        featureSubRoot.createSubdirectory("pages");
        featureSubRoot.createSubdirectory("widgets");
        featureSubRoot.createSubdirectory("blocs");
        featureSubRoot.createSubdirectory("assets");

        featureSubRoot = featureSubRoot.findSubdirectory("assets");
        featureSubRoot.createSubdirectory("images");
        System.out.println(featureSubRoot.isDirectory());
        //endregion
        return null;
    }
}
