/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.parsers;

import gralej.controller.StreamInfo;
import gralej.blocks.BlockPanel;
import gralej.om.IRelation;
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;
import gralej.util.Log;
import java.util.List;

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

    void adviceResult(String title, IVisitable vob, IneqsAndResidue residue) {
        if (_resultReceiver == null) {
            Log.warning("parsed ok, but no result receiver");
            return;
        }
        char[] chars = _charBuffer.toString().toCharArray();
        _charBuffer.delete(0, _charBuffer.length());
        _resultReceiver.newDataPackage(new DataPackage(title, vob, chars,
                _streamInfo, residue));
    }

    static class DataPackage implements IDataPackage {
        String _title;
        IVisitable _model;
        char[] _chars;
        StreamInfo _streamInfo;
        IneqsAndResidue _residue;

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
}
