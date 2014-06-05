package com.promptnet.mobiledev.filepicker;

import java.io.FileFilter;

public interface FilePickerActivity {

    String getFileSelectMessage();

    FileFilter getFileFilter();

}