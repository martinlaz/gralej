package gralej.parsers;

import gralej.controller.StreamInfo;
import gralej.gui.blocks.BlockCreator;
import gralej.gui.blocks.BlockPanel;
import gralej.om.IVisitable;

class TraleMsgHandlerHelper {

    IParseResultReceiver _resultReceiver;

    void setResultReceiver(IParseResultReceiver resultReceiver) {
        _resultReceiver = resultReceiver;
    }

    void adviceResult(final String title, final IVisitable vob) {
        if (_resultReceiver == null) {
            System.err.println("++ parsed ok, but no result receiver");
            return;
        }

        _resultReceiver.newDataPackage(new DataPackage(title, vob));
    }

    class DataPackage implements IDataPackage {

        String _title;
        IVisitable _model;

        DataPackage(String title, IVisitable model) {
            _title = title;
            _model = model;
        }

        public String getTitle() {
            return _title;
        }

        public IVisitable getModel() {
            return _model;
        }

        public char[] getCharacters() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public StreamInfo getStreamInfo() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public BlockPanel createView() {
            return new BlockPanel(new BlockCreator().createBlock(_model));
        }
    }
}
