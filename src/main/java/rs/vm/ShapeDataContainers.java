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
import java.awt.Shape;

public final class ShapeDataContainers {
    private ShapeDataContainers() {
    }

    public static class ShapeRotationContext {
        private final int angle;

        private final double centerX;
        private final double centerY;

        public ShapeRotationContext(int angle, double centerX, double centerY) {
            this.angle = angle;

            this.centerX = centerX;
            this.centerY = centerY;
        }

        public int angle() {
            return angle;
        }

        public double centerX() {
            return centerX;
        }

        public double centerY() {
            return centerY;
        }
    }

    public static class ShapeWithContext {
        private final Shape shape;

        private final Color color;
        private final int thickness;
        private final boolean fill;

        private final ShapeRotationContext rotation;

        public ShapeWithContext(Shape shape, Color color, int thickness, boolean fill, ShapeRotationContext rotation) {
            this.color = color;
            this.thickness = thickness;
            this.fill = fill;
            this.rotation = rotation;
            this.shape = shape;
        }

        public Shape shape() {
            return shape;
        }

        public Color color() {
            return color;
        }

        public int thickness() {
            return thickness;
        }

        public boolean fill() {
            return fill;
        }

        public ShapeRotationContext rotation() {
            return rotation;
        }
    }

    public static class WidthHeight {
        private final int width;
        private final int height;

        public WidthHeight(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int width() {
            return width;
        }

        public int height() {
            return height;
        }
    }

    public static class Sector {
        private final int startAngle;
        private final int length;

        public Sector(int startAngle, int length) {
            this.startAngle = startAngle;
            this.length = length;
        }

        public int startAngle() {
            return startAngle;
        }

        public int length() {
            return length;
        }
    }
}
