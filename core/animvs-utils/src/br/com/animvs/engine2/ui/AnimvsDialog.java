/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************//*


package br.com.animvs.engine2.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.ObjectMap;

import br.com.animvs.engine2.graphics.font.AnimvsDistanceFieldFontController;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

*/
/**
 * Displays a dialog, which is a modal window containing a content table with a button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any widgets can be added. When a button is clicked,
 * {@link #result(Object)} is called and the dialog is removed from the stage.
 *
 * @author Nathan Sweet
 *//*

public class AnimvsDialog extends Window {
    private AnimvsDistanceFieldFontController distanceFieldFontController;

    Table contentTable, buttonTable;
    private Skin skin;
    ObjectMap<Actor, Object> values = new ObjectMap();
    boolean cancelHide;
    Actor previousKeyboardFocus, previousScrollFocus;
    FocusListener focusListener;

    protected InputListener ignoreTouchDown = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            event.cancel();
            return false;
        }
    };

    public AnimvsDialog(String title, Skin skin, AnimvsDistanceFieldFontController distanceFieldFontController) {
        super(title, skin.get(WindowStyle.class));
        this.skin = skin;
        this.distanceFieldFontController = distanceFieldFontController;
        initialize();
    }

    public AnimvsDialog(String title, Skin skin, String windowStyleName, AnimvsDistanceFieldFontController distanceFieldFontController) {
        super(title, skin.get(windowStyleName, WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        this.distanceFieldFontController = distanceFieldFontController;
        initialize();
    }

    public AnimvsDialog(String title, WindowStyle windowStyle, AnimvsDistanceFieldFontController distanceFieldFontController) {
        super(title, windowStyle);
        this.distanceFieldFontController = distanceFieldFontController;
        initialize();
    }

    */
/**
     * Adds a label to the content table. The dialog must have been constructed with a skin to use this method.
     *//*

    public AnimvsDialog text(String text, AnimvsDistanceFieldFontController distanceFieldFontController, float fontSize) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        this.distanceFieldFontController = distanceFieldFontController;
        return text(text, skin.get(Label.LabelDistanceFieldStyle.class), distanceFieldFontController, fontSize);
    }

    */
/**
     * Adds a label to the content table.
     *//*

    public AnimvsDialog text(String text, Label.LabelStyle labelStyle, AnimvsDistanceFieldFontController distanceFieldFontController, float fontSize) {
        this.distanceFieldFontController = distanceFieldFontController;
        return text(new Label(distanceFieldFontController, text, labelStyle, fontSize), distanceFieldFontController);
    }

    */
/**
     * Adds the given Label to the content table
     *//*

    public AnimvsDialog text(LabelDistanceField label, AnimvsDistanceFieldFontController distanceFieldFontController) {
        contentTable.add(label);
        return this;
    }

    */
/**
     * Adds a text button to the button table. Null will be passed to {@link #result(Object)} if this button is clicked. The dialog
     * must have been constructed with a skin to use this method.
     *//*

    public AnimvsDialog button(String text) {
        return button(text, null);
    }

    */
/**
     * Adds a text button to the button table. The dialog must have been constructed with a skin to use this method.
     *
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null.
     *//*

    public AnimvsDialog button(String text, Object object) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        return button(text, object, skin.get(TextButtonStyle.class));
    }

    */
/**
     * Adds a text button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null.
     *//*

    public AnimvsDialog button(String text, Object object, TextButtonStyle buttonStyle) {
        return button(new TextButton(text, buttonStyle), object);
    }

    */
/**
     * Adds the given button to the button table.
     *//*

    public AnimvsDialog button(Button button) {
        return button(button, null);
    }

    private void initialize() {
        setModal(true);

        defaults().space(6);
        add(contentTable = new Table(skin)).expand().fill();
        row();
        add(buttonTable = new Table(skin));

        contentTable.defaults().space(6);
        buttonTable.defaults().space(6);

        buttonTable.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!values.containsKey(actor)) return;
                while (actor.getParent() != buttonTable)
                    actor = actor.getParent();
                result(values.get(actor));
                if (!cancelHide) hide();
                cancelHide = false;
            }
        });

        focusListener = new FocusListener() {
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }

            public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }

            private void focusChanged(FocusEvent event) {
                Stage stage = getStage();
                if (isModal() && stage != null && stage.getRoot().getChildren().size > 0
                        && stage.getRoot().getChildren().peek() == AnimvsDialog.this) { // Dialog is top most actor.
                    Actor newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(AnimvsDialog.this))
                        event.cancel();
                }
            }
        };
    }

    protected void setStage(Stage stage) {
        if (stage == null)
            addListener(focusListener);
        else
            removeListener(focusListener);
        super.setStage(stage);
    }

    public Table getContentTable() {
        return contentTable;
    }

    public Table getButtonTable() {
        return buttonTable;
    }



    */
/**
     * Adds the given button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null.
     *//*

    public AnimvsDialog button(Button button, Object object) {
        buttonTable.add(button);
        setObject(button, object);
        return this;
    }

    */
/**
     * {@link #pack() Packs} the dialog and adds it to the stage with custom action which can be null for instant show
     *//*

    public AnimvsDialog show(Stage stage, Action action) {
        clearActions();
        removeCaptureListener(ignoreTouchDown);

        previousKeyboardFocus = null;
        Actor actor = stage.getKeyboardFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

        previousScrollFocus = null;
        actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;

        pack();
        stage.addActor(this);
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        if (action != null) addAction(action);

        return this;
    }

    */
/**
     * {@link #pack() Packs} the dialog and adds it to the stage, centered with default fadeIn action
     *//*

    public AnimvsDialog show(Stage stage) {
        show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    */
/**
     * Hides the dialog with the given action and then removes it from the stage.
     *//*

    public void hide(Action action) {
        Stage stage = getStage();
        if (stage != null) {
            removeListener(focusListener);
            if (previousKeyboardFocus != null && previousKeyboardFocus.getStage() == null)
                previousKeyboardFocus = null;
            Actor actor = stage.getKeyboardFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setKeyboardFocus(previousKeyboardFocus);

            if (previousScrollFocus != null && previousScrollFocus.getStage() == null)
                previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setScrollFocus(previousScrollFocus);
        }
        if (action != null) {
            addCaptureListener(ignoreTouchDown);
            addAction(sequence(action, Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
        } else
            remove();
    }

    */
/**
     * Hides the dialog. Called automatically when a button is clicked. The default implementation fades out the dialog over 400
     * milliseconds and then removes it from the stage.
     *//*

    public void hide() {
        hide(sequence(fadeOut(0.4f, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
    }

    public void setObject(Actor actor, Object object) {
        values.put(actor, object);
    }

    */
/**
     * If this key is pressed, {@link #result(Object)} is called with the specified object.
     *
     * @see Keys
     *//*

    public AnimvsDialog key(final int keycode, final Object object) {
        addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    result(object);
                    if (!cancelHide) hide();
                    cancelHide = false;
                }
                return false;
            }
        });
        return this;
    }

    */
/**
     * Called when a button is clicked. The dialog will be hidden after this method returns unless {@link #cancel()} is called.
     *
     * @param object The object specified when the button was added.
     *//*

    protected void result(Object object) {
    }

    public void cancel() {
        cancelHide = true;
    }
}
*/
