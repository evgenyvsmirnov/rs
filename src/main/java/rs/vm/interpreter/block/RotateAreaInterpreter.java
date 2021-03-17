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
package rs.vm.interpreter.block;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.RotateAreaContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.RsSemanticsException;
import rs.vm.RsSyntaxException;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.interpreter.InterpreterBase;

import static java.lang.Math.toRadians;

/**
 * <pre>
 * rotate area (20) ( ... )
 * </pre>
 */
public class RotateAreaInterpreter extends InterpreterBase
        implements ICodeBlockInterpreter<RotateAreaContext, ShapeResult>
{
    private static final RotateAreaInterpreter INSTANCE = new RotateAreaInterpreter();

    public static RotateAreaInterpreter instance()
    {
        return INSTANCE;
    }

    private RotateAreaInterpreter()
    {
    }

    @Override
    public boolean matches(RotateAreaContext ctx)
    {
        return true;
    }

    @Override
    public ShapeResult interpret(RotateAreaContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());

        frames.modeRepeat(true);
        for (StepToShapeBodyContext stepToShapeBodyContext : ctx.stepToShapeBody())
        {
            shapeResult.shapes().addAll(((ShapeResult)visitor.visit(stepToShapeBodyContext)).shapes());
        }
        frames.modeRepeat(false);

        int rotate = getRotateFromExprOrRandom(visitor, ctx);
        Point boundRectangleCenter = getBoundaryRectangleCenter(shapeResult, ctx);

        Graphics2D g2dRotate1 = (Graphics2D)frames.g2d().create();
        g2dRotate1.rotate(toRadians(rotate), boundRectangleCenter.getX(), boundRectangleCenter.getY());
        for (ShapeWithContext shapeWithContext : shapeResult.shapes())
        {
            if (shapeWithContext.rotation() != null)
            {
                Graphics2D g2dRotate2 = (Graphics2D)g2dRotate1.create();
                g2dRotate2.setColor(shapeWithContext.color());
                g2dRotate2.setStroke(new BasicStroke(shapeWithContext.thickness(), BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g2dRotate2.rotate(
                        toRadians(shapeWithContext.rotation().angle()),
                        shapeWithContext.rotation().centerX(), shapeWithContext.rotation().centerY());

                if (!shapeWithContext.fill())
                    g2dRotate2.draw(shapeWithContext.shape());
                else
                    g2dRotate2.fill(shapeWithContext.shape());

                g2dRotate2.dispose();
            }
            else
            {
                g2dRotate1.setColor(shapeWithContext.color());
                g2dRotate1.setStroke(new BasicStroke(shapeWithContext.thickness(), BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                if (shapeWithContext.fill())
                    g2dRotate1.fill(shapeWithContext.shape());
                else
                    g2dRotate1.draw(shapeWithContext.shape());
            }
        }
        g2dRotate1.dispose();

        return ShapeResult.create(new ArrayList<>());
    }

    private int getRotateFromExprOrRandom(RSBaseVisitor<Object> visitor, RotateAreaContext ctx)
    {
        if (ctx == null || ctx.expr() == null && ctx.inlineRnd() == null)
            return 0;

        if (ctx.expr() != null)
        {
            return evaluateInt(ctx.expr(), visitor);
        }
        else if (ctx.inlineRnd() != null)
        {
            if (ctx.inlineRnd().rndDefNoVar() == null)
                throw new RsSemanticsException("A random rotation angle definition can't have a body.",
                        ctx.inlineRnd().getStart());

            if (ctx.inlineRnd().rndDefNoVar().ofSet() == null && ctx.inlineRnd().rndDefNoVar().ofRange() == null)
                throw new RsSemanticsException("A random rotation angle can be chosen either from a range or a set.",
                        ctx.inlineRnd().rndDefNoVar().getStart());

            return evaluateRandomInt(ctx.inlineRnd(), visitor);
        }
        else
        {
            throw new RsSyntaxException("Unsupported rotation angle definition: " + ctx.getText(), ctx.getStart());
        }
    }

    private Point getBoundaryRectangleCenter(ShapeResult result, RotateAreaContext ctx)
    {
        Rectangle2D bounds2D = getBoundaryRectangle(result, ctx).getBounds2D();

        int x = (int)(bounds2D.getX() + bounds2D.getWidth() / 2);
        int y = (int)(bounds2D.getY() + bounds2D.getHeight() / 2);

        return new Point(x, y);
    }
}
