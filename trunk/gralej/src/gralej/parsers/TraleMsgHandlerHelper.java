package gralej.parsers;

import gralej.controller.StreamInfo;
import gralej.blocks.BlockPanel;
import gralej.om.IVisitable;

class TraleMsgHandlerHelper {

    IParseResultReceiver _resultReceiver;
    StringBuilder _charBuffer;
    StreamInfo _streamInfo;

    void setResultReceiver(IParseResultReceiver resultReceiver) {
        _resultReceiver = resultReceiver;
    }

    void setCharBuffer(StringBuilder charBuffer) {
        _charBuffer = charBuffer;
    }

    void setStreamInfo(StreamInfo streamInfo) {
        _streamInfo = streamInfo;
    }

    void adviceResult(final String title, final IVisitable vob) {
        if (_resultReceiver == null) {
            System.err.println("++ parsed ok, but no result receiver");
            return;
        }
        char[] chars = _charBuffer.toString().toCharArray();
        _charBuffer.delete(0, _charBuffer.length());
        _resultReceiver.newDataPackage(new DataPackage(title, vob, chars,
                _streamInfo));
    }

    static class DataPackage implements IDataPackage {
        String _title;
        IVisitable _model;
        char[] _chars;
        StreamInfo _streamInfo;

        DataPackage(String title, IVisitable model, char[] chars,
                StreamInfo streamInfo) {
            _title = title;
            _model = model;
            _chars = chars;
            _streamInfo = streamInfo;
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

        public BlockPanel createView() {
            return new BlockPanel(_model);
        }
    }
}
