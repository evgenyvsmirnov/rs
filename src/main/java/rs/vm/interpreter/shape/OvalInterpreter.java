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
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.OvalContext;
import rs.vm.RsSemanticsException;
import rs.vm.ShapeDataContainers.Sector;
import rs.vm.ShapeDataContainers.ShapeRotationContext;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.ShapeDataContainers.WidthHeight;
import rs.vm.interpreter.InterpreterBase;

import static java.lang.Math.toRadians;

/**
 * <pre>
 *      ovals(
 *          ( (80, 450), size 70 x 20, sector from 180 size 45, rotate 35 )
 *          ( (80, 450), 70 x 20, from 180 size 45, rotate 35 )
 *      )
 *
 * </pre>
 */
public class OvalInterpreter extends InterpreterBase implements ICodeBlockInterpreter<OvalContext, ShapeResult> {
    private static final OvalInterpreter INSTANCE = new OvalInterpreter();

    public static OvalInterpreter instance() {
        return INSTANCE;
    }

    private OvalInterpreter() {
    }

    @Override
    public boolean matches(OvalContext ctx) {
        return true;
    }

    @Override
    public ShapeResult interpret(OvalContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        List<ShapeWithContext> shapes = new ArrayList<>();
        for (int i = 0; i < ctx.shapeCoord().size(); i++) {
            Point center = evaluatePointFromExpressionOrRandom(ctx.shapeCoord().get(i), visitor);
            WidthHeight widthHeight = evaluateShapeSize2FromExprOrRandom(ctx.shapeSize2(i), visitor);
            Sector sector = evaluateSectorFromExprOrRandom(ctx.sector(i), visitor);
            int rotate = evaluateRotationAngleFromExprOrRandom(ctx.rotateShape(i), visitor);

            final Arc2D.Double shape = new Arc2D.Double(
                    center.getX() - widthHeight.width(), center.getY() - widthHeight.height(),
                    widthHeight.width() * 2.0, widthHeight.height() * 2.0,
                    sector.startAngle(), sector.length(),
                    Arc2D.OPEN);
            if (!frames.modeUnion() && !frames.modeRepeat()) {
                frames.g2d().setColor(frames.brushColor());
                frames.g2d().setStroke(
                        new BasicStroke(frames.brushThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (rotate > 0) {
                    Graphics2D g2dRotate = (Graphics2D) frames.g2d().create();
                    g2dRotate.rotate(toRadians(rotate), center.x, center.y);

                    if (frames.brushFill())
                        g2dRotate.fill(shape);
                    else
                        g2dRotate.draw(shape);

                    g2dRotate.dispose();
                } else {
                    if (frames.brushFill())
                        frames.g2d().fill(shape);
                    else
                        frames.g2d().draw(shape);
                }
            } else {
                if (rotate > 0 && frames.modeUnion())
                    throw new RsSemanticsException("Rotation is not allowed when shape is a part of a random definition.",
                            ctx.getStart());
            }

            shapes.add(new ShapeWithContext(shape,
                    frames.brushColor(), frames.brushThickness(), frames.brushFill(),
                    rotate > 0 ? new ShapeRotationContext(rotate, center.x, center.y) : null));
        }

        return ShapeResult.create(shapes);
    }
}
