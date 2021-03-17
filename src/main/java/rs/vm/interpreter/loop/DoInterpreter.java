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
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoLoopContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 * do ( ... )
 * </pre>
 */
public class DoInterpreter extends InterpreterBase implements ICodeBlockInterpreter<DoLoopContext, ShapeResult>
{
    private static final DoInterpreter INSTANCE = new DoInterpreter();

    public static DoInterpreter instance()
    {
        return INSTANCE;
    }

    private DoInterpreter()
    {
    }

    @Override
    public boolean matches(DoLoopContext ctx)
    {
        return ctx.loopDef() == null && ctx.loopNDef() == null;
    }

    @Override
    public ShapeResult interpret(DoLoopContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());
        for (StepToShapeBodyContext lbCtx : ctx.stepToShapeBody())
        {
            shapeResult.shapes().addAll(((ShapeResult)visitor.visit(lbCtx)).shapes());
        }

        return shapeResult;
    }
}
