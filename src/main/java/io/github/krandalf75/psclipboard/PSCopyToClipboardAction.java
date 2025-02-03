package io.github.krandalf75.psclipboard;

import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.openide.ErrorManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

@ActionID(
        category = "Edit",
        id = "io.github.krandalf75.psclipboard.PSCopyToClipboardAction"
)
@ActionRegistration(
        displayName = "#CTL_PSCopyToClipboardAction",
        lazy = false
)

@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "CA-V"),
    @ActionReference(path = "Menu/Edit", position = 1326),
    @ActionReference(path = "Editors/Popup", position = 1501) 
})

@NbBundle.Messages("CTL_PSCopyToClipboardAction=PowerShell Paste")
public final class PSCopyToClipboardAction extends CookieAction {

    public PSCopyToClipboardAction() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            // Get the EditorCookie from the activated node
            EditorCookie editorCookie = activatedNodes[0].getLookup().lookup(EditorCookie.class);
             
            if (editorCookie != null) {
                String text = PSClipboard.readClipboard();
                JEditorPane panel = editorCookie.getOpenedPanes()[0];
                int caretPosition = panel.getCaretPosition(); // Posición actual del cursor
                try {
                    // Obtener el documento del editor
                    StyledDocument doc =  editorCookie.getDocument();

                    // Insertar el texto en la posición actual del cursor
                    doc.insertString(caretPosition, text, null);

                    // Actualizar la posición del cursor después de la inserción
                    panel.setCaretPosition(caretPosition + text.length());
                } catch (BadLocationException e) {
                    ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, e);
                }
            }
        }
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
        return Bundle.CTL_PSCopyToClipboardAction();
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
