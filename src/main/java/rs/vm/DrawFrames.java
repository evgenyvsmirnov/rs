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

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Random;

public final class DrawFrames {
    private final Deque<DrawFrame> frames = new ArrayDeque<>();
    private DrawFrame currentFrame;

    private DrawFrames(Graphics2D g2d) {
        this.currentFrame = new DrawFrame(g2d);
    }

    static DrawFrames create(Graphics2D g2d) {
        if (g2d == null) {
            throw new IllegalArgumentException("Graphics2D can't be null.");
        }

        return new DrawFrames(g2d);
    }

    public void newFrame() {
        frames.push(currentFrame);
        currentFrame = currentFrame.duplicate();
    }

    public boolean varExists(String var) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        return currentFrame.vars.ints.containsKey(var) || currentFrame.vars.colors.containsKey(var);
    }

    public boolean varIntExists(String var) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        return currentFrame.vars.ints.containsKey(var);
    }

    public boolean varColorExists(String var) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        return currentFrame.vars.colors.containsKey(var);
    }

    public void varInt(String var, int value) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        currentFrame.vars.ints.put(var, value);
    }

    public void varColor(String var, Color value) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");
        if (value == null)
            throw new IllegalArgumentException("Color can't be blank.");

        currentFrame.vars.colors.put(var, value);
    }

    public int varInt(String var) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        if (!currentFrame.vars.ints.containsKey(var))
            throw new IllegalArgumentException("Var " + var + " doesn't exist.");

        return currentFrame.vars.ints.get(var);
    }

    public Color varColor(String var) {
        if (var == null || var.isBlank())
            throw new IllegalArgumentException("Var can't be blank.");

        if (!currentFrame.vars.ints.containsKey(var))
            throw new IllegalArgumentException("Var " + var + " doesn't exist.");

        return currentFrame.vars.colors.get(var);
    }

    public void brushColor(Color color) {
        if (color == null)
            throw new IllegalArgumentException("Color can't be blank.");

        currentFrame.brush.color = color;
    }

    public Color brushColor() {
        return currentFrame.brush.color;
    }

    public void brushFill(boolean fill) {
        currentFrame.brush.fill = fill;
    }

    public boolean brushFill() {
        return currentFrame.brush.fill;
    }

    public void brushThickness(int thickness) {
        if (thickness <= 0)
            throw new IllegalArgumentException("Thickness can't be negative.");

        currentFrame.brush.thickness = thickness;
    }

    public int brushThickness() {
        return currentFrame.brush.thickness;
    }

    public void modeUnion(boolean value) {
        currentFrame.modeFlags.union = value;
    }

    public boolean modeUnion() {
        return currentFrame.modeFlags.union;
    }

    public void modeRepeat(boolean value) {
        currentFrame.modeFlags.repeat = value;
    }

    public boolean modeRepeat() {
        return currentFrame.modeFlags.repeat;
    }

    public int random(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("Bound can't be negative.");

        return currentFrame.random.nextInt(bound);
    }

    public Graphics2D g2d() {
        return currentFrame.g2d;
    }

    public DrawFrame disposeFrame() {
        currentFrame = frames.pop();
        if (currentFrame == null) {
            throw new IllegalStateException("VM error. The first frame has been disposed.");
        }

        return currentFrame;
    }

    private static class DrawFrame {
        final Graphics2D g2d;

        final Vars vars;
        final ModeFlags modeFlags;
        final Brush brush;
        final Random random;

        private DrawFrame(Graphics2D g2d) {
            this.g2d = g2d;
            this.vars = new Vars();
            this.modeFlags = new ModeFlags();
            this.brush = new Brush();
            this.random = new Random();
        }

        private DrawFrame(DrawFrame drawFrame) {
            this.g2d = drawFrame.g2d;
            this.vars = new Vars(drawFrame.vars);
            this.modeFlags = new ModeFlags(drawFrame.modeFlags);
            this.brush = new Brush(drawFrame.brush);
            this.random = drawFrame.random;
        }

        private DrawFrame duplicate() {
            return new DrawFrame(this);
        }
    }

    public static final class Brush {
        private Color color;
        private int thickness;
        private boolean fill;

        private Brush() {
            this.color = Color.BLACK;
            this.thickness = 1;
            this.fill = false;
        }

        private Brush(Brush brush) {
            this.color = brush.color;
            this.thickness = brush.thickness;
            this.fill = brush.fill;
        }
    }

    private static final class ModeFlags {
        private boolean union;
        private boolean repeat;

        private ModeFlags() {
            this.union = false;
            this.repeat = false;
        }

        private ModeFlags(ModeFlags modeFlags) {
            this.union = modeFlags.union;
            this.repeat = modeFlags.repeat;
        }
    }

    private static class Vars {
        private final HashMap<String, Integer> ints;
        private final HashMap<String, Color> colors;

        private Vars() {
            this.ints = new HashMap<>();
            this.colors = new HashMap<>();
        }

        private Vars(Vars vars) {
            this.ints = new HashMap<>(vars.ints);
            this.colors = new HashMap<>(vars.colors);
        }
    }
}