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
package rs.vm;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.CodeBlockResult;
import rs.vm.CodeBlockResults.ColorResult;
import rs.vm.CodeBlockResults.IntResult;
import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.RSParser.AddSubContext;
import rs.vm.RSParser.CircleContext;
import rs.vm.RSParser.CurvesContext;
import rs.vm.RSParser.DoDrawContext;
import rs.vm.RSParser.DoLoopContext;
import rs.vm.RSParser.DoUnionContext;
import rs.vm.RSParser.InlineRndContext;
import rs.vm.RSParser.IntContext;
import rs.vm.RSParser.LinesContext;
import rs.vm.RSParser.MulDivContext;
import rs.vm.RSParser.OvalContext;
import rs.vm.RSParser.ParensContext;
import rs.vm.RSParser.RndContext;
import rs.vm.RSParser.RndDefCoordContext;
import rs.vm.RSParser.RndDefVarContext;
import rs.vm.RSParser.RotateAreaContext;
import rs.vm.RSParser.StepToShapeBodyContext;
import rs.vm.RSParser.VarContext;
import rs.vm.interpreter.block.DrawInterpreter;
import rs.vm.interpreter.block.RotateAreaInterpreter;
import rs.vm.interpreter.block.UnionInterpreter;
import rs.vm.interpreter.loop.DoInterpreter;
import rs.vm.interpreter.loop.DoLoopFromToStepInterpreter;
import rs.vm.interpreter.loop.DoLoopNInterpreter;
import rs.vm.interpreter.math.AddSubInterpreter;
import rs.vm.interpreter.math.MulDivInterpreter;
import rs.vm.interpreter.rnd.InlineRndColorSetInterpreter;
import rs.vm.interpreter.rnd.InlineRndIntRangeInterpreter;
import rs.vm.interpreter.rnd.InlineRndIntSetInterpreter;
import rs.vm.interpreter.rnd.InlineRndShapesUnionInterpreter;
import rs.vm.interpreter.rnd.VarRndColorSetInterpreter;
import rs.vm.interpreter.rnd.VarRndIntRangeInterpreter;
import rs.vm.interpreter.rnd.VarRndIntSetInterpreter;
import rs.vm.interpreter.rnd.VarRndShapesUnionInterpreter;
import rs.vm.interpreter.shape.CircleInterpreter;
import rs.vm.interpreter.shape.CurvesInterpreter;
import rs.vm.interpreter.shape.LinesInterpreter;
import rs.vm.interpreter.shape.OvalInterpreter;

public class RsVm extends RSBaseVisitor<Object> {
    private final DrawFrames frames;

    public RsVm(Graphics2D g2d) {
        frames = DrawFrames.create(g2d);
    }

    @Override
    public ShapeResult visitDoLoop(DoLoopContext ctx) {
        if (DoInterpreter.instance().matches(ctx))
            return DoInterpreter.instance().interpret(ctx, frames, this);
        else if (DoLoopNInterpreter.instance().matches(ctx))
            return DoLoopNInterpreter.instance().interpret(ctx, frames, this);
        else if (DoLoopFromToStepInterpreter.instance().matches(ctx))
            return DoLoopFromToStepInterpreter.instance().interpret(ctx, frames, this);
        else
            throw new RsSyntaxException("Unsupported loop type.", ctx.getStart());
    }

    @Override
    public ShapeResult visitDoDraw(DoDrawContext ctx) {
        return DrawInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public ShapeResult visitRotateArea(RotateAreaContext ctx) {
        return RotateAreaInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public Object visitInlineRnd(InlineRndContext ctx) {
        if (InlineRndIntRangeInterpreter.instance().matches(ctx))
            return InlineRndIntRangeInterpreter.instance().interpret(ctx, frames, this);
        else if (InlineRndIntSetInterpreter.instance().matches(ctx))
            return InlineRndIntSetInterpreter.instance().interpret(ctx, frames, this);
        else if (InlineRndColorSetInterpreter.instance().matches(ctx))
            return InlineRndColorSetInterpreter.instance().interpret(ctx, frames, this);
        else if (InlineRndShapesUnionInterpreter.instance().matches(ctx))
            return InlineRndShapesUnionInterpreter.instance().interpret(ctx, frames, this);
        else
            throw new RsSyntaxException("Unsupported type of inline random.", ctx.getStart());
    }

    @Override
    public ShapeResult visitRnd(RndContext ctx) {
        frames.newFrame();
        try {
            for (RndDefVarContext intColorVar : ctx.rndDefVar()) {
                if (VarRndIntRangeInterpreter.instance().matches(intColorVar))
                    VarRndIntRangeInterpreter.instance().interpret(intColorVar, frames, this);
                else if (VarRndIntSetInterpreter.instance().matches(intColorVar))
                    VarRndIntSetInterpreter.instance().interpret(intColorVar, frames, this);
                else if (VarRndColorSetInterpreter.instance().matches(intColorVar))
                    VarRndColorSetInterpreter.instance().interpret(intColorVar, frames, this);
                else
                    throw new RsSyntaxException("Unsupported type of random definition.", intColorVar.getStart());
            }

            for (RndDefCoordContext coordsVar : ctx.rndDefCoord()) {
                if (VarRndShapesUnionInterpreter.instance().matches(coordsVar))
                    VarRndShapesUnionInterpreter.instance().interpret(coordsVar, frames, this);
                else
                    throw new RsSyntaxException("Unsupported type of random definition.", coordsVar.getStart());
            }

            ShapeResult shapeResult = ShapeResult.create(new ArrayList<>());

            List<StepToShapeBodyContext> withRandomBody = ctx.stepToShapeBody();
            for (StepToShapeBodyContext shape : withRandomBody)
                shapeResult.shapes().addAll(((ShapeResult) visit(shape)).shapes());

            return shapeResult;
        } finally {
            frames.disposeFrame();
        }
    }

    @Override
    public ShapeResult visitDoUnion(DoUnionContext ctx) {
        return UnionInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public ShapeResult visitCircle(CircleContext ctx) {
        return CircleInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public ShapeResult visitOval(OvalContext ctx) {
        return OvalInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public ShapeResult visitLines(LinesContext ctx) {
        return LinesInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public ShapeResult visitCurves(CurvesContext ctx) {
        return CurvesInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public Object visitParens(ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public IntResult visitMulDiv(MulDivContext ctx) {
        return MulDivInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public IntResult visitAddSub(AddSubContext ctx) {
        return AddSubInterpreter.instance().interpret(ctx, frames, this);
    }

    @Override
    public IntResult visitInt(IntContext ctx) {
        return IntResult.create(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public CodeBlockResult visitVar(VarContext ctx) {
        String var = ctx.VAR().getText();

        if (frames.varIntExists(var))
            return IntResult.create(frames.varInt(var));

        if (frames.varColorExists(var))
            return ColorResult.create(frames.varColor(var));

        throw new UnknownVariableException(ctx.VAR().getText(), ctx.VAR().getSymbol());
    }
}
