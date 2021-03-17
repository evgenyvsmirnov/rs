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
package rs.vm.interpreter.shape;

import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.CircleContext;
import rs.vm.RSParser.ShapeCoordContext;
import rs.vm.RSParser.ShapeSizeContext;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 *    circles (
 *        ((100, 100) 60)
 *        ((100, 200) radius 70)
 *        ((100, 300), radius 80)
 *    )
 * </pre>
 */
public class CircleInterpreter extends InterpreterBase implements ICodeBlockInterpreter<CircleContext, ShapeResult> {
    private static final CircleInterpreter INSTANCE = new CircleInterpreter();

    public static CircleInterpreter instance() {
        return INSTANCE;
    }

    private CircleInterpreter() {
    }

    @Override
    public boolean matches(CircleContext ctx) {
        return true;
    }

    @Override
    public ShapeResult interpret(CircleContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        List<ShapeWithContext> shapes = new ArrayList<>();

        final List<ShapeCoordContext> centerDefs = ctx.shapeCoord();
        final List<ShapeSizeContext> radiusDefs = ctx.shapeSize();

        for (int i = 0; i < centerDefs.size(); i++) {
            Point center = evaluatePointFromExpressionOrRandom(centerDefs.get(i), visitor);
            int radius = evaluateShapeSizeFromExpressionOrRandom(radiusDefs.get(i), visitor);

            frames.g2d().setColor(frames.brushColor());
            frames.g2d().setStroke(
                    new BasicStroke(frames.brushThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Ellipse2D.Double shape = new Ellipse2D.Double(
                    center.getX() - radius, center.getY() - radius,
                    radius * 2.0, radius * 2.0);
            if (!frames.modeUnion() && !frames.modeRepeat()) {
                if (frames.brushFill())
                    frames.g2d().fill(shape);
                else
                    frames.g2d().draw(shape);
            }

            shapes.add(new ShapeWithContext(shape, frames.brushColor(), frames.brushThickness(),
                    frames.brushFill(), null));
        }

        return ShapeResult.create(shapes);
    }
}
