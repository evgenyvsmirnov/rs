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
import java.util.List;

import rs.vm.ShapeDataContainers.ShapeWithContext;

public final class CodeBlockResults {
    private CodeBlockResults() {
    }

    public interface CodeBlockResult {
    }

    public static class ColorResult implements CodeBlockResult {
        private final Color color;

        public static ColorResult create(Color color) {
            if (color == null) {
                throw new IllegalArgumentException("color can't be null.");
            }

            return new ColorResult(color);
        }

        private ColorResult(Color color) {
            this.color = color;
        }

        public Color color() {
            return color;
        }
    }

    public static class CordsResult implements CodeBlockResult {
        private final int x;
        private final int y;

        public static CordsResult create(int x, int y) {
            return new CordsResult(x, y);
        }

        private CordsResult(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }
    }

    public static class IntResult implements CodeBlockResult {
        private final int value;

        public static IntResult create(int value) {
            return new IntResult(value);
        }

        private IntResult(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    public static class ShapeResult implements CodeBlockResult {
        private final List<ShapeWithContext> shapes;

        public static ShapeResult create(List<ShapeWithContext> shapes) {
            if (shapes == null)
                throw new IllegalArgumentException("shapes can't be null.");

            return new ShapeResult(shapes);
        }

        private ShapeResult(List<ShapeWithContext> shapes) {
            this.shapes = shapes;
        }

        public List<ShapeWithContext> shapes() {
            return shapes;
        }
    }
}
