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
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;
import gralej.util.Log;

class TraleMsgHandlerHelper {

    IParseResultReceiver _resultReceiver;
    StringBuilder _charBuffer = new StringBuilder();
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
}
