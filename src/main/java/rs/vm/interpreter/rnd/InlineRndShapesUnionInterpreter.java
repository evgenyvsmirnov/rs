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
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.InlineRndContext;

/**
 * <pre>
 * ; A circle of radius 2 with random coordinates within the given union of shapes
 * circles (
 *     (
 *          ; The block below
 *          in union (
 *              circles (
 *                  ((500, 60) 60)
 *              )
 *          )
 *          2
 *     )
 * )
 * </pre>
 */
public class InlineRndShapesUnionInterpreter extends RndShapesUnionInterpreterBase
        implements ICodeBlockInterpreter<InlineRndContext, CordsResult>
{
    private static final InlineRndShapesUnionInterpreter INSTANCE = new InlineRndShapesUnionInterpreter();

    public static InlineRndShapesUnionInterpreter instance()
    {
        return INSTANCE;
    }

    private InlineRndShapesUnionInterpreter()
    {
    }

    @Override
    public boolean matches(InlineRndContext ctx)
    {
        return ctx.rndDefNoVar().doUnion() != null;
    }

    @Override
    public CordsResult interpret(InlineRndContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor)
    {
        return interpretInternal(ctx.rndDefNoVar().doUnion(), frames, visitor);
    }
}
