package com.yausername.youtubedl_android.util.files

import java.io.File

object FilesUtil {
    /**
     * Asserts that a file exists and creates it if it doesn't
     * @param file the file to assert or create in case it doesn't exist
     */
    fun assertAndCreate(file: File) {
        if(!file.exists()) {
            file.mkdir()
        }
    }
}