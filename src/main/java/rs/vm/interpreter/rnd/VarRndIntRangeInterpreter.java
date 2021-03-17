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

import rs.vm.CodeBlockResults.IntResult;
import rs.vm.DrawFrames;
import rs.vm.DuplicateVariableException;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.RndDefVarContext;

/**
 * <pre>
 * with randoms (
 *      ; This block |
 *      ;            V
 *      _radius in range (10..20)
 * ) ( ... )
 * </pre>
 */
public class VarRndIntRangeInterpreter extends RndIntRangeInterpreterBase
        implements ICodeBlockInterpreter<RndDefVarContext, Void>
{
    private static final VarRndIntRangeInterpreter INSTANCE = new VarRndIntRangeInterpreter();

    public static VarRndIntRangeInterpreter instance()
    {
        return INSTANCE;
    }

    private VarRndIntRangeInterpreter()
    {
    }

    @Override
    public boolean matches(RndDefVarContext ctx)
    {
        return ctx.ofRange() != null;
    }

    @Override
    public Void interpret(RndDefVarContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        String var = ctx.VAR().getSymbol().getText();
        if (frames.varExists(var))
            throw new DuplicateVariableException(var, ctx.VAR().getSymbol());

        IntResult random = interpretInternal(ctx.ofRange(), frames, visitor);
        frames.varInt(var, random.value());

        return null;
    }
}
