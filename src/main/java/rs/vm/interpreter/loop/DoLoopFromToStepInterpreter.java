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
package rs.vm.interpreter.loop;

import java.util.ArrayList;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.DuplicateVariableException;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoLoopContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 * do (_n from 1 to 10 step 2) ( ... )
 * do (_n from 1 to 10) ( ... )
 * do (_n from 10 to 1 step 2) ( ... )
 * do (_n from 10 to 1) ( ... )
 * </pre>
 */
public class DoLoopFromToStepInterpreter extends InterpreterBase
        implements ICodeBlockInterpreter<DoLoopContext, ShapeResult>
{
    private static final DoLoopFromToStepInterpreter INSTANCE = new DoLoopFromToStepInterpreter();

    public static DoLoopFromToStepInterpreter instance()
    {
        return INSTANCE;
    }

    private DoLoopFromToStepInterpreter()
    {
    }

    @Override
    public boolean matches(DoLoopContext ctx)
    {
        return ctx.loopDef() == null && ctx.loopNDef() != null;
    }

    @Override
    public ShapeResult interpret(DoLoopContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        String var = ctx.loopNDef().VAR().getSymbol().getText();

        if (frames.varExists(var))
            throw new DuplicateVariableException(var, ctx.loopNDef().VAR().getSymbol());

        frames.newFrame();

        int from = evaluateInt(ctx.loopNDef().expr(0), visitor);
        int to = evaluateInt(ctx.loopNDef().expr(1), visitor);
        int step = ctx.loopNDef().STEP() != null ? evaluateInt(ctx.loopNDef().expr(2), visitor) : 1;

        ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());
        if (from <= to)
        {
            for (int i = from; i <= to; i += step)
            {
                frames.varInt(var, i);
                for (StepToShapeBodyContext loopbodyContext : ctx.stepToShapeBody())
                {
                    shapeResult.shapes().addAll(evaluateShapes(loopbodyContext, visitor));
                }
            }
        }
        else
        {
            for (int i = from; i >= to; i -= step)
            {
                frames.varInt(var, i);
                for (StepToShapeBodyContext loopbodyContext : ctx.stepToShapeBody())
                {
                    shapeResult.shapes().addAll(evaluateShapes(loopbodyContext, visitor));
                }
            }
        }
        frames.disposeFrame();

        return shapeResult;
    }
}
