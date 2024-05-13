package com.clean.helper.clean_helper.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

class FileTemplateFactory implements FileTemplateGroupDescriptorFactory {

    /**
     */
    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        FileTemplateGroupDescriptor descriptor = new FileTemplateGroupDescriptor("CleanHelper Group", AllIcons.Nodes.Folder);

        descriptor.addTemplate(new FileTemplateDescriptor("MainActivity.java", AllIcons.Nodes.Folder));
        return descriptor;
    }
}
