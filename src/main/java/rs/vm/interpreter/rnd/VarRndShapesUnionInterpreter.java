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

import rs.vm.CodeBlockResults.CordsResult;
import rs.vm.DrawFrames;
import rs.vm.DuplicateVariableException;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.RndDefCoordContext;

/**
 * <pre>
 * with randoms (
 *      ; This block |
 *      ;            V
 *      (_x, _y) in union (
 *          circles (
 *              ((500, 60) 60)
 *              ((100, 90) 70)
 *          )
 *          lines (
 *              (200, 300)
 *              (250, 300)
 *              (225, 400)
 *          )
 *      )
 * ) ( ... )
 * </pre>
 */
public class VarRndShapesUnionInterpreter extends RndShapesUnionInterpreterBase
        implements ICodeBlockInterpreter<RndDefCoordContext, Void>
{
    private static final VarRndShapesUnionInterpreter INSTANCE = new VarRndShapesUnionInterpreter();

    public static VarRndShapesUnionInterpreter instance()
    {
        return INSTANCE;
    }

    private VarRndShapesUnionInterpreter()
    {
    }

    @Override
    public boolean matches(RndDefCoordContext ctx)
    {
        return true;
    }

    @Override
    public Void interpret(RndDefCoordContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        final String xVar = ctx.coordDef().VAR(0).getText();
        final String yVar = ctx.coordDef().VAR(1).getText();

        if (frames.varExists(xVar))
            throw new DuplicateVariableException(xVar, ctx.coordDef().VAR(0).getSymbol());
        if (frames.varExists(yVar))
            throw new DuplicateVariableException(yVar, ctx.coordDef().VAR(1).getSymbol());

        CordsResult randomPoint = interpretInternal(ctx.doUnion(), frames, visitor);
        frames.varInt(xVar, randomPoint.x());
        frames.varInt(yVar, randomPoint.y());


        return null;
    }
}
