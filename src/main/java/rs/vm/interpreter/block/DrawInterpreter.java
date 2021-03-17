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

import java.awt.Color;
import java.util.ArrayList;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoDrawContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.UnknownVariableException;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 * draw (green) ( ... )
 * draw (green, 1) ( ... )
 * </pre>
 */
public class DrawInterpreter extends InterpreterBase implements ICodeBlockInterpreter<DoDrawContext, ShapeResult>
{
    private static final DrawInterpreter INSTANCE = new DrawInterpreter();

    public static DrawInterpreter instance()
    {
        return INSTANCE;
    }

    private DrawInterpreter()
    {
    }

    @Override
    public boolean matches(DoDrawContext ctx)
    {
        return true;
    }

    @Override
    public ShapeResult interpret(DoDrawContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        frames.newFrame();

        try
        {
            frames.brushColor(getColor(ctx, frames, visitor));
            if (ctx.expr() != null)
            {
                frames.brushThickness(evaluateInt(ctx.expr(), visitor));
                frames.brushFill(false);
            }
            else
            {
                frames.brushThickness(1);
                frames.brushFill(true);
            }

            ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());
            for (StepToShapeBodyContext dbdContext : ctx.stepToShapeBody())
            {
                shapeResult.shapes().addAll(((ShapeResult)visitor.visit(dbdContext)).shapes());
            }

            return shapeResult;
        }
        finally
        {
            frames.disposeFrame();
        }
    }

    private Color getColor(DoDrawContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        if (ctx.drawColor().color() != null)
        {
            return mapColor(ctx.drawColor().color());
        }
        else if (ctx.drawColor().VAR() != null)
        {
            final Color color = frames.varColor(ctx.drawColor().VAR().getText());
            if (color == null)
                throw new UnknownVariableException(ctx.drawColor().VAR().getText(), ctx.drawColor().VAR().getSymbol());

            return color;
        }
        else if (ctx.drawColor().inlineRnd() != null)
        {
            return evaluateRandomColor(ctx.drawColor().inlineRnd(), visitor);
        }
        else
        {
            return Color.BLACK;
        }
    }
}
