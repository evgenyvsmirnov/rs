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
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.OfRangeContext;
import rs.vm.RsSemanticsException;
import rs.vm.interpreter.InterpreterBase;

abstract class RndIntRangeInterpreterBase extends InterpreterBase {
    protected final IntResult interpretInternal(OfRangeContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        int from = evaluateInt(ctx.expr(0), visitor);
        int to = evaluateInt(ctx.expr(1), visitor);

        if (from > to)
            throw new RsSemanticsException("Wrong range in random: " + from + " > " + to, ctx.getStart());

        int rnd = frames.random(Math.abs(to - from)) + from;
        return IntResult.create(rnd);
    }
}
