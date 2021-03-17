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
package rs.vm.interpreter.block;

import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.DrawFrames;
import rs.vm.ICodeBlockInterpreter;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.DoUnionContext;
import rs.vm.RSParser.ShapeContext;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.interpreter.InterpreterBase;

/**
 * <pre>
 *     union (
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
 * </pre>
 */
public class UnionInterpreter extends InterpreterBase implements ICodeBlockInterpreter<DoUnionContext, ShapeResult> {
    private static final UnionInterpreter INSTANCE = new UnionInterpreter();

    public static UnionInterpreter instance() {
        return INSTANCE;
    }

    private UnionInterpreter() {
    }

    @Override
    public boolean matches(DoUnionContext ctx) {
        return true;
    }

    @Override
    public ShapeResult interpret(DoUnionContext ctx, DrawFrames frames, RSBaseVisitor<Object> visitor) {
        frames.modeUnion(true);

        List<ShapeWithContext> shapesUnion = new ArrayList<>();
        for (ShapeContext shape : ctx.shape())
            shapesUnion.addAll(((ShapeResult) visitor.visit(shape)).shapes());

        frames.modeUnion(false);

        return ShapeResult.create(shapesUnion);
    }
}
