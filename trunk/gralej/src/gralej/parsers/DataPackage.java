// $Id$
//
// Copyright (C) 2011, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//
package gralej.parsers;

import gralej.blocks.BlockPanel;
import gralej.controller.StreamInfo;
import gralej.om.IRelation;
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;
import java.util.List;

/**
 *
 * @author Martin Lazarov
 */
final class DataPackage implements IDataPackage {

    private String _title;
    private IVisitable _model;
    private char[] _chars;
    private StreamInfo _streamInfo;
    private IneqsAndResidue _residue;

    DataPackage(String title, IVisitable model, char[] chars,
            StreamInfo streamInfo, IneqsAndResidue residue) {
        _title = title;
        _model = model;
        _chars = chars;
        _streamInfo = streamInfo;
        _residue = residue;
    }

    public String getTitle() {
        return _title;
    }

    public IVisitable getModel() {
        return _model;
    }

    public char[] getCharacters() {
        return _chars;
    }

    public StreamInfo getStreamInfo() {
        return _streamInfo;
    }

    DataPackage setStreamInfo(StreamInfo streamInfo) {
        _streamInfo = streamInfo;
        return this;
    }

    public List<IRelation> getInequations() {
        return _residue.ineqs();
    }

    public List<IRelation> getResidue() {
        return _residue.residue();
    }

    public BlockPanel createView() {
        return new BlockPanel(_model, _residue);
    }
}
