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

package gralej.om;

import gralej.blocks.BlockPanel;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Martin
 */
public final class Entities {
    public static String toTraleDesc(final IEntity ent) {
        final StringBuilder sb = new StringBuilder();
        ent.accept(new AbstractVisitor() {
            Set<Integer> _processedTags = new HashSet<Integer>();

            public void visit(IList ls) {
                sb.append('[');
                boolean doComma = false;
                for (IEntity ent : ls.elements()) {
                    if (doComma) sb.append(','); else doComma = true;
                    ent.accept(this);
                }
                if (ls.tail() != null) {
                    sb.append('|');
                    ls.tail().accept(this);
                }
                sb.append(']');
            }

            public void visit(ITag tag) {
                boolean doTarget = false;
                if (tag.target() != null && _processedTags.add(tag.number())) {
                    sb.append('(');
                    doTarget = true;
                }
                sb.append("T").append(tag.number());
                if (doTarget) {
                    sb.append(',');
                    tag.target().accept(this);
                    sb.append(')');
                }
            }

            public void visit(IAny any) {
                sb.append("a_(").append(any.value()).append(')');
            }

            public void visit(ITypedFeatureStructure tfs) {
                int numFVs = tfs.featureValuePairs().size();
                boolean closeBracket = false;
                if (numFVs > 1 || numFVs == 1 && tfs.type() != null) {
                    sb.append('(');
                    closeBracket = true;
                }

                if (tfs.type() != null) {
                    String s = tfs.type().typeName();
                    if (numFVs == 0 && s.startsWith("mgsat"))
                        s = "_"; // anonymous variable
                    sb.append(s);
                    if (numFVs > 0)
                        sb.append(',');
                }
                boolean doComma = false;
                for (IFeatureValuePair fv : tfs.featureValuePairs()) {
                    if (doComma) sb.append(','); else doComma = true;
                    fv.accept(this);
                }
                if (closeBracket)
                    sb.append(')');
            }

            public void visit(IFeatureValuePair fv) {
                sb.append(fv.feature()).append(':');
                fv.value().accept(this);
            }

            public void visit(ITree tree) {
                throw new UnsupportedOperationException();
            }
        });
        return sb.toString();
    }

    public static JFrame show(IEntity ent) {
        return BlockPanel.show(ent);
    }
}
