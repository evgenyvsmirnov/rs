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

import rs.vm.CodeBlockResults.CodeBlockResult;
import rs.vm.CodeBlockResults.IntResult;
import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoLoopContext;
import rs.vm.RSParser.LoopDefContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.RsSyntaxException;
import rs.vm.RsVmInternalException;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 * do (50) ( ... )
 * do (50 times) ( ... )
 * </pre>
 */
public class DoLoopNInterpreter extends InterpreterBase implements ICodeBlockInterpreter<DoLoopContext, ShapeResult>
{
    private static final DoLoopNInterpreter INSTANCE = new DoLoopNInterpreter();

    public static DoLoopNInterpreter instance()
    {
        return INSTANCE;
    }

    private DoLoopNInterpreter()
    {
    }

    @Override
    public boolean matches(DoLoopContext ctx)
    {
        return ctx.loopDef() != null && ctx.loopNDef() == null;
    }

    @Override
    public ShapeResult interpret(DoLoopContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        CodeBlockResult result;

        final LoopDefContext loopDef = ctx.loopDef();
        if (loopDef.expr() != null)
            result = (CodeBlockResult)visitor.visit(loopDef.expr());
        else if (loopDef.inlineRnd() != null)
            result = (CodeBlockResult)visitor.visit(loopDef.inlineRnd());
        else
            throw new RsSyntaxException("Unsupported loop boundary: " + loopDef.getText(), loopDef.getStart());

        if (!(result instanceof IntResult))
            throw new RsVmInternalException("Expected an integer as the loop boundary.");

        ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());
        for (int i = 0; i < ((IntResult)result).value(); i++)
        {
            for (StepToShapeBodyContext loopbodyContext : ctx.stepToShapeBody())
            {
                shapeResult.shapes().addAll(((ShapeResult)visitor.visit(loopbodyContext)).shapes());
            }
        }

        return shapeResult;
    }
}
