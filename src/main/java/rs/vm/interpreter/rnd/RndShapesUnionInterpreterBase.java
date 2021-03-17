// Copyright © 2021 RS Contributors.
//
// Licensed under the Eclipse Public License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. You may
// obtain a copy of the License at https://www.eclipse.org/legal/epl-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//
// See the License for the specific language governing permissions and
// limitations under the License.
package rs.vm.interpreter.rnd;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import rs.vm.CodeBlockResults.CordsResult;
import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoUnionContext;
import rs.vm.RsSemanticsException;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.interpreter.InterpreterBase;

abstract class RndShapesUnionInterpreterBase extends InterpreterBase {
    protected final CordsResult interpretInternal(DoUnionContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        ShapeResult shapesForPoint = (ShapeResult) visitor.visit(ctx);

        Point randomPoint = getRandomPointWithinShapes(shapesForPoint, ctx, frames);
        return CordsResult.create(randomPoint.x, randomPoint.y);
    }

    private Point getRandomPointWithinShapes(ShapeResult result, DoUnionContext doUnion, DrawFrames frames) {
        Rectangle2D union = getBoundaryRectangle(result, doUnion);

        int width = (int) union.getBounds2D().getWidth();
        int height = (int) union.getBounds2D().getHeight();

        if (width == 0 || height == 0)
            throw new RsSemanticsException("Shape sizes for random coordinates can't be zero.", doUnion.getStart());

        while (true) {
            int x = (int) (union.getBounds2D().getX() + (width > 0 ? frames.random(width) : 0));
            int y = (int) (union.getBounds2D().getY() + (height > 0 ? frames.random(height) : 0));

            for (ShapeWithContext shapeWithContext : result.shapes()) {
                if (shapeWithContext.shape().contains(x, y))
                    return new Point(x, y);
            }
        }
    }
}
