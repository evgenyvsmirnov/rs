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
package rs.vm.interpreter;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import rs.vm.CodeBlockResults.ColorResult;
import rs.vm.CodeBlockResults.CordsResult;
import rs.vm.CodeBlockResults.IntResult;
import rs.vm.CodeBlockResults.ShapeResult;
import rs.vm.RSBaseVisitor;
import rs.vm.RSParser.ColorContext;
import rs.vm.RSParser.ExprContext;
import rs.vm.RSParser.InlineRndContext;
import rs.vm.RSParser.RotateShapeContext;
import rs.vm.RSParser.SectorContext;
import rs.vm.RSParser.ShapeCoordContext;
import rs.vm.RSParser.ShapeSize2Context;
import rs.vm.RSParser.ShapeSizeContext;
import rs.vm.RsSemanticsException;
import rs.vm.RsSyntaxException;
import rs.vm.ShapeDataContainers.Sector;
import rs.vm.ShapeDataContainers.ShapeWithContext;
import rs.vm.ShapeDataContainers.WidthHeight;

public abstract class InterpreterBase {
    protected final int evaluateInt(ExprContext expr, RSBaseVisitor<Object> visitor) {
        return ((IntResult) visitor.visit(expr)).value();
    }

    protected final List<ShapeWithContext> evaluateShapes(ParserRuleContext ctx, RSBaseVisitor<Object> visitor) {
        return ((ShapeResult) visitor.visit(ctx)).shapes();
    }

    protected final int evaluateRandomInt(InlineRndContext ctx, RSBaseVisitor<Object> visitor) {
        return ((IntResult) visitor.visit(ctx)).value();
    }

    protected final Color evaluateRandomColor(InlineRndContext ctx, RSBaseVisitor<Object> visitor) {
        return ((ColorResult) visitor.visit(ctx)).color();
    }

    protected final Point evaluatePointFromExpressionOrRandom(ShapeCoordContext shapeCoordContext,
                                                              RSBaseVisitor<Object> visitor) {
        if (shapeCoordContext.coord() != null) {
            int x = evaluateInt(shapeCoordContext.coord().expr(0), visitor);
            int y = evaluateInt(shapeCoordContext.coord().expr(1), visitor);

            return new Point(x, y);
        } else if (shapeCoordContext.inlineRnd() != null) {
            if (shapeCoordContext.inlineRnd().rndDefNoVar() == null)
                throw new RsSyntaxException("A random definition can't have body at this position.",
                        shapeCoordContext.inlineRnd().rndDefNoVar().getStart());

            if (shapeCoordContext.inlineRnd().rndDefNoVar().doUnion() == null)
                throw new RsSyntaxException("A random definition must comprise at least one shape.",
                        shapeCoordContext.inlineRnd().rndDefNoVar().doUnion().getStart());

            CordsResult coordsResult = (CordsResult) visitor.visit(shapeCoordContext.inlineRnd());
            int x = coordsResult.x();
            int y = coordsResult.y();

            return new Point(x, y);
        } else {
            throw new RsSyntaxException("Unknown coordinate definition.", shapeCoordContext.coord().getStart());
        }
    }

    protected final int evaluateShapeSizeFromExpressionOrRandom(ShapeSizeContext ctx, RSBaseVisitor<Object> visitor) {
        int size;
        if (ctx.expr() != null) {
            size = evaluateInt(ctx.expr(), visitor);
        } else if (ctx.inlineRnd() != null) {
            if (ctx.inlineRnd().rndDefNoVar() == null)
                throw new RsSyntaxException("A random definition can't have body at this position.",
                        ctx.inlineRnd().rndDefNoVar().getStart());

            if (ctx.inlineRnd().rndDefNoVar().ofSet() == null && ctx.inlineRnd().rndDefNoVar().ofRange() == null)
                throw new RsSyntaxException("A random value must be chosen from a set or a range.",
                        ctx.inlineRnd().rndDefNoVar().getStart());

            size = evaluateRandomInt(ctx.inlineRnd(), visitor);
        } else {
            throw new RsSyntaxException("Unsupported shape size definition.", ctx.getStart());
        }

        if (size <= 0) {
            throw new RsSemanticsException("Shape size must be positive", ctx.getStart());
        }

        return size;
    }

    protected final int evaluateRotationAngleFromExprOrRandom(RotateShapeContext ctx, RSBaseVisitor<Object> visitor) {
        if (ctx == null || ctx.shapeSize() == null)
            return 0;

        final ShapeSizeContext shapeSize = ctx.shapeSize();
        if (shapeSize.expr() != null) {
            return evaluateInt(shapeSize.expr(), visitor);
        } else if (shapeSize.inlineRnd() != null) {
            if (shapeSize.inlineRnd().rndDefNoVar() == null)
                throw new RsSyntaxException("A random definition can't have body at this position.",
                        shapeSize.inlineRnd().rndDefNoVar().getStart());

            if (shapeSize.inlineRnd().rndDefNoVar().ofSet() == null && shapeSize.inlineRnd().rndDefNoVar().ofRange() == null)
                throw new RsSyntaxException("A random value must be chosen from a set or a range.",
                        shapeSize.inlineRnd().rndDefNoVar().getStart());

            return evaluateRandomInt(shapeSize.inlineRnd(), visitor);
        } else {
            throw new RsSyntaxException("Unsupported rotation angle definition.", ctx.getStart());
        }
    }

