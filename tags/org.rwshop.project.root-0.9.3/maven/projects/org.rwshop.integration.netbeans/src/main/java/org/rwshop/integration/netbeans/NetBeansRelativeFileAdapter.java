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

package org.rwshop.integration.netbeans;

import java.io.File;
import javax.swing.ImageIcon;
import org.robokind.api.common.config.FileSystemAdapter;

/**
 *
 * @author Matthew Stevenson <www.robokind.org>
 */
public class NetBeansRelativeFileAdapter implements FileSystemAdapter{
    private String myRoot;

    public NetBeansRelativeFileAdapter(String rootDir){
        if(rootDir == null){
            throw new NullPointerException();
        }
        myRoot = rootDir;
    }

    @Override
    public File openFile(String path) {
        if(path == null){
            throw new NullPointerException();
        }
        path = myRoot + path;
        File file = new File(path);
        return file;
    }

    @Override
    public ImageIcon openImageIcon(String path) {
        return new ImageIcon(openFile(path).getPath());
    }

}
