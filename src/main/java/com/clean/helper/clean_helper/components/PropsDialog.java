package com.clean.helper.clean_helper.components;

import com.clean.helper.clean_helper.icons.Icons;
import com.intellij.ide.IdeView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PropsDialog extends DialogWrapper {

    private JCheckBox statefulWidgetOption;
    private JCheckBox blocOption;
    private JTextField fileNameField;
    private final JLabel errorLabel = new JLabel("");

    private final IdeView view;

    public PropsDialog(Project project, IdeView view) {
        super(true); // use current window as parent
        this.view = view;
        init();
        setTitle("New Clean Architecture Structure");
        super.getWindow().setIconImage(Icons.ActionImageIcon.getImage());
        try {
            Taskbar.getTaskbar().setIconImage(Icons.ActionImageIcon.getImage());
        } catch(Exception ignored) {}
        setResizable(false);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JPanel checkBoxPanel = new JPanel(new BorderLayout());

        dialogPanel.setBorder(null);
        checkBoxPanel.setBorder(JBUI.Borders.emptyTop(15));

        dialogPanel.setName("New Clean Architecture Structure");

        JLabel actionIcon = new JLabel(Icons.ActionIcon);
        fileNameField = new JTextField("");
        fileNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(isValidName()) {
                    setValidBorder();
                    PropsDialog.super.getOKAction().setEnabled(true);
                } else {
                    setErrorBorder();
                    PropsDialog.super.getOKAction().setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(isValidName()) {
                    setValidBorder();
                    PropsDialog.super.getOKAction().setEnabled(true);
                } else {
                    setErrorBorder();
                    PropsDialog.super.getOKAction().setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(isValidName()) {
                    setValidBorder();
                    PropsDialog.super.getOKAction().setEnabled(true);
                } else {
                    setErrorBorder();
                    PropsDialog.super.getOKAction().setEnabled(false);
                }
            }
        });
        fileNameField.setPreferredSize(new Dimension(100, 20));

        statefulWidgetOption = new JCheckBox("Create an empty Stateful Widget Page");
        blocOption = new JCheckBox("Create Bloc Class Files");
        blocOption.setBorder(JBUI.Borders.emptyTop(7));

        checkBoxPanel.add(statefulWidgetOption);
        checkBoxPanel.add(blocOption, BorderLayout.AFTER_LAST_LINE);

        dialogPanel.add(actionIcon, BorderLayout.BEFORE_LINE_BEGINS);
        dialogPanel.add(fileNameField, BorderLayout.CENTER);
        dialogPanel.add(checkBoxPanel, BorderLayout.AFTER_LAST_LINE);

        return dialogPanel;
    }

    @Override
    protected Action[] createActions() {
        super.createDefaultActions();
        // return right hand side action buttons
        return new Action[] { getOKAction() };
    }

    protected Action[] createLeftSideActions() {
        // return left hand side action buttons
        return new Action[] { };
    }

    @Override
    protected void doOKAction() {
        if(isValidName()) {
            super.doOKAction();
        }
    }

    public boolean isStatefulBoxSelected() {
        return statefulWidgetOption.isSelected();
    }

    public boolean isBlocBoxSelected() {
        return blocOption.isSelected();
    }

    public String getStructureName() {
        return fileNameField.getText();
    }

    private boolean isValidName() {
        return view.getOrChooseDirectory().findSubdirectory(fileNameField.getText()) == null;
    }

    private void setValidBorder() {
        float[] errorColor = Color.RGBtoHSB(69,73,74, null);
        fileNameField.setToolTipText(null);
        fileNameField.setBackground(Color.getHSBColor(errorColor[0], errorColor[1], errorColor[2]));
    }

    private void setErrorBorder() {
        float[] errorColor = Color.RGBtoHSB(175,26,26, null);
        fileNameField.setToolTipText("Feature already exists.");
        fileNameField.setBackground(Color.getHSBColor(errorColor[0], errorColor[1], errorColor[2]));
    }
}
