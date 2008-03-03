/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.parsers;

import gralej.controller.StreamInfo;
import gralej.blocks.BlockPanel;
import gralej.om.IVisitable;

/**
 * 
 * @author Martin
 */
public interface IDataPackage {
    String getTitle();

    IVisitable getModel();

    char[] getCharacters();

    StreamInfo getStreamInfo();

    BlockPanel createView();
}
