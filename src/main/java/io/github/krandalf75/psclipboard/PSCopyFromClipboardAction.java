package io.github.krandalf75.psclipboard;

import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CookieAction;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.cookies.EditorCookie;

@ActionID(
        category = "Edit",
        id = "io.github.krandalf75.psclipboard.PSCopyFromClipboardAction"
)
@ActionRegistration(
        displayName = "#CTL_PSCopyFromClipboardAction",
        lazy = false
)

@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "CA-C"),
    @ActionReference(path = "Menu/Edit", position = 1325),
    @ActionReference(path = "Editors/Popup", position = 1500) 
})

@Messages("CTL_PSCopyFromClipboardAction=PowerShell Copy")
public final class PSCopyFromClipboardAction extends CookieAction {

    public PSCopyFromClipboardAction() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }

    @Override
    protected void performAction(Node[] activatedNodes) {

        System.out.println("PERFORM");
        if (activatedNodes.length == 1) {
            // Get the EditorCookie from the activated node
            EditorCookie editorCookie = activatedNodes[0].getLookup().lookup(EditorCookie.class);
            if (editorCookie != null) {

                // Get the text from the editor
                String selectedText = getSelectedText(editorCookie);
                if (selectedText != null && !selectedText.isEmpty()) {
                    // Copy the selected text to the clipboard
                    PSClipboard.writeClipboard(selectedText);
                }
            }
        }
    }

    /**
     * Gets the selected text from the editor.
     *
     * @param editorCookie The EditorCookie to get the text from.
     * @return The selected text, or null if no text is selected.
     */
    private String getSelectedText(EditorCookie editorCookie) {
        // Get the document associated with the editor
        JEditorPane panel = editorCookie.getOpenedPanes()[0];
        return panel.getSelectedText();
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{DataObject.class}; // Se activa para cualquier archivo
    }

    @Override
    public String getName() {
        return Bundle.CTL_PSCopyFromClipboardAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

}
