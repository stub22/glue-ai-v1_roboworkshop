/*
 * Copyright 2011 Hanson Robokind LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rwshop.swing.animation.timeline.listener;

import org.rwshop.swing.animation.actions.UIAction;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu.Separator;
import org.robokind.api.animation.editor.actions.AnimationPlayerAction.Play;
import org.robokind.api.animation.editor.actions.HistoryAction;
import org.robokind.api.common.utils.RKSource.SourceImpl;
import org.robokind.api.animation.editor.history.HistoryStack;
import org.robokind.api.common.utils.RKSource;
import javax.swing.JPopupMenu;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.robokind.api.animation.editor.EditState;
import org.robokind.api.animation.editor.actions.ChannelActions;
import org.robokind.api.animation.editor.actions.ControlPointActions;
import org.robokind.api.animation.editor.actions.MotionPathActions;
import org.robokind.api.animation.editor.ChannelEditor;
import org.robokind.api.animation.editor.ControlPointEditor;
import org.robokind.api.animation.editor.MotionPathEditor;
import org.rwshop.swing.animation.timeline.TimelineAnimation;
import org.rwshop.swing.animation.timeline.TimelineChannel;
import org.rwshop.swing.animation.timeline.TimelineMotionPath;
import org.rwshop.swing.animation.actions.UIAction.Zoom;
import org.robokind.api.animation.editor.AnimationEditor;
import org.rwshop.swing.animation.actions.FileAction.Save;
import org.rwshop.swing.animation.menus.MenuProvider;
import org.rwshop.swing.animation.menus.UIMenuItem;
import org.rwshop.swing.animation.timeline.AnimationTimelinePanel;
import org.rwshop.swing.common.utils.SettingsRepository;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import static org.robokind.api.common.localization.Localizer.*;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class TimelineContextMenuManager{
    private TimelineAnimation myTimeline;
    private CoordinateScalar myScalar;
    private AnimationTimelinePanel myPanel;
    private List<JComponent> myDefaultItems;
    private RKSource<AnimationEditor> myControllerSource;
    private RKSource<HistoryStack> myHistorySource;
    private List<MenuProvider> myProviders;

    /**
     *
     * @param panel
     * @param scalar
     */
    public TimelineContextMenuManager(AnimationTimelinePanel panel, CoordinateScalar scalar){
        myPanel = panel;
        myScalar = scalar;
        
        myHistorySource = new SourceImpl<HistoryStack>();
        myControllerSource = new SourceImpl<AnimationEditor>();
        myDefaultItems = new ArrayList();
        myDefaultItems.add(new UIMenuItem($("play"), (ActionListener)new Play(myControllerSource)));
        myDefaultItems.add(new Separator());
        myDefaultItems.add(new UIMenuItem($("zoom.in"), new Zoom(scalar, panel, 1.2)));
        myDefaultItems.add(new UIMenuItem($("zoom.out"), new Zoom(scalar, panel, 0.8)));
        myDefaultItems.add(new Separator());
        myDefaultItems.add(new UIMenuItem($("undo"), HistoryAction.Undo(myHistorySource)));
        myDefaultItems.add(new UIMenuItem($("redo"), HistoryAction.Redo(myHistorySource)));
        myDefaultItems.add(new Separator());
        myDefaultItems.add(new UIMenuItem($("save"), new Save(myControllerSource, false)));
        myDefaultItems.add(new UIMenuItem($("save.as"), new Save(myControllerSource, true)));
        
        myProviders = new ArrayList();
    }
    
    public void addMenuProvider(MenuProvider provider){
        if(!myProviders.contains(provider)){
            myProviders.add(provider);
        }
    }
    
    public void removeMenuProvider(MenuProvider provider){
        if(myProviders.contains(provider)){
            myProviders.remove(provider);
        }
    }

    /**
     *
     * @param timeline
     */
    public void setTimeline(TimelineAnimation timeline){
        myTimeline = timeline;
        if(myTimeline == null){
            return;
        }
        AnimationEditor controller = myTimeline.getController();
        myControllerSource.set(controller);
        if(controller == null){
            return;
        }
        myHistorySource.set(controller.getSharedHistory());
    }

    /**
     *
     * @param e
     * @return
     */
    public boolean tryShowPopup(MouseEvent e){
        if(myTimeline == null){
            return false;
        }
        if(!shouldShowPopup(e)){
            return false;
        }
        List<JComponent> menuItems = getMenuItems(e);
        menuItems.add(UIAction.AddChannelMenu(myTimeline.getController()));
        addMenuItems(menuItems, getProvidedMenus(e));
        addMenuItems(menuItems, myDefaultItems);
        showMenu(menuItems, e.getX(), e.getY());
        return true;
    }
    
    private List<JComponent> getProvidedMenus(MouseEvent e){
        List<JComponent> menus = new ArrayList<JComponent>(myProviders.size());
        for(MenuProvider provider : myProviders){
            JMenu menu = provider.getContextSubMenu(e);
            if(menu == null){
                continue;
            }
            menus.add(menu);
        }
        return menus;
    }
    
    private void showMenu(List<JComponent> items, int x, int y){
        JPopupMenu menu = new JPopupMenu();
        for(JComponent item : items){
            menu.add(item);
        }
        menu.show(myPanel, x, y);
    }

    private boolean shouldShowPopup(MouseEvent e){
        return e.getButton() == MouseEvent.BUTTON3 && e.isPopupTrigger();
    }

    private List<JComponent> getMenuItems(MouseEvent e){
        TimelineChannel channel = null;
        TimelineMotionPath path = null;
        channel = myTimeline.getSelected();
        if(channel != null){
            path = channel.getSelected();
        }
        List<JComponent> menu = getSelectedMenuItems(channel, path, e);
        List<JComponent> hoverMenu = getHoverChannelMenuItems(e);
        addMenuItems(menu, hoverMenu);
        return menu;
    }

    private List<JComponent> getSelectedMenuItems(TimelineChannel channel,
            TimelineMotionPath path, MouseEvent e){
        List<JComponent> items = new ArrayList();
        if(path != null){
            Point2D p = new Point2D.Double(myScalar.unscaleX(e.getX()), myScalar.unscaleY(e.getY()));
            MotionPathEditor controller = path.getController();
            items.add(new UIMenuItem($("add.control.point"), MotionPathActions.Add(controller, p)));
            items.addAll(getHoverControlPointMenuItems(path, e));
        }
        if(channel != null){
            ChannelEditor controller = channel.getController();
            items.add(new UIMenuItem($("add.motion.path"), ChannelActions.Add(controller)));
        }
        return items;
    }

    private void addMenuItems(List<JComponent> dest, List<JComponent> src){
        if(dest.size() > 0 && src.size() > 0){
            dest.add(new Separator());
        }
        dest.addAll(src);
    }

    private JMenu buildMenu(String label, List<JComponent> items){
        JMenu menu = new JMenu(label);
        for(JComponent item : items){
            menu.add(item);
        }
        return menu;
    }

    private List<JComponent> getHoverChannelMenuItems(MouseEvent e){
        List<JComponent> menuItems = new ArrayList();
        for (int j = 0; j < myTimeline.getChannels().size(); j++) {
            TimelineChannel channel = myTimeline.getChannels().get(j);
            ChannelEditor controller = channel.getController();
            boolean visible = EditState.hasFlag(controller.getRestrictiveFlags(), EditState.VISIBLE);
            boolean selected = EditState.hasFlag(controller.getInheritedFlags(), EditState.SELECTED);
            if((!visible && !selected)){
                continue;
            }
            if(channel.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
                menuItems.add(buildMenu(controller.getName(), getChannelMenuItems(controller)));
                menuItems.addAll(getHoverMotionPathMenuItems(channel, e));
            }
        }
        return menuItems;
    }

    private List<JComponent> getChannelMenuItems(ChannelEditor controller){
        List<JComponent> items = UIMenuItem.buildMenuItems(new String[]{$("add.motion.path")}, ChannelActions.Add(controller));
        items.addAll(UIMenuItem.buildStateMenuItems(controller));
        return items;
    }

    private List<JComponent> getHoverMotionPathMenuItems(TimelineChannel channel, MouseEvent e){
        List<JComponent> menuItems = new ArrayList();
        for (int j = 0; j < channel.getMotionPaths().size(); j++) {
            TimelineMotionPath path = channel.getMotionPaths().get(j);
            MotionPathEditor controller = path.getController();
            boolean visible = EditState.hasFlag(controller.getRestrictiveFlags(), EditState.VISIBLE);
            boolean selected = EditState.hasFlag(controller.getInheritedFlags(), EditState.SELECTED);
            if((!visible && !selected)){
                continue;
            }
            if(path.contains(e.getX(), e.getY(), SettingsRepository.theHoverDistance)) {
                menuItems.add(buildMenu(controller.getName(), getMotionPathMenuItems(controller, e)));
            }
        }
        return menuItems;
    }

    private List<JComponent> getMotionPathMenuItems(MotionPathEditor controller, MouseEvent e){
        Point2D p = new Point2D.Double(myScalar.unscaleX(e.getX()), myScalar.unscaleY(e.getY()));
        List<JComponent> items = UIMenuItem.buildMenuItems(
            new String[]{$("add.control.point"), $("remove")},
            MotionPathActions.Add(controller, p),
            MotionPathActions.Remove(controller)
        );
        items.addAll(UIMenuItem.buildStateMenuItems(controller));
        return items;
    }

    private List<JComponent> getHoverControlPointMenuItems(TimelineMotionPath path, MouseEvent e){
        List<JComponent> menuItems = new ArrayList();
        MotionPathEditor editor = path.getController();
        int i = TimelineMotionPath.findControlPoint(editor, e.getX(), e.getY(), myScalar, SettingsRepository.theHoverDistance);
        if(i >= 0){
            ControlPointEditor cpc = editor.getChild(i);
            menuItems.addAll(getControlPointItems(cpc));
        }
        return menuItems;
    }

    private List<JComponent> getControlPointItems(ControlPointEditor controller){
        if(!controller.isGrouped()){
            return UIMenuItem.buildMenuItems(
                new String[]{$("remove")},
                ControlPointActions.Remove(controller)
            );
        }else{
            return UIMenuItem.buildMenuItems(
                new String[]{$("remove"), "Unlink"},
                ControlPointActions.Remove(controller),
                controller.getUnlinkAction()
            );
        }
    }
}
