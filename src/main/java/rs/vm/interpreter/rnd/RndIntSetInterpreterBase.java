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

import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.IntResult;
import rs.vm.DrawFrames;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.ExprContext;
import rs.vm.RSParser.OfSetContext;
import rs.vm.interpreter.InterpreterBase;

abstract class RndIntSetInterpreterBase extends InterpreterBase {
    protected final IntResult interpretInternal(OfSetContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        final List<ExprContext> exprs = ctx.expr();

        List<Integer> intsToChoose = new ArrayList<>(exprs.size());
        for (ExprContext expr : exprs)
            intsToChoose.add(evaluateInt(expr, visitor));

        int rnd = frames.random(intsToChoose.size());
        return IntResult.create(intsToChoose.get(rnd));
    }
}