    protected final WidthHeight evaluateShapeSize2FromExprOrRandom(ShapeSize2Context ctx, RSBaseVisitor<Object> visitor) {
        int[] widthHeight = new int[2];
        for (int j = 0; j < 2; j++) {
            ShapeSizeContext shapeSize = ctx.shapeSize(j);
            if (shapeSize.expr() != null) {
                widthHeight[j] = ((IntResult) visitor.visit(shapeSize.expr())).value();
            } else if (shapeSize.inlineRnd() != null) {
                if (shapeSize.inlineRnd().rndDefNoVar() == null)
                    throw new RsSyntaxException("A random definition can't have body at this position.",
                            shapeSize.inlineRnd().rndDefNoVar().getStart());

                if (shapeSize.inlineRnd().rndDefNoVar().ofSet() == null && shapeSize.inlineRnd().rndDefNoVar().ofRange() == null)
                    throw new RsSyntaxException("A random value must be chosen from a set or a range.",
                            shapeSize.inlineRnd().rndDefNoVar().getStart());

                IntResult intResult = (IntResult) visitor.visit(shapeSize.inlineRnd());
                widthHeight[j] = intResult.value();
            } else {
                throw new RsSyntaxException("Unsupported shape size definition.", ctx.getStart());
            }
        }

        if (widthHeight[0] <= 0 || widthHeight[1] <= 0)
            throw new RsSemanticsException("A shape sizes must be positive.", ctx.getStart());

        return new WidthHeight(widthHeight[0], widthHeight[1]);
    }

    protected final Sector evaluateSectorFromExprOrRandom(SectorContext ctx, RSBaseVisitor<Object> visitor) {
        if (ctx == null)
            return new Sector(0, 360);

        List<ShapeSizeContext> shapeSizes = ctx.shapeSize();

        int[] startLength = new int[2];
        for (int i = 0; i != 2; i++) {
            final ShapeSizeContext shapeSize = shapeSizes.get(i);
            if (shapeSize.expr() != null) {
                startLength[i] = evaluateInt(shapeSize.expr(), visitor);
            } else if (shapeSize.inlineRnd() != null) {
                if (shapeSize.inlineRnd().rndDefNoVar() == null)
                    throw new RsSyntaxException("A random definition can't have body at this position.",
                            shapeSize.inlineRnd().rndDefNoVar().getStart());

                if (shapeSize.inlineRnd().rndDefNoVar().ofSet() == null && shapeSize.inlineRnd().rndDefNoVar().ofRange() == null)
                    throw new RsSyntaxException("A random value must be chosen from a set or a range.",
                            shapeSize.inlineRnd().rndDefNoVar().getStart());

                startLength[i] = evaluateRandomInt(shapeSize.inlineRnd(), visitor);
            } else {
                throw new RsSyntaxException("Unsupported shape size definition.", shapeSize.getStart());
            }
        }

        return new Sector(startLength[0], startLength[1]);
    }

    protected final Rectangle2D getBoundaryRectangle(ShapeResult result, ParserRuleContext ctx) {
        if (result.shapes().size() == 0)
            throw new RsSyntaxException("No shapes found for a random coordinates source.", ctx.getStart());

        Rectangle2D union = null;
        for (int i = 0; i < result.shapes().size(); i++) {
            if (union == null)
                union = result.shapes().get(i).shape().getBounds2D();
            else
                union = union.createUnion(result.shapes().get(i).shape().getBounds2D());
        }

        return union;
    }

    protected final Color mapColor(ColorContext ctx) {
        if (ctx.RED() != null)
            return Color.RED;
        else if (ctx.GREEN() != null)
            return Color.GREEN;
        else if (ctx.BLUE() != null)
            return new Color(0, 0, 205);
        else if (ctx.WHITE() != null)
            return Color.WHITE;
        else if (ctx.PINK() != null)
            return Color.PINK;
        else if (ctx.CYAN() != null)
            return Color.CYAN;
        else if (ctx.ORANGE() != null)
            return Color.ORANGE;
        else if (ctx.YELLOW() != null)
            return Color.YELLOW;
        else if (ctx.MAGENTA() != null)
            return Color.MAGENTA;
        else if (ctx.GRAY() != null)
            return Color.GRAY;
        else if (ctx.OLIVE() != null)
            return new Color(128, 128, 0);
        else if (ctx.LIME() != null)
            return new Color(0, 255, 0);
        else if (ctx.NIGHT() != null)
            return new Color(5, 5, 50);
        else if (ctx.SILVER() != null)
            return new Color(192, 192, 192);
        else if (ctx.CLARET() != null)
            return new Color(128, 0, 0);
        else if (ctx.GOLD() != null)
            return new Color(255, 215, 0);
        else if (ctx.VIOLET() != null)
            return new Color(128, 0, 128);
        else if (ctx.BRIGHT_PINK() != null)
            return new Color(255, 20, 147);
        else if (ctx.PINK_VIOLET() != null)
            return new Color(238, 130, 238);
        else if (ctx.DARK_BROWN() != null)
            return new Color(139, 69, 19);
        else if (ctx.LIGHT_BROWN() != null)
            return new Color(205, 133, 63);
        else if (ctx.LIGHT_BLUE() != null)
            return new Color(224, 255, 255);
        else if (ctx.DARK_RED() != null)
            return new Color(139, 0, 0);
        else if (ctx.CREAM() != null)
            return new Color(245, 255, 250);
        else if (ctx.CORNFLOWER() != null)
            return new Color(100, 149, 237);
        else if (ctx.TARDIS() != null)
            return new Color(0, 59, 111);
        else
            return Color.BLACK;
    }
}
