package com.clean.helper.clean_helper.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;

import javax.management.Descriptor;
import javax.swing.Icon;

class FileTemplateFactory implements FileTemplateGroupDescriptorFactory {

    /**
     * @return
     */
    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        var descriptor = new FileTemplateGroupDescriptor("CleanHelper Group", AllIcons.Nodes.Folder);

        descriptor.addTemplate(new FileTemplateDescriptor("MainActivity.java", AllIcons.Nodes.Folder));
        return descriptor;
    }
}
