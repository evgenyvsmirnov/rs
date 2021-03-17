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
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.InlineRndContext;

/**
 * <pre>
 * circles (
 *      ((100, 100) in range (10..20))
 * )
 * </pre>
 */
public class InlineRndIntRangeInterpreter extends RndIntRangeInterpreterBase
        implements ICodeBlockInterpreter<InlineRndContext, IntResult> {
    private static final InlineRndIntRangeInterpreter INSTANCE = new InlineRndIntRangeInterpreter();

    public static InlineRndIntRangeInterpreter instance() {
        return INSTANCE;
    }

    private InlineRndIntRangeInterpreter() {
    }

    @Override
    public boolean matches(InlineRndContext ctx) {
        return ctx.rndDefNoVar().ofRange() != null;
    }

    @Override
    public IntResult interpret(InlineRndContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        return interpretInternal(ctx.rndDefNoVar().ofRange(), frames, visitor);
    }
}
