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
 *      _radius in set (10, 20, 30)
 * ) ( ... )
 * </pre>
 */
public class VarRndIntSetInterpreter extends RndIntSetInterpreterBase
        implements ICodeBlockInterpreter<RndDefVarContext, Void> {
    private static final VarRndIntSetInterpreter INSTANCE = new VarRndIntSetInterpreter();

    public static VarRndIntSetInterpreter instance() {
        return INSTANCE;
    }

    private VarRndIntSetInterpreter() {
    }

    @Override
    public boolean matches(RndDefVarContext ctx) {
        return ctx.ofSet() != null && ctx.ofSet().expr() != null && !ctx.ofSet().expr().isEmpty();
    }

    @Override
    public Void interpret(RndDefVarContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        String var = ctx.VAR().getSymbol().getText();
        if (frames.varExists(var))
            throw new DuplicateVariableException(var, ctx.VAR().getSymbol());

        IntResult random = interpretInternal(ctx.ofSet(), frames, visitor);
        frames.varInt(var, random.value());

        return null;
    }
}
