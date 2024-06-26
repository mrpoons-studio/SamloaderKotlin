package tk.zwander.commonCompose.util

import jnafilechooser.api.WindowsFileChooser
import korlibs.platform.Platform
import tk.zwander.common.data.PlatformFile
import tk.zwander.common.util.BifrostSettings
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JFileChooser

object FilePicker {
    private var frame: Frame? = null

    fun init(frame: Frame) {
        FilePicker.frame = frame
    }

    fun createFile(name: String): PlatformFile? {
        if (BifrostSettings.Keys.useNativeFileDialog()) {
            return if (Platform.isWindows) {
                val dialog = WindowsFileChooser()
                dialog.defaultFilename = name
                dialog.showSaveDialog(frame!!)

                dialog.selectedFile?.let { PlatformFile(it) }
            } else {
                val dialog = FileDialog(frame).apply {
                    mode = FileDialog.SAVE
                    file = name
                    isVisible = true
                }

                dialog.files.firstOrNull()?.let { PlatformFile(it) }
            }
        } else {
            val chooser = JFileChooser().apply {
                dialogType = JFileChooser.SAVE_DIALOG
                selectedFile = File(name)
            }
            return if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                chooser.selectedFile?.let { PlatformFile(it) }
            } else {
                null
            }
        }
    }

    fun pickFile(): PlatformFile? {
        if (BifrostSettings.Keys.useNativeFileDialog()) {
            return if (Platform.isWindows) {
                val dialog = WindowsFileChooser()
                dialog.showOpenDialog(frame!!)

                dialog.selectedFile?.let { PlatformFile(it) }
            } else {
                val dialog = FileDialog(frame).apply {
                    mode = FileDialog.LOAD
                    isVisible = true
                }

                dialog.files.firstOrNull()?.let { PlatformFile(it) }
            }
        } else {
            val chooser = JFileChooser().apply {
                dialogType = JFileChooser.OPEN_DIALOG
            }

            return if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                PlatformFile(chooser.selectedFile)
            } else {
                null
            }
        }
    }
}
