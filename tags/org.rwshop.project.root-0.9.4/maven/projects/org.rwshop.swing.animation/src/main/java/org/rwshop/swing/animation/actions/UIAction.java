/*
 * Copyright 2014 the RoboWorkshop Project
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

package org.rwshop.swing.animation.actions;

import java.util.List;
import org.mechio.api.animation.utils.ChannelNode;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import org.mechio.api.animation.editor.AnimationEditor;
import org.mechio.api.animation.editor.actions.EditorAction.AddChild;
import org.mechio.api.animation.factory.ChannelFactory;
import org.mechio.api.animation.utils.AnimationUtils;
import org.mechio.api.animation.utils.ChannelsParameter;
import org.mechio.api.animation.utils.ChannelsParameterSource;
import org.rwshop.swing.animation.timeline.AnimationTimelinePanel;
import org.rwshop.swing.common.scaling.CoordinateScalar;
import org.rwshop.swing.animation.menus.UIMenuItem;

import static org.jflux.api.common.rk.localization.Localizer.$;

/**
 *
 * @author Matthew Stevenson <www.roboworkshop.org>
 */
public abstract class UIAction{
    
    public static class Zoom implements ActionListener{
        private CoordinateScalar myScalar;
        private double myAmount;
        private AnimationTimelinePanel myPanel;
        /**
         * Creates a Zoom action for a CoordinateScalar.
         * @param label
         * @param s the CoordinateScalar to zoom
         * @param amount the zoom amount
         * @param c
         */
        public Zoom(CoordinateScalar s, AnimationTimelinePanel c, double amount){
            myScalar = s;
            myAmount = amount;
            myPanel = c;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            myScalar.setScaleX(myScalar.getScaleX()*myAmount);
            int minAnim = myPanel.getMinWidth();
            int minPnl = myPanel.getMinimumSize().width;
            JScrollPane scrollPane = (JScrollPane)myPanel.getParent().getParent();
            int minScrl = scrollPane.getWidth() + 1;
            int size = Math.max(minAnim, minPnl);
            size = Math.max(size, minScrl);
            myPanel.setPreferredSize(new Dimension(size, myPanel.getPreferredSize().height));
            myPanel.revalidate();
            scrollPane.repaint();
        }
    }
    
    public static JMenu AddChannelMenu(AnimationEditor controller){
        JMenu menu = new JMenu($("add.channel"));
        ChannelsParameterSource paramSource = AnimationUtils.getChannelsParameterSource();
        if(paramSource == null){
            return menu;
        }
        ChannelNode root = paramSource.getChannelTree();
        if(root != null){
            menu.add(channelNodeMenu(controller, root));
        }
        for(ChannelsParameter channel: paramSource.getChannelParameters()){
            String label = channel.getChannelName();
            JMenuItem item = null;
            if(controller.containsLogicalId(channel.getChannelID())){
                item = new JMenuItem(label);
                item.setEnabled(false);
            }else{
                ChannelFactory fact = new ChannelFactory(channel.getChannelID(), channel.getChannelName());
                item = new UIMenuItem(label, new AddChild(controller, fact, true));
            }
            menu.add(item);
        }
        return menu;
    }
    
    public static JMenu channelNodeMenu(AnimationEditor editor, ChannelNode node) {
        JMenu menu = new JMenu(node.getName());
        ActionListener all = buildAddRecChildChannels(editor, node);
        if(all == null){
            return menu;
        }
        menu.add(new UIMenuItem("Add All: " + node.getName(), all));
        List<ChannelNode> nodes = node.getChildGroups();
        if(nodes == null){
            nodes = Collections.EMPTY_LIST;
        }
        for(ChannelNode n : nodes){
            JMenu childMenu = channelNodeMenu(editor, n);
            menu.add(childMenu);
        }
        List<ChannelNode.ChannelDefinition> chans = node.getChannels();
        if(chans == null){
            chans = Collections.EMPTY_LIST;
        }
        for(ChannelNode.ChannelDefinition def : chans){
            String label = def.getName();
            JMenuItem item = null;
            if(editor.containsLogicalId(def.getId())){
                item = new JMenuItem(label);
                item.setEnabled(false);
            }else{
                ChannelFactory fact = new ChannelFactory(def.getId(), def.getName());
                item = new UIMenuItem(label, new AddChild(editor, fact, true));
            }
            menu.add(item);
        }
        return menu;
    }
    
    public static ActionListener buildAddRecChildChannels(AnimationEditor editor, ChannelNode node){
        List<ChannelNode> list = node.getChildGroups();
        if(list == null){
            list = Collections.EMPTY_LIST;
        }
        int size = list.size();
        List<ActionListener> actions = new ArrayList<ActionListener>(size + 1);
        
        for(ChannelNode n : list){
            ActionListener l = buildAddRecChildChannels(editor, n);
            if(l != null){
                actions.add(l);
            }
        }
        ActionListener l = buildAddChildChannels(editor, node);
        if(l != null){
            actions.add(l);
        }
        if(actions.isEmpty()){
            return null;
        }
        return new CompoundAction(actions);
    }
    
    public static ActionListener buildAddChildChannels(AnimationEditor editor, ChannelNode node){
        List<ChannelNode.ChannelDefinition> l = node.getChannels();
        if(l == null || l.isEmpty()){
            return null;
        }
        List<ActionListener> actions = new ArrayList<ActionListener>(l.size());
        for(ChannelNode.ChannelDefinition def : l){
            if(!editor.containsLogicalId(def.getId())){
                actions.add(new AddChild(editor, new ChannelFactory(def.getId(), def.getName()), true));
            }
        }
        if(actions.isEmpty()){
            return null;
        }
        return new CompoundAction(actions);
    }
    
    public static class CompoundAction implements ActionListener {
        private List<ActionListener> myActions;
        
        public CompoundAction(List<ActionListener> listeners){
            if(listeners == null){
                throw new NullPointerException();
            }
            myActions = listeners;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            for(ActionListener l : myActions){
                l.actionPerformed(e);
            }
        }
        
    }
}
