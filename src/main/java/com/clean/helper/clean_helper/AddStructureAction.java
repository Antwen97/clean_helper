package com.clean.helper.clean_helper;

import com.clean.helper.clean_helper.components.PropsDialog;
import com.clean.helper.clean_helper.icons.Icons;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AddStructureAction extends AnAction {


    public AddStructureAction() {
        super(
                "Clean Architecture Structure",
                "It creates 3 roots folders called \"domain\", \"data\" and \"presentation\" and their own subdirectories in line with clean architecture structure.",
                Icons.ActionIcon
        );
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DataContext dataContext = e.getDataContext();

        IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null)
        {
            return;
        }

        String templateName = "JavaFXApplication";

        Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        PropsDialog infoForm = new PropsDialog(project, view);
        infoForm.getWindow().setIconImage(Icons.ActionImageIcon.getImage());
        infoForm.pack();
        infoForm.show();

        if (infoForm.getExitCode() == DialogWrapper.OK_EXIT_CODE && project != null)
        {
            FileTemplate fileTemplate = FileTemplateManager.getInstance(project).getInternalTemplate(templateName);

            PsiElement element = createFile(project, infoForm.getStructureName(), templateName, Objects.requireNonNull(view.getOrChooseDirectory()), infoForm.isStatefulBoxSelected(), infoForm.isBlocBoxSelected());
        }
    }

    @Override
    public void update(final AnActionEvent event)
    {
        DataContext dataContext = event.getDataContext();
        boolean enabled = true; //whatever criteria you have for when it's visible/enabled
        Presentation presentation = event.getPresentation();
        presentation.setVisible(enabled);
        presentation.setEnabled(enabled);
    }

    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder
                .setTitle("New Clean Architecture Structure")
                .addKind("Clean Architecture Structure", Icons.ActionIcon, "Clean Architecture Structure")
                .setValidator(new InputValidatorEx() {
                    @Override
                    public @Nullable String getErrorText(@NonNls String inputString) {
                        if (inputString.isBlank()) {
                            return "Name can't be empty";
                        } else if (!inputString.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                            return "Invalid name";
                        }
                        return null;
                    }

                    @Override
                    public boolean canClose(String inputString) {
                        return getErrorText(inputString) == null;
                    }
                });
    }

    protected String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return "Clean Architecture Structure";
    }

    protected PsiFile createFile(Project project, String name, String templateName, PsiDirectory dir, boolean isStatefulSelected, boolean isBlocSelected) {

        Runnable r = () -> {
            PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
            System.out.println("Starting Creating Structure...");
            dir.createSubdirectory(name);
            var featureRoot = dir.findSubdirectory(name);

            if(featureRoot != null) {
                //region Root Section
                featureRoot.createSubdirectory("data");
                featureRoot.createSubdirectory("domain");
                featureRoot.createSubdirectory("presentation");
                //endregion

                //region data Section
                var featureSubRoot = featureRoot.findSubdirectory("data");
                if(featureSubRoot != null) {
                    featureSubRoot.createSubdirectory("datasources");
                    featureSubRoot.createSubdirectory("repositories");
                    featureSubRoot.createSubdirectory("models");
                }
                //endregion

                //region domain Section
                featureSubRoot = featureRoot.findSubdirectory("domain");
                if(featureSubRoot != null) {
                    featureSubRoot.createSubdirectory("entities");
                    featureSubRoot.createSubdirectory("repositories");
                    featureSubRoot.createSubdirectory("usecases");
                }
                //endregion

                //region presentation Section
                featureSubRoot = featureRoot.findSubdirectory("presentation");
                if(featureSubRoot != null) {
                    featureSubRoot.createSubdirectory("pages");
                    featureSubRoot.createSubdirectory("widgets");
                    featureSubRoot.createSubdirectory("blocs");
                    featureSubRoot.createSubdirectory("assets");
                }

                if(featureSubRoot != null) {
                    featureSubRoot = featureSubRoot.findSubdirectory("assets");
                    if(featureSubRoot != null) {
                        featureSubRoot.createSubdirectory("images");
                    }
                }
                //System.out.println("Directories created.\nStarting Creating Files if checkbox are selected.\nStateful Selection Status: "+isStatefulSelected+"\nBloc Selection Status: "+isBlocSelected);
                //endregion

                //region presentation Options
                if(isStatefulSelected) {
                    featureSubRoot = featureRoot.findSubdirectory("presentation");
                    if(featureSubRoot != null) {
                        featureSubRoot = featureSubRoot.findSubdirectory("pages");
                    }
                    //region FileContent
                    var className = replaceWithUpperCase(name)+"Page";
                    String widgetContent = "import 'package:flutter/material.dart';\n\n" +
                            "class "+className+" extends StatefulWidget {\n\n" +
                            "\tconst "+className+ "({super.key});\n\n" +
                            "\t@override\n" +
                            "\tState<"+className+"> createState() => _"+className+"State();\n" +
                            "}\n\n" +
                            "class _"+className+"State extends State<"+className+"> {\n"+
                            "\n\t@override\n\tWidget build(BuildContext context) {\n" +
                            "\t\t\n" +
                            "\t}\n}";
                    //endregion

                    if(featureSubRoot != null) {
                        createAndWriteFile(project, documentManager, featureSubRoot, name.toLowerCase()+"_page.dart", widgetContent);
                    }
                }
                final String camelCaseName = replaceWithUpperCase(name);
                if(isBlocSelected) {
                    featureSubRoot = featureRoot.findSubdirectory("presentation").findSubdirectory("blocs");
                    featureSubRoot = featureSubRoot.createSubdirectory(name+"_bloc");

                    //region Blocs Content
                    var blocClassName = camelCaseName+"Bloc";
                    var stateClassName = camelCaseName+"State";
                    var eventClassName = camelCaseName+"Event";
                    String blocContent = "import 'package:flutter_bloc/flutter_bloc.dart';\n\n" +
                            "part '"+ name +"_event.dart';\n" +
                            "part '"+ name +"_state.dart';\n\n" +
                            "class "+blocClassName+" extends " +
                            "Bloc<"+ eventClassName +","+stateClassName+"> {\n\n" +
                            "\t"+blocClassName+"() : super(const "+camelCaseName+"Initial()) {\n" +
                            "\t\ton<"+ camelCaseName +"Event>((event, emit) async {\n\t\n"+
                            "\t\t});\n" +
                            "\t}\n}";
                    String eventContent = "part of '"+name+"_bloc.dart';\n\n" +
                            "abstract class "+camelCaseName+"Event extends Equatable {\n\n" +
                            "\tconst "+camelCaseName+"Event();\n\n" +
                            "\t@override\n" +
                            "\tList<Object?> get props => [];\n" +
                            "}";
                    String stateContent = "part of '"+name+"_bloc.dart';\n\n" +
                            "abstract class "+camelCaseName+"State extends Equatable {\n\n" +
                            "\tconst "+camelCaseName+"State();\n\n" +
                            "\t@override\n" +
                            "\tList<Object?> get props => [];\n" +
                            "}\n\n" +
                            "class "+camelCaseName+"Initial extends"+camelCaseName+"State {\n" +
                            "\tconst "+camelCaseName+"Initial();\n" +
                            "}";
                    //endregion

                    createAndWriteFile(project, documentManager, featureSubRoot, name.toLowerCase()+"_bloc.dart", blocContent);
                    createAndWriteFile(project, documentManager, featureSubRoot, name.toLowerCase()+"_event.dart", eventContent);
                    createAndWriteFile(project, documentManager, featureSubRoot, name.toLowerCase()+"_state.dart", stateContent);
                }
                //endregion
            }
        };

        Application application = ApplicationManager.getApplication();

        if(application.isDispatchThread()) {
            application.runWriteAction(r);
        } else {
            application.invokeLater(()-> application.runWriteAction(r));
        }

        return null;
    }

    private String replaceWithUpperCase(String className) {
        final String[] splittedString = className.split("_");
        StringBuilder result = new StringBuilder();
        for (String s : splittedString) {
            result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

    private void createAndWriteFile(Project project, PsiDocumentManager documentManager, PsiDirectory featureSubRoot, String fileName, String fileContent) {
        System.out.println("Creating the file named: "+fileName);
        PsiFile blocFile = featureSubRoot.createFile(fileName);

        System.out.println("File Created.\nStarting writing into the file...");
        Document document = documentManager.getDocument(blocFile);
        if(document != null) {
            Runnable writeDocument = () -> document.insertString(0, fileContent);
            WriteCommandAction.runWriteCommandAction(project, writeDocument);
            System.out.println("Writing File done.");
            if(!blocFile.isPhysical()) {
                System.out.println("Adding file to the directory because not exists..");
                featureSubRoot.add(blocFile);
            }
            System.out.println("File created Successfully.");
        }
    }
}
