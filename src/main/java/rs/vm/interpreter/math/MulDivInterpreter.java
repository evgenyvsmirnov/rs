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
package rs.vm.interpreter.math;

import rs.vm.CodeBlockResults.IntResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser;
import rs.vm.RSParser.MulDivContext;
import rs.vm.interpreter.InterpreterBase;

public class MulDivInterpreter extends InterpreterBase implements ICodeBlockInterpreter<MulDivContext, IntResult>
{
    private static final MulDivInterpreter INSTANCE = new MulDivInterpreter();

    public static MulDivInterpreter instance()
    {
        return INSTANCE;
    }

    private MulDivInterpreter()
    {
    }

    @Override
    public boolean matches(MulDivContext ctx)
    {
        return true;
    }

    @Override
    public IntResult interpret(MulDivContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        int left = evaluateInt(ctx.expr(0), visitor);
        int right = evaluateInt(ctx.expr(1), visitor);

        if (ctx.op.getType() == RSParser.MUL)
            return IntResult.create(left * right);
        else
            return IntResult.create(left / right);
    }
}
