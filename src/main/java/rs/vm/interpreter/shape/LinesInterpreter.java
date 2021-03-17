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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.LinesContext;
import rs.vm.RSParser.ShapeCoordContext;
import rs.vm.RsSemanticsException;
import rs.vm.ShapeDataContainers.ShapeRotationContext;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.interpreter.InterpreterBase;

import static java.lang.Math.toRadians;

/**
 * <pre>
 *      lines (
 *          (900, 330)
 *          (960, 300)
 *          (980, 400)
 *          (830, 530)
 *      )
 *
 *      lines (
 *          (900, 330)
 *          (960, 300)
 *          (980, 400)
 *          (830, 530)
 *          rotate 55
 *      )
 * </pre>
 */
public class LinesInterpreter extends InterpreterBase implements ICodeBlockInterpreter<LinesContext, ShapeResult> {
    private static final LinesInterpreter INSTANCE = new LinesInterpreter();

    public static LinesInterpreter instance() {
        return INSTANCE;
    }

    private LinesInterpreter() {
    }

    @Override
    public boolean matches(LinesContext ctx) {
        return true;
    }

    @Override
    public ShapeResult interpret(LinesContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        List<ShapeCoordContext> shapeCords = ctx.shapeCoord();

        int[] xs = new int[shapeCords.size()];
        int[] ys = new int[shapeCords.size()];
        for (int i = 0; i < shapeCords.size(); i++) {
            Point point = evaluatePointFromExpressionOrRandom(shapeCords.get(i), visitor);

            xs[i] = point.x;
            ys[i] = point.y;
        }
        int rotate = evaluateRotationAngleFromExprOrRandom(ctx.rotateShape(), visitor);

        Polygon polygon = new Polygon(xs, ys, xs.length);

        if (!frames.modeUnion() && !frames.modeRepeat()) {
            frames.g2d().setColor(frames.brushColor());
            frames.g2d().setStroke(
                    new BasicStroke(frames.brushThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (rotate > 0) {
                Graphics2D g2dRotate = (Graphics2D) frames.g2d().create();

                g2dRotate.rotate(toRadians(rotate), polygon.getBounds().getCenterX(), polygon.getBounds().getCenterY());
                if (frames.brushFill())
                    g2dRotate.fill(polygon);
                else
                    g2dRotate.draw(polygon);

                g2dRotate.dispose();
            } else {

                if (frames.brushFill())
                    frames.g2d().fill(polygon);
                else
                    frames.g2d().draw(polygon);
            }
        } else {
            if (rotate > 0 && frames.modeUnion())
                throw new RsSemanticsException("Rotation is not allowed when shape is a part of a random definition.",
                        ctx.getStart());
        }

        return ShapeResult.create(
                List.of(new ShapeWithContext(
                        polygon,
                        frames.brushColor(), frames.brushThickness(), frames.brushFill(),
                        rotate > 0
                                ? new ShapeRotationContext(
                                rotate, polygon.getBounds().getCenterX(), polygon.getBounds().getCenterY())
                                : null)));
    }
}
