// COPIED FROM xtext-xtend/org.eclipse.xtend.ide/src/org/eclipse/xtend/ide/edit/InsertStringHandler.java
package org.jnario.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;

/**
 * Handler to allow easy typing of guillemet characters on windows
 * and linux, too.
 */
public abstract class InsertStringHandler extends AbstractHandler {

	public static final char LEFT_GUILLEMET = '\u00ab';
	public static final char RIGHT_GUILLEMET = '\u00bb';

	public static class LeftGuillemet extends InsertStringHandler {
		public LeftGuillemet() {
			super(LEFT_GUILLEMET);
		}
	}

	public static class RightGuillemet extends InsertStringHandler {
		public RightGuillemet() {
			super(RIGHT_GUILLEMET);
		}
	}

	private char replaceChar;

	protected InsertStringHandler(char replaceChar) {
		this.replaceChar = replaceChar;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
		if (editor != null) {
			// Hack, would be nicer with document edits, but this way we don't loose auto edit
			StyledText textWidget = editor.getInternalSourceViewer().getTextWidget();
			Event e = new Event();
			e.character = replaceChar;
			e.type = SWT.KeyDown;
			e.doit = true;
			textWidget.notifyListeners(SWT.KeyDown, e);
		}
		return null;
	}
}