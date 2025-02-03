package io.github.krandalf75.psclipboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The PSClipboard class provides methods to interact with the system clipboard.
 * It allows reading from and writing to the clipboard using PowerShell commands.
 * The class contains a constructor and static methods for clipboard operations.
 */
public class PSClipboard {

    /**
     * Reads the current text content from the Windows clipboard using
     * PowerShell.
     *
     * @return The clipboard content as a string, or an error message if
     * something goes wrong.
     */
    public static String readClipboard() {
        try {
            // PowerShell command to read clipboard content
            String[] cmdarray = {
                "powershell.exe",
                "Get-Clipboard"
            };

            // Execute the command
            Process process = Runtime.getRuntime().exec(cmdarray);

            // Read the output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder clipboardContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                clipboardContent.append(line).append("\n");
            }

            // Wait for the process to complete
            process.waitFor();

            return clipboardContent.toString().trim();
        } catch (IOException | InterruptedException e) {
            return "Error reading clipboard: " + e.getMessage();
        }
    }

    /**
     * Writes the specified text content to the Windows clipboard using
     * PowerShell.
     *
     * @param text The text to write to the clipboard.
     */
    public static void writeClipboard(String text) {
        try {
            // PowerShell script as a single-line string
            String script = "$text = [Console]::In.ReadToEnd(); Set-Clipboard -Value $text";

            // Encode the script in UTF-16LE (required for PowerShell -EncodedCommand)
            String encodedScript = Base64.getEncoder()
                    .encodeToString(script.getBytes(StandardCharsets.UTF_16LE));

            // Build the command to execute PowerShell with the encoded script
            String[] cmdarray = {"powershell.exe", "-EncodedCommand", encodedScript};

            // Execute the command
            Process process = Runtime.getRuntime().exec(cmdarray);

            // Write the text to PowerShell's standard input
            try (var os = process.getOutputStream()) {
                os.write(text.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Wait for the process to complete
            process.waitFor();

            System.out.println("Text copied to clipboard: " + text + ": " + process.exitValue());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error writing to clipboard: " + e.getMessage());
        }
    }
}
